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

package com.liferay.arquillian.extension.junit.bridge.container.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.arquillian.config.descriptor.api.ContainerDef;
import org.jboss.arquillian.container.impl.ContainerImpl;
import org.jboss.arquillian.container.spi.Container;
import org.jboss.arquillian.container.spi.ContainerRegistry;
import org.jboss.arquillian.container.spi.client.container.DeployableContainer;
import org.jboss.arquillian.container.spi.client.deployment.TargetDescription;
import org.jboss.arquillian.core.api.Injector;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.core.spi.Validate;

/**
 * @author Matthew Tambara
 */
public class LocalContainerRegistry implements ContainerRegistry {

	public LocalContainerRegistry(Injector injector) {
		_injector = injector;
	}

	@Override
	public Container create(
		ContainerDef containerDef, ServiceLoader serviceLoader) {

		Validate.notNull(containerDef, "Definition must be specified");

		try {
			return _addContainer(
				_injector.inject(
					new ContainerImpl(
						containerDef.getContainerName(),
						serviceLoader.onlyOne(DeployableContainer.class),
						containerDef)));
		}
		catch (Exception e) {
			throw new RuntimeException(
				"Could not create Container " + containerDef.getContainerName(),
				e);
		}
	}

	@Override
	public Container getContainer(String name) {
		return _findMatchingContainer(name);
	}

	@Override
	public Container getContainer(TargetDescription targetDescription) {
		Validate.notNull(targetDescription, "Target must be specified");

		if (TargetDescription.DEFAULT.equals(targetDescription)) {
			return _findDefaultContainer();
		}

		return _findMatchingContainer(targetDescription.getName());
	}

	@Override
	public List<Container> getContainers() {
		return Collections.unmodifiableList(_containers);
	}

	private Container _addContainer(Container container) {
		_containers.add(container);

		return container;
	}

	private Container _findDefaultContainer() {
		if (_containers.size() == 1) {
			return _containers.get(0);
		}

		for (Container container : _containers) {
			ContainerDef containerDef = container.getContainerConfiguration();

			if (containerDef.isDefault()) {
				return container;
			}
		}

		return null;
	}

	private Container _findMatchingContainer(String name) {
		for (Container container : _containers) {
			if (name.equals(container.getName())) {
				return container;
			}
		}

		return null;
	}

	private final List<Container> _containers = new ArrayList<>();
	private final Injector _injector;

}