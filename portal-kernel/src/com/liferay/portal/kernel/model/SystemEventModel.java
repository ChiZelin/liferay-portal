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

package com.liferay.portal.kernel.model;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscape;

import java.util.Date;

/**
 * The base model interface for the SystemEvent service. Represents a row in the &quot;SystemEvent&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.portal.model.impl.SystemEventModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.portal.model.impl.SystemEventImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SystemEvent
 * @generated
 */
@ProviderType
public interface SystemEventModel
	extends AttachedModel, BaseModel<SystemEvent>, MVCCModel, ShardedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a system event model instance should use the {@link SystemEvent} interface instead.
	 */

	/**
	 * Returns the primary key of this system event.
	 *
	 * @return the primary key of this system event
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this system event.
	 *
	 * @param primaryKey the primary key of this system event
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this system event.
	 *
	 * @return the mvcc version of this system event
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this system event.
	 *
	 * @param mvccVersion the mvcc version of this system event
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the system event ID of this system event.
	 *
	 * @return the system event ID of this system event
	 */
	public long getSystemEventId();

	/**
	 * Sets the system event ID of this system event.
	 *
	 * @param systemEventId the system event ID of this system event
	 */
	public void setSystemEventId(long systemEventId);

	/**
	 * Returns the group ID of this system event.
	 *
	 * @return the group ID of this system event
	 */
	public long getGroupId();

	/**
	 * Sets the group ID of this system event.
	 *
	 * @param groupId the group ID of this system event
	 */
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this system event.
	 *
	 * @return the company ID of this system event
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this system event.
	 *
	 * @param companyId the company ID of this system event
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this system event.
	 *
	 * @return the user ID of this system event
	 */
	public long getUserId();

	/**
	 * Sets the user ID of this system event.
	 *
	 * @param userId the user ID of this system event
	 */
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this system event.
	 *
	 * @return the user uuid of this system event
	 */
	public String getUserUuid();

	/**
	 * Sets the user uuid of this system event.
	 *
	 * @param userUuid the user uuid of this system event
	 */
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this system event.
	 *
	 * @return the user name of this system event
	 */
	@AutoEscape
	public String getUserName();

	/**
	 * Sets the user name of this system event.
	 *
	 * @param userName the user name of this system event
	 */
	public void setUserName(String userName);

	/**
	 * Returns the create date of this system event.
	 *
	 * @return the create date of this system event
	 */
	public Date getCreateDate();

	/**
	 * Sets the create date of this system event.
	 *
	 * @param createDate the create date of this system event
	 */
	public void setCreateDate(Date createDate);

	/**
	 * Returns the fully qualified class name of this system event.
	 *
	 * @return the fully qualified class name of this system event
	 */
	@Override
	public String getClassName();

	public void setClassName(String className);

	/**
	 * Returns the class name ID of this system event.
	 *
	 * @return the class name ID of this system event
	 */
	@Override
	public long getClassNameId();

	/**
	 * Sets the class name ID of this system event.
	 *
	 * @param classNameId the class name ID of this system event
	 */
	@Override
	public void setClassNameId(long classNameId);

	/**
	 * Returns the class pk of this system event.
	 *
	 * @return the class pk of this system event
	 */
	@Override
	public long getClassPK();

	/**
	 * Sets the class pk of this system event.
	 *
	 * @param classPK the class pk of this system event
	 */
	@Override
	public void setClassPK(long classPK);

	/**
	 * Returns the class uuid of this system event.
	 *
	 * @return the class uuid of this system event
	 */
	@AutoEscape
	public String getClassUuid();

	/**
	 * Sets the class uuid of this system event.
	 *
	 * @param classUuid the class uuid of this system event
	 */
	public void setClassUuid(String classUuid);

	/**
	 * Returns the referrer class name ID of this system event.
	 *
	 * @return the referrer class name ID of this system event
	 */
	public long getReferrerClassNameId();

	/**
	 * Sets the referrer class name ID of this system event.
	 *
	 * @param referrerClassNameId the referrer class name ID of this system event
	 */
	public void setReferrerClassNameId(long referrerClassNameId);

	/**
	 * Returns the parent system event ID of this system event.
	 *
	 * @return the parent system event ID of this system event
	 */
	public long getParentSystemEventId();

	/**
	 * Sets the parent system event ID of this system event.
	 *
	 * @param parentSystemEventId the parent system event ID of this system event
	 */
	public void setParentSystemEventId(long parentSystemEventId);

	/**
	 * Returns the system event set key of this system event.
	 *
	 * @return the system event set key of this system event
	 */
	public long getSystemEventSetKey();

	/**
	 * Sets the system event set key of this system event.
	 *
	 * @param systemEventSetKey the system event set key of this system event
	 */
	public void setSystemEventSetKey(long systemEventSetKey);

	/**
	 * Returns the type of this system event.
	 *
	 * @return the type of this system event
	 */
	public int getType();

	/**
	 * Sets the type of this system event.
	 *
	 * @param type the type of this system event
	 */
	public void setType(int type);

	/**
	 * Returns the extra data of this system event.
	 *
	 * @return the extra data of this system event
	 */
	@AutoEscape
	public String getExtraData();

	/**
	 * Sets the extra data of this system event.
	 *
	 * @param extraData the extra data of this system event
	 */
	public void setExtraData(String extraData);

}