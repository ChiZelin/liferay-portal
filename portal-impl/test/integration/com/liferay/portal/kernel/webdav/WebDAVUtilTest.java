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

package com.liferay.portal.kernel.webdav;

import com.liferay.portal.kernel.webdav.bundle.webdavutil.TestWebDAVStorage;
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
 * @author Philip Jones
 */
public class WebDAVUtilTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			WebDAVStorage.class, new TestWebDAVStorage(),
			new HashMap<String, Object>() {
				{
					put("service.ranking", Integer.MAX_VALUE);
					put("webdav.storage.token", TestWebDAVStorage.TOKEN);
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration.unregister();
	}

	@Test
	public void testGetStorage() {
		WebDAVStorage webDAVStorage = WebDAVUtil.getStorage(
			TestWebDAVStorage.TOKEN);

		Class<?> clazz = webDAVStorage.getClass();

		Assert.assertEquals(TestWebDAVStorage.class.getName(), clazz.getName());
	}

	@Test
	public void testGetStorageTokens() {
		Collection<String> storageTokens = WebDAVUtil.getStorageTokens();

		Assert.assertTrue(
			storageTokens.toString(),
			storageTokens.contains(TestWebDAVStorage.TOKEN));
	}

	private static ServiceRegistration<WebDAVStorage> _serviceRegistration;

}