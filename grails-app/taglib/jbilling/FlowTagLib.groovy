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

/**
 * This can be used together with the FlowHelper to create alternate flows (view/redirects/templates) through your controller.
 * The taglib will put additional hidden inputs into your html form based on property values passed to the page.
 * The FlowHelper must then be used in the controller to change for a change in flow.
 *
 * As an example /myAccount/editUser.gsp includes /user/_editForm.gsp and passes values for altFailureView and altRedirect
 * The form submits to UserController.save which will change it's normal flow with the help of the FlowHelper
 */
class FlowTagLib {

	static namespace = "jB"

	def flow = { attrs, body ->
        ['altSuccessView', 'altFailureView', 'altView', 'altSuccessTemplate', 'altFailureTemplate', 'altTemplate', 'altChain', 'altRedirect'].each {
            String value = jB.property([name: it])
            if(value) out << "<input type='hidden' name='"+it+"' value='"+value+"'/>"
        }
	}
}
