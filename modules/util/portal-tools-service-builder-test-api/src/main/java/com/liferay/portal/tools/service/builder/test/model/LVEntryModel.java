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

package com.liferay.portal.tools.service.builder.test.model;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.model.version.VersionedModel;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Map;

/**
 * The base model interface for the LVEntry service. Represents a row in the &quot;LVEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.portal.tools.service.builder.test.model.impl.LVEntryModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.portal.tools.service.builder.test.model.impl.LVEntryImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see LVEntry
 * @generated
 */
@ProviderType
public interface LVEntryModel
	extends BaseModel<LVEntry>, MVCCModel, ShardedModel,
			VersionedModel<LVEntryVersion> {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a lv entry model instance should use the {@link LVEntry} interface instead.
	 */

	/**
	 * Returns the primary key of this lv entry.
	 *
	 * @return the primary key of this lv entry
	 */
	@Override
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this lv entry.
	 *
	 * @param primaryKey the primary key of this lv entry
	 */
	@Override
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this lv entry.
	 *
	 * @return the mvcc version of this lv entry
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this lv entry.
	 *
	 * @param mvccVersion the mvcc version of this lv entry
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the uuid of this lv entry.
	 *
	 * @return the uuid of this lv entry
	 */
	@AutoEscape
	public String getUuid();

	/**
	 * Sets the uuid of this lv entry.
	 *
	 * @param uuid the uuid of this lv entry
	 */
	public void setUuid(String uuid);

	/**
	 * Returns the head ID of this lv entry.
	 *
	 * @return the head ID of this lv entry
	 */
	@Override
	public long getHeadId();

	/**
	 * Sets the head ID of this lv entry.
	 *
	 * @param headId the head ID of this lv entry
	 */
	@Override
	public void setHeadId(long headId);

	/**
	 * Returns the default language ID of this lv entry.
	 *
	 * @return the default language ID of this lv entry
	 */
	@AutoEscape
	public String getDefaultLanguageId();

	/**
	 * Sets the default language ID of this lv entry.
	 *
	 * @param defaultLanguageId the default language ID of this lv entry
	 */
	public void setDefaultLanguageId(String defaultLanguageId);

	/**
	 * Returns the lv entry ID of this lv entry.
	 *
	 * @return the lv entry ID of this lv entry
	 */
	public long getLvEntryId();

	/**
	 * Sets the lv entry ID of this lv entry.
	 *
	 * @param lvEntryId the lv entry ID of this lv entry
	 */
	public void setLvEntryId(long lvEntryId);

	/**
	 * Returns the company ID of this lv entry.
	 *
	 * @return the company ID of this lv entry
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this lv entry.
	 *
	 * @param companyId the company ID of this lv entry
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the group ID of this lv entry.
	 *
	 * @return the group ID of this lv entry
	 */
	public long getGroupId();

	/**
	 * Sets the group ID of this lv entry.
	 *
	 * @param groupId the group ID of this lv entry
	 */
	public void setGroupId(long groupId);

	/**
	 * Returns the unique group key of this lv entry.
	 *
	 * @return the unique group key of this lv entry
	 */
	@AutoEscape
	public String getUniqueGroupKey();

	/**
	 * Sets the unique group key of this lv entry.
	 *
	 * @param uniqueGroupKey the unique group key of this lv entry
	 */
	public void setUniqueGroupKey(String uniqueGroupKey);

	public String[] getAvailableLanguageIds();

	public String getTitle();

	public String getTitle(String languageId);

	public String getTitle(String languageId, boolean useDefault);

	public String getTitleMapAsXML();

	public Map<String, String> getLanguageIdToTitleMap();

	public String getContent();

	public String getContent(String languageId);

	public String getContent(String languageId, boolean useDefault);

	public String getContentMapAsXML();

	public Map<String, String> getLanguageIdToContentMap();

	@Override
	public boolean isNew();

	@Override
	public void setNew(boolean n);

	@Override
	public boolean isCachedModel();

	@Override
	public void setCachedModel(boolean cachedModel);

	@Override
	public boolean isEscapedModel();

	@Override
	public Serializable getPrimaryKeyObj();

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	@Override
	public ExpandoBridge getExpandoBridge();

	@Override
	public void setExpandoBridgeAttributes(BaseModel<?> baseModel);

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge);

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	@Override
	public Object clone();

	@Override
	public int compareTo(LVEntry lvEntry);

	@Override
	public int hashCode();

	@Override
	public CacheModel<LVEntry> toCacheModel();

	@Override
	public LVEntry toEscapedModel();

	@Override
	public LVEntry toUnescapedModel();

	@Override
	public String toString();

	@Override
	public String toXmlString();

}