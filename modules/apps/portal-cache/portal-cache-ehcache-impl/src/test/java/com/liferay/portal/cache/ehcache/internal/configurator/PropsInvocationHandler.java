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
import com.liferay.portal.kernel.util.PropsKeys;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.util.Properties;

/**
 * @author Leon Chi
 */
public class PropsInvocationHandler implements InvocationHandler {

	public PropsInvocationHandler(boolean clusterEnabled) {
		this(clusterEnabled, false, false, false);
	}

	public PropsInvocationHandler(
		boolean clusterEnabled, boolean bootstrapLoaderEnabled,
		boolean hasBootstrapLoaderProperties, boolean hasReplicatorProperties) {

		_clusterEnabled = clusterEnabled;
		_bootstrapLoaderEnabled = bootstrapLoaderEnabled;
		_hasBootstrapLoaderProperties = hasBootstrapLoaderProperties;
		_hasReplicatorProperties = hasReplicatorProperties;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) {
		String methodName = method.getName();

		if ("get".equals(methodName)) {
			if (PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED.equals(
					args[0])) {

				return String.valueOf(_bootstrapLoaderEnabled);
			}

			if (PropsKeys.CLUSTER_LINK_ENABLED.equals(args[0])) {
				return String.valueOf(_clusterEnabled);
			}
		}

		if ("getArray".equals(methodName)) {
			if ("portal.property.Key1".equals(args[0])) {
				return new String[0];
			}

			if ("portal.property.Key2".equals(args[0])) {
				return new String[] {"key=value"};
			}

			return new String[] {"key1=value1", "key2=value2"};
		}

		if ("getProperties".equals(methodName) && (args.length > 0)) {
			if (args[0].equals(
					PropsKeys.EHCACHE_BOOTSTRAP_CACHE_LOADER_PROPERTIES +
						StringPool.PERIOD) &&
				_hasBootstrapLoaderProperties) {

				return new Properties() {
					{
						put("portalCacheName1", "key1=value1");
						put("portalCacheName2", "key2=value2");
					}
				};
			}

			if (args[0].equals(
					PropsKeys.EHCACHE_REPLICATOR_PROPERTIES +
						StringPool.PERIOD) &&
				_hasReplicatorProperties) {

				return new Properties() {
					{
						put("portalCacheName1", "key1=value1");
						put("portalCacheName3", "key3=value3");
					}
				};
			}

			return new Properties();
		}

		return null;
	}

	private final boolean _bootstrapLoaderEnabled;
	private final boolean _clusterEnabled;
	private final boolean _hasBootstrapLoaderProperties;
	private final boolean _hasReplicatorProperties;

}