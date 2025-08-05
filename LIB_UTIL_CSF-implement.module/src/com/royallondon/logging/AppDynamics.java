package com.royallondon.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppDynamics {

	static final Logger LOG = LoggerFactory.getLogger(AppDynamics.class);

	public AppDynamics() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		AppDynamics inst = new AppDynamics();
		System.out.println(inst.notifyContext("jobID", "applicationName", "appnodeName", "appspaceName", "engineName", "processID", "transactionID", "correlationID"));
	}

	public int notifyContext(String jobID, String applicationName, String appnodeName, String appspaceName, String engineName, String processID, String transactionID, String correlationID) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("notifyContext(..) invoked" +
					  ", jobID='" + jobID +
					  "', applicationName='" + applicationName +
					  "', appnodeName='" + appnodeName +
					  "', appspaceName='" + appspaceName +
					  "', engineName='" + engineName +
					  "', processID='" + processID +
					  "', transactionID='" + transactionID +
					  "', correlationID='" + correlationID +
					  "'");
		}
		
		// Ensure that we do something to make sure that Java doesn't optimise out this procedure
		return jobID.hashCode();
	}
}