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

package com.liferay.portal.kernel.lar.lifecycle;

import com.liferay.exportimport.kernel.lifecycle.ExportImportLifecycleEvent;
import com.liferay.exportimport.kernel.lifecycle.ExportImportLifecycleEventListenerRegistryUtil;
import com.liferay.exportimport.kernel.lifecycle.ExportImportLifecycleListener;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.HashMap;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class ExportImportLifecycleEventListenerRegistryUtilTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration1 = registry.registerService(
			ExportImportLifecycleListener.class,
			new TestAsyncExportImportLifecycleListener(),
			new HashMap<String, Object>() {
				{
					put("service.ranking", Integer.MAX_VALUE);
				}
			});

		_serviceRegistration2 = registry.registerService(
			ExportImportLifecycleListener.class,
			new TestSyncExportImportLifecycleListener(),
			new HashMap<String, Object>() {
				{
					put("service.ranking", Integer.MAX_VALUE);
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration1.unregister();
		_serviceRegistration2.unregister();
	}

	@Test
	public void testGetAsyncExportImportLifecycleListeners() {
		boolean exists = false;

		Set<ExportImportLifecycleListener> exportImportLifecycleListeners =
			ExportImportLifecycleEventListenerRegistryUtil.
				getAsyncExportImportLifecycleListeners();

		for (ExportImportLifecycleListener exportImportLifecycleListener :
				exportImportLifecycleListeners) {

			Class<?> clazz = exportImportLifecycleListener.getClass();

			String className = clazz.getName();

			Assert.assertNotEquals(
				TestSyncExportImportLifecycleListener.class.getName(),
				className);

			if (className.equals(
					TestAsyncExportImportLifecycleListener.class.getName())) {

				exists = true;

				break;
			}
		}

		Assert.assertTrue(exists);
	}

	@Test
	public void testGetSyncExportImportLifecycleListeners() {
		boolean exists = false;

		Set<ExportImportLifecycleListener> exportImportLifecycleListeners =
			ExportImportLifecycleEventListenerRegistryUtil.
				getSyncExportImportLifecycleListeners();

		for (ExportImportLifecycleListener exportImportLifecycleListener :
				exportImportLifecycleListeners) {

			Class<?> clazz = exportImportLifecycleListener.getClass();

			String className = clazz.getName();

			Assert.assertNotEquals(
				TestAsyncExportImportLifecycleListener.class.getName(),
				className);

			if (className.equals(
					TestSyncExportImportLifecycleListener.class.getName())) {

				exists = true;

				break;
			}
		}

		Assert.assertTrue(exists);
	}

	private static ServiceRegistration<ExportImportLifecycleListener>
		_serviceRegistration1;
	private static ServiceRegistration<ExportImportLifecycleListener>
		_serviceRegistration2;

	private static class TestAsyncExportImportLifecycleListener
		implements ExportImportLifecycleListener {

		@Override
		public boolean isParallel() {
			return true;
		}

		@Override
		public void onExportImportLifecycleEvent(
				ExportImportLifecycleEvent exportImportLifecycleEvent)
			throws Exception {
		}

	}

	private static class TestSyncExportImportLifecycleListener
		implements ExportImportLifecycleListener {

		@Override
		public boolean isParallel() {
			return false;
		}

		@Override
		public void onExportImportLifecycleEvent(
				ExportImportLifecycleEvent exportImportLifecycleEvent)
			throws Exception {
		}

	}

}