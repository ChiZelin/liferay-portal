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

package com.liferay.portal.deploy.hot;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.url.URLContainer;
import com.liferay.portal.kernel.util.CustomJspRegistryUtil;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.util.CustomJspRegistryImpl;
import com.liferay.portal.util.FileImpl;
import com.liferay.portal.util.PortalImpl;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceRegistration;

import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Leon Chi
 */
public class CustomJspBagRegistryUtilTest {

	@BeforeClass
	public static void setUpClass() {
		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(new PortalImpl());

		CustomJspRegistryUtil customJspRegistryUtil =
			new CustomJspRegistryUtil();

		customJspRegistryUtil.setCustomJspRegistry(new CustomJspRegistryImpl());

		FileUtil fileUtil = new FileUtil();

		fileUtil.setFile(new FileImpl());

		RegistryUtil.setRegistry(new BasicRegistryImpl());

		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration1 = registry.registerService(
			CustomJspBag.class, new TestCustomJspBag(),
			new HashMap<String, Object>() {
				{
					put("context.id", "TestCustomJspBag");
					put("context.name", "Test Custom JSP Bag");
				}
			});

		_serviceRegistration2 = registry.registerService(
			CustomJspBag.class, new TestGlobalCustomJspBag(),
			new HashMap<String, Object>() {
				{
					put("context.id", "TestGlobalCustomJspBag");
					put("context.name", "Test Global Custom JSP Bag");
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration1.unregister();
		_serviceRegistration2.unregister();
	}

	@Test
	public void testGetCustomJspBags() {
		_assertCustomJspBag(_serviceRegistration1);
	}

	@Test
	public void testGetGlobalCustomJspBags() {
		_assertCustomJspBag(_serviceRegistration2);
	}

	private void _assertCustomJspBag(
		ServiceRegistration<CustomJspBag> serviceRegistration) {

		Registry registry = RegistryUtil.getRegistry();

		Map<ServiceReference<CustomJspBag>, CustomJspBag> customJspBags =
			CustomJspBagRegistryUtil.getCustomJspBags();

		Assert.assertSame(
			registry.getService(serviceRegistration.getServiceReference()),
			customJspBags.get(serviceRegistration.getServiceReference()));
	}

	private static ServiceRegistration<CustomJspBag> _serviceRegistration1;
	private static ServiceRegistration<CustomJspBag> _serviceRegistration2;

	private static class TestCustomJspBag implements CustomJspBag {

		@Override
		public String getCustomJspDir() {
			return StringPool.SLASH;
		}

		@Override
		public List<String> getCustomJsps() {
			return _customJsps;
		}

		@Override
		public URLContainer getURLContainer() {
			return _urlContainer;
		}

		@Override
		public boolean isCustomJspGlobal() {
			return false;
		}

		private final List<String> _customJsps = new ArrayList<>();

		private final URLContainer _urlContainer = new URLContainer() {

			@Override
			public URL getResource(String name) {
				Class<?> clazz = getClass();

				return clazz.getResource("dependencies/bottom-ext.jsp");
			}

			@Override
			public Set<String> getResources(String path) {
				return Collections.singleton(
					"/html/common/themes/bottom-ext.jsp");
			}

		};

	}

	private static class TestGlobalCustomJspBag implements CustomJspBag {

		@Override
		public String getCustomJspDir() {
			return StringPool.SLASH;
		}

		@Override
		public List<String> getCustomJsps() {
			return _customJsps;
		}

		@Override
		public URLContainer getURLContainer() {
			return _urlContainer;
		}

		@Override
		public boolean isCustomJspGlobal() {
			return true;
		}

		private final List<String> _customJsps = new ArrayList<>();

		private final URLContainer _urlContainer = new URLContainer() {

			@Override
			public URL getResource(String name) {
				Class<?> clazz = getClass();

				return clazz.getResource("dependencies/bottom-ext.jsp");
			}

			@Override
			public Set<String> getResources(String path) {
				return Collections.singleton(
					"/html/common/themes/bottom-ext.jsp");
			}

		};

	}

}