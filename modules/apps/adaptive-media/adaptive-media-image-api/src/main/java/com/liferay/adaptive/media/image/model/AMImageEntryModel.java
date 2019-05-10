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

package com.liferay.adaptive.media.image.model;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.ShardedModel;

import java.util.Date;

/**
 * The base model interface for the AMImageEntry service. Represents a row in the &quot;AMImageEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.adaptive.media.image.model.impl.AMImageEntryModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.adaptive.media.image.model.impl.AMImageEntryImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see AMImageEntry
 * @generated
 */
@ProviderType
public interface AMImageEntryModel
	extends BaseModel<AMImageEntry>, ShardedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a am image entry model instance should use the {@link AMImageEntry} interface instead.
	 */

	/**
	 * Returns the primary key of this am image entry.
	 *
	 * @return the primary key of this am image entry
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this am image entry.
	 *
	 * @param primaryKey the primary key of this am image entry
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this am image entry.
	 *
	 * @return the uuid of this am image entry
	 */
	@AutoEscape
	public String getUuid();

	/**
	 * Sets the uuid of this am image entry.
	 *
	 * @param uuid the uuid of this am image entry
	 */
	public void setUuid(String uuid);

	/**
	 * Returns the am image entry ID of this am image entry.
	 *
	 * @return the am image entry ID of this am image entry
	 */
	public long getAmImageEntryId();

	/**
	 * Sets the am image entry ID of this am image entry.
	 *
	 * @param amImageEntryId the am image entry ID of this am image entry
	 */
	public void setAmImageEntryId(long amImageEntryId);

	/**
	 * Returns the group ID of this am image entry.
	 *
	 * @return the group ID of this am image entry
	 */
	public long getGroupId();

	/**
	 * Sets the group ID of this am image entry.
	 *
	 * @param groupId the group ID of this am image entry
	 */
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this am image entry.
	 *
	 * @return the company ID of this am image entry
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this am image entry.
	 *
	 * @param companyId the company ID of this am image entry
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the create date of this am image entry.
	 *
	 * @return the create date of this am image entry
	 */
	public Date getCreateDate();

	/**
	 * Sets the create date of this am image entry.
	 *
	 * @param createDate the create date of this am image entry
	 */
	public void setCreateDate(Date createDate);

	/**
	 * Returns the configuration uuid of this am image entry.
	 *
	 * @return the configuration uuid of this am image entry
	 */
	@AutoEscape
	public String getConfigurationUuid();

	/**
	 * Sets the configuration uuid of this am image entry.
	 *
	 * @param configurationUuid the configuration uuid of this am image entry
	 */
	public void setConfigurationUuid(String configurationUuid);

	/**
	 * Returns the file version ID of this am image entry.
	 *
	 * @return the file version ID of this am image entry
	 */
	public long getFileVersionId();

	/**
	 * Sets the file version ID of this am image entry.
	 *
	 * @param fileVersionId the file version ID of this am image entry
	 */
	public void setFileVersionId(long fileVersionId);

	/**
	 * Returns the mime type of this am image entry.
	 *
	 * @return the mime type of this am image entry
	 */
	@AutoEscape
	public String getMimeType();

	/**
	 * Sets the mime type of this am image entry.
	 *
	 * @param mimeType the mime type of this am image entry
	 */
	public void setMimeType(String mimeType);

	/**
	 * Returns the height of this am image entry.
	 *
	 * @return the height of this am image entry
	 */
	public int getHeight();

	/**
	 * Sets the height of this am image entry.
	 *
	 * @param height the height of this am image entry
	 */
	public void setHeight(int height);

	/**
	 * Returns the width of this am image entry.
	 *
	 * @return the width of this am image entry
	 */
	public int getWidth();

	/**
	 * Sets the width of this am image entry.
	 *
	 * @param width the width of this am image entry
	 */
	public void setWidth(int width);

	/**
	 * Returns the size of this am image entry.
	 *
	 * @return the size of this am image entry
	 */
	public long getSize();

	/**
	 * Sets the size of this am image entry.
	 *
	 * @param size the size of this am image entry
	 */
	public void setSize(long size);

}