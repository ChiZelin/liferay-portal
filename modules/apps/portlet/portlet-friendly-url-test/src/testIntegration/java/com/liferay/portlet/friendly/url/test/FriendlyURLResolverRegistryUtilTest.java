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

package com.liferay.portlet.friendly.url.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.LayoutFriendlyURLComposite;
import com.liferay.portal.kernel.portlet.FriendlyURLResolver;
import com.liferay.portal.kernel.portlet.FriendlyURLResolverRegistryUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Collection;
import java.util.Map;

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
 * @author Raymond Augé
 */
@RunWith(Arquillian.class)
public class FriendlyURLResolverRegistryUtilTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		Bundle bundle = FrameworkUtil.getBundle(
			FriendlyURLResolverRegistryUtilTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		_serviceRegistration = bundleContext.registerService(
			FriendlyURLResolver.class, new TestFriendlyURLResolver(),
			new HashMapDictionary());
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration.unregister();
	}

	@Test
	public void testGetFriendlyURLResolver() throws Exception {
		Collection<FriendlyURLResolver> collection =
			FriendlyURLResolverRegistryUtil.
				getFriendlyURLResolversAsCollection();

		Assert.assertFalse(collection.toString(), collection.isEmpty());

		FriendlyURLResolver friendlyURLResolver =
			FriendlyURLResolverRegistryUtil.getFriendlyURLResolver(
				TestFriendlyURLResolver.SEPARATOR);

		Assert.assertNotNull(friendlyURLResolver);

		Class<?> clazz = friendlyURLResolver.getClass();

		Assert.assertEquals(
			TestFriendlyURLResolver.class.getName(), clazz.getName());
	}

	@Test
	public void testOverride() throws Exception {
		Bundle bundle = FrameworkUtil.getBundle(
			FriendlyURLResolverRegistryUtilTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		FriendlyURLResolver friendlyURLResolver1 =
			new OverrideFriendlyURLResolver();

		ServiceRegistration<FriendlyURLResolver> serviceRegistration1 =
			bundleContext.registerService(
				FriendlyURLResolver.class, friendlyURLResolver1,
				new HashMapDictionary<String, Object>() {
					{
						put("service.ranking", 25);
					}
				});

		ServiceRegistration<FriendlyURLResolver> serviceRegistration2 = null;

		try {
			Collection<FriendlyURLResolver> collection =
				FriendlyURLResolverRegistryUtil.
					getFriendlyURLResolversAsCollection();

			Assert.assertFalse(collection.toString(), collection.isEmpty());

			FriendlyURLResolver friendlyURLResolver =
				FriendlyURLResolverRegistryUtil.getFriendlyURLResolver(
					TestFriendlyURLResolver.SEPARATOR);

			Assert.assertNotNull(friendlyURLResolver);

			Assert.assertEquals(friendlyURLResolver1, friendlyURLResolver);

			FriendlyURLResolver friendlyURLResolver2 =
				new OverrideFriendlyURLResolver();

			serviceRegistration2 = bundleContext.registerService(
				FriendlyURLResolver.class, friendlyURLResolver2,
				new HashMapDictionary<String, Object>() {
					{
						put("service.ranking", 12);
					}
				});

			friendlyURLResolver =
				FriendlyURLResolverRegistryUtil.getFriendlyURLResolver(
					TestFriendlyURLResolver.SEPARATOR);

			Assert.assertNotNull(friendlyURLResolver);

			Assert.assertEquals(
				"Should still be 1 since it is higher rankged.",
				friendlyURLResolver1, friendlyURLResolver);
		}
		finally {
			serviceRegistration1.unregister();

			if (serviceRegistration2 != null) {
				serviceRegistration2.unregister();
			}

			FriendlyURLResolver friendlyURLResolver =
				FriendlyURLResolverRegistryUtil.getFriendlyURLResolver(
					TestFriendlyURLResolver.SEPARATOR);

			Assert.assertNotNull(friendlyURLResolver);

			Class<?> clazz = friendlyURLResolver.getClass();

			Assert.assertEquals(
				TestFriendlyURLResolver.class.getName(), clazz.getName());
		}
	}

	private static ServiceRegistration<FriendlyURLResolver>
		_serviceRegistration;

	private static class TestFriendlyURLResolver
		implements FriendlyURLResolver {

		public static final String SEPARATOR = "/-foo-";

		@Override
		public String getActualURL(
				long companyId, long groupId, boolean privateLayout,
				String mainPath, String friendlyURL,
				Map<String, String[]> params,
				Map<String, Object> requestContext)
			throws PortalException {

			return null;
		}

		@Override
		public LayoutFriendlyURLComposite getLayoutFriendlyURLComposite(
				long companyId, long groupId, boolean privateLayout,
				String friendlyURL, Map<String, String[]> params,
				Map<String, Object> requestContext)
			throws PortalException {

			return null;
		}

		@Override
		public String getURLSeparator() {
			return SEPARATOR;
		}

	}

	private class OverrideFriendlyURLResolver implements FriendlyURLResolver {

		@Override
		public String getActualURL(
				long companyId, long groupId, boolean privateLayout,
				String mainPath, String friendlyURL,
				Map<String, String[]> params,
				Map<String, Object> requestContext)
			throws PortalException {

			return null;
		}

		@Override
		public LayoutFriendlyURLComposite getLayoutFriendlyURLComposite(
				long companyId, long groupId, boolean privateLayout,
				String friendlyURL, Map<String, String[]> params,
				Map<String, Object> requestContext)
			throws PortalException {

			return null;
		}

		@Override
		public String getURLSeparator() {
			return TestFriendlyURLResolver.SEPARATOR;
		}

	}

}