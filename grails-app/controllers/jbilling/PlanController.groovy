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

package jbilling

import com.sapienter.jbilling.server.util.IWebServicesSessionBean
import grails.plugin.springsecurity.annotation.Secured

/**
 * PlanController
 *
 * @author Brian Cowdery
 * @since 01-Feb-2011
 */
@Secured(["isAuthenticated()"])
class PlanController {

    static pagination = [ max: 10, offset: 0 ]

    IWebServicesSessionBean webServicesSession
    def viewUtils
    def filterService
    def breadcrumbService

    def index = {
        // show enterprise-only feature page
        flash.info = 'plan.community.disabled'
    }
}
