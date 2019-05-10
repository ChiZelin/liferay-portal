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

package com.liferay.asset.list.model;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.portal.kernel.annotation.ImplementationClassName;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.util.Accessor;

/**
 * The extended model interface for the AssetListEntrySegmentsEntryRel service. Represents a row in the &quot;AssetListEntrySegmentsEntryRel&quot; database table, with each column mapped to a property of this class.
 *
 * @author Brian Wing Shun Chan
 * @see AssetListEntrySegmentsEntryRelModel
 * @generated
 */
@ImplementationClassName(
	"com.liferay.asset.list.model.impl.AssetListEntrySegmentsEntryRelImpl"
)
@ProviderType
public interface AssetListEntrySegmentsEntryRel
	extends AssetListEntrySegmentsEntryRelModel, PersistedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this interface directly. Add methods to <code>com.liferay.asset.list.model.impl.AssetListEntrySegmentsEntryRelImpl</code> and rerun ServiceBuilder to automatically copy the method declarations to this interface.
	 */
	public static final Accessor<AssetListEntrySegmentsEntryRel, Long>
		ASSET_LIST_ENTRY_SEGMENTS_ENTRY_REL_ID_ACCESSOR =
			new Accessor<AssetListEntrySegmentsEntryRel, Long>() {

				@Override
				public Long get(
					AssetListEntrySegmentsEntryRel
						assetListEntrySegmentsEntryRel) {

					return assetListEntrySegmentsEntryRel.
						getAssetListEntrySegmentsEntryRelId();
				}

				@Override
				public Class<Long> getAttributeClass() {
					return Long.class;
				}

				@Override
				public Class<AssetListEntrySegmentsEntryRel> getTypeClass() {
					return AssetListEntrySegmentsEntryRel.class;
				}

			};

}