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

package com.liferay.dynamic.data.mapping.exception;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;

/**
 * @author Marcellus Tavares
 */
public class RequiredDataProviderInstanceException extends PortalException {

	public static class
		MustNotDeleteDataProviderInstanceReferencedByDataProviderInstanceLinks
			extends RequiredDataProviderInstanceException {

		public MustNotDeleteDataProviderInstanceReferencedByDataProviderInstanceLinks(
			long dataProviderInstanceId) {

			super(
				StringBundler.concat(
					"Data provider instance ", dataProviderInstanceId,
					" cannot be deleted because it is referenced by one or " +
						"more data provider instance links"));

			this.dataProviderInstanceId = dataProviderInstanceId;
		}

		public long dataProviderInstanceId;

	}

	private RequiredDataProviderInstanceException(String message) {
		super(message);
	}

}