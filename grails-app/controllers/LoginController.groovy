/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

 This file is part of jbilling.

 jbilling is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 jbilling is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

import grails.converters.JSON

import grails.plugin.springsecurity.SpringSecurityUtils

import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.context.SecurityContextHolder as SCH
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import com.sapienter.jbilling.server.user.db.CompanyDTO
import com.sapienter.jbilling.client.authentication.exception.LicenseMissingException
import com.sapienter.jbilling.client.authentication.exception.LicenseExpiredException
import com.sapienter.jbilling.client.authentication.exception.LicenseInvalidException
import com.sapienter.jbilling.client.authentication.CompanyUserDetails
import com.sapienter.jbilling.server.user.db.UserDTO
import com.sapienter.jbilling.server.util.IWebServicesSessionBean

class LoginController {

	IWebServicesSessionBean webServicesSession
	static scope = "singleton"

	/**
	 * Dependency injection for the authenticationTrustResolver.
	 */
	def authenticationTrustResolver

	/**
	 * Dependency injection for the springSecurityService.
	 */
	def springSecurityService

	/**
	 * Default action; redirects to 'defaultTargetUrl' if logged in, /login/auth otherwise.
	 */
	def index () {
		if (springSecurityService.isLoggedIn()) {
			redirect uri: SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
		}
		else {
			redirect action: 'auth', params: params
		}
	}

	/**
	 * Show the login page.
	 */
	def auth () {

		def config = SpringSecurityUtils.securityConfig

		if (springSecurityService.isLoggedIn()) {
			redirect uri: config.successHandler.defaultTargetUrl
			return
		}

        def companies = CompanyDTO.list()

		String view = 'auth'
		String postUrl = "${request.contextPath}${config.apf.filterProcessesUrl}"
		render view: view, model: [postUrl: postUrl,
		                           rememberMeParameter: config.rememberMe.parameter,
                                   companies: companies]
	}

	/**
	 * Show denied page.
	 */
	def denied () {
		if (springSecurityService.isLoggedIn() &&
				authenticationTrustResolver.isRememberMe(SCH.context?.authentication)) {
			// have cookie but the page is guarded with IS_AUTHENTICATED_FULLY
			redirect action: 'full', params: params
		}
	}

	/**
	 * Login page for users with a remember-me cookie but accessing a IS_AUTHENTICATED_FULLY page.
	 */
	def full () {
		def config = SpringSecurityUtils.securityConfig
		render view: 'auth', params: params,
			model: [hasCookie: authenticationTrustResolver.isRememberMe(SCH.context?.authentication),
			        postUrl: "${request.contextPath}${config.apf.filterProcessesUrl}"]
	}

	/**
	 * Callback after a failed login. Redirects to the auth page with a warning message.
	 */
	def authfail () {

		def username = session[UsernamePasswordAuthenticationFilter.SPRING_SECURITY_LAST_USERNAME_KEY]
		String msg = ''
		def exception = session[AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY]
		System.out.println("Exception ******************" +exception);
		if (exception) {
			if (exception instanceof AccountExpiredException) {
                msg = g.message(code: "springSecurity.errors.login.inactive")
			}
			else if (exception instanceof CredentialsExpiredException) {
				msg = g.message(code: "springSecurity.errors.login.passwordExpired")
				flash.error = msg
				redirect url: '/resetPassword/resetExpiryPassword'
				return
			}
			else if (exception instanceof DisabledException) {
                msg = g.message(code: "springSecurity.errors.login.disabled")
			}
			else if (exception instanceof LockedException) {
                msg = g.message(code: "springSecurity.errors.login.locked.temporary")
			}
            else if (exception instanceof LicenseMissingException) {
                msg = 'auth.fail.license.missing.exception'
            }
            else if (exception instanceof LicenseInvalidException) {
                msg = 'auth.fail.license.invalid.exception'
            }
            else if (exception instanceof LicenseExpiredException) {
                msg = 'auth.fail.license.expired.exception'
            }
			else {
                msg = g.message(code: "springSecurity.errors.login.fail")
			}
		}

		if (springSecurityService.isAjax(request)) {

			render([error: msg] as JSON)
		}
		else {
			flash.error = msg
			redirect action: 'auth', params: params
		}
	}

	/**
	 * The Ajax success redirect url.
	 */
	def ajaxSuccess () {
		render([success: true, username: springSecurityService.authentication.name] as JSON)
	}

	/**
	 * The Ajax denied redirect url.
	 */
	def ajaxDenied () {
		render([error: 'access denied'] as JSON)
	}
}
