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

package com.liferay.portal.workflow.kaleo.definition.exception;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.workflow.WorkflowException;

/**
 * @author Inácio Nery
 */
public class KaleoDefinitionValidationException extends WorkflowException {

	public KaleoDefinitionValidationException() {
	}

	public KaleoDefinitionValidationException(String msg) {
		super(msg);
	}

	public KaleoDefinitionValidationException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public KaleoDefinitionValidationException(Throwable cause) {
		super(cause);
	}

	public static class EmptyNotificationTemplate
		extends KaleoDefinitionValidationException {

		public EmptyNotificationTemplate(String node) {
			super(
				StringBundler.concat(
					"The ", node, " node has a empty notification template"));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private final String _node;

	}

	public static class MultipleInitialStateNodes
		extends KaleoDefinitionValidationException {

		public MultipleInitialStateNodes(String state1, String state2) {
			super(
				StringBundler.concat(
					"The workflow has too many start nodes (state nodes ",
					state1, " and ", state2));

			_state1 = state1;
			_state2 = state2;
		}

		public String getState1() {
			return _state1;
		}

		public String getState2() {
			return _state2;
		}

		private final String _state1;
		private final String _state2;

	}

	public static class MustNotSetIncomingTransition
		extends KaleoDefinitionValidationException {

		public MustNotSetIncomingTransition(String node) {
			super(
				StringBundler.concat(
					"The ", node, " node cannot have an incoming transition"));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private final String _node;

	}

	public static class MustPairedForkAndJoinNodes
		extends KaleoDefinitionValidationException {

		public MustPairedForkAndJoinNodes(String fork, String node) {
			super(
				StringBundler.concat(
					"Fork ", fork, " and join ", node,
					" nodes must be paired"));

			_fork = fork;
			_node = node;
		}

		public String getFork() {
			return _fork;
		}

		public String getNode() {
			return _node;
		}

		private final String _fork;
		private final String _node;

	}

	public static class MustSetAssignments
		extends KaleoDefinitionValidationException {

		public MustSetAssignments(String task) {
			super(
				StringBundler.concat(
					"Specify at least one assignment for the ", task,
					" task node"));

			_task = task;
		}

		public String getTask() {
			return _task;
		}

		private final String _task;

	}

	public static class MustSetIncomingTransition
		extends KaleoDefinitionValidationException {

		public MustSetIncomingTransition(String node) {
			super(
				StringBundler.concat(
					"The ", node, " node must have an incoming transition"));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private final String _node;

	}

	public static class MustSetInitialStateNode
		extends KaleoDefinitionValidationException {

		public MustSetInitialStateNode() {
			super("You must define a start node");
		}

	}

	public static class MustSetJoinNode
		extends KaleoDefinitionValidationException {

		public MustSetJoinNode(String fork) {
			super(
				StringBundler.concat(
					"The ", fork, " fork node must have a matching join node"));

			_fork = fork;
		}

		public String getFork() {
			return _fork;
		}

		private final String _fork;

	}

	public static class MustSetMultipleOutgoingTransition
		extends KaleoDefinitionValidationException {

		public MustSetMultipleOutgoingTransition(String node) {
			super(
				StringBundler.concat(
					"The ", node,
					" node must have at least 2 outgoing transitions"));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private final String _node;

	}

	public static class MustSetOutgoingTransition
		extends KaleoDefinitionValidationException {

		public MustSetOutgoingTransition(String node) {
			super(
				StringBundler.concat(
					"The ", node, " node must have an outgoing transition"));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private final String _node;

	}

	public static class MustSetSourceNode
		extends KaleoDefinitionValidationException {

		public MustSetSourceNode(String node) {
			super(
				StringBundler.concat(
					"The ", node, " transition must have a source node"));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private final String _node;

	}

	public static class MustSetTargetNode
		extends KaleoDefinitionValidationException {

		public MustSetTargetNode(String node) {
			super(
				StringBundler.concat(
					"The ", node, " transition must end at a node"));

			_node = node;
		}

		public String getNode() {
			return _node;
		}

		private final String _node;

	}

	public static class MustSetTaskFormDefinitionOrReference
		extends KaleoDefinitionValidationException {

		public MustSetTaskFormDefinitionOrReference(
			String task, String taskForm) {

			super(
				StringBundler.concat(
					"The task form ", taskForm, " for task ", task,
					" must specify a form reference or form definition"));

			_task = task;
			_taskForm = taskForm;
		}

		public String getTask() {
			return _task;
		}

		public String getTaskForm() {
			return _taskForm;
		}

		private final String _task;
		private final String _taskForm;

	}

	public static class MustSetTerminalStateNode
		extends KaleoDefinitionValidationException {

		public MustSetTerminalStateNode() {
			super("You must define an end node");
		}

	}

	public static class UnbalancedForkAndJoinNode
		extends KaleoDefinitionValidationException {

		public UnbalancedForkAndJoinNode(String fork, String join) {
			super(
				StringBundler.concat(
					"Fix the errors between the fork node ", fork,
					" and join node ", join));

			_fork = fork;
			_join = join;
		}

		public String getFork() {
			return _fork;
		}

		public String getJoin() {
			return _join;
		}

		private final String _fork;
		private final String _join;

	}

	public static class UnbalancedForkAndJoinNodes
		extends KaleoDefinitionValidationException {

		public UnbalancedForkAndJoinNodes() {
			super(
				"Each fork node requires a join node. Make sure all forks and" +
					"joins are properly paired");
		}

	}

}