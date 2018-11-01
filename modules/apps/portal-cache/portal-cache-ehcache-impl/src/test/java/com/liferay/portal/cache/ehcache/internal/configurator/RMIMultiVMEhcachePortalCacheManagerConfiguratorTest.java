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

package com.liferay.portal.cache.ehcache.internal.configurator;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.cache.configuration.PortalCacheManagerConfiguration;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.util.Collections;
import java.util.List;

import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.FactoryConfiguration;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Leon Chi
 */
public class RMIMultiVMEhcachePortalCacheManagerConfiguratorTest
	extends MultiVMEhcachePortalCacheManagerConfiguratorTest {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Override
	@Test
	public void testActivate() {
		super.testActivate();

		_assertRMIMultiVMEhcachePortalCacheManagerConfigurator(
			null, null, null, null,
			_getRMIMultiVMEhcachePortalCacheManagerConfigurator(false));
		_assertRMIMultiVMEhcachePortalCacheManagerConfigurator(
			"com.liferay.portal.cache.ehcache.internal.rmi." +
				"TestLiferayRMICacheManagerPeerListenerFactory",
			"key1=value1,key2=value2",
			"net.sf.ehcache.distribution." +
				"TestRMICacheManagerPeerProviderFactory",
			"key1=value1,key2=value2",
			_getRMIMultiVMEhcachePortalCacheManagerConfigurator(true));
	}

	@Override
	@Test
	public void testManageConfiguration() {
		super.testManageConfiguration();

		_assertManageConfiguration(
			Collections.<FactoryConfiguration>emptyList(),
			Collections.<FactoryConfiguration>emptyList(), false);
		_assertManageConfiguration(
			Collections.singletonList(
				_getFactoryConfiguration(
					"net.sf.ehcache.distribution." +
						"TestRMICacheManagerPeerProviderFactory",
					"key1=value1,key2=value2", StringPool.COMMA)),
			Collections.singletonList(
				_getFactoryConfiguration(
					"com.liferay.portal.cache.ehcache.internal.rmi." +
						"TestLiferayRMICacheManagerPeerListenerFactory",
					"key1=value1,key2=value2", StringPool.COMMA)),
			true);
	}

	@Override
	protected MultiVMEhcachePortalCacheManagerConfigurator
		getMultiVMEhcachePortalCacheManagerConfigurator(
			boolean clusterEnabled, boolean bootstrapLoaderEnabled,
			boolean bootstrapLoaderPropertiesIsEmpty,
			boolean replicatorPropertiesIsEmpty) {

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator =
				new RMIMultiVMEhcachePortalCacheManagerConfigurator();

		multiVMEhcachePortalCacheManagerConfigurator.setProps(
			(Props)ProxyUtil.newProxyInstance(
				MultiVMEhcachePortalCacheManagerConfiguratorTest.
					class.getClassLoader(),
				new Class<?>[] {Props.class},
				new PropsInvocationHandler(
					clusterEnabled, bootstrapLoaderEnabled,
					bootstrapLoaderPropertiesIsEmpty,
					replicatorPropertiesIsEmpty)));

		multiVMEhcachePortalCacheManagerConfigurator.activate();

		return multiVMEhcachePortalCacheManagerConfigurator;
	}

	private void _assertManageConfiguration(
		List<FactoryConfiguration>
			expectedCacheManagerPeerProviderFactoryConfigurations,
		List<FactoryConfiguration>
			expectedCacheManagerPeerListenerFactoryConfigurations,
		boolean clusterEnabled) {

		RMIMultiVMEhcachePortalCacheManagerConfigurator
			rmiMultiVMEhcachePortalCacheManagerConfigurator =
				_getRMIMultiVMEhcachePortalCacheManagerConfigurator(
					clusterEnabled);

		Configuration configuration = new Configuration();

		rmiMultiVMEhcachePortalCacheManagerConfigurator.manageConfiguration(
			configuration,
			new PortalCacheManagerConfiguration(null, null, null));

		Assert.assertEquals(
			expectedCacheManagerPeerProviderFactoryConfigurations,
			configuration.getCacheManagerPeerProviderFactoryConfiguration());
		Assert.assertEquals(
			expectedCacheManagerPeerListenerFactoryConfigurations,
			configuration.getCacheManagerPeerListenerFactoryConfigurations());
	}

	private void _assertRMIMultiVMEhcachePortalCacheManagerConfigurator(
		String expectedPeerListenerFactoryClass,
		String expectedPeerListenerFactoryPropertiesString,
		String expectedPeerProviderFactoryClass,
		String expectedPeerProviderFactoryPropertiesString,
		RMIMultiVMEhcachePortalCacheManagerConfigurator
			rmiMultiVMEhcachePortalCacheManagerConfigurator) {

		Assert.assertEquals(
			expectedPeerListenerFactoryClass,
			ReflectionTestUtil.getFieldValue(
				rmiMultiVMEhcachePortalCacheManagerConfigurator,
				"_peerListenerFactoryClass"));
		Assert.assertEquals(
			expectedPeerListenerFactoryPropertiesString,
			ReflectionTestUtil.getFieldValue(
				rmiMultiVMEhcachePortalCacheManagerConfigurator,
				"_peerListenerFactoryPropertiesString"));
		Assert.assertEquals(
			expectedPeerProviderFactoryClass,
			ReflectionTestUtil.getFieldValue(
				rmiMultiVMEhcachePortalCacheManagerConfigurator,
				"_peerProviderFactoryClass"));
		Assert.assertEquals(
			expectedPeerProviderFactoryPropertiesString,
			ReflectionTestUtil.getFieldValue(
				rmiMultiVMEhcachePortalCacheManagerConfigurator,
				"_peerProviderFactoryPropertiesString"));
	}

	private FactoryConfiguration _getFactoryConfiguration(
		String fullyQualifiedClassPath, String properties,
		String propertySeparator) {

		FactoryConfiguration factoryConfiguration = new FactoryConfiguration();

		factoryConfiguration.setClass(fullyQualifiedClassPath);
		factoryConfiguration.setProperties(properties);
		factoryConfiguration.setPropertySeparator(propertySeparator);

		return factoryConfiguration;
	}

	private RMIMultiVMEhcachePortalCacheManagerConfigurator
		_getRMIMultiVMEhcachePortalCacheManagerConfigurator(
			boolean clusterEnabled) {

		RMIMultiVMEhcachePortalCacheManagerConfigurator
			rmiMultiVMEhcachePortalCacheManagerConfigurator =
				new RMIMultiVMEhcachePortalCacheManagerConfigurator();

		rmiMultiVMEhcachePortalCacheManagerConfigurator.setProps(
			(Props)ProxyUtil.newProxyInstance(
				RMIMultiVMEhcachePortalCacheManagerConfiguratorTest.class.
					getClassLoader(),
				new Class<?>[] {Props.class},
				new PropsInvocationHandler(clusterEnabled)));

		rmiMultiVMEhcachePortalCacheManagerConfigurator.activate();

		return rmiMultiVMEhcachePortalCacheManagerConfigurator;
	}

}