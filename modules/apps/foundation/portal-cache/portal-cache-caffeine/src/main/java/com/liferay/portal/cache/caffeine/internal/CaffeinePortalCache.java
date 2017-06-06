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
import java.util.List;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.liferay.portal.cache.BasePortalCache;
import com.liferay.portal.cache.caffeine.CaffeineWrapper;
import com.liferay.portal.kernel.cache.PortalCacheManager;

/**
 * @author Leon Chi
 */
public class CaffeinePortalCache <K extends Serializable, V>
	extends BasePortalCache<K, V> implements CaffeineWrapper{

	public CaffeinePortalCache(
		PortalCacheManager<K, V> portalCacheManager, Caffeine caffeine) {
		
		super(portalCacheManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<K> getKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPortalCacheName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Caffeine getCaffeine() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected V doGet(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doPut(K key, V value, int timeToLive) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected V doPutIfAbsent(K key, V value, int timeToLive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doRemove(K key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean doRemove(K key, V value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected V doReplace(K key, V value, int timeToLive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean doReplace(K key, V oldValue, V newValue, int timeToLive) {
		// TODO Auto-generated method stub
		return false;
	}

}
