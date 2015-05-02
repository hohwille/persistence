/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.persistence.impl.hibernate;

import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;

import net.sf.mmm.util.lang.api.DatatypeDescriptorManager;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder;
import org.hibernate.jpa.internal.EntityManagerFactoryImpl;
import org.hibernate.type.TypeResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: this class ...
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.1.0
 */
public class HibernatePersistenceProviderWithGenericDatatypeSupport extends HibernatePersistenceProvider {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(HibernatePersistenceProviderWithGenericDatatypeSupport.class);

  /** @see #setDatatypeDescriptorManager(DatatypeDescriptorManager) */
  private DatatypeDescriptorManager datatypeDescriptorManager;

  /**
   * The constructor.
   */
  public HibernatePersistenceProviderWithGenericDatatypeSupport() {

    super();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {

    LOGGER.trace("Starting createEntityManagerFactory for persistenceUnitName {}", persistenceUnitName);

    try {
      final EntityManagerFactoryBuilder builder = getEntityManagerFactoryBuilderOrNull(persistenceUnitName, properties);
      if (builder == null) {
        LOGGER.trace("Could not obtain matching EntityManagerFactoryBuilder, returning null");
        return null;
      } else {
        EntityManagerFactoryImpl impl = (EntityManagerFactoryImpl) builder;
        TypeResolver typeResolver = impl.getSessionFactory().getTypeResolver();
        return builder.build();
      }
    } catch (PersistenceException pe) {
      throw pe;
    } catch (Exception e) {
      LOGGER.debug("Unable to build entity manager factory", e);
      throw new PersistenceException("Unable to build entity manager factory", e);
    }
    // EntityManagerFactory entityManagerFactory = super.createEntityManagerFactory(persistenceUnitName,
    // properties);
    // if (entityManagerFactory instanceof EntityManagerFactoryImpl) {
    // SessionFactoryImpl sessionFactory = ((EntityManagerFactoryImpl)
    // entityManagerFactory).getSessionFactory();
    // TypeResolver typeResolver = sessionFactory.getTypeResolver();
    // // typeResolver.registerTypeOverride(type, keys);
    // }
    // return entityManagerFactory;
  }

  /**
   * @param datatypeDescriptorManager is the {@link DatatypeDescriptorManager} to {@link Inject}.
   */
  @Inject
  public void setDatatypeDescriptorManager(DatatypeDescriptorManager datatypeDescriptorManager) {

    this.datatypeDescriptorManager = datatypeDescriptorManager;
  }

}
