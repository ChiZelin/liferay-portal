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

package com.liferay.portal.workflow.kaleo.model;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.model.ShardedModel;

import java.util.Date;

/**
 * The base model interface for the KaleoNode service. Represents a row in the &quot;KaleoNode&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.portal.workflow.kaleo.model.impl.KaleoNodeModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.portal.workflow.kaleo.model.impl.KaleoNodeImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KaleoNode
 * @generated
 */
@ProviderType
public interface KaleoNodeModel
	extends BaseModel<KaleoNode>, GroupedModel, MVCCModel, ShardedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a kaleo node model instance should use the {@link KaleoNode} interface instead.
	 */

	/**
	 * Returns the primary key of this kaleo node.
	 *
	 * @return the primary key of this kaleo node
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this kaleo node.
	 *
	 * @param primaryKey the primary key of this kaleo node
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this kaleo node.
	 *
	 * @return the mvcc version of this kaleo node
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this kaleo node.
	 *
	 * @param mvccVersion the mvcc version of this kaleo node
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the kaleo node ID of this kaleo node.
	 *
	 * @return the kaleo node ID of this kaleo node
	 */
	public long getKaleoNodeId();

	/**
	 * Sets the kaleo node ID of this kaleo node.
	 *
	 * @param kaleoNodeId the kaleo node ID of this kaleo node
	 */
	public void setKaleoNodeId(long kaleoNodeId);

	/**
	 * Returns the group ID of this kaleo node.
	 *
	 * @return the group ID of this kaleo node
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this kaleo node.
	 *
	 * @param groupId the group ID of this kaleo node
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this kaleo node.
	 *
	 * @return the company ID of this kaleo node
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this kaleo node.
	 *
	 * @param companyId the company ID of this kaleo node
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this kaleo node.
	 *
	 * @return the user ID of this kaleo node
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this kaleo node.
	 *
	 * @param userId the user ID of this kaleo node
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this kaleo node.
	 *
	 * @return the user uuid of this kaleo node
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this kaleo node.
	 *
	 * @param userUuid the user uuid of this kaleo node
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this kaleo node.
	 *
	 * @return the user name of this kaleo node
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this kaleo node.
	 *
	 * @param userName the user name of this kaleo node
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this kaleo node.
	 *
	 * @return the create date of this kaleo node
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this kaleo node.
	 *
	 * @param createDate the create date of this kaleo node
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this kaleo node.
	 *
	 * @return the modified date of this kaleo node
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this kaleo node.
	 *
	 * @param modifiedDate the modified date of this kaleo node
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the kaleo definition version ID of this kaleo node.
	 *
	 * @return the kaleo definition version ID of this kaleo node
	 */
	public long getKaleoDefinitionVersionId();

	/**
	 * Sets the kaleo definition version ID of this kaleo node.
	 *
	 * @param kaleoDefinitionVersionId the kaleo definition version ID of this kaleo node
	 */
	public void setKaleoDefinitionVersionId(long kaleoDefinitionVersionId);

	/**
	 * Returns the name of this kaleo node.
	 *
	 * @return the name of this kaleo node
	 */
	@AutoEscape
	public String getName();

	/**
	 * Sets the name of this kaleo node.
	 *
	 * @param name the name of this kaleo node
	 */
	public void setName(String name);

	/**
	 * Returns the metadata of this kaleo node.
	 *
	 * @return the metadata of this kaleo node
	 */
	@AutoEscape
	public String getMetadata();

	/**
	 * Sets the metadata of this kaleo node.
	 *
	 * @param metadata the metadata of this kaleo node
	 */
	public void setMetadata(String metadata);

	/**
	 * Returns the description of this kaleo node.
	 *
	 * @return the description of this kaleo node
	 */
	@AutoEscape
	public String getDescription();

	/**
	 * Sets the description of this kaleo node.
	 *
	 * @param description the description of this kaleo node
	 */
	public void setDescription(String description);

	/**
	 * Returns the type of this kaleo node.
	 *
	 * @return the type of this kaleo node
	 */
	@AutoEscape
	public String getType();

	/**
	 * Sets the type of this kaleo node.
	 *
	 * @param type the type of this kaleo node
	 */
	public void setType(String type);

	/**
	 * Returns the initial of this kaleo node.
	 *
	 * @return the initial of this kaleo node
	 */
	public boolean getInitial();

	/**
	 * Returns <code>true</code> if this kaleo node is initial.
	 *
	 * @return <code>true</code> if this kaleo node is initial; <code>false</code> otherwise
	 */
	public boolean isInitial();

	/**
	 * Sets whether this kaleo node is initial.
	 *
	 * @param initial the initial of this kaleo node
	 */
	public void setInitial(boolean initial);

	/**
	 * Returns the terminal of this kaleo node.
	 *
	 * @return the terminal of this kaleo node
	 */
	public boolean getTerminal();

	/**
	 * Returns <code>true</code> if this kaleo node is terminal.
	 *
	 * @return <code>true</code> if this kaleo node is terminal; <code>false</code> otherwise
	 */
	public boolean isTerminal();

	/**
	 * Sets whether this kaleo node is terminal.
	 *
	 * @param terminal the terminal of this kaleo node
	 */
	public void setTerminal(boolean terminal);

}