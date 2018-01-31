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

package com.liferay.portlet;

import com.liferay.portal.ccpp.PortalProfileFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.model.PortletApp;
import com.liferay.portal.kernel.model.PortletConstants;
import com.liferay.portal.kernel.model.PublicRenderParameter;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.InvokerPortlet;
import com.liferay.portal.kernel.portlet.LiferayPortletConfig;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletSession;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.portlet.PortletQName;
import com.liferay.portal.kernel.portlet.PortletQNameUtil;
import com.liferay.portal.kernel.service.PortletLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.servlet.DynamicServletRequest;
import com.liferay.portal.kernel.servlet.PortletServlet;
import com.liferay.portal.kernel.servlet.ProtectedPrincipal;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.security.lang.DoPrivilegedBean;
import com.liferay.portal.security.lang.DoPrivilegedUtil;
import com.liferay.portal.servlet.NamespaceServletRequest;
import com.liferay.portal.servlet.SharedSessionServletRequest;
import com.liferay.portal.util.PropsValues;
import com.liferay.portlet.portletconfiguration.util.PublicRenderParameterConfiguration;

import java.security.Principal;
import java.security.PrivilegedAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ccpp.Profile;

import javax.portlet.PortalContext;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;
import javax.portlet.WindowState;
import javax.portlet.filter.PortletRequestWrapper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Brian Wing Shun Chan
 * @author Brian Myunghun Kim
 * @author Sergey Ponomarev
 * @author Raymond Augé
 */
public abstract class PortletRequestImpl implements LiferayPortletRequest {

	public static PortletRequestImpl getPortletRequestImpl(
		PortletRequest portletRequest) {

		while (!(portletRequest instanceof PortletRequestImpl)) {
			if (portletRequest instanceof DoPrivilegedBean) {
				DoPrivilegedBean doPrivilegedBean =
					(DoPrivilegedBean)portletRequest;

				portletRequest =
					(PortletRequest)doPrivilegedBean.getActualBean();
			}
			else if (portletRequest instanceof PortletRequestWrapper) {
				PortletRequestWrapper portletRequestWrapper =
					(PortletRequestWrapper)portletRequest;

				portletRequest = portletRequestWrapper.getRequest();
			}
			else {
				throw new RuntimeException(
					"Unable to unwrap the portlet request from " +
						portletRequest.getClass());
			}
		}

		return (PortletRequestImpl)portletRequest;
	}

	public void cleanUp() {
		_request.removeAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
		_request.removeAttribute(JavaConstants.JAVAX_PORTLET_REQUEST);
		_request.removeAttribute(JavaConstants.JAVAX_PORTLET_RESPONSE);
		_request.removeAttribute(PortletRequest.LIFECYCLE_PHASE);
		_request.removeAttribute(WebKeys.PORTLET_ID);
		_request.removeAttribute(WebKeys.PORTLET_CONTENT);
	}

	@Override
	public Map<String, String[]> clearRenderParameters() {
		return RenderParametersPool.clear(_request, _plid, _portletName);
	}

	@Override
	public void defineObjects(
		PortletConfig portletConfig, PortletResponse portletResponse) {

		LiferayPortletConfig liferayPortletConfig =
			(LiferayPortletConfig)portletConfig;

		setAttribute(WebKeys.PORTLET_ID, liferayPortletConfig.getPortletId());

		setAttribute(JavaConstants.JAVAX_PORTLET_CONFIG, portletConfig);
		setAttribute(JavaConstants.JAVAX_PORTLET_REQUEST, this);
		setAttribute(JavaConstants.JAVAX_PORTLET_RESPONSE, portletResponse);
		setAttribute(PortletRequest.LIFECYCLE_PHASE, getLifecycle());
	}

	public Map<String, Object> getActionScopedRequestAttributesPool() {
		return _actionScopedRequestAttributesPool;
	}

	@Override
	public Object getAttribute(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}

		if (name.equals(PortletRequest.CCPP_PROFILE)) {
			return getCCPPProfile();
		}
		else if (name.equals(PortletRequest.USER_INFO)) {
			Object value = getUserInfo();

			if (value != null) {
				return value;
			}
		}

		Object object = null;

		if (_actionScopedRequestAttributesPool != null) {
			object = _actionScopedRequestAttributesPool.get(name);

			if (object == null) {
				object = _request.getAttribute(name);
			}
		}
		else {
			object = _request.getAttribute(name);
		}

		return object;
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		Set<String> names = new HashSet<>();

		if (_actionScopedRequestAttributesPool != null) {
			Set<String> keySet = _actionScopedRequestAttributesPool.keySet();

			_copyAttributeNames(names, keySet);
		}

		Enumeration<String> enu = _request.getAttributeNames();

		_copyAttributeNames(names, enu);

		if (_portletRequestDispatcherRequest != null) {
			enu = _portletRequestDispatcherRequest.getAttributeNames();

			_copyAttributeNames(names, enu);
		}

		return Collections.enumeration(names);
	}

	@Override
	public String getAuthType() {
		return _request.getAuthType();
	}

	public Profile getCCPPProfile() {
		if (_profile == null) {
			_profile = PortalProfileFactory.getCCPPProfile(_request);
		}

		return _profile;
	}

	@Override
	public String getContextPath() {
		return _portlet.getContextPath();
	}

	@Override
	public Cookie[] getCookies() {
		return _request.getCookies();
	}

	public String getETag() {
		return null;
	}

	@Override
	public HttpServletRequest getHttpServletRequest() {
		return _request;
	}

	public abstract String getLifecycle();

	@Override
	public Locale getLocale() {
		Locale locale = _locale;

		if (locale == null) {
			locale = _request.getLocale();
		}

		if (locale == null) {
			locale = LocaleUtil.getDefault();
		}

		return locale;
	}

	@Override
	public Enumeration<Locale> getLocales() {
		return _request.getLocales();
	}

	public String getMethod() {
		return _request.getMethod();
	}

	@Override
	public HttpServletRequest getOriginalHttpServletRequest() {
		return _originalRequest;
	}

	@Override
	public String getParameter(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}

		if (_portletRequestDispatcherRequest != null) {
			return _portletRequestDispatcherRequest.getParameter(name);
		}

		return _request.getParameter(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		if (_portletRequestDispatcherRequest != null) {
			return Collections.unmodifiableMap(
				_portletRequestDispatcherRequest.getParameterMap());
		}

		return Collections.unmodifiableMap(_request.getParameterMap());
	}

	@Override
	public Enumeration<String> getParameterNames() {
		if (_portletRequestDispatcherRequest != null) {
			return _portletRequestDispatcherRequest.getParameterNames();
		}

		return _request.getParameterNames();
	}

	@Override
	public String[] getParameterValues(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}

		if (_portletRequestDispatcherRequest != null) {
			return _portletRequestDispatcherRequest.getParameterValues(name);
		}

		return _request.getParameterValues(name);
	}

	@Override
	public long getPlid() {
		return _plid;
	}

	@Override
	public PortalContext getPortalContext() {
		return _portalContext;
	}

	@Override
	public Portlet getPortlet() {
		return _portlet;
	}

	public PortletContext getPortletContext() {
		return _portletContext;
	}

	@Override
	public PortletMode getPortletMode() {
		return _portletMode;
	}

	@Override
	public String getPortletName() {
		return _portletName;
	}

	public HttpServletRequest getPortletRequestDispatcherRequest() {
		return _portletRequestDispatcherRequest;
	}

	@Override
	public PortletSession getPortletSession() {
		return _session;
	}

	@Override
	public PortletSession getPortletSession(boolean create) {
		if (!create && _invalidSession) {
			return null;
		}

		return _session;
	}

	@Override
	public PortletPreferences getPreferences() {
		String lifecycle = getLifecycle();

		if (lifecycle.equals(PortletRequest.RENDER_PHASE) &&
			PropsValues.PORTLET_PREFERENCES_STRICT_STORE) {

			return DoPrivilegedUtil.wrap(
				new PortletPreferencesPrivilegedAction());
		}

		return getPreferencesImpl();
	}

	public PortletPreferencesImpl getPreferencesImpl() {
		return (PortletPreferencesImpl)_preferences;
	}

	@Override
	public Map<String, String[]> getPrivateParameterMap() {
		Map<String, String[]> parameterMap = null;

		if (_portletRequestDispatcherRequest != null) {
			parameterMap = _portletRequestDispatcherRequest.getParameterMap();
		}
		else {
			parameterMap = _request.getParameterMap();
		}

		Map<String, String[]> privateParameterMap = null;

		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String name = entry.getKey();

			if (_portlet.getPublicRenderParameter(name) != null) {
				continue;
			}

			if (privateParameterMap == null) {
				privateParameterMap = new HashMap<>(parameterMap.size(), 1);
			}

			privateParameterMap.put(name, entry.getValue());
		}

		if (privateParameterMap == null) {
			return Collections.emptyMap();
		}

		return Collections.unmodifiableMap(privateParameterMap);
	}

	@Override
	public Enumeration<String> getProperties(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}

		List<String> values = new ArrayList<>();

		Enumeration<String> enumeration = _request.getHeaders(name);

		if (enumeration != null) {
			while (enumeration.hasMoreElements()) {
				String header = enumeration.nextElement();

				if (header != null) {
					values.add(header);
				}
			}
		}

		String value = _portalContext.getProperty(name);

		if (value != null) {
			values.add(value);
		}

		return Collections.enumeration(values);
	}

	@Override
	public String getProperty(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}

		String value = _request.getHeader(name);

		if (value == null) {
			value = _portalContext.getProperty(name);
		}

		return value;
	}

	@Override
	public Enumeration<String> getPropertyNames() {
		List<String> names = new ArrayList<>();

		Enumeration<String> headerNamesEnumeration = _request.getHeaderNames();

		if (headerNamesEnumeration != null) {
			while (headerNamesEnumeration.hasMoreElements()) {
				names.add(headerNamesEnumeration.nextElement());
			}
		}

		Enumeration<String> propertyNamesEnumeration =
			_portalContext.getPropertyNames();

		while (propertyNamesEnumeration.hasMoreElements()) {
			names.add(propertyNamesEnumeration.nextElement());
		}

		return Collections.enumeration(names);
	}

	@Override
	public Map<String, String[]> getPublicParameterMap() {
		Map<String, String[]> parameterMap = null;

		if (_portletRequestDispatcherRequest != null) {
			parameterMap = _portletRequestDispatcherRequest.getParameterMap();
		}
		else {
			parameterMap = _request.getParameterMap();
		}

		Map<String, String[]> publicParameterMap = null;

		for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
			String name = entry.getKey();

			if (_portlet.getPublicRenderParameter(name) != null) {
				if (publicParameterMap == null) {
					publicParameterMap = new HashMap<>(parameterMap.size(), 1);
				}

				publicParameterMap.put(name, entry.getValue());
			}
		}

		if (publicParameterMap == null) {
			return Collections.emptyMap();
		}
		else {
			return Collections.unmodifiableMap(publicParameterMap);
		}
	}

	@Override
	public String getRemoteUser() {
		return _remoteUser;
	}

	@Override
	public String getRequestedSessionId() {
		if (_session != null) {
			return _session.getId();
		}

		HttpSession session = _request.getSession(false);

		if (session == null) {
			return StringPool.BLANK;
		}
		else {
			return session.getId();
		}
	}

	@Override
	public String getResponseContentType() {
		return ContentTypes.TEXT_HTML;
	}

	@Override
	public Enumeration<String> getResponseContentTypes() {
		List<String> responseContentTypes = new ArrayList<>();

		responseContentTypes.add(getResponseContentType());

		return Collections.enumeration(responseContentTypes);
	}

	@Override
	public String getScheme() {
		return _request.getScheme();
	}

	@Override
	public String getServerName() {
		return _request.getServerName();
	}

	@Override
	public int getServerPort() {
		return _request.getServerPort();
	}

	public LinkedHashMap<String, String> getUserInfo() {
		return UserInfoFactory.getUserInfo(_remoteUserId, _portlet);
	}

	@Override
	public Principal getUserPrincipal() {
		return _userPrincipal;
	}

	@Override
	public String getWindowID() {
		return _portletName.concat(
			LiferayPortletSession.LAYOUT_SEPARATOR).concat(
				String.valueOf(_plid));
	}

	@Override
	public WindowState getWindowState() {
		return _windowState;
	}

	public void invalidateSession() {
		_invalidSession = true;
	}

	public boolean isInvalidParameter(String name) {
		if (Validator.isNull(name) ||
			name.startsWith(PortletQName.PUBLIC_RENDER_PARAMETER_NAMESPACE) ||
			name.startsWith(
				PortletQName.REMOVE_PUBLIC_RENDER_PARAMETER_NAMESPACE) ||
			PortalUtil.isReservedParameter(name)) {

			return true;
		}

		if (_strutsPortlet) {
			Matcher matcher = _strutsPortletIgnoredParamtersPattern.matcher(
				name);

			if (matcher.matches()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean isPortletModeAllowed(PortletMode portletMode) {
		if ((portletMode == null) || Validator.isNull(portletMode.toString())) {
			return true;
		}
		else {
			return _portlet.hasPortletMode(
				getResponseContentType(), portletMode);
		}
	}

	public boolean isPrivateRequestAttributes() {
		return _portlet.isPrivateRequestAttributes();
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		if (_session.isInvalidated() || _invalidSession) {
			return false;
		}

		return _request.isRequestedSessionIdValid();
	}

	@Override
	public boolean isSecure() {
		return _request.isSecure();
	}

	public boolean isTriggeredByActionURL() {
		return _triggeredByActionURL;
	}

	@Override
	public boolean isUserInRole(String role) {
		if (_remoteUserId <= 0) {
			return false;
		}

		try {
			long companyId = PortalUtil.getCompanyId(_request);

			Map<String, String> roleMappersMap = _portlet.getRoleMappers();

			String roleLink = roleMappersMap.get(role);

			if (Validator.isNotNull(roleLink)) {
				return RoleLocalServiceUtil.hasUserRole(
					_remoteUserId, companyId, roleLink, true);
			}
			else {
				return RoleLocalServiceUtil.hasUserRole(
					_remoteUserId, companyId, role, true);
			}
		}
		catch (Exception e) {
			_log.error("Unable to check if a user is in role " + role, e);
		}

		return _request.isUserInRole(role);
	}

	@Override
	public boolean isWindowStateAllowed(WindowState windowState) {
		return PortalContextImpl.isSupportedWindowState(windowState);
	}

	@Override
	public void removeAttribute(String name) {
		if (name == null) {
			throw new IllegalArgumentException();
		}

		if ((_actionScopedRequestAttributesPool != null) && _isNameOK(name)) {
			_actionScopedRequestAttributesPool.remove(name);
		}
		else {
			_request.removeAttribute(name);
		}
	}

	public void removePortletRequestAttrs() {
		Enumeration<String> attributesNames = getAttributeNames();

		while (attributesNames.hasMoreElements()) {
			String attributeName = attributesNames.nextElement();

			if (_isNameOK(attributeName)) {
				_request.removeAttribute(attributeName);
			}
		}
	}

	public void setActionScopedRequestAttributesPool(
		Map<String, Object> actionScopedRequestAttributesPool) {

		_actionScopedRequestAttributesPool = actionScopedRequestAttributesPool;
	}

	@Override
	public void setAttribute(String name, Object obj) {
		if (name == null) {
			throw new IllegalArgumentException();
		}

		if (obj == null) {
			if ((_actionScopedRequestAttributesPool != null) &&
				_isNameOK(name)) {

				_actionScopedRequestAttributesPool.remove(name);
			}
			else {
				_request.removeAttribute(name);
			}
		}
		else {
			if ((_actionScopedRequestAttributesPool != null) &&
				_isNameOK(name)) {

				_actionScopedRequestAttributesPool.put(name, obj);
			}
			else {
				_request.setAttribute(name, obj);
			}
		}
	}

	public void setPortletMode(PortletMode portletMode) {
		_portletMode = portletMode;
	}

	public void setPortletRequestDispatcherRequest(HttpServletRequest request) {
		_portletRequestDispatcherRequest = request;
	}

	public void setWindowState(WindowState windowState) {
		_windowState = windowState;
	}

	protected void init(
		HttpServletRequest request, Portlet portlet,
		InvokerPortlet invokerPortlet, PortletContext portletContext,
		WindowState windowState, PortletMode portletMode,
		PortletPreferences preferences, long plid) {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		_portlet = portlet;
		_portletName = portlet.getPortletId();

		PortletApp portletApp = portlet.getPortletApp();

		Map<String, String[]> publicRenderParametersMap =
			PublicRenderParametersPool.get(request, plid);

		if (invokerPortlet != null) {
			if (invokerPortlet.isStrutsPortlet() ||
				invokerPortlet.isStrutsBridgePortlet()) {

				_strutsPortlet = true;
			}
		}

		String portletNamespace = PortalUtil.getPortletNamespace(_portletName);

		boolean warFile = portletApp.isWARFile();

		if (!warFile) {
			String portletResource = ParamUtil.getString(
				request, portletNamespace.concat("portletResource"));

			if (Validator.isNotNull(portletResource)) {
				Portlet resourcePortlet = null;

				try {
					resourcePortlet = PortletLocalServiceUtil.getPortletById(
						themeDisplay.getCompanyId(), portletResource);
				}
				catch (Exception e) {
				}

				if (resourcePortlet != null) {
					PortletApp resourcePortletApp =
						resourcePortlet.getPortletApp();

					if (resourcePortletApp.isWARFile()) {
						warFile = true;
					}
				}
			}
		}

		if (warFile) {
			request = new SharedSessionServletRequest(
				request, !portlet.isPrivateSessionAttributes());
		}

		String dynamicQueryString = (String)request.getAttribute(
			DynamicServletRequest.DYNAMIC_QUERY_STRING);

		if (dynamicQueryString != null) {
			request.removeAttribute(DynamicServletRequest.DYNAMIC_QUERY_STRING);

			request = DynamicServletRequest.addQueryString(
				request, dynamicQueryString, true);
		}

		DynamicServletRequest dynamicRequest = null;

		if (portlet.isPrivateRequestAttributes()) {
			dynamicRequest = new NamespaceServletRequest(
				request, portletNamespace, portletNamespace, false);
		}
		else {
			dynamicRequest = new DynamicServletRequest(request, false);
		}

		boolean portletFocus = false;

		String ppid = ParamUtil.getString(request, "p_p_id");

		boolean windowStateRestoreCurrentView = ParamUtil.getBoolean(
			request, "p_p_state_rcv");

		if (_portletName.equals(ppid) &&
			!(windowStateRestoreCurrentView &&
			  portlet.isRestoreCurrentView())) {

			// Request was targeted to this portlet

			if (themeDisplay.isLifecycleRender() ||
				themeDisplay.isLifecycleResource()) {

				// Request was triggered by a render or resource URL

				portletFocus = true;
			}
			else if (themeDisplay.isLifecycleAction()) {
				_triggeredByActionURL = true;

				if (getLifecycle().equals(PortletRequest.ACTION_PHASE)) {

					// Request was triggered by an action URL and is being
					// processed by com.liferay.portlet.ActionRequestImpl

					portletFocus = true;
				}
			}
		}

		if (portletFocus) {
			Map<String, String[]> renderParameters = null;

			Map<String, String[]> parameters = request.getParameterMap();

			for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
				String name = entry.getKey();

				if (isInvalidParameter(name)) {
					continue;
				}

				String[] values = entry.getValue();

				if (themeDisplay.isLifecycleRender()) {
					if (renderParameters == null) {
						renderParameters = new HashMap<>();
					}

					renderParameters.put(name, values);
				}

				if (values == null) {
					continue;
				}

				if ((invokerPortlet != null) &&
					invokerPortlet.isFacesPortlet()) {

					if (name.startsWith(portletNamespace) ||
						!portlet.isRequiresNamespacedParameters()) {

						dynamicRequest.setParameterValues(name, values);
					}
				}
				else {
					String realName = removePortletNamespace(
						portletNamespace, name);

					if (!realName.equals(name) ||
						!portlet.isRequiresNamespacedParameters()) {

						dynamicRequest.setParameterValues(realName, values);
					}
				}
			}

			if (getLifecycle().equals(PortletRequest.RENDER_PHASE) &&
				!LiferayWindowState.isExclusive(request) &&
				!LiferayWindowState.isPopUp(request)) {

				if ((renderParameters == null) || renderParameters.isEmpty()) {
					RenderParametersPool.clear(request, plid, _portletName);
				}
				else {
					RenderParametersPool.put(
						request, plid, _portletName, renderParameters);
				}
			}
		}
		else {
			Map<String, String[]> renderParameters = RenderParametersPool.get(
				request, plid, _portletName);

			if (renderParameters != null) {
				for (Map.Entry<String, String[]> entry :
						renderParameters.entrySet()) {

					String name = entry.getKey();
					String[] values = entry.getValue();

					if ((invokerPortlet == null) ||
						!invokerPortlet.isFacesPortlet()) {

						name = removePortletNamespace(portletNamespace, name);
					}

					dynamicRequest.setParameterValues(name, values);
				}
			}
		}

		String actionScopeId = (String)request.getAttribute(
			PortletRequest.ACTION_SCOPE_ID);

		if (actionScopeId != null) {
			dynamicRequest.setParameter(
				PortletRequest.ACTION_SCOPE_ID, actionScopeId);

			request.removeAttribute(PortletRequest.ACTION_SCOPE_ID);
		}

		_mergePublicRenderParameters(
			dynamicRequest, publicRenderParametersMap, preferences,
			getLifecycle());

		_processCheckbox(dynamicRequest);

		_request = dynamicRequest;
		_originalRequest = request;
		_portlet = portlet;
		_portalContext = new PortalContextImpl();
		_portletContext = portletContext;
		_windowState = windowState;
		_portletMode = portletMode;
		_preferences = preferences;
		_session = new PortletSessionImpl(
			_request.getSession(), _portletContext, _portletName, plid);

		String remoteUser = request.getRemoteUser();

		String userPrincipalStrategy = portlet.getUserPrincipalStrategy();

		if (userPrincipalStrategy.equals(
				PortletConstants.USER_PRINCIPAL_STRATEGY_SCREEN_NAME)) {

			try {
				User user = PortalUtil.getUser(request);

				if (user != null) {
					_remoteUser = user.getScreenName();
					_remoteUserId = user.getUserId();
					_userPrincipal = new ProtectedPrincipal(_remoteUser);
				}
			}
			catch (Exception e) {
				_log.error("Unable to get user", e);
			}
		}
		else {
			long userId = PortalUtil.getUserId(request);

			if ((userId > 0) && (remoteUser == null)) {
				_remoteUser = String.valueOf(userId);
				_remoteUserId = userId;
				_userPrincipal = new ProtectedPrincipal(_remoteUser);
			}
			else {
				_remoteUser = remoteUser;
				_remoteUserId = GetterUtil.getLong(remoteUser);
				_userPrincipal = request.getUserPrincipal();
			}
		}

		_locale = themeDisplay.getLocale();
		_plid = plid;
	}

	/**
	 * @deprecated As of 7.0.0, replaced by {@link
	 *             #_mergePublicRenderParameters(DynamicServletRequest, Map,
	 *             PortletPreferences, String)}
	 */
	@Deprecated
	protected void mergePublicRenderParameters(
		DynamicServletRequest dynamicRequest, PortletPreferences preferences,
		long plid) {

		_mergePublicRenderParameters(
			dynamicRequest, Collections.emptyMap(), preferences,
			getLifecycle());
	}

	protected String removePortletNamespace(
		String portletNamespace, String name) {

		if (name.startsWith(portletNamespace)) {
			name = name.substring(portletNamespace.length());
		}

		return name;
	}

	private void _copyAttributeNames(
		Set<String> names, Enumeration<String> enumeration) {

		while (enumeration.hasMoreElements()) {
			String name = enumeration.nextElement();

			if (!name.equals(JavaConstants.JAVAX_SERVLET_INCLUDE_PATH_INFO)) {
				names.add(name);
			}
		}
	}

	private void _copyAttributeNames(Set<String> names, Set<String> keySet) {
		for (String name : keySet) {
			names.add(name);
		}
	}

	private boolean _isNameOK(String name) {
		if (name.startsWith("javax.portlet.") ||
			name.startsWith("javax.servlet.") ||
			_reservedAttrs.contains(name)) {

			return false;
		}

		return true;
	}

	private void _mergePublicRenderParameters(
		DynamicServletRequest dynamicRequest,
		Map<String, String[]> publicRenderParametersMap,
		PortletPreferences preferences, String lifecycle) {

		Set<PublicRenderParameter> publicRenderParameters =
			_portlet.getPublicRenderParameters();

		if (publicRenderParameters.isEmpty()) {
			return;
		}

		Enumeration<String> enumeration = preferences.getNames();

		if (!enumeration.hasMoreElements()) {
			if (publicRenderParametersMap.isEmpty()) {
				return;
			}

			for (PublicRenderParameter publicRenderParameter :
					publicRenderParameters) {

				String[] values = publicRenderParametersMap.get(
					PortletQNameUtil.getPublicRenderParameterName(
						publicRenderParameter.getQName()));

				if (ArrayUtil.isEmpty(values) || Validator.isNull(values[0])) {
					continue;
				}

				String name = publicRenderParameter.getIdentifier();

				String[] requestValues = dynamicRequest.getParameterValues(
					name);

				if ((requestValues != null) &&
					(lifecycle.equals(PortletRequest.ACTION_PHASE) ||
					 lifecycle.equals(PortletRequest.RESOURCE_PHASE))) {

					dynamicRequest.setParameterValues(
						name, ArrayUtil.append(requestValues, values));
				}
				else {
					dynamicRequest.setParameterValues(name, values);
				}
			}

			return;
		}

		for (PublicRenderParameter publicRenderParameter :
				publicRenderParameters) {

			String publicRenderParameterName =
				PortletQNameUtil.getPublicRenderParameterName(
					publicRenderParameter.getQName());

			String ignoreKey = PublicRenderParameterConfiguration.getIgnoreKey(
				publicRenderParameterName);

			boolean ignoreValue = GetterUtil.getBoolean(
				preferences.getValue(ignoreKey, null));

			if (ignoreValue) {
				continue;
			}

			String mappingKey =
				PublicRenderParameterConfiguration.getMappingKey(
					publicRenderParameterName);

			String mappingValue = GetterUtil.getString(
				preferences.getValue(mappingKey, null));

			HttpServletRequest request =
				(HttpServletRequest)dynamicRequest.getRequest();

			String[] newValues = request.getParameterValues(mappingValue);

			if ((newValues != null) && (newValues.length != 0)) {
				newValues = ArrayUtil.remove(newValues, StringPool.NULL);
			}

			String name = publicRenderParameter.getIdentifier();

			if (ArrayUtil.isEmpty(newValues)) {
				String[] values = publicRenderParametersMap.get(
					publicRenderParameterName);

				if (ArrayUtil.isEmpty(values) || Validator.isNull(values[0])) {
					continue;
				}

				if (dynamicRequest.getParameter(name) == null) {
					dynamicRequest.setParameterValues(name, values);
				}
			}
			else {
				dynamicRequest.setParameterValues(name, newValues);
			}
		}
	}

	private void _processCheckbox(DynamicServletRequest dynamicServletRequest) {
		String checkboxNames = dynamicServletRequest.getParameter(
			"checkboxNames");

		if (Validator.isNull(checkboxNames)) {
			return;
		}

		for (String checkboxName : StringUtil.split(checkboxNames)) {
			String value = dynamicServletRequest.getParameter(checkboxName);

			if (value == null) {
				dynamicServletRequest.setParameter(
					checkboxName, Boolean.FALSE.toString());
			}
			else if (Objects.equals(value, "on")) {
				dynamicServletRequest.setParameter(
					checkboxName, Boolean.TRUE.toString());
			}
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		PortletRequestImpl.class);

	private static final Set<String> _reservedAttrs = new HashSet<>();
	private static final Pattern _strutsPortletIgnoredParamtersPattern =
		Pattern.compile(PropsValues.STRUTS_PORTLET_IGNORED_PARAMETERS_REGEXP);

	static {
		_reservedAttrs.add(WebKeys.INVOKER_FILTER_URI);
		_reservedAttrs.add(WebKeys.PORTLET_CONTENT);
		_reservedAttrs.add(WebKeys.PORTLET_ID);
		_reservedAttrs.add(WebKeys.THEME_DISPLAY);
		_reservedAttrs.add(WebKeys.WINDOW_STATE);
		_reservedAttrs.add(WebKeys.LAYOUT);
		_reservedAttrs.add(WebKeys.RENDER_PATH);
		_reservedAttrs.add(WebKeys.RENDER_PORTLET);
		_reservedAttrs.add(WebKeys.PORTLET_CONFIGURATOR_VISIBILITY);
		_reservedAttrs.add(PortletServlet.PORTLET_APP);
		_reservedAttrs.add(PortletServlet.PORTLET_SERVLET_CONFIG);
		_reservedAttrs.add(PortletServlet.PORTLET_SERVLET_FILTER_CHAIN);
		_reservedAttrs.add(PortletServlet.PORTLET_SERVLET_REQUEST);
		_reservedAttrs.add(PortletServlet.PORTLET_SERVLET_RESPONSE);
	}

	private Map<String, Object> _actionScopedRequestAttributesPool;
	private boolean _invalidSession;
	private Locale _locale;
	private HttpServletRequest _originalRequest;
	private long _plid;
	private PortalContext _portalContext;
	private Portlet _portlet;
	private PortletContext _portletContext;
	private PortletMode _portletMode;
	private String _portletName;
	private HttpServletRequest _portletRequestDispatcherRequest;
	private PortletPreferences _preferences;
	private Profile _profile;
	private String _remoteUser;
	private long _remoteUserId;
	private HttpServletRequest _request;
	private PortletSessionImpl _session;
	private boolean _strutsPortlet;
	private boolean _triggeredByActionURL;
	private Principal _userPrincipal;
	private WindowState _windowState;

	private class PortletPreferencesPrivilegedAction
		implements PrivilegedAction<PortletPreferences> {

		@Override
		public PortletPreferences run() {
			return new PortletPreferencesWrapper(getPreferencesImpl());
		}

	}

}