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
			getActivatedMultiVMEhcachePortalCacheManagerConfigurator(
				new PropsInvocationHandler(false)));
		_assertRMIMultiVMEhcachePortalCacheManagerConfigurator(
			"com.liferay.portal.cache.ehcache.internal.rmi." +
				"TestLiferayRMICacheManagerPeerListenerFactory",
			"key1=value1,key2=value2",
			"net.sf.ehcache.distribution." +
				"TestRMICacheManagerPeerProviderFactory",
			"key1=value1,key2=value2",
			getActivatedMultiVMEhcachePortalCacheManagerConfigurator(
				new PropsInvocationHandler(true)));
	}

	@Override
	@Test
	public void testManageConfiguration() {
		super.testManageConfiguration();

		_testManageConfiguration(
			Collections.emptyList(), Collections.emptyList(), false);
		_testManageConfiguration(
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
	protected RMIMultiVMEhcachePortalCacheManagerConfigurator
		getActivatedMultiVMEhcachePortalCacheManagerConfigurator(
			PropsInvocationHandler propsInvocationHandler) {

		RMIMultiVMEhcachePortalCacheManagerConfigurator
			rmiMultiVMEhcachePortalCacheManagerConfigurator =
				getBaseEhcachePortalCacheManagerConfigurator();

		rmiMultiVMEhcachePortalCacheManagerConfigurator.setProps(
			(Props)ProxyUtil.newProxyInstance(
				RMIMultiVMEhcachePortalCacheManagerConfigurator.
					class.getClassLoader(),
				new Class<?>[] {Props.class}, propsInvocationHandler));

		rmiMultiVMEhcachePortalCacheManagerConfigurator.activate();

		return rmiMultiVMEhcachePortalCacheManagerConfigurator;
	}

	@Override
	protected RMIMultiVMEhcachePortalCacheManagerConfigurator
		getBaseEhcachePortalCacheManagerConfigurator() {

		return new RMIMultiVMEhcachePortalCacheManagerConfigurator();
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

	private void _testManageConfiguration(
		List<FactoryConfiguration>
			expectedCacheManagerPeerProviderFactoryConfigurations,
		List<FactoryConfiguration>
			expectedCacheManagerPeerListenerFactoryConfigurations,
		boolean clusterEnabled) {

		RMIMultiVMEhcachePortalCacheManagerConfigurator
			rmiMultiVMEhcachePortalCacheManagerConfigurator =
				getActivatedMultiVMEhcachePortalCacheManagerConfigurator(
					new PropsInvocationHandler(clusterEnabled));

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

}