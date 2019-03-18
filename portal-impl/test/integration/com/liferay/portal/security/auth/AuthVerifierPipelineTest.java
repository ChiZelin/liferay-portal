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

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.AccessControlContext;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifier;
import com.liferay.portal.kernel.servlet.HttpMethods;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.auth.bundle.authverifierpipeline.TestAuthVerifier;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;

/**
 * @author Peter Fellwock
 */
public class AuthVerifierPipelineTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			AuthVerifier.class, new TestAuthVerifier(),
			new HashMap<String, Object>() {
				{
					put("service.ranking", Integer.MAX_VALUE);
					put(
						"urls.includes",
						"/TestAuthVerifier/*,/TestAuthVerifierTest/*");
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration.unregister();
	}

	@Test
	public void testVerifyRequest() throws PortalException {
		AccessControlContext accessControlContext = new AccessControlContext();

		MockHttpServletRequest mockHttpServletRequest = createHttpRequest(
			"/foo/hello");

		mockHttpServletRequest.setAttribute(
			WebKeys.COMPANY_ID, TestPropsValues.getCompanyId());

		accessControlContext.setRequest(mockHttpServletRequest);

		AuthVerifierPipeline.verifyRequest(accessControlContext);
	}

	protected MockHttpServletRequest createHttpRequest(String pathInfo) {
		MockServletContext mockServletContext = new MockServletContext();

		mockServletContext.setContextPath(StringPool.BLANK);
		mockServletContext.setServletContextName(StringPool.BLANK);

		MockHttpServletRequest mockHttpServletRequest =
			new MockHttpServletRequest(mockServletContext);

		mockHttpServletRequest.setMethod(HttpMethods.GET);
		mockHttpServletRequest.setPathInfo(pathInfo);

		return mockHttpServletRequest;
	}

	private static ServiceRegistration<AuthVerifier> _serviceRegistration;

}