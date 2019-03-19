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

package com.liferay.portal.jmx;

import com.liferay.portal.test.rule.Inject;
import com.liferay.registry.BasicRegistryImpl;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistration;

import java.util.HashMap;

import javax.management.DynamicMBean;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Raymond Augé
 */
public class JMXWhiteboardTest {

	@BeforeClass
	public static void setUpClass() throws NotCompliantMBeanException {
		RegistryUtil.setRegistry(new BasicRegistryImpl());

		Registry registry = RegistryUtil.getRegistry();

		_serviceRegistration1 = registry.registerService(
			DynamicMBean.class, new JMXWhiteboardByDynamicMBean(),
			new HashMap<String, Object>() {
				{
					put(
						"jmx.objectname",
						JMXWhiteboardByDynamicMBean.OBJECT_NAME);
				}
			});

		_serviceRegistration2 = registry.registerService(
			JMXWhiteboardByInterfaceMBean.class, new JMXWhiteboardByInterface(),
			new HashMap<String, Object>() {
				{
					put(
						"jmx.objectname",
						JMXWhiteboardByInterfaceMBean.OBJECT_NAME);
				}
			});
	}

	@AfterClass
	public static void tearDownClass() {
		_serviceRegistration1.unregister();
		_serviceRegistration2.unregister();
	}

	@Test
	public void testMBeanByDynamicMBean() throws Exception {
		ObjectName objectName = new ObjectName(
			JMXWhiteboardByDynamicMBean.OBJECT_NAME);

		MBeanInfo mBeanInfo = _mBeanServer.getMBeanInfo(objectName);

		Assert.assertNotNull(mBeanInfo);

		MBeanOperationInfo[] operations = mBeanInfo.getOperations();

		MBeanOperationInfo mBeanOperationInfo = operations[0];

		MBeanParameterInfo[] signatureParameters =
			mBeanOperationInfo.getSignature();

		String[] sinature = new String[signatureParameters.length];

		for (int i = 0; i < signatureParameters.length; i++) {
			MBeanParameterInfo mBeanParameterInfo = signatureParameters[i];

			sinature[i] = mBeanParameterInfo.getType();
		}

		Object result = _mBeanServer.invoke(
			objectName, mBeanOperationInfo.getName(), new Object[] {"Hello!"},
			sinature);

		Assert.assertEquals("{Hello!}", result);
	}

	@Test
	public void testMBeanBySuffix() throws Exception {
		ObjectName objectName = new ObjectName(
			JMXWhiteboardByInterfaceMBean.OBJECT_NAME);

		MBeanInfo mBeanInfo = _mBeanServer.getMBeanInfo(objectName);

		Assert.assertNotNull(mBeanInfo);

		MBeanOperationInfo[] operations = mBeanInfo.getOperations();

		MBeanOperationInfo mBeanOperationInfo = operations[0];

		MBeanParameterInfo[] signatureParameters =
			mBeanOperationInfo.getSignature();

		String[] sinature = new String[signatureParameters.length];

		for (int i = 0; i < signatureParameters.length; i++) {
			MBeanParameterInfo mBeanParameterInfo = signatureParameters[i];

			sinature[i] = mBeanParameterInfo.getType();
		}

		Object result = _mBeanServer.invoke(
			objectName, mBeanOperationInfo.getName(), new Object[] {"Hello!"},
			sinature);

		Assert.assertEquals("{Hello!}", result);
	}

	public interface JMXWhiteboardByInterfaceMBean {

		public static final String OBJECT_NAME =
			"JMXWhiteboard:name=com.liferay.portal.jmx.JMXWhiteboardTest." +
				"JMXWhiteboardByInterfaceMBean";

		public String returnValue(String value);

	}

	@Inject
	private static MBeanServer _mBeanServer;

	private static ServiceRegistration<DynamicMBean> _serviceRegistration1;
	private static ServiceRegistration<JMXWhiteboardByInterfaceMBean>
		_serviceRegistration2;

	private static class JMXWhiteboardByDynamicMBean
		extends StandardMBean implements JMXWhiteboardByInterfaceMBean {

		public static final String OBJECT_NAME =
			"JMXWhiteboard:name=com.liferay.portal.jmx.JMXWhiteboardTest." +
				"JMXWhiteboardByDynamicMBean";

		public JMXWhiteboardByDynamicMBean() throws NotCompliantMBeanException {
			super(JMXWhiteboardByInterfaceMBean.class);
		}

		@Override
		public String returnValue(String value) {
			return "{" + value + "}";
		}

	}

	private static class JMXWhiteboardByInterface
		implements JMXWhiteboardByInterfaceMBean {

		@Override
		public String returnValue(String value) {
			return "{" + value + "}";
		}

	}

}