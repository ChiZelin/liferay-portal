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

package com.liferay.knowledge.base.model.impl;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.knowledge.base.model.KBTemplate;
import com.liferay.knowledge.base.model.KBTemplateModel;
import com.liferay.knowledge.base.model.KBTemplateSoap;
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
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.ProxyUtil;

import java.io.Serializable;

import java.sql.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the KBTemplate service. Represents a row in the &quot;KBTemplate&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface </code>KBTemplateModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link KBTemplateImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see KBTemplateImpl
 * @generated
 */
@JSON(strict = true)
@ProviderType
public class KBTemplateModelImpl
	extends BaseModelImpl<KBTemplate> implements KBTemplateModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a kb template model instance should use the <code>KBTemplate</code> interface instead.
	 */
	public static final String TABLE_NAME = "KBTemplate";

	public static final Object[][] TABLE_COLUMNS = {
		{"uuid_", Types.VARCHAR}, {"kbTemplateId", Types.BIGINT},
		{"groupId", Types.BIGINT}, {"companyId", Types.BIGINT},
		{"userId", Types.BIGINT}, {"userName", Types.VARCHAR},
		{"createDate", Types.TIMESTAMP}, {"modifiedDate", Types.TIMESTAMP},
		{"title", Types.VARCHAR}, {"content", Types.CLOB},
		{"lastPublishDate", Types.TIMESTAMP}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("kbTemplateId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("createDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("modifiedDate", Types.TIMESTAMP);
		TABLE_COLUMNS_MAP.put("title", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("content", Types.CLOB);
		TABLE_COLUMNS_MAP.put("lastPublishDate", Types.TIMESTAMP);
	}

	public static final String TABLE_SQL_CREATE =
		"create table KBTemplate (uuid_ VARCHAR(75) null,kbTemplateId LONG not null primary key,groupId LONG,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,title STRING null,content TEXT null,lastPublishDate DATE null)";

	public static final String TABLE_SQL_DROP = "drop table KBTemplate";

	public static final String ORDER_BY_JPQL =
		" ORDER BY kbTemplate.modifiedDate DESC";

	public static final String ORDER_BY_SQL =
		" ORDER BY KBTemplate.modifiedDate DESC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.knowledge.base.service.util.ServiceProps.get(
			"value.object.entity.cache.enabled.com.liferay.knowledge.base.model.KBTemplate"),
		true);

	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.knowledge.base.service.util.ServiceProps.get(
			"value.object.finder.cache.enabled.com.liferay.knowledge.base.model.KBTemplate"),
		true);

	public static final boolean COLUMN_BITMASK_ENABLED = GetterUtil.getBoolean(
		com.liferay.knowledge.base.service.util.ServiceProps.get(
			"value.object.column.bitmask.enabled.com.liferay.knowledge.base.model.KBTemplate"),
		true);

	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	public static final long GROUPID_COLUMN_BITMASK = 2L;

	public static final long UUID_COLUMN_BITMASK = 4L;

	public static final long MODIFIEDDATE_COLUMN_BITMASK = 8L;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static KBTemplate toModel(KBTemplateSoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		KBTemplate model = new KBTemplateImpl();

		model.setUuid(soapModel.getUuid());
		model.setKbTemplateId(soapModel.getKbTemplateId());
		model.setGroupId(soapModel.getGroupId());
		model.setCompanyId(soapModel.getCompanyId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setCreateDate(soapModel.getCreateDate());
		model.setModifiedDate(soapModel.getModifiedDate());
		model.setTitle(soapModel.getTitle());
		model.setContent(soapModel.getContent());
		model.setLastPublishDate(soapModel.getLastPublishDate());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<KBTemplate> toModels(KBTemplateSoap[] soapModels) {
		if (soapModels == null) {
			return null;
		}

		List<KBTemplate> models = new ArrayList<KBTemplate>(soapModels.length);

		for (KBTemplateSoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(
		com.liferay.knowledge.base.service.util.ServiceProps.get(
			"lock.expiration.time.com.liferay.knowledge.base.model.KBTemplate"));

	public KBTemplateModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _kbTemplateId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setKbTemplateId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _kbTemplateId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return KBTemplate.class;
	}

	@Override
	public String getModelClassName() {
		return KBTemplate.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<KBTemplate, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		for (Map.Entry<String, Function<KBTemplate, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<KBTemplate, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName, attributeGetterFunction.apply((KBTemplate)this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<KBTemplate, Object>> attributeSetterBiConsumers =
			getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<KBTemplate, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(KBTemplate)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<KBTemplate, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<KBTemplate, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static final Map<String, Function<KBTemplate, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<KBTemplate, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<KBTemplate, Object>> attributeGetterFunctions =
			new LinkedHashMap<String, Function<KBTemplate, Object>>();
		Map<String, BiConsumer<KBTemplate, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<KBTemplate, ?>>();

		attributeGetterFunctions.put("uuid", KBTemplate::getUuid);
		attributeSetterBiConsumers.put(
			"uuid", (BiConsumer<KBTemplate, String>)KBTemplate::setUuid);
		attributeGetterFunctions.put(
			"kbTemplateId", KBTemplate::getKbTemplateId);
		attributeSetterBiConsumers.put(
			"kbTemplateId",
			(BiConsumer<KBTemplate, Long>)KBTemplate::setKbTemplateId);
		attributeGetterFunctions.put("groupId", KBTemplate::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId", (BiConsumer<KBTemplate, Long>)KBTemplate::setGroupId);
		attributeGetterFunctions.put("companyId", KBTemplate::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<KBTemplate, Long>)KBTemplate::setCompanyId);
		attributeGetterFunctions.put("userId", KBTemplate::getUserId);
		attributeSetterBiConsumers.put(
			"userId", (BiConsumer<KBTemplate, Long>)KBTemplate::setUserId);
		attributeGetterFunctions.put("userName", KBTemplate::getUserName);
		attributeSetterBiConsumers.put(
			"userName",
			(BiConsumer<KBTemplate, String>)KBTemplate::setUserName);
		attributeGetterFunctions.put("createDate", KBTemplate::getCreateDate);
		attributeSetterBiConsumers.put(
			"createDate",
			(BiConsumer<KBTemplate, Date>)KBTemplate::setCreateDate);
		attributeGetterFunctions.put(
			"modifiedDate", KBTemplate::getModifiedDate);
		attributeSetterBiConsumers.put(
			"modifiedDate",
			(BiConsumer<KBTemplate, Date>)KBTemplate::setModifiedDate);
		attributeGetterFunctions.put("title", KBTemplate::getTitle);
		attributeSetterBiConsumers.put(
			"title", (BiConsumer<KBTemplate, String>)KBTemplate::setTitle);
		attributeGetterFunctions.put("content", KBTemplate::getContent);
		attributeSetterBiConsumers.put(
			"content", (BiConsumer<KBTemplate, String>)KBTemplate::setContent);
		attributeGetterFunctions.put(
			"lastPublishDate", KBTemplate::getLastPublishDate);
		attributeSetterBiConsumers.put(
			"lastPublishDate",
			(BiConsumer<KBTemplate, Date>)KBTemplate::setLastPublishDate);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@JSON
	@Override
	public String getUuid() {
		if (_uuid == null) {
			return "";
		}
		else {
			return _uuid;
		}
	}

	@Override
	public void setUuid(String uuid) {
		_columnBitmask |= UUID_COLUMN_BITMASK;

		if (_originalUuid == null) {
			_originalUuid = _uuid;
		}

		_uuid = uuid;
	}

	public String getOriginalUuid() {
		return GetterUtil.getString(_originalUuid);
	}

	@JSON
	@Override
	public long getKbTemplateId() {
		return _kbTemplateId;
	}

	@Override
	public void setKbTemplateId(long kbTemplateId) {
		_kbTemplateId = kbTemplateId;
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

	@JSON
	@Override
	public String getUserName() {
		if (_userName == null) {
			return "";
		}
		else {
			return _userName;
		}
	}

	@Override
	public void setUserName(String userName) {
		_userName = userName;
	}

	@JSON
	@Override
	public Date getCreateDate() {
		return _createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	@JSON
	@Override
	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public boolean hasSetModifiedDate() {
		return _setModifiedDate;
	}

	@Override
	public void setModifiedDate(Date modifiedDate) {
		_setModifiedDate = true;

		_columnBitmask = -1L;

		_modifiedDate = modifiedDate;
	}

	@JSON
	@Override
	public String getTitle() {
		if (_title == null) {
			return "";
		}
		else {
			return _title;
		}
	}

	@Override
	public void setTitle(String title) {
		_title = title;
	}

	@JSON
	@Override
	public String getContent() {
		if (_content == null) {
			return "";
		}
		else {
			return _content;
		}
	}

	@Override
	public void setContent(String content) {
		_content = content;
	}

	@JSON
	@Override
	public Date getLastPublishDate() {
		return _lastPublishDate;
	}

	@Override
	public void setLastPublishDate(Date lastPublishDate) {
		_lastPublishDate = lastPublishDate;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return new StagedModelType(
			PortalUtil.getClassNameId(KBTemplate.class.getName()));
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), KBTemplate.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public KBTemplate toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (KBTemplate)ProxyUtil.newProxyInstance(
				_classLoader, _escapedModelInterfaces,
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		KBTemplateImpl kbTemplateImpl = new KBTemplateImpl();

		kbTemplateImpl.setUuid(getUuid());
		kbTemplateImpl.setKbTemplateId(getKbTemplateId());
		kbTemplateImpl.setGroupId(getGroupId());
		kbTemplateImpl.setCompanyId(getCompanyId());
		kbTemplateImpl.setUserId(getUserId());
		kbTemplateImpl.setUserName(getUserName());
		kbTemplateImpl.setCreateDate(getCreateDate());
		kbTemplateImpl.setModifiedDate(getModifiedDate());
		kbTemplateImpl.setTitle(getTitle());
		kbTemplateImpl.setContent(getContent());
		kbTemplateImpl.setLastPublishDate(getLastPublishDate());

		kbTemplateImpl.resetOriginalValues();

		return kbTemplateImpl;
	}

	@Override
	public int compareTo(KBTemplate kbTemplate) {
		int value = 0;

		value = DateUtil.compareTo(
			getModifiedDate(), kbTemplate.getModifiedDate());

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

		if (!(obj instanceof KBTemplate)) {
			return false;
		}

		KBTemplate kbTemplate = (KBTemplate)obj;

		long primaryKey = kbTemplate.getPrimaryKey();

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
		KBTemplateModelImpl kbTemplateModelImpl = this;

		kbTemplateModelImpl._originalUuid = kbTemplateModelImpl._uuid;

		kbTemplateModelImpl._originalGroupId = kbTemplateModelImpl._groupId;

		kbTemplateModelImpl._setOriginalGroupId = false;

		kbTemplateModelImpl._originalCompanyId = kbTemplateModelImpl._companyId;

		kbTemplateModelImpl._setOriginalCompanyId = false;

		kbTemplateModelImpl._setModifiedDate = false;

		kbTemplateModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<KBTemplate> toCacheModel() {
		KBTemplateCacheModel kbTemplateCacheModel = new KBTemplateCacheModel();

		kbTemplateCacheModel.uuid = getUuid();

		String uuid = kbTemplateCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			kbTemplateCacheModel.uuid = null;
		}

		kbTemplateCacheModel.kbTemplateId = getKbTemplateId();

		kbTemplateCacheModel.groupId = getGroupId();

		kbTemplateCacheModel.companyId = getCompanyId();

		kbTemplateCacheModel.userId = getUserId();

		kbTemplateCacheModel.userName = getUserName();

		String userName = kbTemplateCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			kbTemplateCacheModel.userName = null;
		}

		Date createDate = getCreateDate();

		if (createDate != null) {
			kbTemplateCacheModel.createDate = createDate.getTime();
		}
		else {
			kbTemplateCacheModel.createDate = Long.MIN_VALUE;
		}

		Date modifiedDate = getModifiedDate();

		if (modifiedDate != null) {
			kbTemplateCacheModel.modifiedDate = modifiedDate.getTime();
		}
		else {
			kbTemplateCacheModel.modifiedDate = Long.MIN_VALUE;
		}

		kbTemplateCacheModel.title = getTitle();

		String title = kbTemplateCacheModel.title;

		if ((title != null) && (title.length() == 0)) {
			kbTemplateCacheModel.title = null;
		}

		kbTemplateCacheModel.content = getContent();

		String content = kbTemplateCacheModel.content;

		if ((content != null) && (content.length() == 0)) {
			kbTemplateCacheModel.content = null;
		}

		Date lastPublishDate = getLastPublishDate();

		if (lastPublishDate != null) {
			kbTemplateCacheModel.lastPublishDate = lastPublishDate.getTime();
		}
		else {
			kbTemplateCacheModel.lastPublishDate = Long.MIN_VALUE;
		}

		return kbTemplateCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<KBTemplate, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<KBTemplate, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<KBTemplate, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((KBTemplate)this));
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
		Map<String, Function<KBTemplate, Object>> attributeGetterFunctions =
			getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<KBTemplate, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<KBTemplate, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((KBTemplate)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader =
		KBTemplate.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
		KBTemplate.class, ModelWrapper.class
	};

	private String _uuid;
	private String _originalUuid;
	private long _kbTemplateId;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;
	private boolean _setModifiedDate;
	private String _title;
	private String _content;
	private Date _lastPublishDate;
	private long _columnBitmask;
	private KBTemplate _escapedModel;

}