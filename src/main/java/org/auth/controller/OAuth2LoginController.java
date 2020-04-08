package org.auth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.auth.provider.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:8080/app/"})
@RestController
@RequestMapping("/login/oauth2")
public class OAuth2LoginController extends AbstractGenericController {

	@GetMapping(path = { "/callback/{registrationId}" })
	public void callback(@PathVariable(required = false) String registrationId,  Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
		logger.debug("OAuth2 Callback.");
		UserPrincipal userPrincipal = (UserPrincipal)authentication.getPrincipal();
		logger.trace("userPrincipal: "+ userPrincipal);
		String targetUrl = "http://localhost:8080/app/login/callback";
		//response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		//response.getWriter().write(userPrincipal.toString());
		//response.sendRedirect(targetUrl);
	}
	@GetMapping(path = {"/authorize/{provider}"})
	public ModelAndView  authorize(@PathVariable String provider, HttpServletRequest request, RedirectAttributes redirectAttribute) {
		String path = request.getContextPath()+"/oauth2/authorization/"+provider;
		logger.trace("Forwarding request to: "+path);
		return new ModelAndView("redirect:http://localhost:8081"+path);
	}
	@GetMapping(path = { "/error" })
	public String error(HttpServletRequest request) {
		logger.debug("Error occured.");
		request.getSession().removeAttribute("error.message");
		String message = (String) request.getSession().getAttribute("error.message");
		return "Error Message: " + message;
	}

}
