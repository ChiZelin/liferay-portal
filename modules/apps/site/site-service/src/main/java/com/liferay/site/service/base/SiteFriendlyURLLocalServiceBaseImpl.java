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

package com.liferay.site.service.base;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DefaultActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.ExportActionableDynamicQuery;
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
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;
import com.liferay.site.model.SiteFriendlyURL;
import com.liferay.site.service.SiteFriendlyURLLocalService;
import com.liferay.site.service.persistence.SiteFriendlyURLPersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the site friendly url local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.site.service.impl.SiteFriendlyURLLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.site.service.impl.SiteFriendlyURLLocalServiceImpl
 * @generated
 */
@ProviderType
public abstract class SiteFriendlyURLLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements SiteFriendlyURLLocalService, IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>SiteFriendlyURLLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.site.service.SiteFriendlyURLLocalServiceUtil</code>.
	 */

	/**
	 * Adds the site friendly url to the database. Also notifies the appropriate model listeners.
	 *
	 * @param siteFriendlyURL the site friendly url
	 * @return the site friendly url that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SiteFriendlyURL addSiteFriendlyURL(SiteFriendlyURL siteFriendlyURL) {
		siteFriendlyURL.setNew(true);

		return siteFriendlyURLPersistence.update(siteFriendlyURL);
	}

	/**
	 * Creates a new site friendly url with the primary key. Does not add the site friendly url to the database.
	 *
	 * @param siteFriendlyURLId the primary key for the new site friendly url
	 * @return the new site friendly url
	 */
	@Override
	@Transactional(enabled = false)
	public SiteFriendlyURL createSiteFriendlyURL(long siteFriendlyURLId) {
		return siteFriendlyURLPersistence.create(siteFriendlyURLId);
	}

	/**
	 * Deletes the site friendly url with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param siteFriendlyURLId the primary key of the site friendly url
	 * @return the site friendly url that was removed
	 * @throws PortalException if a site friendly url with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SiteFriendlyURL deleteSiteFriendlyURL(long siteFriendlyURLId)
		throws PortalException {

		return siteFriendlyURLPersistence.remove(siteFriendlyURLId);
	}

	/**
	 * Deletes the site friendly url from the database. Also notifies the appropriate model listeners.
	 *
	 * @param siteFriendlyURL the site friendly url
	 * @return the site friendly url that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SiteFriendlyURL deleteSiteFriendlyURL(
		SiteFriendlyURL siteFriendlyURL) {

		return siteFriendlyURLPersistence.remove(siteFriendlyURL);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			SiteFriendlyURL.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return siteFriendlyURLPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.site.model.impl.SiteFriendlyURLModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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

		return siteFriendlyURLPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.site.model.impl.SiteFriendlyURLModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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

		return siteFriendlyURLPersistence.findWithDynamicQuery(
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
		return siteFriendlyURLPersistence.countWithDynamicQuery(dynamicQuery);
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

		return siteFriendlyURLPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public SiteFriendlyURL fetchSiteFriendlyURL(long siteFriendlyURLId) {
		return siteFriendlyURLPersistence.fetchByPrimaryKey(siteFriendlyURLId);
	}

	/**
	 * Returns the site friendly url matching the UUID and group.
	 *
	 * @param uuid the site friendly url's UUID
	 * @param groupId the primary key of the group
	 * @return the matching site friendly url, or <code>null</code> if a matching site friendly url could not be found
	 */
	@Override
	public SiteFriendlyURL fetchSiteFriendlyURLByUuidAndGroupId(
		String uuid, long groupId) {

		return siteFriendlyURLPersistence.fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the site friendly url with the primary key.
	 *
	 * @param siteFriendlyURLId the primary key of the site friendly url
	 * @return the site friendly url
	 * @throws PortalException if a site friendly url with the primary key could not be found
	 */
	@Override
	public SiteFriendlyURL getSiteFriendlyURL(long siteFriendlyURLId)
		throws PortalException {

		return siteFriendlyURLPersistence.findByPrimaryKey(siteFriendlyURLId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(siteFriendlyURLLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(SiteFriendlyURL.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("siteFriendlyURLId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			siteFriendlyURLLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(SiteFriendlyURL.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"siteFriendlyURLId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(siteFriendlyURLLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(SiteFriendlyURL.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("siteFriendlyURLId");
	}

	@Override
	public ExportActionableDynamicQuery getExportActionableDynamicQuery(
		final PortletDataContext portletDataContext) {

		final ExportActionableDynamicQuery exportActionableDynamicQuery =
			new ExportActionableDynamicQuery() {

				@Override
				public long performCount() throws PortalException {
					ManifestSummary manifestSummary =
						portletDataContext.getManifestSummary();

					StagedModelType stagedModelType = getStagedModelType();

					long modelAdditionCount = super.performCount();

					manifestSummary.addModelAdditionCount(
						stagedModelType, modelAdditionCount);

					long modelDeletionCount =
						ExportImportHelperUtil.getModelDeletionCount(
							portletDataContext, stagedModelType);

					manifestSummary.addModelDeletionCount(
						stagedModelType, modelDeletionCount);

					return modelAdditionCount;
				}

			};

		initActionableDynamicQuery(exportActionableDynamicQuery);

		exportActionableDynamicQuery.setAddCriteriaMethod(
			new ActionableDynamicQuery.AddCriteriaMethod() {

				@Override
				public void addCriteria(DynamicQuery dynamicQuery) {
					portletDataContext.addDateRangeCriteria(
						dynamicQuery, "modifiedDate");
				}

			});

		exportActionableDynamicQuery.setCompanyId(
			portletDataContext.getCompanyId());

		exportActionableDynamicQuery.setGroupId(
			portletDataContext.getScopeGroupId());

		exportActionableDynamicQuery.setPerformActionMethod(
			new ActionableDynamicQuery.PerformActionMethod<SiteFriendlyURL>() {

				@Override
				public void performAction(SiteFriendlyURL siteFriendlyURL)
					throws PortalException {

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, siteFriendlyURL);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				PortalUtil.getClassNameId(SiteFriendlyURL.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return siteFriendlyURLLocalService.deleteSiteFriendlyURL(
			(SiteFriendlyURL)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return siteFriendlyURLPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns all the site friendly urls matching the UUID and company.
	 *
	 * @param uuid the UUID of the site friendly urls
	 * @param companyId the primary key of the company
	 * @return the matching site friendly urls, or an empty list if no matches were found
	 */
	@Override
	public List<SiteFriendlyURL> getSiteFriendlyURLsByUuidAndCompanyId(
		String uuid, long companyId) {

		return siteFriendlyURLPersistence.findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of site friendly urls matching the UUID and company.
	 *
	 * @param uuid the UUID of the site friendly urls
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of site friendly urls
	 * @param end the upper bound of the range of site friendly urls (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching site friendly urls, or an empty list if no matches were found
	 */
	@Override
	public List<SiteFriendlyURL> getSiteFriendlyURLsByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<SiteFriendlyURL> orderByComparator) {

		return siteFriendlyURLPersistence.findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the site friendly url matching the UUID and group.
	 *
	 * @param uuid the site friendly url's UUID
	 * @param groupId the primary key of the group
	 * @return the matching site friendly url
	 * @throws PortalException if a matching site friendly url could not be found
	 */
	@Override
	public SiteFriendlyURL getSiteFriendlyURLByUuidAndGroupId(
			String uuid, long groupId)
		throws PortalException {

		return siteFriendlyURLPersistence.findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns a range of all the site friendly urls.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.site.model.impl.SiteFriendlyURLModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of site friendly urls
	 * @param end the upper bound of the range of site friendly urls (not inclusive)
	 * @return the range of site friendly urls
	 */
	@Override
	public List<SiteFriendlyURL> getSiteFriendlyURLs(int start, int end) {
		return siteFriendlyURLPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of site friendly urls.
	 *
	 * @return the number of site friendly urls
	 */
	@Override
	public int getSiteFriendlyURLsCount() {
		return siteFriendlyURLPersistence.countAll();
	}

	/**
	 * Updates the site friendly url in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param siteFriendlyURL the site friendly url
	 * @return the site friendly url that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SiteFriendlyURL updateSiteFriendlyURL(
		SiteFriendlyURL siteFriendlyURL) {

		return siteFriendlyURLPersistence.update(siteFriendlyURL);
	}

	/**
	 * Returns the site friendly url local service.
	 *
	 * @return the site friendly url local service
	 */
	public SiteFriendlyURLLocalService getSiteFriendlyURLLocalService() {
		return siteFriendlyURLLocalService;
	}

	/**
	 * Sets the site friendly url local service.
	 *
	 * @param siteFriendlyURLLocalService the site friendly url local service
	 */
	public void setSiteFriendlyURLLocalService(
		SiteFriendlyURLLocalService siteFriendlyURLLocalService) {

		this.siteFriendlyURLLocalService = siteFriendlyURLLocalService;
	}

	/**
	 * Returns the site friendly url persistence.
	 *
	 * @return the site friendly url persistence
	 */
	public SiteFriendlyURLPersistence getSiteFriendlyURLPersistence() {
		return siteFriendlyURLPersistence;
	}

	/**
	 * Sets the site friendly url persistence.
	 *
	 * @param siteFriendlyURLPersistence the site friendly url persistence
	 */
	public void setSiteFriendlyURLPersistence(
		SiteFriendlyURLPersistence siteFriendlyURLPersistence) {

		this.siteFriendlyURLPersistence = siteFriendlyURLPersistence;
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
			"com.liferay.site.model.SiteFriendlyURL",
			siteFriendlyURLLocalService);
	}

	public void destroy() {
		persistedModelLocalServiceRegistry.unregister(
			"com.liferay.site.model.SiteFriendlyURL");
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return SiteFriendlyURLLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return SiteFriendlyURL.class;
	}

	protected String getModelClassName() {
		return SiteFriendlyURL.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = siteFriendlyURLPersistence.getDataSource();

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

	@BeanReference(type = SiteFriendlyURLLocalService.class)
	protected SiteFriendlyURLLocalService siteFriendlyURLLocalService;

	@BeanReference(type = SiteFriendlyURLPersistence.class)
	protected SiteFriendlyURLPersistence siteFriendlyURLPersistence;

	@ServiceReference(
		type = com.liferay.counter.kernel.service.CounterLocalService.class
	)
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

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