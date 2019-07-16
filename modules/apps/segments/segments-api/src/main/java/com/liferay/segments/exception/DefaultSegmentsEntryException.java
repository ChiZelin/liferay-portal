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

package com.liferay.segments.exception;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.exception.PortalException;

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Eduardo García
 */
@ProviderType
public class DefaultSegmentsEntryException extends PortalException {

	public DefaultSegmentsEntryException() {
	}

	public DefaultSegmentsEntryException(String msg) {
		super(msg);
	}

	public DefaultSegmentsEntryException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public DefaultSegmentsEntryException(Throwable cause) {
		super(cause);
	}

	public static class MustNotDeleteDefaultSegmentsEntry
		extends DefaultSegmentsEntryException {

		public MustNotDeleteDefaultSegmentsEntry(long segmentsEntryId) {
			super(
				StringBundler.concat(
					"The default segments entry ", segmentsEntryId,
					" cannot be deleted"));
		}

	}

	public static class MustNotUpdateDefaultSegmentsEntry
		extends DefaultSegmentsEntryException {

		public MustNotUpdateDefaultSegmentsEntry(long segmentsEntryId) {
			super(
				StringBundler.concat(
					"The default segments entry ", segmentsEntryId,
					" cannot be updated"));
		}

	}

}