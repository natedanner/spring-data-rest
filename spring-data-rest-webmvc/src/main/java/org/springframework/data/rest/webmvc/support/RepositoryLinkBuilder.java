/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.rest.webmvc.support;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import org.springframework.data.mapping.PersistentProperty;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.webmvc.BaseUri;
import org.springframework.hateoas.Affordance;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.hateoas.server.core.LinkBuilderSupport;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * {@link LinkBuilder} to be able to create links pointing to repositories.
 *
 * @author Oliver Gierke
 */
public class RepositoryLinkBuilder extends LinkBuilderSupport<RepositoryLinkBuilder> {

	private final ResourceMetadata metadata;

	/**
	 * Creates a new {@link RepositoryLinkBuilder} with the given {@link ResourceMetadata} and base {@link URI}.
	 *
	 * @param metadata must not be {@literal null}.
	 * @param baseUri
	 */
	public RepositoryLinkBuilder(ResourceMetadata metadata, BaseUri baseUri) {
		this(metadata, baseUri.getUriComponentsBuilder().path(metadata.getPath().toString()), Collections.emptyList());
	}

	/**
	 * Creates a new {@link RepositoryLinkBuilder} with the given {@link ResourceMetadata} and
	 * {@link UriComponentsBuilder}.
	 *
	 * @param metadata must not be {@literal null}.
	 * @param builder must not be {@literal null}.
	 */
	private RepositoryLinkBuilder(ResourceMetadata metadata, UriComponentsBuilder builder, List<Affordance> affordances) {

		super(builder, affordances);

		Assert.notNull(metadata, "ResourceMetadata must not be null!");

		this.metadata = metadata;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.hateoas.server.core.LinkBuilderSupport#slash(java.lang.Object)
	 */
	@Override
	public RepositoryLinkBuilder slash(Object object) {

		return PersistentProperty.class.isInstance(object) //
				? slash((PersistentProperty<?>) object)
				: super.slash(object);
	}

	public RepositoryLinkBuilder slash(PersistentProperty<?> property) {
		return slash(metadata.getMappingFor(property).getPath());
	}

	public Link withResourceRel() {
		return withRel(metadata.getRel());
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.hateoas.server.core.LinkBuilderSupport#createNewInstance(org.springframework.web.util.UriComponentsBuilder, java.util.List)
	 */
	@Override
	protected RepositoryLinkBuilder createNewInstance(UriComponentsBuilder builder, List<Affordance> affordances) {
		return new RepositoryLinkBuilder(this.metadata, builder, affordances);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.hateoas.server.core.LinkBuilderSupport#getThis()
	 */
	@Override
	protected RepositoryLinkBuilder getThis() {
		return this;
	}
}
