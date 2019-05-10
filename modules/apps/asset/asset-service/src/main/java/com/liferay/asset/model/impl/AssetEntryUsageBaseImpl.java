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

package com.liferay.asset.model.impl;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.asset.model.AssetEntryUsage;
import com.liferay.asset.service.AssetEntryUsageLocalServiceUtil;

/**
 * The extended model base implementation for the AssetEntryUsage service. Represents a row in the &quot;AssetEntryUsage&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This class exists only as a container for the default extended model level methods generated by ServiceBuilder. Helper methods and all application logic should be put in {@link AssetEntryUsageImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AssetEntryUsageImpl
 * @see AssetEntryUsage
 * @generated
 */
@ProviderType
public abstract class AssetEntryUsageBaseImpl
	extends AssetEntryUsageModelImpl implements AssetEntryUsage {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a asset entry usage model instance should use the <code>AssetEntryUsage</code> interface instead.
	 */
	@Override
	public void persist() {
		if (this.isNew()) {
			AssetEntryUsageLocalServiceUtil.addAssetEntryUsage(this);
		}
		else {
			AssetEntryUsageLocalServiceUtil.updateAssetEntryUsage(this);
		}
	}

}