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

package com.liferay.portal.security.auth.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.security.auth.AuthException;
import com.liferay.portal.kernel.security.auth.AuthFailure;
import com.liferay.portal.kernel.security.auth.Authenticator;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.HashMapDictionary;
import com.liferay.portal.security.auth.AuthPipeline;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
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
 * @author Philip Jones
 */
@RunWith(Arquillian.class)
public class AuthPipelineTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		Bundle bundle = FrameworkUtil.getBundle(AuthPipelineTest.class);

		BundleContext bundleContext = bundle.getBundleContext();

		_authFailureServiceRegistration = bundleContext.registerService(
			AuthFailure.class, new TestAuthFailure(),
			new HashMapDictionary<String, Object>() {
				{
					put(
						"key",
						new String[] {"auth.failure", "auth.max.failures"});
					put("service.ranking", Integer.MAX_VALUE);
				}
			});

		_authenticatorServiceRegistration = bundleContext.registerService(
			Authenticator.class, new TestAuthenticator(),
			new HashMapDictionary<String, Object>() {
				{
					put("key", "auth.pipeline.pre");
					put("service.ranking", Integer.MAX_VALUE);
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_authFailureServiceRegistration.unregister();
		_authenticatorServiceRegistration.unregister();
	}

	@Before
	public void setUp() {
		_called = false;
	}

	@Test
	public void testAuthenticateByEmailAddress() throws AuthException {
		AuthPipeline.authenticateByEmailAddress(
			"auth.pipeline.pre", 0, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), null, null);

		Assert.assertTrue(_called);
	}

	@Test
	public void testAuthenticateByScreenName() throws AuthException {
		AuthPipeline.authenticateByScreenName(
			"auth.pipeline.pre", 0, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), null, null);

		Assert.assertTrue(_called);
	}

	@Test
	public void testAuthenticateByUserId() throws AuthException {
		AuthPipeline.authenticateByUserId(
			"auth.pipeline.pre", 0, RandomTestUtil.randomLong(),
			RandomTestUtil.randomString(), null, null);

		Assert.assertTrue(_called);
	}

	@Test
	public void testOnFailureByScreenName() {
		try {
			AuthPipeline.onFailureByScreenName(
				"auth.failure", 0, RandomTestUtil.randomString(), null, null);
		}
		catch (AuthException ae) {
		}

		Assert.assertTrue(_called);
	}

	@Test
	public void testOnFailureByUserId() {
		try {
			AuthPipeline.onFailureByUserId(
				"auth.failure", 0, RandomTestUtil.randomLong(), null, null);
		}
		catch (AuthException ae) {
		}

		Assert.assertTrue(_called);
	}

	@Test
	public void testOnMaxFailuresByEmailAddress() {
		try {
			AuthPipeline.onMaxFailuresByEmailAddress(
				"auth.max.failures", 0, RandomTestUtil.randomString(), null,
				null);
		}
		catch (AuthException ae) {
		}

		Assert.assertTrue(_called);
	}

	@Test
	public void testOnMaxFailuresByScreenName() {
		try {
			AuthPipeline.onMaxFailuresByScreenName(
				"auth.max.failures", 0, RandomTestUtil.randomString(), null,
				null);
		}
		catch (AuthException ae) {
		}

		Assert.assertTrue(_called);
	}

	@Test
	public void testOnMaxFailuresByUserId() {
		try {
			AuthPipeline.onMaxFailuresByUserId(
				"auth.max.failures", 0, RandomTestUtil.randomLong(), null,
				null);
		}
		catch (AuthException ae) {
		}

		Assert.assertTrue(_called);
	}

	private static ServiceRegistration<Authenticator>
		_authenticatorServiceRegistration;
	private static ServiceRegistration<AuthFailure>
		_authFailureServiceRegistration;
	private static boolean _called;

	private static class TestAuthenticator implements Authenticator {

		@Override
		public int authenticateByEmailAddress(
			long companyId, String emailAddress, String password,
			Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) {

			_called = true;

			return Authenticator.SUCCESS;
		}

		@Override
		public int authenticateByScreenName(
			long companyId, String screenName, String password,
			Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) {

			_called = true;

			return Authenticator.SUCCESS;
		}

		@Override
		public int authenticateByUserId(
			long companyId, long userId, String password,
			Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) {

			_called = true;

			return Authenticator.SUCCESS;
		}

	}

	private static class TestAuthFailure implements AuthFailure {

		@Override
		public void onFailureByEmailAddress(
			long companyId, String emailAddress,
			Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) {

			_called = true;
		}

		@Override
		public void onFailureByScreenName(
			long companyId, String screenName, Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) {

			_called = true;
		}

		@Override
		public void onFailureByUserId(
			long companyId, long userId, Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) {

			_called = true;
		}

	}

}