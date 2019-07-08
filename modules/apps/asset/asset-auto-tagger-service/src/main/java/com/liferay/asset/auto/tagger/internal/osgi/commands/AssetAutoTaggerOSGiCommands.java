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

package com.liferay.asset.auto.tagger.internal.osgi.commands;

import com.liferay.asset.auto.tagger.AssetAutoTagger;
import com.liferay.asset.auto.tagger.configuration.AssetAutoTaggerConfiguration;
import com.liferay.asset.auto.tagger.configuration.AssetAutoTaggerConfigurationFactory;
import com.liferay.asset.auto.tagger.internal.AssetAutoTaggerImpl;
import com.liferay.asset.auto.tagger.model.AssetAutoTaggerEntry;
import com.liferay.asset.auto.tagger.service.AssetAutoTaggerEntryLocalService;
import com.liferay.asset.kernel.model.AssetEntry;
import com.liferay.asset.kernel.service.AssetEntryLocalService;
import com.liferay.petra.function.UnsafeConsumer;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.dao.orm.Criterion;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alejandro Tardín
 */
@Component(
	immediate = true,
	property = {
		"osgi.command.function=commitAutoTags",
		"osgi.command.function=tagAllUntagged",
		"osgi.command.function=untagAll", "osgi.command.scope=assetAutoTagger"
	},
	service = AssetAutoTaggerOSGiCommands.class
)
public class AssetAutoTaggerOSGiCommands {

	public void commitAutoTags(String companyId, String... classNames) {
		_forEachAssetEntry(
			companyId, classNames,
			assetEntry -> {
				List<AssetAutoTaggerEntry> assetAutoTaggerEntries =
					_assetAutoTaggerEntryLocalService.getAssetAutoTaggerEntries(
						assetEntry);

				for (AssetAutoTaggerEntry assetAutoTaggerEntry :
						assetAutoTaggerEntries) {

					_assetAutoTaggerEntryLocalService.
						deleteAssetAutoTaggerEntry(assetAutoTaggerEntry);
				}

				if (!assetAutoTaggerEntries.isEmpty()) {
					System.out.println(
						StringBundler.concat(
							"Commited ", assetAutoTaggerEntries.size(),
							" auto tags for asset entry ",
							assetEntry.getTitle()));
				}
			});
	}

	public void tagAllUntagged(String companyId, String... classNames) {
		AssetAutoTaggerConfiguration assetAutoTaggerConfiguration =
			_assetAutoTaggerConfigurationFactory.
				getSystemAssetAutoTaggerConfiguration();

		if (!assetAutoTaggerConfiguration.isEnabled()) {
			System.out.println("Asset auto tagger is disabled");

			return;
		}

		if (ArrayUtil.isEmpty(classNames)) {
			Set<String> classNamesSet = new HashSet<>(
				_assetAutoTaggerImpl.getClassNames());

			classNamesSet.remove("*");

			classNames = classNamesSet.toArray(new String[0]);
		}

		_forEachAssetEntry(
			companyId, classNames,
			assetEntry -> {
				String[] oldAssetTagNames = assetEntry.getTagNames();

				if (oldAssetTagNames.length > 0) {
					return;
				}

				_assetAutoTagger.tag(assetEntry);

				String[] newAssetTagNames = assetEntry.getTagNames();

				if (oldAssetTagNames.length != newAssetTagNames.length) {
					System.out.println(
						StringBundler.concat(
							"Added ",
							newAssetTagNames.length - oldAssetTagNames.length,
							" tags to asset entry ", assetEntry.getTitle()));
				}
			});
	}

	public void untagAll(String companyId, String... classNames) {
		_forEachAssetEntry(
			companyId, classNames,
			assetEntry -> {
				String[] oldAssetTagNames = assetEntry.getTagNames();

				_assetAutoTagger.untag(assetEntry);

				String[] newAssetTagNames = assetEntry.getTagNames();

				if (oldAssetTagNames.length != newAssetTagNames.length) {
					System.out.println(
						StringBundler.concat(
							"Deleted ",
							oldAssetTagNames.length - newAssetTagNames.length,
							" tags to asset entry ", assetEntry.getTitle()));
				}
			});
	}

	private void _forEachAssetEntry(
		String companyId, String[] classNames,
		UnsafeConsumer<AssetEntry, PortalException> consumer) {

		try {
			ActionableDynamicQuery actionableDynamicQuery =
				_assetEntryLocalService.getActionableDynamicQuery();

			if (!ArrayUtil.isEmpty(classNames)) {
				actionableDynamicQuery.setAddCriteriaMethod(
					dynamicQuery -> dynamicQuery.add(
						_getClassNameIdCriterion(classNames)));
			}

			if (Validator.isNotNull(companyId)) {
				actionableDynamicQuery.setCompanyId(Long.valueOf(companyId));
			}

			actionableDynamicQuery.setPerformActionMethod(
				(AssetEntry assetEntry) -> consumer.accept(assetEntry));

			actionableDynamicQuery.performActions();
		}
		catch (Exception pe) {
			_log.error(pe, pe);
		}
	}

	private Criterion _getClassNameIdCriterion(String[] classNames) {
		Property property = PropertyFactoryUtil.forName("classNameId");

		Criterion criterion = property.eq(
			_classNameLocalService.getClassNameId(classNames[0]));

		for (int i = 1; i < classNames.length; i++) {
			long classNameId = _classNameLocalService.getClassNameId(
				classNames[i]);

			criterion = RestrictionsFactoryUtil.or(
				criterion, property.eq(classNameId));
		}

		return criterion;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		AssetAutoTaggerOSGiCommands.class);

	@Reference
	private AssetAutoTagger _assetAutoTagger;

	@Reference
	private AssetAutoTaggerConfigurationFactory
		_assetAutoTaggerConfigurationFactory;

	@Reference
	private AssetAutoTaggerEntryLocalService _assetAutoTaggerEntryLocalService;

	@Reference
	private AssetAutoTaggerImpl _assetAutoTaggerImpl;

	@Reference
	private AssetEntryLocalService _assetEntryLocalService;

	@Reference
	private ClassNameLocalService _classNameLocalService;

}