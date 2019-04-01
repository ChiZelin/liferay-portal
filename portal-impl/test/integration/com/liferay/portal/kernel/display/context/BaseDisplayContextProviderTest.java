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

package com.liferay.portal.kernel.display.context;

import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.Iterator;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class BaseDisplayContextProviderTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		_baseDisplayContextProvider = new BaseDisplayContextProvider<>(
			DisplayContextFactory.class);

		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			DisplayContextFactory.class,
			new TestBaseDisplayContextFactoryImpl());
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		_baseDisplayContextProvider.destroy();
		_serviceRegistration.unregister();
	}

	@Test
	public void testDisplayContextHasBeenRegistered() throws Exception {
		DisplayContextFactory displayContextFactoryExtension = null;

		Iterable<DisplayContextFactory> displayContextFactories =
			_baseDisplayContextProvider.getDisplayContextFactories();

		Iterator<DisplayContextFactory> iterator =
			displayContextFactories.iterator();

		while (iterator.hasNext()) {
			DisplayContextFactory displayContextFactory = iterator.next();

			Class<?> clazz = displayContextFactory.getClass();

			String className = clazz.getName();

			if (className.equals(
					TestBaseDisplayContextFactoryImpl.class.getName())) {

				displayContextFactoryExtension = displayContextFactory;
			}
		}

		Assert.assertNotNull(displayContextFactoryExtension);
	}

	private static BaseDisplayContextProvider<DisplayContextFactory>
		_baseDisplayContextProvider;
	private static ServiceRegistration<DisplayContextFactory>
		_serviceRegistration;

	private static class TestBaseDisplayContextFactoryImpl
		implements DisplayContextFactory {
	}

}