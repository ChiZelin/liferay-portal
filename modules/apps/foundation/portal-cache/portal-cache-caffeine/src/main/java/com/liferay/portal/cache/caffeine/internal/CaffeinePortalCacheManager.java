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

package com.liferay.portal.cache.caffeine.internal;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.PortalCacheException;
import com.liferay.portal.kernel.cache.PortalCacheManager;
import com.liferay.portal.kernel.cache.PortalCacheManagerListener;

import java.io.Serializable;

import java.net.URL;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Leon Chi
 */
public class CaffeinePortalCacheManager<K extends Serializable, V>
	implements PortalCacheManager<K, V> {

	@Override
	public void clearAll() throws PortalCacheException {
		for (String cacheName : portalCaches.keySet()) {
			PortalCache<K, V> portalCache = portalCaches.get(cacheName);

			if (portalCache != null) {
				portalCache.removeAll();
			}
		}
	}

	@Override
	public void destroy() {
		portalCaches.clear();
	}

	@Override
	public PortalCache<K, V> getPortalCache(String portalCacheName)
		throws PortalCacheException {

		PortalCache<K, V> portalCache = portalCaches.get(portalCacheName);

		if (portalCache != null) {
			return portalCache;
		}

		Cache<K, V> cache = Caffeine.newBuilder()
			.maximumSize(10000)
			.expireAfterWrite(10, TimeUnit.MINUTES)
			.build();

		portalCache = new CaffeinePortalCache<>(this, cache, portalCacheName);

		portalCaches.putIfAbsent(portalCacheName, portalCache);

		return portalCache;
	}

	@Override
	public PortalCache<K, V> getPortalCache(
			String portalCacheName, boolean blocking)
		throws PortalCacheException {

		return getPortalCache(portalCacheName);
	}

	@Override
	public PortalCache<K, V> getPortalCache(
			String portalCacheName, boolean blocking, boolean mvcc)
		throws PortalCacheException {

		return getPortalCache(portalCacheName);
	}

	@Override
	public Set<PortalCacheManagerListener> getPortalCacheManagerListeners() {

		// TODO Auto-generated method stub

		return null;
	}

	@Override
	public String getPortalCacheManagerName() {
		return _portalCacheManagerName;
	}

	@Override
	public boolean isClusterAware() {
		return _clusterAware;
	}

	@Override
	public void reconfigurePortalCaches(URL configurationURL) {

		// TODO Auto-generated method stub

	}

	@Override
	public boolean registerPortalCacheManagerListener(
		PortalCacheManagerListener portalCacheManagerListener) {

		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public void removePortalCache(String portalCacheName) {
		portalCaches.remove(portalCacheName);
	}

	public void setClusterAware(boolean clusterAware) {
		this._clusterAware = clusterAware;
	}

	public void setPortalCacheManagerName(String portalCacheManagerName) {
		_portalCacheManagerName = portalCacheManagerName;
	}

	@Override
	public boolean unregisterPortalCacheManagerListener(
		PortalCacheManagerListener portalCacheManagerListener) {

		// TODO Auto-generated method stub

		return false;
	}

	@Override
	public void unregisterPortalCacheManagerListeners() {

		// TODO Auto-generated method stub

	}

	private boolean _clusterAware;
	private String _portalCacheManagerName;
	private final ConcurrentMap<String, PortalCache<K, V>> portalCaches =
		new ConcurrentHashMap<>();

}