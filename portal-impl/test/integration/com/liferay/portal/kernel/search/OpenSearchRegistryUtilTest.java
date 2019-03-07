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

package com.liferay.portal.kernel.search;

import com.liferay.portal.kernel.search.bundle.opensearchregistryutil.TestOpenSearch;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class OpenSearchRegistryUtilTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			OpenSearch.class, new TestOpenSearch());
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration.unregister();
	}

	@Test
	public void testGetOpenSearch() {
		OpenSearch openSearch = OpenSearchRegistryUtil.getOpenSearch(
			TestOpenSearch.class);

		Assert.assertEquals(
			TestOpenSearch.class.getName(), openSearch.getClassName());
	}

	@Test
	public void testGetOpenSearchInstances() {
		boolean exists = false;

		List<OpenSearch> openSearches =
			OpenSearchRegistryUtil.getOpenSearchInstances();

		for (OpenSearch openSearch : openSearches) {
			String openSearchClassName = openSearch.getClassName();

			if (openSearchClassName.equals(TestOpenSearch.class.getName())) {
				exists = true;

				break;
			}
		}

		Assert.assertTrue(exists);
	}

	private static ServiceRegistration<OpenSearch> _serviceRegistration;

}