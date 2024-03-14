/*
 * Copyright 2016-2023 original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.rest.core;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentEntity;
import org.springframework.data.keyvalue.core.mapping.KeyValuePersistentProperty;
import org.springframework.data.keyvalue.core.mapping.context.KeyValueMappingContext;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.util.TypeInformation;
import org.springframework.validation.Errors;

/**
 * Unit tests for {@link ValidationErrors}.
 *
 * @author Oliver Gierke
 * @author Florian Cramer
 */
class ValidationErrorsUnitTests {

	PersistentEntities entities;

	@BeforeEach
	void setUp() {

		KeyValueMappingContext<?, ?> context = new TestKeyValueMappingContext<>();
		context.getPersistentEntity(Foo.class);

		this.entities = new PersistentEntities(Arrays.asList(context));
	}

	@Test // DATAREST-798
	void exposesNestedViolationsCorrectly() {

		ValidationErrors errors = new ValidationErrors(new Foo(), entities);

		errors.pushNestedPath("bars[0]");
		errors.rejectValue("field", "asdf");
		errors.popNestedPath();

		assertThat(errors.getFieldError().getField()).isEqualTo("bars[0].field");
	}

	@Test // DATAREST-801
	void getsTheNestedFieldsValue() {
		expectedErrorBehavior(new ValidationErrors(new Foo(), entities));
	}

	@Test // DATAREST-1163
	void returnsNullForPropertyValue() {

		ValidationErrors errors = new ValidationErrors(new Foo(), entities);

		assertThat(errors.getFieldValue("bar")).isNull();
	}

	@Test // GH-2252
	void getsTheNestedFieldsValueForNonPersistentEntity() {

		ValidationErrors errors = new ValidationErrors(new Foo(), entities);

		assertThat(errors.getFieldValue("qux.field")).isEqualTo("World");
	}

	private static void expectedErrorBehavior(Errors errors) {

		assertThat(errors.getFieldValue("bars")).isNotNull();

		errors.pushNestedPath("bars[0]");

		try {
			errors.getFieldValue("bars");
			fail("Expected NotReadablePropertyException");
		} catch (NotReadablePropertyException e) {}

		assertThat(errors.getFieldValue("field")).isEqualTo("Hello");
	}

	static class Foo {
		List<Bar> bars = Collections.singletonList(new Bar());
		Bar bar = null;
		Qux qux = new Qux();
	}

	static class Bar {
		String field = "Hello";
	}

	static class Qux {
		String field = "World";
	}

	static class TestKeyValueMappingContext<E extends KeyValuePersistentEntity<?, P>, P extends KeyValuePersistentProperty<P>>
			extends KeyValueMappingContext<E, P> {

		@Override
		protected boolean shouldCreatePersistentEntityFor(TypeInformation<?> type) {
			return Qux.class != type.getType() && super.shouldCreatePersistentEntityFor(type);
		}
	}
}
