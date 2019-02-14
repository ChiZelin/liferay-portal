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
import com.liferay.portal.cache.configuration.PortalCacheConfiguration;
import com.liferay.portal.cache.configuration.PortalCacheManagerConfiguration;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;
import com.liferay.portal.kernel.test.util.PropsTestUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.Props;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.ProxyFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Leon Chi
 */
public class MultiVMEhcachePortalCacheManagerConfiguratorTest
	extends BaseEhcachePortalCacheManagerConfiguratorTestCase {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		CodeCoverageAssertor.INSTANCE;

	@Test
	public void testActivate() {
		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator =
				getBaseEhcachePortalCacheManagerConfigurator(_propsMap);

		Assert.assertTrue(
			"The _bootstrapLoaderEnabled should be true if props.get(" +
				"PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED) return true",
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator,
				"_bootstrapLoaderEnabled"));
		Assert.assertTrue(
			"The clusterEnabled should be true if props.get(PropsKeys." +
				"CLUSTER_LINK_ENABLED) return true",
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator,
				"clusterEnabled"));
		Assert.assertEquals(
			new Properties(),
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator,
				"_bootstrapLoaderProperties"));
		Assert.assertEquals(
			new Properties(),
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator,
				"_replicatorProperties"));
		Assert.assertNull(
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator,
				"_defaultBootstrapLoaderPropertiesString"));
		Assert.assertNull(
			ReflectionTestUtil.getFieldValue(
				multiVMEhcachePortalCacheManagerConfigurator,
				"_defaultReplicatorPropertiesString"));
	}

	@Test
	public void testGetMergedPropertiesMap() {
		_testGetMergedPropertiesMap(
			new String[0], new ObjectValuePair[0], _propsMap);
		_testGetMergedPropertiesMap(
			new String[] {_TEST_PORTAL_CACHE_NAME},
			new ObjectValuePair[] {
				new ObjectValuePair(_properties2.clone(), _properties3.clone())
			},
			new HashMap<String, Object>() {
				{
					putAll(_propsMap);
					put(
						PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES +
							StringPool.PERIOD,
						_properties1.clone());
					put(
						PropsKeys.EHCACHE_REPLICATOR_PROPERTIES +
							StringPool.PERIOD,
						_properties1.clone());
				}
			});
		_testGetMergedPropertiesMap(
			new String[] {_TEST_PORTAL_CACHE_NAME},
			new ObjectValuePair[] {
				new ObjectValuePair(null, _properties3.clone())
			},
			new HashMap<String, Object>() {
				{
					putAll(_propsMap);
					put(
						PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED,
						"false");
					put(
						PropsKeys.EHCACHE_REPLICATOR_PROPERTIES +
							StringPool.PERIOD,
						_properties1.clone());
				}
			});
	}

	@Test
	public void testGetPortalPropertiesString() {
		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator =
				new MultiVMEhcachePortalCacheManagerConfigurator();

		multiVMEhcachePortalCacheManagerConfigurator.setProps(
			PropsTestUtil.setProps(
				new HashMap<String, Object>() {
					{
						put("key1", new String[0]);
						put("key2", new String[] {"key=value"});
						put(
							"key3",
							new String[] {"key1=value1", "key2=value2"});
					}
				}
			));

		Assert.assertEquals(
			null,
			multiVMEhcachePortalCacheManagerConfigurator.
				getPortalPropertiesString("key1"));
		Assert.assertEquals(
			"key=value",
			multiVMEhcachePortalCacheManagerConfigurator.
				getPortalPropertiesString("key2"));
		Assert.assertEquals(
			"key1=value1,key2=value2",
			multiVMEhcachePortalCacheManagerConfigurator.
				getPortalPropertiesString("key3"));
	}

	@Override
	@Test
	public void testIsRequireSerialization() {
		super.testIsRequireSerialization();

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator =
				getBaseEhcachePortalCacheManagerConfigurator(_propsMap);

		Assert.assertTrue(
			"The true value should be returned if clusterEnabled is true",
			multiVMEhcachePortalCacheManagerConfigurator.isRequireSerialization(
				new CacheConfiguration()));
	}

	@Test
	public void testManageConfiguration() {
		_assertManageConfiguration(
			true, null, new HashSet<>(),
			getBaseEhcachePortalCacheManagerConfigurator(
				new HashMap<String, Object>() {
					{
						putAll(_propsMap);
						put(PropsKeys.CLUSTER_LINK_ENABLED, "false");
					}
				}),
			new PortalCacheManagerConfiguration(
				null,
				new PortalCacheConfiguration(
					PortalCacheConfiguration.PORTAL_CACHE_NAME_DEFAULT, null,
					null),
				null));
		_assertManageConfiguration(
			false, (Properties)_properties2.clone(),
			new HashSet<Properties>() {
				{
					add((Properties)_properties3.clone());
					add((Properties)_properties5.clone());
				}
			},
			getBaseEhcachePortalCacheManagerConfigurator(
				new HashMap<String, Object>() {
					{
						putAll(_propsMap);
						put(
							PropsKeys.
								EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES +
									StringPool.PERIOD,
							_properties1.clone());
						put(
							PropsKeys.EHCACHE_REPLICATOR_PROPERTIES +
								StringPool.PERIOD,
							_properties1.clone());
					}
				}),
			new PortalCacheManagerConfiguration(
				null,
				new PortalCacheConfiguration(
					PortalCacheConfiguration.PORTAL_CACHE_NAME_DEFAULT,
					new HashSet<Properties>() {
						{
							add((Properties)_properties4.clone());
							add((Properties)_properties5.clone());
						}
					},
					null),
				null));
		_assertManageConfiguration(
			false, null,
			new HashSet<Properties>() {
				{
					add((Properties)_properties3.clone());
				}

			},
			getBaseEhcachePortalCacheManagerConfigurator(
				new HashMap<String, Object>() {
					{
						putAll(_propsMap);
						put(
							PropsKeys.EHCACHE_REPLICATOR_PROPERTIES +
								StringPool.PERIOD,
							_properties1.clone());
					}
				}),
			new PortalCacheManagerConfiguration(
				null,
				new PortalCacheConfiguration(
					PortalCacheConfiguration.PORTAL_CACHE_NAME_DEFAULT, null,
					null),
				null));
		_assertManageConfiguration(
			false, null,
			new HashSet<Properties>() {
				{
					add((Properties)_properties3.clone());
				}

			},
			getBaseEhcachePortalCacheManagerConfigurator(
				new HashMap<String, Object>() {
					{
						putAll(_propsMap);
						put(
							PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED,
							"false");
						put(
							PropsKeys.EHCACHE_REPLICATOR_PROPERTIES +
								StringPool.PERIOD,
							_properties1.clone());
					}
				}),
			new PortalCacheManagerConfiguration(
				null,
				new PortalCacheConfiguration(
					PortalCacheConfiguration.PORTAL_CACHE_NAME_DEFAULT, null,
					null),
				null));
		_assertManageConfiguration(
			false, (Properties)_properties2.clone(), new HashSet<>(),
			getBaseEhcachePortalCacheManagerConfigurator(
				new HashMap<String, Object>() {
					{
						putAll(_propsMap);
						put(
							PropsKeys.
								EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES +
									StringPool.PERIOD,
							_properties1.clone());
					}
				}),
			new PortalCacheManagerConfiguration(
				null,
				new PortalCacheConfiguration(
					PortalCacheConfiguration.PORTAL_CACHE_NAME_DEFAULT, null,
					null),
				null));
	}

	@Override
	@Test
	public void testParseCacheListenerConfigurations() {
		super.testParseCacheListenerConfigurations();

		_assertParseCacheListenerConfigurations(
			new Properties(),
			Collections.singleton((Properties)_properties4.clone()),
			getBaseEhcachePortalCacheManagerConfigurator(_propsMap));
		_assertParseCacheListenerConfigurations(
			null, Collections.singleton((Properties)_properties4.clone()),
			getBaseEhcachePortalCacheManagerConfigurator(
				new HashMap<String, Object>() {
					{
						putAll(_propsMap);
						put(
							PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED,
							"false");
					}
				}));
		_assertParseCacheListenerConfigurations(
			(Properties)_properties2.clone(),
			Collections.singleton((Properties)_properties3.clone()),
			getBaseEhcachePortalCacheManagerConfigurator(
				new HashMap<String, Object>() {
					{
						putAll(_propsMap);
						put(
							PropsKeys.
								EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES +
									StringPool.PERIOD,
							_properties1.clone());
						put(
							PropsKeys.EHCACHE_REPLICATOR_PROPERTIES +
								StringPool.PERIOD,
							_properties1.clone());
					}
				}));
	}

	@Test
	public void testSetProps() {
		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator =
				new MultiVMEhcachePortalCacheManagerConfigurator();

		Props dummyProps = ProxyFactory.newDummyInstance(Props.class);

		multiVMEhcachePortalCacheManagerConfigurator.setProps(dummyProps);

		Assert.assertSame(
			dummyProps, multiVMEhcachePortalCacheManagerConfigurator.props);
	}

	@Override
	protected MultiVMEhcachePortalCacheManagerConfigurator
		getBaseEhcachePortalCacheManagerConfigurator(
			Map<String, Object> properties) {

		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator =
				new MultiVMEhcachePortalCacheManagerConfigurator();

		if (properties != null) {
			multiVMEhcachePortalCacheManagerConfigurator.setProps(
				PropsTestUtil.setProps(properties));

			multiVMEhcachePortalCacheManagerConfigurator.activate();
		}

		return multiVMEhcachePortalCacheManagerConfigurator;
	}

	private void _assertManageConfiguration(
		boolean expectedPortalCacheConfigurationIsNull,
		Properties expectedPortalCacheBootstrapLoaderProperties,
		Set<Properties> expectedPortalCacheListenerPropertiesSet,
		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator,
		PortalCacheManagerConfiguration portalCacheManagerConfiguration) {

		multiVMEhcachePortalCacheManagerConfigurator.manageConfiguration(
			new Configuration(), portalCacheManagerConfiguration);

		PortalCacheConfiguration portalCacheConfiguration =
			portalCacheManagerConfiguration.getPortalCacheConfiguration(
				_TEST_PORTAL_CACHE_NAME);

		Assert.assertEquals(
			expectedPortalCacheConfigurationIsNull,
			portalCacheConfiguration == null);

		if (expectedPortalCacheConfigurationIsNull) {
			return;
		}

		Assert.assertEquals(
			_TEST_PORTAL_CACHE_NAME,
			portalCacheConfiguration.getPortalCacheName());
		Assert.assertEquals(
			expectedPortalCacheBootstrapLoaderProperties,
			portalCacheConfiguration.getPortalCacheBootstrapLoaderProperties());
		Assert.assertEquals(
			expectedPortalCacheListenerPropertiesSet,
			portalCacheConfiguration.getPortalCacheListenerPropertiesSet());

		multiVMEhcachePortalCacheManagerConfigurator.manageConfiguration(
			new Configuration(), portalCacheManagerConfiguration);

		Assert.assertSame(
			portalCacheConfiguration,
			portalCacheManagerConfiguration.getPortalCacheConfiguration(
				_TEST_PORTAL_CACHE_NAME));
	}

	private void _assertParseCacheListenerConfigurations(
		Properties expectedBootstrapLoaderProperties,
		Set<Properties> expectedPortalCacheListenerPropertiesSet,
		MultiVMEhcachePortalCacheManagerConfigurator
			multiVMEhcachePortalCacheManagerConfigurator) {

		PortalCacheConfiguration portalCacheConfiguration =
			multiVMEhcachePortalCacheManagerConfigurator.
				parseCacheListenerConfigurations(
					new CacheConfiguration(_TEST_PORTAL_CACHE_NAME, 0), null,
					true);

		Properties bootstrapLoaderProperties = ReflectionTestUtil.getFieldValue(
			multiVMEhcachePortalCacheManagerConfigurator,
			"_bootstrapLoaderProperties");
		Properties replicatorProperties = ReflectionTestUtil.getFieldValue(
			multiVMEhcachePortalCacheManagerConfigurator,
			"_replicatorProperties");

		Assert.assertNull(
			bootstrapLoaderProperties.get(_TEST_PORTAL_CACHE_NAME));
		Assert.assertNull(replicatorProperties.get(_TEST_PORTAL_CACHE_NAME));
		Assert.assertEquals(
			_TEST_PORTAL_CACHE_NAME,
			portalCacheConfiguration.getPortalCacheName());
		Assert.assertEquals(
			expectedBootstrapLoaderProperties,
			portalCacheConfiguration.getPortalCacheBootstrapLoaderProperties());
		Assert.assertEquals(
			expectedPortalCacheListenerPropertiesSet,
			portalCacheConfiguration.getPortalCacheListenerPropertiesSet());
	}

	private void _testGetMergedPropertiesMap(
		String[] expectedPortalCacheNames,
		ObjectValuePair[] expectedObjectValuePairs,
		Map<String, Object> properties) {

		Map<String, ObjectValuePair<Properties, Properties>>
			mergedPropertiesMap = ReflectionTestUtil.invoke(
				getBaseEhcachePortalCacheManagerConfigurator(properties),
				"_getMergedPropertiesMap", null, null);

		Assert.assertEquals(
			mergedPropertiesMap.toString(), expectedObjectValuePairs.length,
			mergedPropertiesMap.size());

		for (int i = 0; i < mergedPropertiesMap.size(); i++) {
			Assert.assertEquals(
				expectedObjectValuePairs[i],
				mergedPropertiesMap.get(expectedPortalCacheNames[i]));
		}
	}

	private static final String _TEST_PORTAL_CACHE_NAME = "testPortalCacheName";

	private final Properties _properties1 = new Properties() {
		{
			put(_TEST_PORTAL_CACHE_NAME, "key=value");
		}
	};
	private final Properties _properties2 = new Properties() {
		{
			put("key", "value");
		}
	};
	private final Properties _properties3 = new Properties() {
		{
			put("key", "value");
			put("replicator", true);
		}
	};
	private final Properties _properties4 = new Properties() {
		{
			put("replicator", true);
		}
	};
	private final Properties _properties5 = new Properties() {
		{
			put("replicator", false);
		}
	};
	private final Map<String, Object> _propsMap = new HashMap() {
		{
			put(PropsKeys.CLUSTER_LINK_ENABLED, "true");
			put(PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED, "true");
			put(
				PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES +
					StringPool.PERIOD,
				new Properties());
			put(
				PropsKeys.EHCACHE_REPLICATOR_PROPERTIES + StringPool.PERIOD,
				new Properties());
			put(
				PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES_DEFAULT,
				new String[0]);
			put(PropsKeys.EHCACHE_REPLICATOR_PROPERTIES_DEFAULT, new String[0]);
		}
	};

}