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

package com.liferay.dynamic.data.mapping.service.base;

import org.osgi.annotation.versioning.ProviderType;

import com.liferay.dynamic.data.mapping.model.DDMFormInstance;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalService;
import com.liferay.dynamic.data.mapping.service.persistence.DDMFormInstanceFinder;
import com.liferay.dynamic.data.mapping.service.persistence.DDMFormInstancePersistence;
import com.liferay.dynamic.data.mapping.service.persistence.DDMFormInstanceRecordFinder;
import com.liferay.dynamic.data.mapping.service.persistence.DDMFormInstanceRecordPersistence;
import com.liferay.dynamic.data.mapping.service.persistence.DDMFormInstanceVersionPersistence;
import com.liferay.dynamic.data.mapping.service.persistence.DDMStructureFinder;
import com.liferay.dynamic.data.mapping.service.persistence.DDMStructurePersistence;
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
import com.liferay.portal.kernel.service.persistence.ClassNamePersistence;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.service.persistence.WorkflowDefinitionLinkPersistence;
import com.liferay.portal.kernel.transaction.Transactional;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.spring.extender.service.ServiceReference;

import java.io.Serializable;

import java.util.List;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the ddm form instance local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.dynamic.data.mapping.service.impl.DDMFormInstanceLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.dynamic.data.mapping.service.impl.DDMFormInstanceLocalServiceImpl
 * @generated
 */
@ProviderType
public abstract class DDMFormInstanceLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements DDMFormInstanceLocalService, IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>DDMFormInstanceLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.dynamic.data.mapping.service.DDMFormInstanceLocalServiceUtil</code>.
	 */

	/**
	 * Adds the ddm form instance to the database. Also notifies the appropriate model listeners.
	 *
	 * @param ddmFormInstance the ddm form instance
	 * @return the ddm form instance that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public DDMFormInstance addDDMFormInstance(DDMFormInstance ddmFormInstance) {
		ddmFormInstance.setNew(true);

		return ddmFormInstancePersistence.update(ddmFormInstance);
	}

	/**
	 * Creates a new ddm form instance with the primary key. Does not add the ddm form instance to the database.
	 *
	 * @param formInstanceId the primary key for the new ddm form instance
	 * @return the new ddm form instance
	 */
	@Override
	@Transactional(enabled = false)
	public DDMFormInstance createDDMFormInstance(long formInstanceId) {
		return ddmFormInstancePersistence.create(formInstanceId);
	}

	/**
	 * Deletes the ddm form instance with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param formInstanceId the primary key of the ddm form instance
	 * @return the ddm form instance that was removed
	 * @throws PortalException if a ddm form instance with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public DDMFormInstance deleteDDMFormInstance(long formInstanceId)
		throws PortalException {

		return ddmFormInstancePersistence.remove(formInstanceId);
	}

	/**
	 * Deletes the ddm form instance from the database. Also notifies the appropriate model listeners.
	 *
	 * @param ddmFormInstance the ddm form instance
	 * @return the ddm form instance that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public DDMFormInstance deleteDDMFormInstance(
		DDMFormInstance ddmFormInstance) {

		return ddmFormInstancePersistence.remove(ddmFormInstance);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			DDMFormInstance.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return ddmFormInstancePersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.dynamic.data.mapping.model.impl.DDMFormInstanceModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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

		return ddmFormInstancePersistence.findWithDynamicQuery(
			dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.dynamic.data.mapping.model.impl.DDMFormInstanceModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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

		return ddmFormInstancePersistence.findWithDynamicQuery(
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
		return ddmFormInstancePersistence.countWithDynamicQuery(dynamicQuery);
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

		return ddmFormInstancePersistence.countWithDynamicQuery(
			dynamicQuery, projection);
	}

	@Override
	public DDMFormInstance fetchDDMFormInstance(long formInstanceId) {
		return ddmFormInstancePersistence.fetchByPrimaryKey(formInstanceId);
	}

	/**
	 * Returns the ddm form instance matching the UUID and group.
	 *
	 * @param uuid the ddm form instance's UUID
	 * @param groupId the primary key of the group
	 * @return the matching ddm form instance, or <code>null</code> if a matching ddm form instance could not be found
	 */
	@Override
	public DDMFormInstance fetchDDMFormInstanceByUuidAndGroupId(
		String uuid, long groupId) {

		return ddmFormInstancePersistence.fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the ddm form instance with the primary key.
	 *
	 * @param formInstanceId the primary key of the ddm form instance
	 * @return the ddm form instance
	 * @throws PortalException if a ddm form instance with the primary key could not be found
	 */
	@Override
	public DDMFormInstance getDDMFormInstance(long formInstanceId)
		throws PortalException {

		return ddmFormInstancePersistence.findByPrimaryKey(formInstanceId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(ddmFormInstanceLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(DDMFormInstance.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("formInstanceId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(
			ddmFormInstanceLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(DDMFormInstance.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName(
			"formInstanceId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(ddmFormInstanceLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(DDMFormInstance.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("formInstanceId");
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
			new ActionableDynamicQuery.PerformActionMethod<DDMFormInstance>() {

				@Override
				public void performAction(DDMFormInstance ddmFormInstance)
					throws PortalException {

					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, ddmFormInstance);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				PortalUtil.getClassNameId(DDMFormInstance.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return ddmFormInstanceLocalService.deleteDDMFormInstance(
			(DDMFormInstance)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return ddmFormInstancePersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns all the ddm form instances matching the UUID and company.
	 *
	 * @param uuid the UUID of the ddm form instances
	 * @param companyId the primary key of the company
	 * @return the matching ddm form instances, or an empty list if no matches were found
	 */
	@Override
	public List<DDMFormInstance> getDDMFormInstancesByUuidAndCompanyId(
		String uuid, long companyId) {

		return ddmFormInstancePersistence.findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of ddm form instances matching the UUID and company.
	 *
	 * @param uuid the UUID of the ddm form instances
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of ddm form instances
	 * @param end the upper bound of the range of ddm form instances (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching ddm form instances, or an empty list if no matches were found
	 */
	@Override
	public List<DDMFormInstance> getDDMFormInstancesByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<DDMFormInstance> orderByComparator) {

		return ddmFormInstancePersistence.findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the ddm form instance matching the UUID and group.
	 *
	 * @param uuid the ddm form instance's UUID
	 * @param groupId the primary key of the group
	 * @return the matching ddm form instance
	 * @throws PortalException if a matching ddm form instance could not be found
	 */
	@Override
	public DDMFormInstance getDDMFormInstanceByUuidAndGroupId(
			String uuid, long groupId)
		throws PortalException {

		return ddmFormInstancePersistence.findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns a range of all the ddm form instances.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.dynamic.data.mapping.model.impl.DDMFormInstanceModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of ddm form instances
	 * @param end the upper bound of the range of ddm form instances (not inclusive)
	 * @return the range of ddm form instances
	 */
	@Override
	public List<DDMFormInstance> getDDMFormInstances(int start, int end) {
		return ddmFormInstancePersistence.findAll(start, end);
	}

	/**
	 * Returns the number of ddm form instances.
	 *
	 * @return the number of ddm form instances
	 */
	@Override
	public int getDDMFormInstancesCount() {
		return ddmFormInstancePersistence.countAll();
	}

	/**
	 * Updates the ddm form instance in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param ddmFormInstance the ddm form instance
	 * @return the ddm form instance that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public DDMFormInstance updateDDMFormInstance(
		DDMFormInstance ddmFormInstance) {

		return ddmFormInstancePersistence.update(ddmFormInstance);
	}

	/**
	 * Returns the ddm form instance local service.
	 *
	 * @return the ddm form instance local service
	 */
	public DDMFormInstanceLocalService getDDMFormInstanceLocalService() {
		return ddmFormInstanceLocalService;
	}

	/**
	 * Sets the ddm form instance local service.
	 *
	 * @param ddmFormInstanceLocalService the ddm form instance local service
	 */
	public void setDDMFormInstanceLocalService(
		DDMFormInstanceLocalService ddmFormInstanceLocalService) {

		this.ddmFormInstanceLocalService = ddmFormInstanceLocalService;
	}

	/**
	 * Returns the ddm form instance persistence.
	 *
	 * @return the ddm form instance persistence
	 */
	public DDMFormInstancePersistence getDDMFormInstancePersistence() {
		return ddmFormInstancePersistence;
	}

	/**
	 * Sets the ddm form instance persistence.
	 *
	 * @param ddmFormInstancePersistence the ddm form instance persistence
	 */
	public void setDDMFormInstancePersistence(
		DDMFormInstancePersistence ddmFormInstancePersistence) {

		this.ddmFormInstancePersistence = ddmFormInstancePersistence;
	}

	/**
	 * Returns the ddm form instance finder.
	 *
	 * @return the ddm form instance finder
	 */
	public DDMFormInstanceFinder getDDMFormInstanceFinder() {
		return ddmFormInstanceFinder;
	}

	/**
	 * Sets the ddm form instance finder.
	 *
	 * @param ddmFormInstanceFinder the ddm form instance finder
	 */
	public void setDDMFormInstanceFinder(
		DDMFormInstanceFinder ddmFormInstanceFinder) {

		this.ddmFormInstanceFinder = ddmFormInstanceFinder;
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

	/**
	 * Returns the workflow definition link local service.
	 *
	 * @return the workflow definition link local service
	 */
	public com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalService
		getWorkflowDefinitionLinkLocalService() {

		return workflowDefinitionLinkLocalService;
	}

	/**
	 * Sets the workflow definition link local service.
	 *
	 * @param workflowDefinitionLinkLocalService the workflow definition link local service
	 */
	public void setWorkflowDefinitionLinkLocalService(
		com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalService
			workflowDefinitionLinkLocalService) {

		this.workflowDefinitionLinkLocalService =
			workflowDefinitionLinkLocalService;
	}

	/**
	 * Returns the workflow definition link persistence.
	 *
	 * @return the workflow definition link persistence
	 */
	public WorkflowDefinitionLinkPersistence
		getWorkflowDefinitionLinkPersistence() {

		return workflowDefinitionLinkPersistence;
	}

	/**
	 * Sets the workflow definition link persistence.
	 *
	 * @param workflowDefinitionLinkPersistence the workflow definition link persistence
	 */
	public void setWorkflowDefinitionLinkPersistence(
		WorkflowDefinitionLinkPersistence workflowDefinitionLinkPersistence) {

		this.workflowDefinitionLinkPersistence =
			workflowDefinitionLinkPersistence;
	}

	/**
	 * Returns the ddm form instance record local service.
	 *
	 * @return the ddm form instance record local service
	 */
	public
		com.liferay.dynamic.data.mapping.service.
			DDMFormInstanceRecordLocalService
				getDDMFormInstanceRecordLocalService() {

		return ddmFormInstanceRecordLocalService;
	}

	/**
	 * Sets the ddm form instance record local service.
	 *
	 * @param ddmFormInstanceRecordLocalService the ddm form instance record local service
	 */
	public void setDDMFormInstanceRecordLocalService(
		com.liferay.dynamic.data.mapping.service.
			DDMFormInstanceRecordLocalService
				ddmFormInstanceRecordLocalService) {

		this.ddmFormInstanceRecordLocalService =
			ddmFormInstanceRecordLocalService;
	}

	/**
	 * Returns the ddm form instance record persistence.
	 *
	 * @return the ddm form instance record persistence
	 */
	public DDMFormInstanceRecordPersistence
		getDDMFormInstanceRecordPersistence() {

		return ddmFormInstanceRecordPersistence;
	}

	/**
	 * Sets the ddm form instance record persistence.
	 *
	 * @param ddmFormInstanceRecordPersistence the ddm form instance record persistence
	 */
	public void setDDMFormInstanceRecordPersistence(
		DDMFormInstanceRecordPersistence ddmFormInstanceRecordPersistence) {

		this.ddmFormInstanceRecordPersistence =
			ddmFormInstanceRecordPersistence;
	}

	/**
	 * Returns the ddm form instance record finder.
	 *
	 * @return the ddm form instance record finder
	 */
	public DDMFormInstanceRecordFinder getDDMFormInstanceRecordFinder() {
		return ddmFormInstanceRecordFinder;
	}

	/**
	 * Sets the ddm form instance record finder.
	 *
	 * @param ddmFormInstanceRecordFinder the ddm form instance record finder
	 */
	public void setDDMFormInstanceRecordFinder(
		DDMFormInstanceRecordFinder ddmFormInstanceRecordFinder) {

		this.ddmFormInstanceRecordFinder = ddmFormInstanceRecordFinder;
	}

	/**
	 * Returns the ddm form instance version local service.
	 *
	 * @return the ddm form instance version local service
	 */
	public
		com.liferay.dynamic.data.mapping.service.
			DDMFormInstanceVersionLocalService
				getDDMFormInstanceVersionLocalService() {

		return ddmFormInstanceVersionLocalService;
	}

	/**
	 * Sets the ddm form instance version local service.
	 *
	 * @param ddmFormInstanceVersionLocalService the ddm form instance version local service
	 */
	public void setDDMFormInstanceVersionLocalService(
		com.liferay.dynamic.data.mapping.service.
			DDMFormInstanceVersionLocalService
				ddmFormInstanceVersionLocalService) {

		this.ddmFormInstanceVersionLocalService =
			ddmFormInstanceVersionLocalService;
	}

	/**
	 * Returns the ddm form instance version persistence.
	 *
	 * @return the ddm form instance version persistence
	 */
	public DDMFormInstanceVersionPersistence
		getDDMFormInstanceVersionPersistence() {

		return ddmFormInstanceVersionPersistence;
	}

	/**
	 * Sets the ddm form instance version persistence.
	 *
	 * @param ddmFormInstanceVersionPersistence the ddm form instance version persistence
	 */
	public void setDDMFormInstanceVersionPersistence(
		DDMFormInstanceVersionPersistence ddmFormInstanceVersionPersistence) {

		this.ddmFormInstanceVersionPersistence =
			ddmFormInstanceVersionPersistence;
	}

	/**
	 * Returns the ddm structure local service.
	 *
	 * @return the ddm structure local service
	 */
	public com.liferay.dynamic.data.mapping.service.DDMStructureLocalService
		getDDMStructureLocalService() {

		return ddmStructureLocalService;
	}

	/**
	 * Sets the ddm structure local service.
	 *
	 * @param ddmStructureLocalService the ddm structure local service
	 */
	public void setDDMStructureLocalService(
		com.liferay.dynamic.data.mapping.service.DDMStructureLocalService
			ddmStructureLocalService) {

		this.ddmStructureLocalService = ddmStructureLocalService;
	}

	/**
	 * Returns the ddm structure persistence.
	 *
	 * @return the ddm structure persistence
	 */
	public DDMStructurePersistence getDDMStructurePersistence() {
		return ddmStructurePersistence;
	}

	/**
	 * Sets the ddm structure persistence.
	 *
	 * @param ddmStructurePersistence the ddm structure persistence
	 */
	public void setDDMStructurePersistence(
		DDMStructurePersistence ddmStructurePersistence) {

		this.ddmStructurePersistence = ddmStructurePersistence;
	}

	/**
	 * Returns the ddm structure finder.
	 *
	 * @return the ddm structure finder
	 */
	public DDMStructureFinder getDDMStructureFinder() {
		return ddmStructureFinder;
	}

	/**
	 * Sets the ddm structure finder.
	 *
	 * @param ddmStructureFinder the ddm structure finder
	 */
	public void setDDMStructureFinder(DDMStructureFinder ddmStructureFinder) {
		this.ddmStructureFinder = ddmStructureFinder;
	}

	public void afterPropertiesSet() {
		persistedModelLocalServiceRegistry.register(
			"com.liferay.dynamic.data.mapping.model.DDMFormInstance",
			ddmFormInstanceLocalService);
	}

	public void destroy() {
		persistedModelLocalServiceRegistry.unregister(
			"com.liferay.dynamic.data.mapping.model.DDMFormInstance");
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return DDMFormInstanceLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return DDMFormInstance.class;
	}

	protected String getModelClassName() {
		return DDMFormInstance.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = ddmFormInstancePersistence.getDataSource();

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

	@BeanReference(type = DDMFormInstanceLocalService.class)
	protected DDMFormInstanceLocalService ddmFormInstanceLocalService;

	@BeanReference(type = DDMFormInstancePersistence.class)
	protected DDMFormInstancePersistence ddmFormInstancePersistence;

	@BeanReference(type = DDMFormInstanceFinder.class)
	protected DDMFormInstanceFinder ddmFormInstanceFinder;

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

	@ServiceReference(
		type = com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalService.class
	)
	protected
		com.liferay.portal.kernel.service.WorkflowDefinitionLinkLocalService
			workflowDefinitionLinkLocalService;

	@ServiceReference(type = WorkflowDefinitionLinkPersistence.class)
	protected WorkflowDefinitionLinkPersistence
		workflowDefinitionLinkPersistence;

	@BeanReference(
		type = com.liferay.dynamic.data.mapping.service.DDMFormInstanceRecordLocalService.class
	)
	protected
		com.liferay.dynamic.data.mapping.service.
			DDMFormInstanceRecordLocalService ddmFormInstanceRecordLocalService;

	@BeanReference(type = DDMFormInstanceRecordPersistence.class)
	protected DDMFormInstanceRecordPersistence ddmFormInstanceRecordPersistence;

	@BeanReference(type = DDMFormInstanceRecordFinder.class)
	protected DDMFormInstanceRecordFinder ddmFormInstanceRecordFinder;

	@BeanReference(
		type = com.liferay.dynamic.data.mapping.service.DDMFormInstanceVersionLocalService.class
	)
	protected
		com.liferay.dynamic.data.mapping.service.
			DDMFormInstanceVersionLocalService
				ddmFormInstanceVersionLocalService;

	@BeanReference(type = DDMFormInstanceVersionPersistence.class)
	protected DDMFormInstanceVersionPersistence
		ddmFormInstanceVersionPersistence;

	@BeanReference(
		type = com.liferay.dynamic.data.mapping.service.DDMStructureLocalService.class
	)
	protected com.liferay.dynamic.data.mapping.service.DDMStructureLocalService
		ddmStructureLocalService;

	@BeanReference(type = DDMStructurePersistence.class)
	protected DDMStructurePersistence ddmStructurePersistence;

	@BeanReference(type = DDMStructureFinder.class)
	protected DDMStructureFinder ddmStructureFinder;

	@ServiceReference(type = PersistedModelLocalServiceRegistry.class)
	protected PersistedModelLocalServiceRegistry
		persistedModelLocalServiceRegistry;

}