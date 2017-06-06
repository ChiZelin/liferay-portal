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

import java.io.Serializable;
import java.net.URL;

import com.liferay.portal.cache.BasePortalCacheManager;
import com.liferay.portal.cache.configuration.PortalCacheConfiguration;
import com.liferay.portal.cache.configuration.PortalCacheManagerConfiguration;
import com.liferay.portal.kernel.cache.PortalCache;


/**
 * @author Leon Chi
 */
public class CaffeinePortalCacheManager<K extends Serializable, V>
	extends BasePortalCacheManager<K, V> {

	@Override
	public void reconfigurePortalCaches(URL configurationURL) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected PortalCache<K, V> createPortalCache(PortalCacheConfiguration portalCacheConfiguration) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doClearAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doDestroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doRemovePortalCache(String portalCacheName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected PortalCacheManagerConfiguration getPortalCacheManagerConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void initPortalCacheManager() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void removeConfigurableEhcachePortalCacheListeners(PortalCache<K, V> portalCache) {
		// TODO Auto-generated method stub
		
	}

}
