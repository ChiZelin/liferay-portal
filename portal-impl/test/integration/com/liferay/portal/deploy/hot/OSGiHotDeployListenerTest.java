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

package com.liferay.portal.deploy.hot;

import com.liferay.portal.kernel.deploy.hot.DependencyManagementThreadLocal;
import com.liferay.portal.kernel.deploy.hot.HotDeployEvent;
import com.liferay.portal.kernel.deploy.hot.HotDeployException;
import com.liferay.portal.kernel.deploy.hot.HotDeployListener;
import com.liferay.portal.kernel.util.ProxyFactory;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.test.AtomicState;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.ServletContext;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Adolfo Pérez
 */
public class OSGiHotDeployListenerTest {

	@ClassRule
	@Rule
	public static final LiferayIntegrationTestRule liferayIntegrationTestRule =
		new LiferayIntegrationTestRule();

	@BeforeClass
	public static void setUpClass() {
		_atomicState = new AtomicState();

		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration = registry.registerService(
			HotDeployListener.class, new TestHotDeployListener());
	}

	@AfterClass
	public static void tearDownClass() {
		_atomicState.close();

		_serviceRegistration.unregister();
	}

	@Before
	public void setUp() {
		_dependencyManagerEnabled = DependencyManagementThreadLocal.isEnabled();

		DependencyManagementThreadLocal.setEnabled(false);
	}

	@After
	public void tearDown() {
		DependencyManagementThreadLocal.setEnabled(_dependencyManagerEnabled);
	}

	@Test
	public void testInvokeDeploy() throws HotDeployException {
		_hotDeployListener.invokeDeploy(
			new HotDeployEvent(_servletContext, null));

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testInvokeUndeploy() throws HotDeployException {
		_hotDeployListener.invokeUndeploy(
			new HotDeployEvent(_servletContext, null));

		Assert.assertTrue(_atomicState.isSet());
	}

	private static AtomicState _atomicState;
	private static ServiceRegistration<HotDeployListener> _serviceRegistration;

	private boolean _dependencyManagerEnabled;
	private final HotDeployListener _hotDeployListener =
		new OSGiHotDeployListener();
	private final ServletContext _servletContext =
		ProxyFactory.newDummyInstance(ServletContext.class);

	private static class TestHotDeployListener implements HotDeployListener {

		@Override
		public void invokeDeploy(HotDeployEvent event) {
			_atomicBoolean.set(Boolean.TRUE);
		}

		@Override
		public void invokeUndeploy(HotDeployEvent event) {
			_atomicBoolean.set(Boolean.TRUE);
		}

		protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
			_atomicBoolean = atomicBoolean;
		}

		private AtomicBoolean _atomicBoolean;

	}

}