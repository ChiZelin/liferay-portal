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

package com.liferay.portal.servlet.filters.autologin;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.security.auto.login.AutoLogin;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.portal.util.PortalImpl;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Leon Chi
 */
public class AutoLoginFilterTest {

	@BeforeClass
	public static void setUpClass() {
		PortalUtil portalUtil = new PortalUtil();

		portalUtil.setPortal(new PortalImpl());

		RegistryUtil.setRegistry(new BasicRegistryImpl());

		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			AutoLogin.class, new TestAutoLogin());
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration.unregister();
	}

	@Test
	public void testDoFilter() throws IOException, ServletException {
		_called = false;

		AutoLoginFilter autoLoginFilter = new AutoLoginFilter();

		autoLoginFilter.doFilter(
			new HttpServletRequestWrapper(
				ProxyFactory.newDummyInstance(HttpServletRequest.class)) {

				@Override
				public String getRequestURI() {
					return StringPool.BLANK;
				}

				@Override
				public HttpSession getSession() {
					return ProxyFactory.newDummyInstance(HttpSession.class);
				}

			},
			null, ProxyFactory.newDummyInstance(FilterChain.class));

		Assert.assertTrue(_called);
	}

	private static boolean _called;
	private static ServiceRegistration<AutoLogin> _serviceRegistration;

	private static class TestAutoLogin implements AutoLogin {

		@Override
		public String[] handleException(
			HttpServletRequest request, HttpServletResponse response,
			Exception e) {

			return null;
		}

		@Override
		public String[] login(
			HttpServletRequest request, HttpServletResponse response) {

			_called = true;

			return null;
		}

	}

}