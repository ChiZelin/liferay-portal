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

package com.liferay.portal.search.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.search.SearchEngineConfigurator;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceRegistration;

/**
 * @author Peter Fellwock
 */
@RunWith(Arquillian.class)
public class SearchEngineHelperImplTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		Bundle bundle = FrameworkUtil.getBundle(
			SearchEngineHelperImplTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		_serviceRegistration = bundleContext.registerService(
			SearchEngineConfigurator.class,
			(SearchEngineConfigurator)ProxyUtil.newProxyInstance(
				SearchEngineHelperImplTest.class.getClassLoader(),
				new Class<?>[] {SearchEngineConfigurator.class},
				(proxy, method, args) -> {
					if ("afterPropertiesSet".equals(method.getName())) {
						_calledAfterPropertiesSet = true;
					}

					return null;
				}),
			new HashMapDictionary<String, Object>() {
				{
					put("service.ranking", Integer.MAX_VALUE);
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration.unregister();
	}

	@Test
	public void testAfterPropertiesSet() {
		Assert.assertTrue(_calledAfterPropertiesSet);
	}

	private static boolean _calledAfterPropertiesSet;
	private static ServiceRegistration<SearchEngineConfigurator>
		_serviceRegistration;

}