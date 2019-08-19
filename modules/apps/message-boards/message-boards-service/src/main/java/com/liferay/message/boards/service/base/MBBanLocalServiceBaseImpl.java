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

package com.liferay.message.boards.service.base;

import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.ManifestSummary;
import com.liferay.exportimport.kernel.lar.PortletDataContext;
import com.liferay.exportimport.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.exportimport.kernel.lar.StagedModelType;
import com.liferay.message.boards.model.MBBan;
import com.liferay.message.boards.service.MBBanLocalService;
import com.liferay.message.boards.service.persistence.MBBanPersistence;
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
 * Provides the base implementation for the message boards ban local service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.message.boards.service.impl.MBBanLocalServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.message.boards.service.impl.MBBanLocalServiceImpl
 * @generated
 */
@ProviderType
public abstract class MBBanLocalServiceBaseImpl
	extends BaseLocalServiceImpl
	implements MBBanLocalService, AopService, IdentifiableOSGiService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>MBBanLocalService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.message.boards.service.MBBanLocalServiceUtil</code>.
	 */

	/**
	 * Adds the message boards ban to the database. Also notifies the appropriate model listeners.
	 *
	 * @param mbBan the message boards ban
	 * @return the message boards ban that was added
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public MBBan addMBBan(MBBan mbBan) {
		mbBan.setNew(true);

		return mbBanPersistence.update(mbBan);
	}

	/**
	 * Creates a new message boards ban with the primary key. Does not add the message boards ban to the database.
	 *
	 * @param banId the primary key for the new message boards ban
	 * @return the new message boards ban
	 */
	@Override
	@Transactional(enabled = false)
	public MBBan createMBBan(long banId) {
		return mbBanPersistence.create(banId);
	}

	/**
	 * Deletes the message boards ban with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param banId the primary key of the message boards ban
	 * @return the message boards ban that was removed
	 * @throws PortalException if a message boards ban with the primary key could not be found
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public MBBan deleteMBBan(long banId) throws PortalException {
		return mbBanPersistence.remove(banId);
	}

	/**
	 * Deletes the message boards ban from the database. Also notifies the appropriate model listeners.
	 *
	 * @param mbBan the message boards ban
	 * @return the message boards ban that was removed
	 */
	@Indexable(type = IndexableType.DELETE)
	@Override
	public MBBan deleteMBBan(MBBan mbBan) {
		return mbBanPersistence.remove(mbBan);
	}

	@Override
	public DynamicQuery dynamicQuery() {
		Class<?> clazz = getClass();

		return DynamicQueryFactoryUtil.forClass(
			MBBan.class, clazz.getClassLoader());
	}

	/**
	 * Performs a dynamic query on the database and returns the matching rows.
	 *
	 * @param dynamicQuery the dynamic query
	 * @return the matching rows
	 */
	@Override
	public <T> List<T> dynamicQuery(DynamicQuery dynamicQuery) {
		return mbBanPersistence.findWithDynamicQuery(dynamicQuery);
	}

	/**
	 * Performs a dynamic query on the database and returns a range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.message.boards.model.impl.MBBanModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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

		return mbBanPersistence.findWithDynamicQuery(dynamicQuery, start, end);
	}

	/**
	 * Performs a dynamic query on the database and returns an ordered range of the matching rows.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.message.boards.model.impl.MBBanModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
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

		return mbBanPersistence.findWithDynamicQuery(
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
		return mbBanPersistence.countWithDynamicQuery(dynamicQuery);
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

		return mbBanPersistence.countWithDynamicQuery(dynamicQuery, projection);
	}

	@Override
	public MBBan fetchMBBan(long banId) {
		return mbBanPersistence.fetchByPrimaryKey(banId);
	}

	/**
	 * Returns the message boards ban matching the UUID and group.
	 *
	 * @param uuid the message boards ban's UUID
	 * @param groupId the primary key of the group
	 * @return the matching message boards ban, or <code>null</code> if a matching message boards ban could not be found
	 */
	@Override
	public MBBan fetchMBBanByUuidAndGroupId(String uuid, long groupId) {
		return mbBanPersistence.fetchByUUID_G(uuid, groupId);
	}

	/**
	 * Returns the message boards ban with the primary key.
	 *
	 * @param banId the primary key of the message boards ban
	 * @return the message boards ban
	 * @throws PortalException if a message boards ban with the primary key could not be found
	 */
	@Override
	public MBBan getMBBan(long banId) throws PortalException {
		return mbBanPersistence.findByPrimaryKey(banId);
	}

	@Override
	public ActionableDynamicQuery getActionableDynamicQuery() {
		ActionableDynamicQuery actionableDynamicQuery =
			new DefaultActionableDynamicQuery();

		actionableDynamicQuery.setBaseLocalService(mbBanLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(MBBan.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("banId");

		return actionableDynamicQuery;
	}

	@Override
	public IndexableActionableDynamicQuery
		getIndexableActionableDynamicQuery() {

		IndexableActionableDynamicQuery indexableActionableDynamicQuery =
			new IndexableActionableDynamicQuery();

		indexableActionableDynamicQuery.setBaseLocalService(mbBanLocalService);
		indexableActionableDynamicQuery.setClassLoader(getClassLoader());
		indexableActionableDynamicQuery.setModelClass(MBBan.class);

		indexableActionableDynamicQuery.setPrimaryKeyPropertyName("banId");

		return indexableActionableDynamicQuery;
	}

	protected void initActionableDynamicQuery(
		ActionableDynamicQuery actionableDynamicQuery) {

		actionableDynamicQuery.setBaseLocalService(mbBanLocalService);
		actionableDynamicQuery.setClassLoader(getClassLoader());
		actionableDynamicQuery.setModelClass(MBBan.class);

		actionableDynamicQuery.setPrimaryKeyPropertyName("banId");
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
			new ActionableDynamicQuery.PerformActionMethod<MBBan>() {

				@Override
				public void performAction(MBBan mbBan) throws PortalException {
					StagedModelDataHandlerUtil.exportStagedModel(
						portletDataContext, mbBan);
				}

			});
		exportActionableDynamicQuery.setStagedModelType(
			new StagedModelType(
				PortalUtil.getClassNameId(MBBan.class.getName())));

		return exportActionableDynamicQuery;
	}

	/**
	 * @throws PortalException
	 */
	@Override
	public PersistedModel deletePersistedModel(PersistedModel persistedModel)
		throws PortalException {

		return mbBanLocalService.deleteMBBan((MBBan)persistedModel);
	}

	@Override
	public PersistedModel getPersistedModel(Serializable primaryKeyObj)
		throws PortalException {

		return mbBanPersistence.findByPrimaryKey(primaryKeyObj);
	}

	/**
	 * Returns all the message boards bans matching the UUID and company.
	 *
	 * @param uuid the UUID of the message boards bans
	 * @param companyId the primary key of the company
	 * @return the matching message boards bans, or an empty list if no matches were found
	 */
	@Override
	public List<MBBan> getMBBansByUuidAndCompanyId(
		String uuid, long companyId) {

		return mbBanPersistence.findByUuid_C(uuid, companyId);
	}

	/**
	 * Returns a range of message boards bans matching the UUID and company.
	 *
	 * @param uuid the UUID of the message boards bans
	 * @param companyId the primary key of the company
	 * @param start the lower bound of the range of message boards bans
	 * @param end the upper bound of the range of message boards bans (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the range of matching message boards bans, or an empty list if no matches were found
	 */
	@Override
	public List<MBBan> getMBBansByUuidAndCompanyId(
		String uuid, long companyId, int start, int end,
		OrderByComparator<MBBan> orderByComparator) {

		return mbBanPersistence.findByUuid_C(
			uuid, companyId, start, end, orderByComparator);
	}

	/**
	 * Returns the message boards ban matching the UUID and group.
	 *
	 * @param uuid the message boards ban's UUID
	 * @param groupId the primary key of the group
	 * @return the matching message boards ban
	 * @throws PortalException if a matching message boards ban could not be found
	 */
	@Override
	public MBBan getMBBanByUuidAndGroupId(String uuid, long groupId)
		throws PortalException {

		return mbBanPersistence.findByUUID_G(uuid, groupId);
	}

	/**
	 * Returns a range of all the message boards bans.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent and pagination is required (<code>start</code> and <code>end</code> are not <code>com.liferay.portal.kernel.dao.orm.QueryUtil#ALL_POS</code>), then the query will include the default ORDER BY logic from <code>com.liferay.message.boards.model.impl.MBBanModelImpl</code>. If both <code>orderByComparator</code> and pagination are absent, for performance reasons, the query will not have an ORDER BY clause and the returned result set will be sorted on by the primary key in an ascending order.
	 * </p>
	 *
	 * @param start the lower bound of the range of message boards bans
	 * @param end the upper bound of the range of message boards bans (not inclusive)
	 * @return the range of message boards bans
	 */
	@Override
	public List<MBBan> getMBBans(int start, int end) {
		return mbBanPersistence.findAll(start, end);
	}

	/**
	 * Returns the number of message boards bans.
	 *
	 * @return the number of message boards bans
	 */
	@Override
	public int getMBBansCount() {
		return mbBanPersistence.countAll();
	}

	/**
	 * Updates the message boards ban in the database or adds it if it does not yet exist. Also notifies the appropriate model listeners.
	 *
	 * @param mbBan the message boards ban
	 * @return the message boards ban that was updated
	 */
	@Indexable(type = IndexableType.REINDEX)
	@Override
	public MBBan updateMBBan(MBBan mbBan) {
		return mbBanPersistence.update(mbBan);
	}

	@Override
	public Class<?>[] getAopInterfaces() {
		return new Class<?>[] {
			MBBanLocalService.class, IdentifiableOSGiService.class,
			PersistedModelLocalService.class
		};
	}

	@Override
	public void setAopProxy(Object aopProxy) {
		mbBanLocalService = (MBBanLocalService)aopProxy;
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return MBBanLocalService.class.getName();
	}

	protected Class<?> getModelClass() {
		return MBBan.class;
	}

	protected String getModelClassName() {
		return MBBan.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = mbBanPersistence.getDataSource();

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

	protected MBBanLocalService mbBanLocalService;

	@Reference
	protected MBBanPersistence mbBanPersistence;

	@Reference
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

}