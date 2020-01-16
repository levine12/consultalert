/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openmrs.module.consultalert.page.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.openmrs.module.appframework.domain.Extension;
import org.openmrs.module.appframework.service.AppFrameworkService;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author levine
 */
public class HelloWorldPageController {
	
	public void controller(HttpServletRequest request, PageModel model,
	        @SpringBean("appFrameworkService") AppFrameworkService appFrameworkService, HttpSession session) {
		
		getExtensionPoints(appFrameworkService);
	}
	
	public String post(HttpSession session, HttpServletRequest request,
	        @RequestParam(value = "specialtyTypeName", required = false) String specialtyTypeName) {
		return "redirect:" + "template/helloWorld.page";
		
	}
	
	private void getExtensionPoints(AppFrameworkService appFrameworkService) {
		List<Extension> exts = appFrameworkService.getAllEnabledExtensions();
		System.out.println("EXTENSIONS");
		for (Extension ext : exts) {
			System.out.println(ext.getExtensionPointId());
		}
	}
}
