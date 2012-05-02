package org.springframework.data.rest.repository;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.repository.Repository;

/**
 * Encapsulates necessary metadata about a {@link Repository}.
 *
 * @author Jon Brisbin <jbrisbin@vmware.com>
 */
public interface RepositoryMetadata<R extends Repository<? extends Object, ? extends Serializable>, E extends EntityMetadata<? extends AttributeMetadata>> {

  /**
   * The name this {@link Repository} is exported under.
   *
   * @return
   */
  String name();

  /**
   * Get the string value to be used as part of a link {@literal rel} attribute.
   *
   * @return
   */
  String rel();

  /**
   * The type of domain object this {@link Repository} is repsonsible for.
   *
   * @return
   */
  Class<? extends Object> domainType();

  /**
   * The Class of the {@link Repository} subinterface.
   *
   * @return
   */
  Class<? extends Repository<? extends Object, ? extends Serializable>> repositoryClass();

  /**
   * The {@link Repository} instance.
   *
   * @return
   */
  R repository();

  /**
   * The {@link EntityMetadata} associated with the domain type of this {@literal Repository}.
   *
   * @return
   */
  E entityMetadata();

  /**
   * Get a {@link RepositoryQueryMethod} by key.
   *
   * @param key
   * @return
   */
  RepositoryQueryMethod queryMethod(String key);

  /**
   * Get a Map of all {@link RepositoryQueryMethod}s, keyed by name.
   *
   * @return
   */
  Map<String, RepositoryQueryMethod> queryMethods();

}
