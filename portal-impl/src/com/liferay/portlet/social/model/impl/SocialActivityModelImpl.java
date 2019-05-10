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

package com.liferay.portlet.social.model.impl;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSON;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.social.kernel.model.SocialActivity;
import com.liferay.social.kernel.model.SocialActivityModel;
import com.liferay.social.kernel.model.SocialActivitySoap;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the SocialActivity service. Represents a row in the &quot;SocialActivity&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface </code>SocialActivityModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link SocialActivityImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SocialActivityImpl
 * @generated
 */
@JSON(strict = true)
@ProviderType
public class SocialActivityModelImpl
	extends BaseModelImpl<SocialActivity> implements SocialActivityModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a social activity model instance should use the <code>SocialActivity</code> interface instead.
	 */
	public static final String TABLE_NAME = "SocialActivity";

	public static final Object[][] TABLE_COLUMNS = {
		{"activityId", Types.BIGINT}, {"groupId", Types.BIGINT},
		{"companyId", Types.BIGINT}, {"userId", Types.BIGINT},
		{"createDate", Types.BIGINT}, {"activitySetId", Types.BIGINT},
		{"mirrorActivityId", Types.BIGINT}, {"classNameId", Types.BIGINT},
		{"classPK", Types.BIGINT}, {"parentClassNameId", Types.BIGINT},
		{"parentClassPK", Types.BIGINT}, {"type_", Types.INTEGER},
		{"extraData", Types.VARCHAR}, {"receiverUserId", Types.BIGINT}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("activityId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("createDate", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("activitySetId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("mirrorActivityId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("classNameId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("classPK", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("parentClassNameId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("parentClassPK", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("type_", Types.INTEGER);
		TABLE_COLUMNS_MAP.put("extraData", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("receiverUserId", Types.BIGINT);
	}

	public static final String TABLE_SQL_CREATE =
		"create table SocialActivity (activityId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,createDate LONG,activitySetId LONG,mirrorActivityId LONG,classNameId LONG,classPK LONG,parentClassNameId LONG,parentClassPK LONG,type_ INTEGER,extraData STRING null,receiverUserId LONG)";

	public static final String TABLE_SQL_DROP = "drop table SocialActivity";

	public static final String ORDER_BY_JPQL =
		" ORDER BY socialActivity.createDate DESC";

	public static final String ORDER_BY_SQL =
		" ORDER BY SocialActivity.createDate DESC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.portal.util.PropsUtil.get(
			"value.object.entity.cache.enabled.com.liferay.social.kernel.model.SocialActivity"),
		true);

	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.portal.util.PropsUtil.get(
			"value.object.finder.cache.enabled.com.liferay.social.kernel.model.SocialActivity"),
		true);

	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(
		com.liferay.portal.util.PropsUtil.get(
			"value.object.column.bitmask.enabled.com.liferay.social.kernel.model.SocialActivity"),
		true);

	public static final long ACTIVITYSETID_COLUMN_BITMASK = 1L;

	public static final long CLASSNAMEID_COLUMN_BITMASK = 2L;

	public static final long CLASSPK_COLUMN_BITMASK = 4L;

	public static final long COMPANYID_COLUMN_BITMASK = 8L;

	public static final long CREATEDATE_COLUMN_BITMASK = 16L;

	public static final long GROUPID_COLUMN_BITMASK = 32L;

	public static final long MIRRORACTIVITYID_COLUMN_BITMASK = 64L;

	public static final long RECEIVERUSERID_COLUMN_BITMASK = 128L;

	public static final long TYPE_COLUMN_BITMASK = 256L;

	public static final long USERID_COLUMN_BITMASK = 512L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static SocialActivity toModel(SocialActivitySoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		SocialActivity model = new SocialActivityImpl();

		model.setActivityId(soapModel.getActivityId());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setCreateDate(soapModel.getCreateDate());
		model.setActivitySetId(soapModel.getActivitySetId());
		model.setMirrorActivityId(soapModel.getMirrorActivityId());
		model.setClassNameId(soapModel.getClassNameId());
		model.setClassPK(soapModel.getClassPK());
		model.setParentClassNameId(soapModel.getParentClassNameId());
		model.setParentClassPK(soapModel.getParentClassPK());
		model.setType(soapModel.getType());
		model.setExtraData(soapModel.getExtraData());
		model.setReceiverUserId(soapModel.getReceiverUserId());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<SocialActivity> toModels(
		SocialActivitySoap[] soapModels) {

		if (soapModels == null) {
			return null;
		}

		List<SocialActivity> models = new ArrayList<SocialActivity>(
			soapModels.length);

		for (SocialActivitySoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(
		com.liferay.portal.util.PropsUtil.get(
			"lock.expiration.time.com.liferay.social.kernel.model.SocialActivity"));

	public SocialActivityModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _activityId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setActivityId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _activityId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return SocialActivity.class;
	}

	@Override
	public String getModelClassName() {
		return SocialActivity.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<SocialActivity, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<SocialActivity, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<SocialActivity, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((SocialActivity)this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<SocialActivity, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<SocialActivity, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(SocialActivity)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<SocialActivity, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<SocialActivity, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static final Map<String, Function<SocialActivity, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<SocialActivity, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<SocialActivity, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<SocialActivity, Object>>();
		Map<String, BiConsumer<SocialActivity, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<SocialActivity, ?>>();

		attributeGetterFunctions.put(
			"activityId", SocialActivity::getActivityId);
		attributeSetterBiConsumers.put(
			"activityId",
			(BiConsumer<SocialActivity, Long>)SocialActivity::setActivityId);
		attributeGetterFunctions.put("groupId", SocialActivity::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId",
			(BiConsumer<SocialActivity, Long>)SocialActivity::setGroupId);
		attributeGetterFunctions.put("companyId", SocialActivity::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<SocialActivity, Long>)SocialActivity::setCompanyId);
		attributeGetterFunctions.put("userId", SocialActivity::getUserId);
		attributeSetterBiConsumers.put(
			"userId",
			(BiConsumer<SocialActivity, Long>)SocialActivity::setUserId);
		attributeGetterFunctions.put(
			"createDate", SocialActivity::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<SocialActivity, Long>)SocialActivity::setCreateDate);
		attributeGetterFunctions.put(
			"activitySetId", SocialActivity::getActivitySetId);
		attributeSetterBiConsumers.put(
			"activitySetId",
			(BiConsumer<SocialActivity, Long>)SocialActivity::setActivitySetId);
		attributeGetterFunctions.put(
			"mirrorActivityId", SocialActivity::getMirrorActivityId);
		attributeSetterBiConsumers.put(
			"mirrorActivityId",
			(BiConsumer<SocialActivity, Long>)
				SocialActivity::setMirrorActivityId);
		attributeGetterFunctions.put(
			"classNameId", SocialActivity::getClassNameId);
		attributeSetterBiConsumers.put(
			"classNameId",
			(BiConsumer<SocialActivity, Long>)SocialActivity::setClassNameId);
		attributeGetterFunctions.put("classPK", SocialActivity::getClassPK);
		attributeSetterBiConsumers.put(
			"classPK",
			(BiConsumer<SocialActivity, Long>)SocialActivity::setClassPK);
		attributeGetterFunctions.put(
			"parentClassNameId", SocialActivity::getParentClassNameId);
		attributeSetterBiConsumers.put(
			"parentClassNameId",
			(BiConsumer<SocialActivity, Long>)
				SocialActivity::setParentClassNameId);
		attributeGetterFunctions.put(
			"parentClassPK", SocialActivity::getParentClassPK);
		attributeSetterBiConsumers.put(
			"parentClassPK",
			(BiConsumer<SocialActivity, Long>)SocialActivity::setParentClassPK);
		attributeGetterFunctions.put("type", SocialActivity::getType);
		attributeSetterBiConsumers.put(
			"type",
			(BiConsumer<SocialActivity, Integer>)SocialActivity::setType);
		attributeGetterFunctions.put("extraData", SocialActivity::getExtraData);
		attributeSetterBiConsumers.put(
			"extraData",
			(BiConsumer<SocialActivity, String>)SocialActivity::setExtraData);
		attributeGetterFunctions.put(
			"receiverUserId", SocialActivity::getReceiverUserId);
		attributeSetterBiConsumers.put(
			"receiverUserId",
			(BiConsumer<SocialActivity, Long>)
				SocialActivity::setReceiverUserId);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@JSON
	@Override
	public long getActivityId() {
		return _activityId;
	}

	@Override
	public void setActivityId(long activityId) {
		_activityId = activityId;
	}

	@JSON
	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		_columnBitmask |= GROUPID_COLUMN_BITMASK;

		if (!_setOriginalGroupId) {
			_setOriginalGroupId = true;

			_originalGroupId = _groupId;
		}

		_groupId = groupId;
	}

	public long getOriginalGroupId() {
		return _originalGroupId;
	}

	@JSON
	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_columnBitmask |= COMPANYID_COLUMN_BITMASK;

		if (!_setOriginalCompanyId) {
			_setOriginalCompanyId = true;

			_originalCompanyId = _companyId;
		}

		_companyId = companyId;
	}

	public long getOriginalCompanyId() {
		return _originalCompanyId;
	}

	@JSON
	@Override
	public long getUserId() {
		return _userId;
	}

	@Override
	public void setUserId(long userId) {
		_columnBitmask |= USERID_COLUMN_BITMASK;

		if (!_setOriginalUserId) {
			_setOriginalUserId = true;

			_originalUserId = _userId;
		}

		_userId = userId;
	}

	@Override
	public String getUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getUserId());

			return user.getUuid();
		}
		catch (PortalException pe) {
			return "";
		}
	}

	@Override
	public void setUserUuid(String userUuid) {
	}

	public long getOriginalUserId() {
		return _originalUserId;
	}

	@JSON
	@Override
	public long getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(long createDate) {
		_columnBitmask = -1L;

		if (!_setOriginalCreateDate) {
			_setOriginalCreateDate = true;

			_originalCreateDate = _createDate;
		}

		_createDate = createDate;
	}

	public long getOriginalCreateDate() {
		return _originalCreateDate;
	}

	@JSON
	@Override
	public long getActivitySetId() {
		return _activitySetId;
	}

	@Override
	public void setActivitySetId(long activitySetId) {
		_columnBitmask |= ACTIVITYSETID_COLUMN_BITMASK;

		if (!_setOriginalActivitySetId) {
			_setOriginalActivitySetId = true;

			_originalActivitySetId = _activitySetId;
		}

		_activitySetId = activitySetId;
	}

	public long getOriginalActivitySetId() {
		return _originalActivitySetId;
	}

	@JSON
	@Override
	public long getMirrorActivityId() {
		return _mirrorActivityId;
	}

	@Override
	public void setMirrorActivityId(long mirrorActivityId) {
		_columnBitmask |= MIRRORACTIVITYID_COLUMN_BITMASK;

		if (!_setOriginalMirrorActivityId) {
			_setOriginalMirrorActivityId = true;

			_originalMirrorActivityId = _mirrorActivityId;
		}

		_mirrorActivityId = mirrorActivityId;
	}

	public long getOriginalMirrorActivityId() {
		return _originalMirrorActivityId;
	}

	@Override
	public String getClassName() {
		if (getClassNameId() <= 0) {
			return "";
		}

		return PortalUtil.getClassName(getClassNameId());
	}

	@Override
	public void setClassName(String className) {
		long classNameId = 0;

		if (Validator.isNotNull(className)) {
			classNameId = PortalUtil.getClassNameId(className);
		}

		setClassNameId(classNameId);
	}

	@JSON
	@Override
	public long getClassNameId() {
		return _classNameId;
	}

	@Override
	public void setClassNameId(long classNameId) {
		_columnBitmask |= CLASSNAMEID_COLUMN_BITMASK;

		if (!_setOriginalClassNameId) {
			_setOriginalClassNameId = true;

			_originalClassNameId = _classNameId;
		}

		_classNameId = classNameId;
	}

	public long getOriginalClassNameId() {
		return _originalClassNameId;
	}

	@JSON
	@Override
	public long getClassPK() {
		return _classPK;
	}

	@Override
	public void setClassPK(long classPK) {
		_columnBitmask |= CLASSPK_COLUMN_BITMASK;

		if (!_setOriginalClassPK) {
			_setOriginalClassPK = true;

			_originalClassPK = _classPK;
		}

		_classPK = classPK;
	}

	public long getOriginalClassPK() {
		return _originalClassPK;
	}

	@JSON
	@Override
	public long getParentClassNameId() {
		return _parentClassNameId;
	}

	@Override
	public void setParentClassNameId(long parentClassNameId) {
		_parentClassNameId = parentClassNameId;
	}

	@JSON
	@Override
	public long getParentClassPK() {
		return _parentClassPK;
	}

	@Override
	public void setParentClassPK(long parentClassPK) {
		_parentClassPK = parentClassPK;
	}

	@JSON
	@Override
	public int getType() {
		return _type;
	}

	@Override
	public void setType(int type) {
		_columnBitmask |= TYPE_COLUMN_BITMASK;

		if (!_setOriginalType) {
			_setOriginalType = true;

			_originalType = _type;
		}

		_type = type;
	}

	public int getOriginalType() {
		return _originalType;
	}

	@JSON
	@Override
	public String getExtraData() {
		if (_extraData == null) {
			return "";
		}
		else {
			return _extraData;
		}
	}

	@Override
	public void setExtraData(String extraData) {
		_extraData = extraData;
	}

	@JSON
	@Override
	public long getReceiverUserId() {
		return _receiverUserId;
	}

	@Override
	public void setReceiverUserId(long receiverUserId) {
		_columnBitmask |= RECEIVERUSERID_COLUMN_BITMASK;

		if (!_setOriginalReceiverUserId) {
			_setOriginalReceiverUserId = true;

			_originalReceiverUserId = _receiverUserId;
		}

		_receiverUserId = receiverUserId;
	}

	@Override
	public String getReceiverUserUuid() {
		try {
			User user = UserLocalServiceUtil.getUserById(getReceiverUserId());

			return user.getUuid();
		}
		catch (PortalException pe) {
			return "";
		}
	}

	@Override
	public void setReceiverUserUuid(String receiverUserUuid) {
	}

	public long getOriginalReceiverUserId() {
		return _originalReceiverUserId;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), SocialActivity.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public SocialActivity toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (SocialActivity)ProxyUtil.newProxyInstance(
				_classLoader, _escapedModelInterfaces,
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		SocialActivityImpl socialActivityImpl = new SocialActivityImpl();

		socialActivityImpl.setActivityId(getActivityId());
		socialActivityImpl.setGroupId(getGroupId());
		socialActivityImpl.setCompanyId(getCompanyId());
		socialActivityImpl.setUserId(getUserId());
		socialActivityImpl.setCreateDate(getCreateDate());
		socialActivityImpl.setActivitySetId(getActivitySetId());
		socialActivityImpl.setMirrorActivityId(getMirrorActivityId());
		socialActivityImpl.setClassNameId(getClassNameId());
		socialActivityImpl.setClassPK(getClassPK());
		socialActivityImpl.setParentClassNameId(getParentClassNameId());
		socialActivityImpl.setParentClassPK(getParentClassPK());
		socialActivityImpl.setType(getType());
		socialActivityImpl.setExtraData(getExtraData());
		socialActivityImpl.setReceiverUserId(getReceiverUserId());

		socialActivityImpl.resetOriginalValues();

		return socialActivityImpl;
	}

	@Override
	public int compareTo(SocialActivity socialActivity) {
		int value = 0;

		if (getCreateDate() < socialActivity.getCreateDate()) {
			value = -1;
		}
		else if (getCreateDate() > socialActivity.getCreateDate()) {
			value = 1;
		}
		else {
			value = 0;
		}

		value = value * -1;

		if (value != 0) {
			return value;
		}

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof SocialActivity)) {
			return false;
		}

		SocialActivity socialActivity = (SocialActivity)obj;

		long primaryKey = socialActivity.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return ENTITY_CACHE_ENABLED;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return FINDER_CACHE_ENABLED;
	}

	@Override
	public void resetOriginalValues() {
		SocialActivityModelImpl socialActivityModelImpl = this;

		socialActivityModelImpl._originalGroupId =
			socialActivityModelImpl._groupId;

		socialActivityModelImpl._setOriginalGroupId = false;

		socialActivityModelImpl._originalCompanyId =
			socialActivityModelImpl._companyId;

		socialActivityModelImpl._setOriginalCompanyId = false;

		socialActivityModelImpl._originalUserId =
			socialActivityModelImpl._userId;

		socialActivityModelImpl._setOriginalUserId = false;

		socialActivityModelImpl._originalCreateDate =
			socialActivityModelImpl._createDate;

		socialActivityModelImpl._setOriginalCreateDate = false;

		socialActivityModelImpl._originalActivitySetId =
			socialActivityModelImpl._activitySetId;

		socialActivityModelImpl._setOriginalActivitySetId = false;

		socialActivityModelImpl._originalMirrorActivityId =
			socialActivityModelImpl._mirrorActivityId;

		socialActivityModelImpl._setOriginalMirrorActivityId = false;

		socialActivityModelImpl._originalClassNameId =
			socialActivityModelImpl._classNameId;

		socialActivityModelImpl._setOriginalClassNameId = false;

		socialActivityModelImpl._originalClassPK =
			socialActivityModelImpl._classPK;

		socialActivityModelImpl._setOriginalClassPK = false;

		socialActivityModelImpl._originalType = socialActivityModelImpl._type;

		socialActivityModelImpl._setOriginalType = false;

		socialActivityModelImpl._originalReceiverUserId =
			socialActivityModelImpl._receiverUserId;

		socialActivityModelImpl._setOriginalReceiverUserId = false;

		socialActivityModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<SocialActivity> toCacheModel() {
		SocialActivityCacheModel socialActivityCacheModel =
			new SocialActivityCacheModel();

		socialActivityCacheModel.activityId = getActivityId();

		socialActivityCacheModel.groupId = getGroupId();

		socialActivityCacheModel.companyId = getCompanyId();

		socialActivityCacheModel.userId = getUserId();

		socialActivityCacheModel.createDate = getCreateDate();

		socialActivityCacheModel.activitySetId = getActivitySetId();

		socialActivityCacheModel.mirrorActivityId = getMirrorActivityId();

		socialActivityCacheModel.classNameId = getClassNameId();

		socialActivityCacheModel.classPK = getClassPK();

		socialActivityCacheModel.parentClassNameId = getParentClassNameId();

		socialActivityCacheModel.parentClassPK = getParentClassPK();

		socialActivityCacheModel.type = getType();

		socialActivityCacheModel.extraData = getExtraData();

		String extraData = socialActivityCacheModel.extraData;

		if ((extraData != null) && (extraData.length() == 0)) {
			socialActivityCacheModel.extraData = null;
		}

		socialActivityCacheModel.receiverUserId = getReceiverUserId();

		return socialActivityCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<SocialActivity, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<SocialActivity, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<SocialActivity, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((SocialActivity)this));
			sb.append(", ");
		}

		if (sb.index() > 1) {
			sb.setIndex(sb.index() - 1);
		}

		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		Map<String, Function<SocialActivity, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<SocialActivity, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<SocialActivity, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((SocialActivity)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader =
		SocialActivity.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
		SocialActivity.class, ModelWrapper.class
	};

	private long _activityId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _userId;
	private long _originalUserId;
	private boolean _setOriginalUserId;
	private long _createDate;
	private long _originalCreateDate;
	private boolean _setOriginalCreateDate;
	private long _activitySetId;
	private long _originalActivitySetId;
	private boolean _setOriginalActivitySetId;
	private long _mirrorActivityId;
	private long _originalMirrorActivityId;
	private boolean _setOriginalMirrorActivityId;
	private long _classNameId;
	private long _originalClassNameId;
	private boolean _setOriginalClassNameId;
	private long _classPK;
	private long _originalClassPK;
	private boolean _setOriginalClassPK;
	private long _parentClassNameId;
	private long _parentClassPK;
	private int _type;
	private int _originalType;
	private boolean _setOriginalType;
	private String _extraData;
	private long _receiverUserId;
	private long _originalReceiverUserId;
	private boolean _setOriginalReceiverUserId;
	private long _columnBitmask;
	private SocialActivity _escapedModel;

}