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

import com.liferay.portal.kernel.security.auth.AuthException;
import com.liferay.portal.kernel.security.auth.AuthFailure;
import com.liferay.portal.kernel.security.auth.Authenticator;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.test.AtomicState;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Philip Jones
 */
public class AuthPipelineTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		_atomicState = new AtomicState();

		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration1 = registry.registerService(
			AuthFailure.class, new TestAuthFailure(),
			new HashMap<String, Object>() {
				{
					put(
						"key",
						new String[] {"auth.failure", "auth.max.failures"});
					put("service.ranking", Integer.MAX_VALUE);
				}
			});

		_serviceRegistration2 = registry.registerService(
			Authenticator.class, new TestAuthenticator(),
			new HashMap<String, Object>() {
				{
					put("key", "auth.pipeline.pre");
					put("service.ranking", Integer.MAX_VALUE);
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_atomicState.close();

		_serviceRegistration1.unregister();
		_serviceRegistration2.unregister();
	}

	@Test
	public void testAuthenticateByEmailAddress() throws AuthException {
		_atomicState.reset();

		AuthPipeline.authenticateByEmailAddress(
			"auth.pipeline.pre", 0, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), null, null);

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testAuthenticateByScreenName() throws AuthException {
		_atomicState.reset();

		AuthPipeline.authenticateByScreenName(
			"auth.pipeline.pre", 0, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), null, null);

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testAuthenticateByUserId() throws AuthException {
		_atomicState.reset();

		AuthPipeline.authenticateByUserId(
			"auth.pipeline.pre", 0, RandomTestUtil.randomLong(),
			RandomTestUtil.randomString(), null, null);

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testOnFailureByScreenName() {
		_atomicState.reset();

		try {
			AuthPipeline.onFailureByScreenName(
				"auth.failure", 0, RandomTestUtil.randomString(), null, null);
		}
		catch (AuthException ae) {
		}

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testOnFailureByUserId() {
		_atomicState.reset();

		try {
			AuthPipeline.onFailureByUserId(
				"auth.failure", 0, RandomTestUtil.randomLong(), null, null);
		}
		catch (AuthException ae) {
		}

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testOnMaxFailuresByEmailAddress() {
		_atomicState.reset();

		try {
			AuthPipeline.onMaxFailuresByEmailAddress(
				"auth.max.failures", 0, RandomTestUtil.randomString(), null,
				null);
		}
		catch (AuthException ae) {
		}

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testOnMaxFailuresByScreenName() {
		_atomicState.reset();

		try {
			AuthPipeline.onMaxFailuresByScreenName(
				"auth.max.failures", 0, RandomTestUtil.randomString(), null,
				null);
		}
		catch (AuthException ae) {
		}

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testOnMaxFailuresByUserId() {
		_atomicState.reset();

		try {
			AuthPipeline.onMaxFailuresByUserId(
				"auth.max.failures", 0, RandomTestUtil.randomLong(), null,
				null);
		}
		catch (AuthException ae) {
		}

		Assert.assertTrue(_atomicState.isSet());
	}

	private static AtomicState _atomicState;
	private static ServiceRegistration<AuthFailure> _serviceRegistration1;
	private static ServiceRegistration<Authenticator> _serviceRegistration2;

	private static class TestAuthenticator implements Authenticator {

		@Override
		public int authenticateByEmailAddress(
			long companyId, String emailAddress, String password,
			Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) {

			_atomicBoolean.set(Boolean.TRUE);

			return Authenticator.SUCCESS;
		}

		@Override
		public int authenticateByScreenName(
			long companyId, String screenName, String password,
			Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) {

			_atomicBoolean.set(Boolean.TRUE);

			return Authenticator.SUCCESS;
		}

		@Override
		public int authenticateByUserId(
			long companyId, long userId, String password,
			Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) {

			_atomicBoolean.set(Boolean.TRUE);

			return Authenticator.SUCCESS;
		}

		protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
			_atomicBoolean = atomicBoolean;
		}

		private AtomicBoolean _atomicBoolean;

	}

	private static class TestAuthFailure implements AuthFailure {

		@Override
		public void onFailureByEmailAddress(
			long companyId, String emailAddress,
			Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) {

			_atomicBoolean.set(Boolean.TRUE);
		}

		@Override
		public void onFailureByScreenName(
			long companyId, String screenName, Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) {

			_atomicBoolean.set(Boolean.TRUE);
		}

		@Override
		public void onFailureByUserId(
			long companyId, long userId, Map<String, String[]> headerMap,
			Map<String, String[]> parameterMap) {

			_atomicBoolean.set(Boolean.TRUE);
		}

		protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
			_atomicBoolean = atomicBoolean;
		}

		private AtomicBoolean _atomicBoolean;

	}

}