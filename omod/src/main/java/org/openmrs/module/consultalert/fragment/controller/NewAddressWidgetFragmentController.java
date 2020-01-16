/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.consultalert.fragment.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.notification.Alert;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class NewAddressWidgetFragmentController {
	
	//	public void controller(FragmentModel model, @FragmentParam("patientId") Patient patient, UiUtils ui,
	public void controller(FragmentModel model, @RequestParam("patientId") Patient patient, UiUtils ui,
	        HttpServletRequest request, HttpSession session) {
		String address = null;
		User user = Context.getUserContext().getAuthenticatedUser();
		int patientId = patient.getPatientId();
		System.out.println("NewAddressWidgetPageController, PATIENTID: " + patientId);
		List<Alert> alerts = Context.getAlertService().getAllActiveAlerts(user);
		int patId;
		int alertId = 0;
		boolean foundAlert = false;
		String alertText;
		for (Alert alert : alerts) {
			alertText = alert.getText();
			if (alertText.indexOf("/") == -1) { /// alert caused by something other than response
				continue;
			}
			patId = Integer.valueOf((alertText.split("/"))[1]);
			if (patId == patient.getPatientId()) {
				foundAlert = true;
				alertId = alert.getAlertId();
				break;
			}
		}
		model.addAttribute("alertId", alertId);
		model.addAttribute("patientId", patientId);
		
		/*
		PersonAttributeType attType = Context.getPersonService().getPersonAttributeTypeByName("Address");
		int attTypeId = attType.getId();
		PersonAttribute att = patient.getAttribute(attTypeId);
		if (att != null) {
		address = att.getValue();
		} else {
		address = "***address not provided***";
		}
		//model.addAttribute("patientId", patientId);
		model.addAttribute("address", address);
		 */
	}
	
	public SimpleObject purgeAlert(@RequestParam(value = "patientId", required = false) String patientId,
	        @RequestParam(value = "alertId", required = false) int alertId, UiUtils ui) {
		System.out.println("********PURGING ALERT: " + alertId);
		Alert alert = Context.getAlertService().getAlert(alertId);
		String[] properties = new String[] { "text" };
		Context.getAlertService().purgeAlert(alert);
		//System.out.println("SPECIALTY SAVED, SPECIALTY: " + specId + " patientId: " + patientId);
		return SimpleObject.fromObject(alert, ui, "text");
	}
}
