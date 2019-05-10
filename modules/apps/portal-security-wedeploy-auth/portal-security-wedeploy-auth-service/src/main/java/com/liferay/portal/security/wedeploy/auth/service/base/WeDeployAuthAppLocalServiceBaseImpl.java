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

package com.liferay.portal.security.wedeploy.auth.service.base;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.IndexableActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Projection;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;
import com.liferay.portal.kernel.service.BaseLocalServiceImpl;
import com.liferay.portal.kernel.service.PersistedModelLocalServiceRegistry;
import com.liferay.portal.kernel.service.persistence.ClassNamePersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.security.wedeploy.auth.model.WeDeployAuthApp;
import com.liferay.portal.security.wedeploy.auth.service.WeDeployAuthAppLocalService;
import com.liferay.portal.security.wedeploy.auth.service.persistence.WeDeployAuthAppPersistence;
import com.liferay.portal.security.wedeploy.auth.service.persistence.WeDeployAuthTokenPersistence;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the we deploy auth app local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.portal.security.wedeploy.auth.service.impl.WeDeployAuthAppLocalServiceImpl}.
 * </p>
 *
 * @author Supritha Sundaram
 * @see com.liferay.portal.security.wedeploy.auth.service.impl.WeDeployAuthAppLocalServiceImpl
 * @generated
 */
@ProviderType
public abstract class WeDeployAuthAppLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements WeDeployAuthAppLocalService, IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>WeDeployAuthAppLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.portal.security.wedeploy.auth.service.WeDeployAuthAppLocalServiceUtil</code>.
	 */

	/**
	 * Adds the we deploy auth app to the database. Also notifies the appropriate model listeners.
	 *
	 * @param weDeployAuthApp the we deploy auth app
	 * @return the we deploy auth app that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public WeDeployAuthApp addWeDeployAuthApp(WeDeployAuthApp weDeployAuthApp) {
		weDeployAuthApp.setNew(true);

		return weDeployAuthAppPersistence.update(weDeployAuthApp);
	}

	/**
	 * Creates a new we deploy auth app with the primary key. Does not add the we deploy auth app to the database.
	 *
	 * @param weDeployAuthAppId the primary key for the new we deploy auth app
	 * @return the new we deploy auth app
	 */
	@Override
	@Transactional(enabled = false)
	public WeDeployAuthApp createWeDeployAuthApp(long weDeployAuthAppId) {
		return weDeployAuthAppPersistence.create(weDeployAuthAppId);
	}

	/**
	 * Deletes the we deploy auth app with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param weDeployAuthAppId the primary key of the we deploy auth app
	 * @return the we deploy auth app that was removed
	 * @throws PortalException if a we deploy auth app with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public WeDeployAuthApp deleteWeDeployAuthApp(long weDeployAuthAppId)
		throws PortalException {

		return weDeployAuthAppPersistence.remove(weDeployAuthAppId);
	}

	/**
	 * Deletes the we deploy auth app from the database. Also notifies the appropriate model listeners.
	 *
	 * @param weDeployAuthApp the we deploy auth app
	 * @return the we deploy auth app that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public WeDeployAuthApp deleteWeDeployAuthApp(
		WeDeployAuthApp weDeployAuthApp) {

		return weDeployAuthAppPersistence.remove(weDeployAuthApp);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			WeDeployAuthApp.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return weDeployAuthAppPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.wedeploy.auth.model.impl.WeDeployAuthAppModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @return the range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end) {

		return weDeployAuthAppPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.wedeploy.auth.model.impl.WeDeployAuthAppModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param dynamicQuery the dynamic query
	 * @param start the lower bound of the range of model instances
	 * @param end the upper bound of the range of model instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(
		DynamicQuery dynamicQuery, int start, int end,
		OrderByComparator<T> orderByComparator) {

		return weDeployAuthAppPersistence.findWithDynamicQuery(
			dynamicQuery, start, end, orderByComparator);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(DynamicQuery dynamicQuery) {
		return weDeployAuthAppPersistence.countWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Returns the number of rows matching the dynamic query.
	 *
	 * @param dynamicQuery the dynamic query
	 * @param projection the projection to apply to the query
	 * @return the number of rows matching the dynamic query
	 */
	@Override
	public long dynamicQueryCount(
		DynamicQuery dynamicQuery, Projection projection) {

		return weDeployAuthAppPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public WeDeployAuthApp fetchWeDeployAuthApp(long weDeployAuthAppId) {
		return weDeployAuthAppPersistence.fetchByPrimaryKey(weDeployAuthAppId);
	}

	/**
	 * Returns the we deploy auth app with the primary key.
	 *
	 * @param weDeployAuthAppId the primary key of the we deploy auth app
	 * @return the we deploy auth app
	 * @throws PortalException if a we deploy auth app with the primary key could not be found
	 */
	@Override
	public WeDeployAuthApp getWeDeployAuthApp(long weDeployAuthAppId)
		throws PortalException {

		return weDeployAuthAppPersistence.findByPrimaryKey(weDeployAuthAppId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(weDeployAuthAppLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(WeDeployAuthApp.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("weDeployAuthAppId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			weDeployAuthAppLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(WeDeployAuthApp.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"weDeployAuthAppId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(weDeployAuthAppLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(WeDeployAuthApp.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("weDeployAuthAppId");
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return weDeployAuthAppLocalService.deleteWeDeployAuthApp(
			(WeDeployAuthApp)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return weDeployAuthAppPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns a range of all the we deploy auth apps.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.portal.security.wedeploy.auth.model.impl.WeDeployAuthAppModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of we deploy auth apps
	 * @param end the upper bound of the range of we deploy auth apps (not inclusive)
	 * @return the range of we deploy auth apps
	 */
	@Override
	public List<WeDeployAuthApp> getWeDeployAuthApps(int start, int end) {
		return weDeployAuthAppPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of we deploy auth apps.
	 *
	 * @return the number of we deploy auth apps
	 */
	@Override
	public int getWeDeployAuthAppsCount() {
		return weDeployAuthAppPersistence.countAll();
	}

	/**
	 * Updates the we deploy auth app in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param weDeployAuthApp the we deploy auth app
	 * @return the we deploy auth app that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public WeDeployAuthApp updateWeDeployAuthApp(
		WeDeployAuthApp weDeployAuthApp) {

		return weDeployAuthAppPersistence.update(weDeployAuthApp);
	}

	/**
	 * Returns the we deploy auth app local service.
	 *
	 * @return the we deploy auth app local service
	 */
	public WeDeployAuthAppLocalService getWeDeployAuthAppLocalService() {
		return weDeployAuthAppLocalService;
	}

	/**
	 * Sets the we deploy auth app local service.
	 *
	 * @param weDeployAuthAppLocalService the we deploy auth app local service
	 */
	public void setWeDeployAuthAppLocalService(
		WeDeployAuthAppLocalService weDeployAuthAppLocalService) {

		this.weDeployAuthAppLocalService = weDeployAuthAppLocalService;
	}

	/**
	 * Returns the we deploy auth app persistence.
	 *
	 * @return the we deploy auth app persistence
	 */
	public WeDeployAuthAppPersistence getWeDeployAuthAppPersistence() {
		return weDeployAuthAppPersistence;
	}

	/**
	 * Sets the we deploy auth app persistence.
	 *
	 * @param weDeployAuthAppPersistence the we deploy auth app persistence
	 */
	public void setWeDeployAuthAppPersistence(
		WeDeployAuthAppPersistence weDeployAuthAppPersistence) {

		this.weDeployAuthAppPersistence = weDeployAuthAppPersistence;
	}

	/**
	 * Returns the we deploy auth token local service.
	 *
	 * @return the we deploy auth token local service
	 */
	public com.liferay.portal.security.wedeploy.auth.service.
		WeDeployAuthTokenLocalService getWeDeployAuthTokenLocalService() {

		return weDeployAuthTokenLocalService;
	}

	/**
	 * Sets the we deploy auth token local service.
	 *
	 * @param weDeployAuthTokenLocalService the we deploy auth token local service
	 */
	public void setWeDeployAuthTokenLocalService(
		com.liferay.portal.security.wedeploy.auth.service.
			WeDeployAuthTokenLocalService weDeployAuthTokenLocalService) {

		this.weDeployAuthTokenLocalService = weDeployAuthTokenLocalService;
	}

	/**
	 * Returns the we deploy auth token persistence.
	 *
	 * @return the we deploy auth token persistence
	 */
	public WeDeployAuthTokenPersistence getWeDeployAuthTokenPersistence() {
		return weDeployAuthTokenPersistence;
	}

	/**
	 * Sets the we deploy auth token persistence.
	 *
	 * @param weDeployAuthTokenPersistence the we deploy auth token persistence
	 */
	public void setWeDeployAuthTokenPersistence(
		WeDeployAuthTokenPersistence weDeployAuthTokenPersistence) {

		this.weDeployAuthTokenPersistence = weDeployAuthTokenPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService
		getCounterLocalService() {

		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService
			counterLocalService) {

		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the class name local service.
	 *
	 * @return the class name local service
	 */
	public com.liferay.portal.kernel.service.ClassNameLocalService
		getClassNameLocalService() {

		return classNameLocalService;
	}

	/**
	 * Sets the class name local service.
	 *
	 * @param classNameLocalService the class name local service
	 */
	public void setClassNameLocalService(
		com.liferay.portal.kernel.service.ClassNameLocalService
			classNameLocalService) {

		this.classNameLocalService = classNameLocalService;
	}

	/**
	 * Returns the class name persistence.
	 *
	 * @return the class name persistence
	 */
	public ClassNamePersistence getClassNamePersistence() {
		return classNamePersistence;
	}

	/**
	 * Sets the class name persistence.
	 *
	 * @param classNamePersistence the class name persistence
	 */
	public void setClassNamePersistence(
		ClassNamePersistence classNamePersistence) {

		this.classNamePersistence = classNamePersistence;
	}

	/**
	 * Returns the resource local service.
	 *
	 * @return the resource local service
	 */
	public com.liferay.portal.kernel.service.ResourceLocalService
		getResourceLocalService() {

		return resourceLocalService;
	}

	/**
	 * Sets the resource local service.
	 *
	 * @param resourceLocalService the resource local service
	 */
	public void setResourceLocalService(
		com.liferay.portal.kernel.service.ResourceLocalService
			resourceLocalService) {

		this.resourceLocalService = resourceLocalService;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService
		getUserLocalService() {

		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.kernel.service.UserLocalService userLocalService) {

		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	public void afterPropertiesSet() {
		persistedModelLocalServiceRegistry.register(
			"com.liferay.portal.security.wedeploy.auth.model.WeDeployAuthApp",
			weDeployAuthAppLocalService);
	}

	public void destroy() {
		persistedModelLocalServiceRegistry.unregister(
			"com.liferay.portal.security.wedeploy.auth.model.WeDeployAuthApp");
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return WeDeployAuthAppLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return WeDeployAuthApp.class;
	}

	protected String getModelClassName() {
		return WeDeployAuthApp.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = weDeployAuthAppPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception e) {
			throw new SystemException(e);
		}
	}

	@BeanReference(type = WeDeployAuthAppLocalService.class)
	protected WeDeployAuthAppLocalService weDeployAuthAppLocalService;

	@BeanReference(type = WeDeployAuthAppPersistence.class)
	protected WeDeployAuthAppPersistence weDeployAuthAppPersistence;

	@BeanReference(
		type = com.liferay.portal.security.wedeploy.auth.service.WeDeployAuthTokenLocalService.class
	)
	protected com.liferay.portal.security.wedeploy.auth.service.
		WeDeployAuthTokenLocalService weDeployAuthTokenLocalService;

	@BeanReference(type = WeDeployAuthTokenPersistence.class)
	protected WeDeployAuthTokenPersistence weDeployAuthTokenPersistence;

	@ServiceReference(
		type = com.liferay.counter.kernel.service.CounterLocalService.class
	)
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.ClassNameLocalService.class
	)
	protected com.liferay.portal.kernel.service.ClassNameLocalService
		classNameLocalService;

	@ServiceReference(type = ClassNamePersistence.class)
	protected ClassNamePersistence classNamePersistence;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.ResourceLocalService.class
	)
	protected com.liferay.portal.kernel.service.ResourceLocalService
		resourceLocalService;

	@ServiceReference(
		type = com.liferay.portal.kernel.service.UserLocalService.class
	)
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

	@ServiceReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;

	@ServiceReference(type = PersistedModelLocalServiceRegistry.class)
	protected PersistedModelLocalServiceRegistry
		persistedModelLocalServiceRegistry;

}