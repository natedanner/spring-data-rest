/*
 * Copyright 2014-2024 the original author or authors.
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
package org.springframework.data.rest.tests.shop;

import lombok.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Identifier;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Reference;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.data.rest.tests.shop.LineItem.LineItemProductsOnlyProjection;
import org.springframework.data.rest.tests.shop.Order.OrderIdentifier;

/**
 * @author Oliver Gierke
 * @author Craig Andrews
 */
@Value
public class Order implements AggregateRoot<Order, OrderIdentifier> {

	@Projection(name = "itemsOnly", types = Order.class)
	public interface OrderItemsOnlyProjection {
		List<LineItemProductsOnlyProjection> getItems();
	}

	private final @Id OrderIdentifier id = new OrderIdentifier(UUID.randomUUID());
	private final List<LineItem> items = new ArrayList<>();
	private final @Reference Customer customer;

	public Order add(LineItem item) {

		this.items.add(item);
		return this;
	}

	@Value
	static class OrderIdentifier implements Identifier, Serializable {
		private static final long serialVersionUID = -3362660123468974881L;
		UUID id;
	}
}
