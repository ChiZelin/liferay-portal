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

package com.liferay.segments.service.base;

import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
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
import com.liferay.segments.model.SegmentsEntry;
import com.liferay.segments.service.SegmentsEntryLocalService;
import com.liferay.segments.service.persistence.SegmentsEntryPersistence;
import com.liferay.segments.service.persistence.SegmentsEntryRelPersistence;
import com.liferay.segments.service.persistence.SegmentsExperiencePersistence;
import com.liferay.segments.service.persistence.SegmentsExperimentPersistence;
import com.liferay.segments.service.persistence.SegmentsExperimentRelPersistence;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

import org.osgi.annotation.versioning.ProviderType;
import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the segments entry local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.segments.service.impl.SegmentsEntryLocalServiceImpl}.
 * </p>
 *
 * @author Eduardo Garcia
 * @see com.liferay.segments.service.impl.SegmentsEntryLocalServiceImpl
 * @generated
 */
@ProviderType
public abstract class SegmentsEntryLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements SegmentsEntryLocalService, AopService, IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>SegmentsEntryLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.segments.service.SegmentsEntryLocalServiceUtil</code>.
	 */

	/**
	 * Adds the segments entry to the database. Also notifies the appropriate model listeners.
	 *
	 * @param segmentsEntry the segments entry
	 * @return the segments entry that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SegmentsEntry addSegmentsEntry(SegmentsEntry segmentsEntry) {
		segmentsEntry.setNew(true);

		return segmentsEntryPersistence.update(segmentsEntry);
	}

	/**
	 * Creates a new segments entry with the primary key. Does not add the segments entry to the database.
	 *
	 * @param segmentsEntryId the primary key for the new segments entry
	 * @return the new segments entry
	 */
	@Override
	@Transactional(enabled = false)
	public SegmentsEntry createSegmentsEntry(long segmentsEntryId) {
		return segmentsEntryPersistence.create(segmentsEntryId);
	}

	/**
	 * Deletes the segments entry with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param segmentsEntryId the primary key of the segments entry
	 * @return the segments entry that was removed
	 * @throws PortalException if a segments entry with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SegmentsEntry deleteSegmentsEntry(long segmentsEntryId)
		throws PortalException {

		return segmentsEntryPersistence.remove(segmentsEntryId);
	}

	/**
	 * Deletes the segments entry from the database. Also notifies the appropriate model listeners.
	 *
	 * @param segmentsEntry the segments entry
	 * @return the segments entry that was removed
	 * @throws PortalException
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public SegmentsEntry deleteSegmentsEntry(SegmentsEntry segmentsEntry)
		throws PortalException {

		return segmentsEntryPersistence.remove(segmentsEntry);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			SegmentsEntry.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return segmentsEntryPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.segments.model.impl.SegmentsEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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

		return segmentsEntryPersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.segments.model.impl.SegmentsEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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

		return segmentsEntryPersistence.findWithDynamicQuery(
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
		return segmentsEntryPersistence.countWithDynamicQuery(dynamicQuery);
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

		return segmentsEntryPersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public SegmentsEntry fetchSegmentsEntry(long segmentsEntryId) {
		return segmentsEntryPersistence.fetchByPrimaryKey(segmentsEntryId);
	}

	/**
	 * Returns the segments entry matching the UUID and group.
	 *
	 * @param uuid the segments entry's UUID
	 * @param groupId the primary key of the group
	 * @return the matching segments entry, or <code>null</code> if a matching segments entry could not be found
	 */
	@Override
	public SegmentsEntry fetchSegmentsEntryByUuidAndGroupId(
		String uuid, long groupId) {

		return segmentsEntryPersistence.fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the segments entry with the primary key.
	 *
	 * @param segmentsEntryId the primary key of the segments entry
	 * @return the segments entry
	 * @throws PortalException if a segments entry with the primary key could not be found
	 */
	@Override
	public SegmentsEntry getSegmentsEntry(long segmentsEntryId)
		throws PortalException {

		return segmentsEntryPersistence.findByPrimaryKey(segmentsEntryId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(segmentsEntryLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(SegmentsEntry.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("segmentsEntryId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			segmentsEntryLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(SegmentsEntry.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"segmentsEntryId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(segmentsEntryLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(SegmentsEntry.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("segmentsEntryId");
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
			new ActionableDynamicQuery.PerformActionMethod<SegmentsEntry>() {

				@Override
				public void performAction(SegmentsEntry segmentsEntry)
					throws PortalException {

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, segmentsEntry);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				PortalUtil.getClassNameId(SegmentsEntry.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return segmentsEntryLocalService.deleteSegmentsEntry(
			(SegmentsEntry)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return segmentsEntryPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns all the segments entries matching the UUID and company.
	 *
	 * @param uuid the UUID of the segments entries
	 * @param companyId the primary key of the company
	 * @return the matching segments entries, or an empty list if no matches were found
	 */
	@Override
	public List<SegmentsEntry> getSegmentsEntriesByUuidAndCompanyId(
		String uuid, long companyId) {

		return segmentsEntryPersistence.findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of segments entries matching the UUID and company.
	 *
	 * @param uuid the UUID of the segments entries
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of segments entries
	 * @param end the upper bound of the range of segments entries (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching segments entries, or an empty list if no matches were found
	 */
	@Override
	public List<SegmentsEntry> getSegmentsEntriesByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<SegmentsEntry> orderByComparator) {

		return segmentsEntryPersistence.findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the segments entry matching the UUID and group.
	 *
	 * @param uuid the segments entry's UUID
	 * @param groupId the primary key of the group
	 * @return the matching segments entry
	 * @throws PortalException if a matching segments entry could not be found
	 */
	@Override
	public SegmentsEntry getSegmentsEntryByUuidAndGroupId(
			String uuid, long groupId)
		throws PortalException {

		return segmentsEntryPersistence.findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns a range of all the segments entries.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.segments.model.impl.SegmentsEntryModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of segments entries
	 * @param end the upper bound of the range of segments entries (not inclusive)
	 * @return the range of segments entries
	 */
	@Override
	public List<SegmentsEntry> getSegmentsEntries(int start, int end) {
		return segmentsEntryPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of segments entries.
	 *
	 * @return the number of segments entries
	 */
	@Override
	public int getSegmentsEntriesCount() {
		return segmentsEntryPersistence.countAll();
	}

	/**
	 * Updates the segments entry in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param segmentsEntry the segments entry
	 * @return the segments entry that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public SegmentsEntry updateSegmentsEntry(SegmentsEntry segmentsEntry) {
		return segmentsEntryPersistence.update(segmentsEntry);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			SegmentsEntryLocalService.class, IdentifiableOSGiService.class,
			PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		segmentsEntryLocalService = (SegmentsEntryLocalService)aopProxy;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return SegmentsEntryLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return SegmentsEntry.class;
	}

	protected String getModelClassName() {
		return SegmentsEntry.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = segmentsEntryPersistence.getDataSource();

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

	protected SegmentsEntryLocalService segmentsEntryLocalService;

	@Reference
	protected SegmentsEntryPersistence segmentsEntryPersistence;

	@Reference
	protected SegmentsEntryRelPersistence segmentsEntryRelPersistence;

	@Reference
	protected SegmentsExperiencePersistence segmentsExperiencePersistence;

	@Reference
	protected SegmentsExperimentPersistence segmentsExperimentPersistence;

	@Reference
	protected SegmentsExperimentRelPersistence segmentsExperimentRelPersistence;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.ClassNameLocalService
		classNameLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.ResourceLocalService
		resourceLocalService;

	@Reference
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

}