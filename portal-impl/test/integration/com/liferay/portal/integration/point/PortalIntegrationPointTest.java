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

package com.liferay.portal.integration.point;

import aQute.bnd.osgi.Builder;
import aQute.bnd.osgi.Jar;

import com.liferay.portal.integration.point.bundle.portalintegrationpoint.TestPortalIntegrationPoint;
import com.liferay.portal.kernel.display.context.TestDisplayContextFactory;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayOutputStream;
import com.liferay.portal.kernel.util.ServiceProxyFactory;
import com.liferay.portal.module.framework.ModuleFrameworkUtilAdapter;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceReference;
import com.liferay.registry.ServiceTracker;
import com.liferay.registry.ServiceTrackerCustomizer;

import java.io.File;
import java.io.InputStream;

import java.net.URL;

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Leon Chi
 */
public class PortalIntegrationPointTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() throws Exception {
		InputStream inputStream = _createBundle(
			PortalIntegrationPointTest.class, "bundle.portalintegrationpoint");

		_bundleId = ModuleFrameworkUtilAdapter.addBundle(
			PortalIntegrationPointTest.class.getName(), inputStream);

		ModuleFrameworkUtilAdapter.startBundle(_bundleId);
	}

	@AfterClass
	public static void tearDownClass() throws PortalException {
		ModuleFrameworkUtilAdapter.stopBundle(_bundleId);

		ModuleFrameworkUtilAdapter.uninstallBundle(_bundleId);
	}

	@Test
	public void testPortalIntegrationPoint() {
		_testPortalIntegrationPointWithServiceProxyFactory();

		_testPortalIntegrationPointWithServiceTracker(true);
		_testPortalIntegrationPointWithServiceTracker(false);
	}

	private static InputStream _createBundle(
			Class<?> clazz, String bundlePackageName)
		throws Exception {

		URL url = clazz.getResource("");

		String protocol = url.getProtocol();

		if (!protocol.equals("file")) {
			throw new IllegalStateException(
				"Test classes are not on the file system");
		}

		String basePath = url.getPath();

		Package pkg = clazz.getPackage();

		String packageName = pkg.getName();

		int index = basePath.indexOf(packageName.replace('.', '/') + '/');

		basePath = basePath.substring(0, index);

		File baseDir = new File(basePath);

		try (Builder builder = new Builder();
			InputStream inputStream = clazz.getResourceAsStream(
				bundlePackageName.replace('.', '/') + "/bnd.bnd")) {

			builder.setBundleSymbolicName(clazz.getName());
			builder.setBase(baseDir);
			builder.setClasspath(new File[] {baseDir});
			builder.setProperty(
				"bundle.package", packageName + "." + bundlePackageName);

			Properties properties = builder.getProperties();

			properties.load(inputStream);

			try (Jar jar = builder.build()) {
				UnsyncByteArrayOutputStream outputStream =
					new UnsyncByteArrayOutputStream();

				jar.write(outputStream);

				return new UnsyncByteArrayInputStream(
					outputStream.unsafeGetByteArray(), 0, outputStream.size());
			}
		}
	}

	private void _testPortalIntegrationPointWithServiceProxyFactory() {
		_testDisplayContextFactory =
			ServiceProxyFactory.newServiceTrackedInstance(
				TestDisplayContextFactory.class,
				PortalIntegrationPointTest.class, "_testDisplayContextFactory",
				false);

		Class<?> clazz = _testDisplayContextFactory.getClass();

		Assert.assertEquals(
			TestPortalIntegrationPoint.class.getName(), clazz.getName());
	}

	private void _testPortalIntegrationPointWithServiceTracker(
		boolean useServiceTrackerCustomizer) {

		Registry registry = RegistryUtil.getRegistry();

		ServiceTracker<TestDisplayContextFactory, TestDisplayContextFactory>
			serviceTracker;

		if (useServiceTrackerCustomizer) {
			serviceTracker = registry.trackServices(
				TestDisplayContextFactory.class,
				new TestDisplayContextFactoryServiceTrackerCustomizer());
		}
		else {
			serviceTracker = registry.trackServices(
				TestDisplayContextFactory.class);
		}

		serviceTracker.open();

		try {
			TestDisplayContextFactory testDisplayContextFactory =
				serviceTracker.getService();

			Class<?> clazz = testDisplayContextFactory.getClass();

			Assert.assertSame(
				TestPortalIntegrationPoint.class.getName(), clazz.getName());
		}
		finally {
			serviceTracker.close();
		}
	}

	private static Long _bundleId;

	private TestDisplayContextFactory _testDisplayContextFactory;

	private static class TestDisplayContextFactoryServiceTrackerCustomizer
		implements ServiceTrackerCustomizer
			<TestDisplayContextFactory, TestDisplayContextFactory> {

		@Override
		public TestDisplayContextFactory addingService(
			ServiceReference<TestDisplayContextFactory> serviceReference) {

			Registry registry = RegistryUtil.getRegistry();

			return registry.getService(serviceReference);
		}

		@Override
		public void modifiedService(
			ServiceReference<TestDisplayContextFactory> serviceReference,
			TestDisplayContextFactory testDisplayContextFactory) {
		}

		@Override
		public void removedService(
			ServiceReference<TestDisplayContextFactory> serviceReference,
			TestDisplayContextFactory testDisplayContextFactory) {

			Registry registry = RegistryUtil.getRegistry();

			registry.ungetService(serviceReference);
		}

	}

}