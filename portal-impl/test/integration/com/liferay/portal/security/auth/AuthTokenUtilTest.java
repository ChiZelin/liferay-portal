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

package com.liferay.portal.security.auth;

import com.liferay.portal.kernel.portlet.LiferayPortletURL;
import com.liferay.portal.kernel.portlet.PortletURLFactoryUtil;
import com.liferay.portal.kernel.security.auth.AuthToken;
import com.liferay.portal.kernel.security.auth.AuthTokenUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.PortletKeys;
import com.liferay.portal.security.auth.bundle.authtokenutil.TestAuthToken;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.HashMap;

import javax.portlet.PortletRequest;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * @author Manuel de la Peña
 */
public class AuthTokenUtilTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			AuthToken.class, new TestAuthToken(),
			new HashMap<String, Object>() {
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
	public void testAddCSRFToken() {
		MockHttpServletRequest request = new MockHttpServletRequest();

		LiferayPortletURL liferayPortletURL = PortletURLFactoryUtil.create(
			request, PortletKeys.PORTAL, 0, PortletRequest.ACTION_PHASE);

		AuthTokenUtil.addCSRFToken(request, liferayPortletURL);

		Assert.assertEquals(
			"TEST_TOKEN", liferayPortletURL.getParameter("p_auth"));
	}

	@Test
	public void testAddPortletInvocationToken() {
		MockHttpServletRequest request = new MockHttpServletRequest();

		LiferayPortletURL liferayPortletURL = PortletURLFactoryUtil.create(
			request, PortletKeys.PORTAL, 0, PortletRequest.ACTION_PHASE);

		AuthTokenUtil.addPortletInvocationToken(request, liferayPortletURL);

		Assert.assertEquals(
			"TEST_TOKEN_BY_PLID_AND_PORTLET_ID",
			liferayPortletURL.getParameter("p_p_auth"));
	}

	@Test
	public void testGetToken() {
		Assert.assertEquals(
			"TEST_TOKEN", AuthTokenUtil.getToken(new MockHttpServletRequest()));
	}

	@Test
	public void testGetTokenByPlidAndPortletId() {
		Assert.assertEquals(
			"TEST_TOKEN_BY_PLID_AND_PORTLET_ID",
			AuthTokenUtil.getToken(
				new MockHttpServletRequest(), 0L,
				RandomTestUtil.randomString()));
	}

	@Test
	public void testIsValidPortletInvocationToken() {
		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest();

		mockHttpServletRequest.setParameter(
			"p_p_auth", "VALID_PORTLET_INVOCATION_TOKEN");

		Assert.assertTrue(
			AuthTokenUtil.isValidPortletInvocationToken(
				mockHttpServletRequest, null, null));

		mockHttpServletRequest.setParameter(
			"p_p_auth", "INVALID_PORTLET_INVOCATION_TOKEN");

		Assert.assertFalse(
			AuthTokenUtil.isValidPortletInvocationToken(
				mockHttpServletRequest, null, null));
	}

	private static ServiceRegistration<AuthToken> _serviceRegistration;

}