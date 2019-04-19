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

package com.liferay.portal.xmlrpc;

import com.liferay.portal.kernel.xmlrpc.Method;
import com.liferay.portal.kernel.xmlrpc.Response;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Leon Chi
 */
public class XmlRpcMethodUtilTest {

	@BeforeClass
	public static void setUpClass() {
		RegistryUtil.setRegistry(new BasicRegistryImpl());

		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			Method.class, new TestMethod());
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration.unregister();
	}

	@Test
	public void testNoReturn() {
		Method method = XmlRpcMethodUtil.getMethod(
			TestMethod.TOKEN, TestMethod.METHOD_NAME);

		Class<?> clazz = method.getClass();

		Assert.assertEquals(TestMethod.class.getName(), clazz.getName());
	}

	private static ServiceRegistration<Method> _serviceRegistration;

	private static class TestMethod implements Method {

		public static final String METHOD_NAME = "METHOD_NAME";

		public static final String TOKEN = "TOKEN";

		@Override
		public Response execute(long companyId) {
			return null;
		}

		@Override
		public String getMethodName() {
			return METHOD_NAME;
		}

		@Override
		public String getToken() {
			return TOKEN;
		}

		@Override
		public boolean setArguments(Object[] arguments) {
			return false;
		}

	}

}