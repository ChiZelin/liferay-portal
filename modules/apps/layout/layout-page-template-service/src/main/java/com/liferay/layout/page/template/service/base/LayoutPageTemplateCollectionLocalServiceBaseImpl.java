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

package com.liferay.layout.page.template.service.base;

import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.layout.page.template.model.LayoutPageTemplateCollection;
import com.liferay.layout.page.template.service.LayoutPageTemplateCollectionLocalService;
import com.liferay.layout.page.template.service.persistence.LayoutPageTemplateCollectionPersistence;
import com.liferay.portal.aop.AopService;
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
import com.liferay.portal.kernel.service.PersistedModelLocalService;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.annotation.versioning.ProviderType;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the layout page template collection local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.layout.page.template.service.impl.LayoutPageTemplateCollectionLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.layout.page.template.service.impl.LayoutPageTemplateCollectionLocalServiceImpl
 * @generated
 */
@ProviderType
public abstract class LayoutPageTemplateCollectionLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements LayoutPageTemplateCollectionLocalService, AopService,
			   IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>LayoutPageTemplateCollectionLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.layout.page.template.service.LayoutPageTemplateCollectionLocalServiceUtil</code>.
	 */

	/**
	 * Adds the layout page template collection to the database. Also notifies the appropriate model listeners.
	 *
	 * @param layoutPageTemplateCollection the layout page template collection
	 * @return the layout page template collection that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public LayoutPageTemplateCollection addLayoutPageTemplateCollection(
		LayoutPageTemplateCollection layoutPageTemplateCollection) {

		layoutPageTemplateCollection.setNew(true);

		return layoutPageTemplateCollectionPersistence.update(
			layoutPageTemplateCollection);
	}

	/**
	 * Creates a new layout page template collection with the primary key. Does not add the layout page template collection to the database.
	 *
	 * @param layoutPageTemplateCollectionId the primary key for the new layout page template collection
	 * @return the new layout page template collection
	 */
	@Override
	@Transactional(enabled = false)
	public LayoutPageTemplateCollection createLayoutPageTemplateCollection(
		long layoutPageTemplateCollectionId) {

		return layoutPageTemplateCollectionPersistence.create(
			layoutPageTemplateCollectionId);
	}

	/**
	 * Deletes the layout page template collection with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param layoutPageTemplateCollectionId the primary key of the layout page template collection
	 * @return the layout page template collection that was removed
	 * @throws PortalException if a layout page template collection with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public LayoutPageTemplateCollection deleteLayoutPageTemplateCollection(
			long layoutPageTemplateCollectionId)
		throws PortalException {

		return layoutPageTemplateCollectionPersistence.remove(
			layoutPageTemplateCollectionId);
	}

	/**
	 * Deletes the layout page template collection from the database. Also notifies the appropriate model listeners.
	 *
	 * @param layoutPageTemplateCollection the layout page template collection
	 * @return the layout page template collection that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public LayoutPageTemplateCollection deleteLayoutPageTemplateCollection(
			LayoutPageTemplateCollection layoutPageTemplateCollection)
		throws PortalException {

		return layoutPageTemplateCollectionPersistence.remove(
			layoutPageTemplateCollection);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			LayoutPageTemplateCollection.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return layoutPageTemplateCollectionPersistence.findWithDynamicQuery(
			dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.layout.page.template.model.impl.LayoutPageTemplateCollectionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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

		return layoutPageTemplateCollectionPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.layout.page.template.model.impl.LayoutPageTemplateCollectionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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

		return layoutPageTemplateCollectionPersistence.findWithDynamicQuery(
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
		return layoutPageTemplateCollectionPersistence.countWithDynamicQuery(
			dynamicQuery);
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

		return layoutPageTemplateCollectionPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public LayoutPageTemplateCollection fetchLayoutPageTemplateCollection(
		long layoutPageTemplateCollectionId) {

		return layoutPageTemplateCollectionPersistence.fetchByPrimaryKey(
			layoutPageTemplateCollectionId);
	}

	/**
	 * Returns the layout page template collection matching the UUID and group.
	 *
	 * @param uuid the layout page template collection's UUID
	 * @param groupId the primary key of the group
	 * @return the matching layout page template collection, or <code>null</code> if a matching layout page template collection could not be found
	 */
	@Override
	public LayoutPageTemplateCollection
		fetchLayoutPageTemplateCollectionByUuidAndGroupId(
			String uuid, long groupId) {

		return layoutPageTemplateCollectionPersistence.fetchByUUID_G(
			uuid, groupId);
	}

	/**
	 * Returns the layout page template collection with the primary key.
	 *
	 * @param layoutPageTemplateCollectionId the primary key of the layout page template collection
	 * @return the layout page template collection
	 * @throws PortalException if a layout page template collection with the primary key could not be found
	 */
	@Override
	public LayoutPageTemplateCollection getLayoutPageTemplateCollection(
			long layoutPageTemplateCollectionId)
		throws PortalException {

		return layoutPageTemplateCollectionPersistence.findByPrimaryKey(
			layoutPageTemplateCollectionId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(
			layoutPageTemplateCollectionLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(
			LayoutPageTemplateCollection.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"layoutPageTemplateCollectionId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			layoutPageTemplateCollectionLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(
			LayoutPageTemplateCollection.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"layoutPageTemplateCollectionId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(
			layoutPageTemplateCollectionLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(
			LayoutPageTemplateCollection.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName(
			"layoutPageTemplateCollectionId");
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
			new ActionableDynamicQuery.PerformActionMethod
				<LayoutPageTemplateCollection>() {

				@Override
				public void performAction(
						LayoutPageTemplateCollection
							layoutPageTemplateCollection)
					throws PortalException {

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, layoutPageTemplateCollection);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				PortalUtil.getClassNameId(
					LayoutPageTemplateCollection.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return layoutPageTemplateCollectionLocalService.
			deleteLayoutPageTemplateCollection(
				(LayoutPageTemplateCollection)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return layoutPageTemplateCollectionPersistence.findByPrimaryKey(
			primaryKeyObj);
	}

	/**
	 * Returns all the layout page template collections matching the UUID and company.
	 *
	 * @param uuid the UUID of the layout page template collections
	 * @param companyId the primary key of the company
	 * @return the matching layout page template collections, or an empty list if no matches were found
	 */
	@Override
	public List<LayoutPageTemplateCollection>
		getLayoutPageTemplateCollectionsByUuidAndCompanyId(
			String uuid, long companyId) {

		return layoutPageTemplateCollectionPersistence.findByUuid_C(
			uuid, companyId);
	}

	/**
	 * Returns a range of layout page template collections matching the UUID and company.
	 *
	 * @param uuid the UUID of the layout page template collections
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of layout page template collections
	 * @param end the upper bound of the range of layout page template collections (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching layout page template collections, or an empty list if no matches were found
	 */
	@Override
	public List<LayoutPageTemplateCollection>
		getLayoutPageTemplateCollectionsByUuidAndCompanyId(
			String uuid, long companyId, int start, int end,
			OrderByComparator<LayoutPageTemplateCollection> orderByComparator) {

		return layoutPageTemplateCollectionPersistence.findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the layout page template collection matching the UUID and group.
	 *
	 * @param uuid the layout page template collection's UUID
	 * @param groupId the primary key of the group
	 * @return the matching layout page template collection
	 * @throws PortalException if a matching layout page template collection could not be found
	 */
	@Override
	public LayoutPageTemplateCollection
			getLayoutPageTemplateCollectionByUuidAndGroupId(
				String uuid, long groupId)
		throws PortalException {

		return layoutPageTemplateCollectionPersistence.findByUUID_G(
			uuid, groupId);
	}

	/**
	 * Returns a range of all the layout page template collections.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.layout.page.template.model.impl.LayoutPageTemplateCollectionModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of layout page template collections
	 * @param end the upper bound of the range of layout page template collections (not inclusive)
	 * @return the range of layout page template collections
	 */
	@Override
	public List<LayoutPageTemplateCollection> getLayoutPageTemplateCollections(
		int start, int end) {

		return layoutPageTemplateCollectionPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of layout page template collections.
	 *
	 * @return the number of layout page template collections
	 */
	@Override
	public int getLayoutPageTemplateCollectionsCount() {
		return layoutPageTemplateCollectionPersistence.countAll();
	}

	/**
	 * Updates the layout page template collection in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param layoutPageTemplateCollection the layout page template collection
	 * @return the layout page template collection that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public LayoutPageTemplateCollection updateLayoutPageTemplateCollection(
		LayoutPageTemplateCollection layoutPageTemplateCollection) {

		return layoutPageTemplateCollectionPersistence.update(
			layoutPageTemplateCollection);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			LayoutPageTemplateCollectionLocalService.class,
			IdentifiableOSGiService.class, PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		layoutPageTemplateCollectionLocalService =
			(LayoutPageTemplateCollectionLocalService)aopProxy;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return LayoutPageTemplateCollectionLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return LayoutPageTemplateCollection.class;
	}

	protected String getModelClassName() {
		return LayoutPageTemplateCollection.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource =
				layoutPageTemplateCollectionPersistence.getDataSource();

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

	protected LayoutPageTemplateCollectionLocalService
		layoutPageTemplateCollectionLocalService;

	@Reference
	protected LayoutPageTemplateCollectionPersistence
		layoutPageTemplateCollectionPersistence;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.ResourceLocalService
		resourceLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

}