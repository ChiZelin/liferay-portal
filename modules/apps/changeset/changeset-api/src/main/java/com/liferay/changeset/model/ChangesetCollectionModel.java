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

package com.liferay.changeset.model;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.ShardedModel;

import java.util.Date;

/**
 * The base model interface for the ChangesetCollection service. Represents a row in the &quot;ChangesetCollection&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.changeset.model.impl.ChangesetCollectionModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.changeset.model.impl.ChangesetCollectionImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see ChangesetCollection
 * @generated
 */
@ProviderType
public interface ChangesetCollectionModel
	extends BaseModel<ChangesetCollection>, GroupedModel, ShardedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a changeset collection model instance should use the {@link ChangesetCollection} interface instead.
	 */

	/**
	 * Returns the primary key of this changeset collection.
	 *
	 * @return the primary key of this changeset collection
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this changeset collection.
	 *
	 * @param primaryKey the primary key of this changeset collection
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the changeset collection ID of this changeset collection.
	 *
	 * @return the changeset collection ID of this changeset collection
	 */
	public long getChangesetCollectionId();

	/**
	 * Sets the changeset collection ID of this changeset collection.
	 *
	 * @param changesetCollectionId the changeset collection ID of this changeset collection
	 */
	public void setChangesetCollectionId(long changesetCollectionId);

	/**
	 * Returns the group ID of this changeset collection.
	 *
	 * @return the group ID of this changeset collection
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this changeset collection.
	 *
	 * @param groupId the group ID of this changeset collection
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this changeset collection.
	 *
	 * @return the company ID of this changeset collection
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this changeset collection.
	 *
	 * @param companyId the company ID of this changeset collection
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this changeset collection.
	 *
	 * @return the user ID of this changeset collection
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this changeset collection.
	 *
	 * @param userId the user ID of this changeset collection
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this changeset collection.
	 *
	 * @return the user uuid of this changeset collection
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this changeset collection.
	 *
	 * @param userUuid the user uuid of this changeset collection
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this changeset collection.
	 *
	 * @return the user name of this changeset collection
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this changeset collection.
	 *
	 * @param userName the user name of this changeset collection
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this changeset collection.
	 *
	 * @return the create date of this changeset collection
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this changeset collection.
	 *
	 * @param createDate the create date of this changeset collection
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this changeset collection.
	 *
	 * @return the modified date of this changeset collection
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this changeset collection.
	 *
	 * @param modifiedDate the modified date of this changeset collection
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the name of this changeset collection.
	 *
	 * @return the name of this changeset collection
	 */
	@AutoEscape
	public String getName();

	/**
	 * Sets the name of this changeset collection.
	 *
	 * @param name the name of this changeset collection
	 */
	public void setName(String name);

	/**
	 * Returns the description of this changeset collection.
	 *
	 * @return the description of this changeset collection
	 */
	@AutoEscape
	public String getDescription();

	/**
	 * Sets the description of this changeset collection.
	 *
	 * @param description the description of this changeset collection
	 */
	public void setDescription(String description);

}