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

package com.liferay.portlet.ratings.transformer.bundle.ratingsdatatransformerutil;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery.PerformActionMethod;
import com.liferay.ratings.kernel.RatingsType;
import com.liferay.ratings.kernel.model.RatingsEntry;
import com.liferay.ratings.kernel.transformer.RatingsDataTransformer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Peter Fellwock
 */
public class TestRatingsDataTransformer implements RatingsDataTransformer {

	@Override
	public PerformActionMethod<RatingsEntry> transformRatingsData(
		RatingsType fromRatingsType, RatingsType toRatingsType) {

		_atomicBoolean.set(Boolean.TRUE);

		return null;
	}

	protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
		_atomicBoolean = atomicBoolean;
	}

	private AtomicBoolean _atomicBoolean;

}