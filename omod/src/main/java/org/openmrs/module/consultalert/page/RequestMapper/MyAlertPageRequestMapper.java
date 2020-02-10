/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.consultalert.page.RequestMapper;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.notification.Alert;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.ui.framework.page.PageRequestMapper;
import org.springframework.stereotype.Component;

/**
 * @author levine
 */
@Component
public class MyAlertPageRequestMapper implements PageRequestMapper {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * Implementations should call {@link PageRequest#setProviderNameOverride(String)} and
	 * {@link PageRequest#setPageNameOverride(String)}, and return true if they want to remap a
	 * request, or return false if they didn't remap it.
	 * 
	 * @param request may have its providerNameOverride and pageNameOverride set
	 * @return true if this page was mapped (by overriding the provider and/or page), false
	 *         otherwise
	 */
	public boolean mapRequest(PageRequest request) {
		String requestPage = request.getPageName();
		System.out.println("***************mapRequest: " + requestPage);
		if (!(requestPage.equals("home") || requestPage.equals("login"))) {
			return false;
		}
		User user = Context.getUserContext().getAuthenticatedUser();
		if (user == null) {
			return false;
		}
		int userId = user.getUserId();
		String userIdStr = String.valueOf(userId);
		HttpSession session = request.getRequest().getSession();
		String alertMessage;
		List<Alert> alerts = Context.getAlertService().getAllActiveAlerts(user);
		if (alerts.isEmpty()) {
			//InfoErrorMessageUtil.flashInfoMessage(session, "YOU HAVE NO ALERTS");
			return false;
		}
		giveAlertMessage(alerts, session);
		
		return false;
	}
	
	private void giveAlertMessage(List<Alert> alerts, HttpSession session) {
		String patients = "";
		String alertText;
		String patientName;
		for (Alert alert : alerts) {
			alertText = alert.getText();
			if (alertText.indexOf("/") == -1) { /// alert caused by something other than response
				continue;
			}
			if (alert.getAlertRead()) {
				continue;
			}
			patientName = (alertText.split("/"))[0];
			patients += patientName + ", ";
		}
		System.out.println("Alerts: " + patients);
		if (patients.trim().equals("")) {
			return;
		}
		patients = patients.substring(0, patients.lastIndexOf(", "));
		InfoErrorMessageUtil.flashInfoMessage(session, "YOU HAVE ALERT(S) FROM PATIENT CONSULT REQUEST(S): " + patients);
	}
	
	void checkOnAlertDismiss(List<Alert> alerts, PageRequest request, HttpSession session) {
		//    if attempt to dismiss and we're in same visit as what generated the alert the don't dismiss, check other cases
		/*
		String patientId = request.getRequest().getParameter("patientId").trim();
		System.out.println("checkOnAlertDismiss, patientId: " + patientId);
		for (Alert alert : alerts) {
			String patId = alert.getText().split("/")[1].trim();
			System.out.println("patientId: " + patientId + " patId: " + patId);
			if (patId.equals(patientId)) {
				alert.markAlertRead();
				Context.getAlertService().purgeAlert(alert);
			}
			
		}
		 */
	}
}
