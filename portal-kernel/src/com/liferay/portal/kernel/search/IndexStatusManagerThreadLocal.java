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

package com.liferay.portal.kernel.search;

import com.liferay.petra.lang.CentralizedThreadLocal;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Daniel Kocsis
 */
@ProviderType
public class IndexStatusManagerThreadLocal {

	public static boolean isIndexReadOnly() {
		return _indexReadOnly.get();
	}

	public static void setIndexReadOnly(boolean indexReadOnly) {
		_indexReadOnly.set(indexReadOnly);
	}

	private static final ThreadLocal<Boolean> _indexReadOnly =
		new CentralizedThreadLocal<>(
			IndexStatusManagerThreadLocal.class + "._indexReadOnly",
			() -> Boolean.FALSE);

}