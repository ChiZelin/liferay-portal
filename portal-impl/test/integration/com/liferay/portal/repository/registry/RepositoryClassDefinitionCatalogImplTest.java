/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.repository.registry;

import com.liferay.portal.kernel.repository.registry.RepositoryDefiner;
import com.liferay.portal.repository.registry.bundle.repositoryclassdefinitioncatalogimpl.TestExternalRepositoryDefiner;
import com.liferay.portal.repository.registry.bundle.repositoryclassdefinitioncatalogimpl.TestRepositoryDefiner;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.Collection;
import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class RepositoryClassDefinitionCatalogImplTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration1 = registry.registerService(
			RepositoryDefiner.class, new TestExternalRepositoryDefiner(),
			new HashMap<String, Object>() {
				{
					put("service.ranking", Integer.MAX_VALUE);
				}
			});

		_serviceRegistration2 = registry.registerService(
			RepositoryDefiner.class, new TestRepositoryDefiner(),
			new HashMap<String, Object>() {
				{
					put("service.ranking", Integer.MAX_VALUE);
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration1.unregister();
		_serviceRegistration2.unregister();
	}

	@Test
	public void testGetExternalRepositoryClassDefinitions() {
		Iterable<RepositoryClassDefinition> repositoryClassDefinitions =
			RepositoryClassDefinitionCatalogUtil.
				getExternalRepositoryClassDefinitions();

		Assert.assertTrue(
			_REPOSITORY_DEFINER_CLASS_NAME + " not found in " +
				repositoryClassDefinitions,
			_containsExternalRepositoryDefiner(repositoryClassDefinitions));
	}

	@Test
	public void testGetExternalRepositoryClassNames() {
		Collection<String> externalRepositoryClassNames =
			RepositoryClassDefinitionCatalogUtil.
				getExternalRepositoryClassNames();

		Assert.assertTrue(
			externalRepositoryClassNames.toString(),
			externalRepositoryClassNames.contains(
				_EXTERNAL_REPOSITORY_DEFINER_CLASS_NAME));
	}

	@Test
	public void testGetRepositoryClassDefinition() {
		RepositoryClassDefinition repositoryClassDefinition =
			RepositoryClassDefinitionCatalogUtil.getRepositoryClassDefinition(
				_REPOSITORY_DEFINER_CLASS_NAME);

		Assert.assertEquals(
			_REPOSITORY_DEFINER_CLASS_NAME,
			repositoryClassDefinition.getClassName());

		RepositoryClassDefinition repositoryExternalClassDefinition =
			RepositoryClassDefinitionCatalogUtil.getRepositoryClassDefinition(
				_EXTERNAL_REPOSITORY_DEFINER_CLASS_NAME);

		Assert.assertEquals(
			_EXTERNAL_REPOSITORY_DEFINER_CLASS_NAME,
			repositoryExternalClassDefinition.getClassName());
	}

	@Test
	public void testInstanceGetExternalRepositoryClassDefinitions() {
		RepositoryClassDefinitionCatalog repositoryClassDefinitionCatalog =
			RepositoryClassDefinitionCatalogUtil.
				getRepositoryClassDefinitionCatalog();

		Iterable<RepositoryClassDefinition> repositoryClassDefinitions =
			repositoryClassDefinitionCatalog.
				getExternalRepositoryClassDefinitions();

		Assert.assertTrue(
			_REPOSITORY_DEFINER_CLASS_NAME + " not found in " +
				repositoryClassDefinitions,
			_containsExternalRepositoryDefiner(repositoryClassDefinitions));
	}

	@Test
	public void testInstanceGetExternalRepositoryClassNames() {
		RepositoryClassDefinitionCatalog repositoryClassDefinitionCatalog =
			RepositoryClassDefinitionCatalogUtil.
				getRepositoryClassDefinitionCatalog();

		Collection<String> externalRepositoryClassNames =
			repositoryClassDefinitionCatalog.getExternalRepositoryClassNames();

		Assert.assertTrue(
			externalRepositoryClassNames.toString(),
			externalRepositoryClassNames.contains(
				_EXTERNAL_REPOSITORY_DEFINER_CLASS_NAME));
	}

	@Test
	public void testInstanceGetRepositoryClassDefinition() {
		RepositoryClassDefinitionCatalog repositoryClassDefinitionCatalog =
			RepositoryClassDefinitionCatalogUtil.
				getRepositoryClassDefinitionCatalog();

		RepositoryClassDefinition repositoryClassDefinition =
			repositoryClassDefinitionCatalog.getRepositoryClassDefinition(
				_REPOSITORY_DEFINER_CLASS_NAME);

		Assert.assertEquals(
			_REPOSITORY_DEFINER_CLASS_NAME,
			repositoryClassDefinition.getClassName());

		RepositoryClassDefinition repositoryExternalClassDefinition =
			repositoryClassDefinitionCatalog.getRepositoryClassDefinition(
				_EXTERNAL_REPOSITORY_DEFINER_CLASS_NAME);

		Assert.assertEquals(
			_EXTERNAL_REPOSITORY_DEFINER_CLASS_NAME,
			repositoryExternalClassDefinition.getClassName());
	}

	private boolean _containsExternalRepositoryDefiner(
		Iterable<RepositoryClassDefinition> repositoryClassDefinitions) {

		for (RepositoryClassDefinition repositoryClassDefinition :
				repositoryClassDefinitions) {

			if (_EXTERNAL_REPOSITORY_DEFINER_CLASS_NAME.equals(
					repositoryClassDefinition.getClassName())) {

				return true;
			}
		}

		return false;
	}

	private static final String _EXTERNAL_REPOSITORY_DEFINER_CLASS_NAME =
		TestExternalRepositoryDefiner.class.getName();

	private static final String _REPOSITORY_DEFINER_CLASS_NAME =
		TestRepositoryDefiner.class.getName();

	private static ServiceRegistration<RepositoryDefiner> _serviceRegistration1;
	private static ServiceRegistration<RepositoryDefiner> _serviceRegistration2;

}