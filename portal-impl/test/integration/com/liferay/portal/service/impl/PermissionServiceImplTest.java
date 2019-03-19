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

package com.liferay.portal.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.permission.BaseModelPermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.PermissionService;
import com.liferay.portal.kernel.service.PermissionServiceUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.test.AtomicState;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.HashMap;
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
public class PermissionServiceImplTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		_atomicState = new AtomicState();

		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			BaseModelPermissionChecker.class,
			new TestBaseModelPermissionChecker(),
			new HashMap<String, Object>() {
				{
					put("model.class.name", "PermissionServiceImplTest");
					put("service.ranking", Integer.MAX_VALUE);
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_atomicState.close();

		_serviceRegistration.unregister();
	}

	@Test
	public void testCheckPermission1() throws PortalException {
		PermissionService permissionService =
			PermissionServiceUtil.getService();

		_atomicState.reset();

		permissionService.checkPermission(0, "PermissionServiceImplTest", 0);

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testCheckPermission2() throws PortalException {
		PermissionService permissionService =
			PermissionServiceUtil.getService();

		_atomicState.reset();

		permissionService.checkPermission(0, "PermissionServiceImplTest", null);

		Assert.assertTrue(_atomicState.isSet());
	}

	private static AtomicState _atomicState;
	private static ServiceRegistration<BaseModelPermissionChecker>
		_serviceRegistration;

	private static class TestBaseModelPermissionChecker
		implements BaseModelPermissionChecker {

		@Override
		public void checkBaseModel(
			PermissionChecker permissionChecker, long groupId, long primaryKey,
			String actionId) {

			_atomicBoolean.set(Boolean.TRUE);
		}

		protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
			_atomicBoolean = atomicBoolean;
		}

		private AtomicBoolean _atomicBoolean;

	}

}