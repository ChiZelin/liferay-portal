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

package com.liferay.polls.model;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.model.StagedGroupedModel;

import java.util.Date;

/**
 * The base model interface for the PollsVote service. Represents a row in the &quot;PollsVote&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.polls.model.impl.PollsVoteModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.polls.model.impl.PollsVoteImpl</code>.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see PollsVote
 * @generated
 */
@ProviderType
public interface PollsVoteModel
	extends BaseModel<PollsVote>, ShardedModel, StagedGroupedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a polls vote model instance should use the {@link PollsVote} interface instead.
	 */

	/**
	 * Returns the primary key of this polls vote.
	 *
	 * @return the primary key of this polls vote
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this polls vote.
	 *
	 * @param primaryKey the primary key of this polls vote
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this polls vote.
	 *
	 * @return the uuid of this polls vote
	 */
	@AutoEscape
	@Override
	public String getUuid();

	/**
	 * Sets the uuid of this polls vote.
	 *
	 * @param uuid the uuid of this polls vote
	 */
	@Override
	public void setUuid(String uuid);

	/**
	 * Returns the vote ID of this polls vote.
	 *
	 * @return the vote ID of this polls vote
	 */
	public long getVoteId();

	/**
	 * Sets the vote ID of this polls vote.
	 *
	 * @param voteId the vote ID of this polls vote
	 */
	public void setVoteId(long voteId);

	/**
	 * Returns the group ID of this polls vote.
	 *
	 * @return the group ID of this polls vote
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this polls vote.
	 *
	 * @param groupId the group ID of this polls vote
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this polls vote.
	 *
	 * @return the company ID of this polls vote
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this polls vote.
	 *
	 * @param companyId the company ID of this polls vote
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this polls vote.
	 *
	 * @return the user ID of this polls vote
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this polls vote.
	 *
	 * @param userId the user ID of this polls vote
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this polls vote.
	 *
	 * @return the user uuid of this polls vote
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this polls vote.
	 *
	 * @param userUuid the user uuid of this polls vote
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this polls vote.
	 *
	 * @return the user name of this polls vote
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this polls vote.
	 *
	 * @param userName the user name of this polls vote
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this polls vote.
	 *
	 * @return the create date of this polls vote
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this polls vote.
	 *
	 * @param createDate the create date of this polls vote
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this polls vote.
	 *
	 * @return the modified date of this polls vote
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this polls vote.
	 *
	 * @param modifiedDate the modified date of this polls vote
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the question ID of this polls vote.
	 *
	 * @return the question ID of this polls vote
	 */
	public long getQuestionId();

	/**
	 * Sets the question ID of this polls vote.
	 *
	 * @param questionId the question ID of this polls vote
	 */
	public void setQuestionId(long questionId);

	/**
	 * Returns the choice ID of this polls vote.
	 *
	 * @return the choice ID of this polls vote
	 */
	public long getChoiceId();

	/**
	 * Sets the choice ID of this polls vote.
	 *
	 * @param choiceId the choice ID of this polls vote
	 */
	public void setChoiceId(long choiceId);

	/**
	 * Returns the last publish date of this polls vote.
	 *
	 * @return the last publish date of this polls vote
	 */
	@Override
	public Date getLastPublishDate();

	/**
	 * Sets the last publish date of this polls vote.
	 *
	 * @param lastPublishDate the last publish date of this polls vote
	 */
	@Override
	public void setLastPublishDate(Date lastPublishDate);

	/**
	 * Returns the vote date of this polls vote.
	 *
	 * @return the vote date of this polls vote
	 */
	public Date getVoteDate();

	/**
	 * Sets the vote date of this polls vote.
	 *
	 * @param voteDate the vote date of this polls vote
	 */
	public void setVoteDate(Date voteDate);

}