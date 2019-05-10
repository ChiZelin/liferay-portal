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

package com.liferay.portal.tools.service.builder.test.model.impl;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
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
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.tools.service.builder.test.model.UADPartialEntry;
import com.liferay.portal.tools.service.builder.test.model.UADPartialEntryModel;
import com.liferay.portal.tools.service.builder.test.model.UADPartialEntrySoap;

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
 * The base model implementation for the UADPartialEntry service. Represents a row in the &quot;UADPartialEntry&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface </code>UADPartialEntryModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link UADPartialEntryImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see UADPartialEntryImpl
 * @generated
 */
@JSON(strict = true)
@ProviderType
public class UADPartialEntryModelImpl
	extends BaseModelImpl<UADPartialEntry> implements UADPartialEntryModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a uad partial entry model instance should use the <code>UADPartialEntry</code> interface instead.
	 */
	public static final String TABLE_NAME = "UADPartialEntry";

	public static final Object[][] TABLE_COLUMNS = {
		{"uadPartialEntryId", Types.BIGINT}, {"userId", Types.BIGINT},
		{"userName", Types.VARCHAR}, {"message", Types.VARCHAR}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("uadPartialEntryId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("userName", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("message", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE =
		"create table UADPartialEntry (uadPartialEntryId LONG not null primary key,userId LONG,userName VARCHAR(75) null,message VARCHAR(75) null)";

	public static final String TABLE_SQL_DROP = "drop table UADPartialEntry";

	public static final String ORDER_BY_JPQL =
		" ORDER BY uadPartialEntry.uadPartialEntryId ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY UADPartialEntry.uadPartialEntryId ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	public static final boolean ENTITY_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.portal.tools.service.builder.test.service.util.ServiceProps.
			get(
				"value.object.entity.cache.enabled.com.liferay.portal.tools.service.builder.test.model.UADPartialEntry"),
		true);

	public static final boolean FINDER_CACHE_ENABLED = GetterUtil.getBoolean(
		com.liferay.portal.tools.service.builder.test.service.util.ServiceProps.
			get(
				"value.object.finder.cache.enabled.com.liferay.portal.tools.service.builder.test.model.UADPartialEntry"),
		true);

	public static final boolean COLUMN_BITMASK_ENABLED = false;

	/**
	 * Converts the soap model instance into a normal model instance.
	 *
	 * @param soapModel the soap model instance to convert
	 * @return the normal model instance
	 */
	public static UADPartialEntry toModel(UADPartialEntrySoap soapModel) {
		if (soapModel == null) {
			return null;
		}

		UADPartialEntry model = new UADPartialEntryImpl();

		model.setUadPartialEntryId(soapModel.getUadPartialEntryId());
		model.setUserId(soapModel.getUserId());
		model.setUserName(soapModel.getUserName());
		model.setMessage(soapModel.getMessage());

		return model;
	}

	/**
	 * Converts the soap model instances into normal model instances.
	 *
	 * @param soapModels the soap model instances to convert
	 * @return the normal model instances
	 */
	public static List<UADPartialEntry> toModels(
		UADPartialEntrySoap[] soapModels) {

		if (soapModels == null) {
			return null;
		}

		List<UADPartialEntry> models = new ArrayList<UADPartialEntry>(
			soapModels.length);

		for (UADPartialEntrySoap soapModel : soapModels) {
			models.add(toModel(soapModel));
		}

		return models;
	}

	public static final long LOCK_EXPIRATION_TIME = GetterUtil.getLong(
		com.liferay.portal.tools.service.builder.test.service.util.ServiceProps.
			get(
				"lock.expiration.time.com.liferay.portal.tools.service.builder.test.model.UADPartialEntry"));

	public UADPartialEntryModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _uadPartialEntryId;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setUadPartialEntryId(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _uadPartialEntryId;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return UADPartialEntry.class;
	}

	@Override
	public String getModelClassName() {
		return UADPartialEntry.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<UADPartialEntry, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<UADPartialEntry, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<UADPartialEntry, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((UADPartialEntry)this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<UADPartialEntry, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<UADPartialEntry, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(UADPartialEntry)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<UADPartialEntry, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<UADPartialEntry, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static final Map<String, Function<UADPartialEntry, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<UADPartialEntry, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<UADPartialEntry, Object>>
			attributeGetterFunctions =
				new LinkedHashMap<String, Function<UADPartialEntry, Object>>();
		Map<String, BiConsumer<UADPartialEntry, ?>> attributeSetterBiConsumers =
			new LinkedHashMap<String, BiConsumer<UADPartialEntry, ?>>();

		attributeGetterFunctions.put(
			"uadPartialEntryId",
			new Function<UADPartialEntry, Object>() {

				@Override
				public Object apply(UADPartialEntry uadPartialEntry) {
					return uadPartialEntry.getUadPartialEntryId();
				}

			});
		attributeSetterBiConsumers.put(
			"uadPartialEntryId",
			new BiConsumer<UADPartialEntry, Object>() {

				@Override
				public void accept(
					UADPartialEntry uadPartialEntry, Object uadPartialEntryId) {

					uadPartialEntry.setUadPartialEntryId(
						(Long)uadPartialEntryId);
				}

			});
		attributeGetterFunctions.put(
			"userId",
			new Function<UADPartialEntry, Object>() {

				@Override
				public Object apply(UADPartialEntry uadPartialEntry) {
					return uadPartialEntry.getUserId();
				}

			});
		attributeSetterBiConsumers.put(
			"userId",
			new BiConsumer<UADPartialEntry, Object>() {

				@Override
				public void accept(
					UADPartialEntry uadPartialEntry, Object userId) {

					uadPartialEntry.setUserId((Long)userId);
				}

			});
		attributeGetterFunctions.put(
			"userName",
			new Function<UADPartialEntry, Object>() {

				@Override
				public Object apply(UADPartialEntry uadPartialEntry) {
					return uadPartialEntry.getUserName();
				}

			});
		attributeSetterBiConsumers.put(
			"userName",
			new BiConsumer<UADPartialEntry, Object>() {

				@Override
				public void accept(
					UADPartialEntry uadPartialEntry, Object userName) {

					uadPartialEntry.setUserName((String)userName);
				}

			});
		attributeGetterFunctions.put(
			"message",
			new Function<UADPartialEntry, Object>() {

				@Override
				public Object apply(UADPartialEntry uadPartialEntry) {
					return uadPartialEntry.getMessage();
				}

			});
		attributeSetterBiConsumers.put(
			"message",
			new BiConsumer<UADPartialEntry, Object>() {

				@Override
				public void accept(
					UADPartialEntry uadPartialEntry, Object message) {

					uadPartialEntry.setMessage((String)message);
				}

			});

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@JSON
	@Override
	public long getUadPartialEntryId() {
		return _uadPartialEntryId;
	}

	@Override
	public void setUadPartialEntryId(long uadPartialEntryId) {
		_uadPartialEntryId = uadPartialEntryId;
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
	public String getMessage() {
		if (_message == null) {
			return "";
		}
		else {
			return _message;
		}
	}

	@Override
	public void setMessage(String message) {
		_message = message;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			0, UADPartialEntry.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public UADPartialEntry toEscapedModel() {
		if (_escapedModel == null) {
			_escapedModel = (UADPartialEntry)ProxyUtil.newProxyInstance(
				_classLoader, _escapedModelInterfaces,
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		UADPartialEntryImpl uadPartialEntryImpl = new UADPartialEntryImpl();

		uadPartialEntryImpl.setUadPartialEntryId(getUadPartialEntryId());
		uadPartialEntryImpl.setUserId(getUserId());
		uadPartialEntryImpl.setUserName(getUserName());
		uadPartialEntryImpl.setMessage(getMessage());

		uadPartialEntryImpl.resetOriginalValues();

		return uadPartialEntryImpl;
	}

	@Override
	public int compareTo(UADPartialEntry uadPartialEntry) {
		long primaryKey = uadPartialEntry.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof UADPartialEntry)) {
			return false;
		}

		UADPartialEntry uadPartialEntry = (UADPartialEntry)obj;

		long primaryKey = uadPartialEntry.getPrimaryKey();

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
	}

	@Override
	public CacheModel<UADPartialEntry> toCacheModel() {
		UADPartialEntryCacheModel uadPartialEntryCacheModel =
			new UADPartialEntryCacheModel();

		uadPartialEntryCacheModel.uadPartialEntryId = getUadPartialEntryId();

		uadPartialEntryCacheModel.userId = getUserId();

		uadPartialEntryCacheModel.userName = getUserName();

		String userName = uadPartialEntryCacheModel.userName;

		if ((userName != null) && (userName.length() == 0)) {
			uadPartialEntryCacheModel.userName = null;
		}

		uadPartialEntryCacheModel.message = getMessage();

		String message = uadPartialEntryCacheModel.message;

		if ((message != null) && (message.length() == 0)) {
			uadPartialEntryCacheModel.message = null;
		}

		return uadPartialEntryCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<UADPartialEntry, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<UADPartialEntry, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<UADPartialEntry, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((UADPartialEntry)this));
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
		Map<String, Function<UADPartialEntry, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<UADPartialEntry, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<UADPartialEntry, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((UADPartialEntry)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static final ClassLoader _classLoader =
		UADPartialEntry.class.getClassLoader();
	private static final Class<?>[] _escapedModelInterfaces = new Class[] {
		UADPartialEntry.class, ModelWrapper.class
	};

	private long _uadPartialEntryId;
	private long _userId;
	private String _userName;
	private String _message;
	private UADPartialEntry _escapedModel;

}