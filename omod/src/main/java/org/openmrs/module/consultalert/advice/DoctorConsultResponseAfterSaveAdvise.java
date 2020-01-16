/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.consultalert.advice;

import java.lang.reflect.Method;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Patient;
import org.openmrs.User;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.notification.Alert;
//import org.openmrs.module.internlmsgs.InternlMessage;
//import org.openmrs.module.internlmsgs.api.InternlMessageService;
import org.springframework.aop.AfterReturningAdvice;

/**
 * @author levine
 */
public class DoctorConsultResponseAfterSaveAdvise implements AfterReturningAdvice {
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		if (!method.getName().equals("saveEncounter")) {
			return;
		}
		Encounter encounter = (Encounter) args[0];
		String encTypeName = encounter.getEncounterType().getName();
		if (!encTypeName.equals("Telemedicine Consult")) {
			return;
		}
		
		System.out.println("SAVE ENCOUNTER: " + encTypeName);
		
		String subject = "Physician consult request response is posted";
		Visit visit = encounter.getVisit();
		Patient patient = encounter.getPatient();
		User requestingDoctor = visit.getCreator();
		//User msgSenderUser = Context.getUserService().getUserByUsername("omgbemena");
		User msgSenderUser = Context.getUserService().getUserByUsername("admin");
		
		String body = patient.getGivenName() + " " + patient.getFamilyName() + " /" + patient.getPatientId();
		createAlert(msgSenderUser, requestingDoctor, patient, subject, body);
		return;
	}
	
	void createAlert(User msgSenderUser, User requestingDoctor, Patient patient, String subject, String body) {
		
		System.out.println("************CONSULT RESPONSE: msgSenderUserUser: " + msgSenderUser.getId()
		        + " requestingDoctor: " + requestingDoctor + " patient: " + patient.getGivenName() + " "
		        + patient.getFamilyName() + " subject: " + subject + " body: " + body);
		Alert alert = new Alert();
		alert.addRecipient(requestingDoctor);
		alert.setCreator(msgSenderUser);
		alert.setDateCreated(new Date());
		alert.setText(body);
		//alert.markAlertRead();
		Context.getAlertService().saveAlert(alert);
	}
}
