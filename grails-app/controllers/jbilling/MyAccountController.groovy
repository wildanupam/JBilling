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

import com.sapienter.jbilling.common.Constants
import grails.plugin.springsecurity.annotation.Secured

@Secured(["isFullyAuthenticated()"])
class MyAccountController {
	static scope = "prototype"
    def breadcrumbService

    def index () {
        breadcrumbService.addBreadcrumb("myAccount", "index", null, null)

        if(session['main_role_id'] == Constants.TYPE_CUSTOMER) {
            flash.altView = '/myAccount/showCustomer'
            chain controller: "customer", action: "show", id:session['user_id']
        } else {
            flash.altView = '/myAccount/showUser'
            chain controller: "user", action:"show", id:session['user_id']
        }
    }

    def editUser () {
        flash.altView = '/myAccount/editUser'
        chain controller: "user", action:"edit", id:session['user_id']
    }

}
