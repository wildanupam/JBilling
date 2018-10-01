%{--
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
 
  --}%
<%@ page import="com.sapienter.jbilling.server.util.db.CurrencyDTO; com.sapienter.jbilling.server.util.db.CountryDTO; com.sapienter.jbilling.server.user.db.CompanyDTO; com.sapienter.jbilling.server.user.permisson.db.RoleDTO; com.sapienter.jbilling.common.Constants; com.sapienter.jbilling.server.util.db.LanguageDTO" %>
<html>
<head>
    <meta name="layout" content="public" />
    <title><g:message code="signup.page.title"/></title>
    
    <r:script disposition="head">
	
		function replacePhoneCountryCodePlusSign(phoneCountryCode) {
		
			document.getElementById('contact.phoneCountryCode').value = document.getElementById('contact.phoneCountryCode1').value; 
		
			if (null != phoneCountryCode && phoneCountryCode.trim() != '') {
				if (phoneCountryCode.indexOf('+') == 0) {
					phoneCountryCode = phoneCountryCode.replace('+', '');
				}
				document.getElementById('contact.phoneCountryCode').value = phoneCountryCode; 
			}
		}

    </r:script>

</head>
<body>
    <g:render template="/layouts/includes/messages"/>

    <div class="form-edit">
        <div class="heading">
            <strong>
                <g:message code="signup.title"/>
            </strong>
        </div>

        <div class="form-hold">
            <g:form name="company-edit-form" action="save">
                <fieldset>
                    <div class="form-columns">

                        <!-- admin user column -->
                        <div class="column">
                            <g:applyLayout name="form/input">
                                <content tag="label"><g:message code="prompt.login.name"/><span id="mandatory-meta-field">*</span></content>
                                <content tag="label.for">user.userName</content>
                                <g:textField class="field" name="user.userName" value="${params['user.userName']}"/>
                            </g:applyLayout>

                            <g:applyLayout name="form/input">
                                <content tag="label"><g:message code="prompt.first.name"/><span id="mandatory-meta-field">*</span></content>
                                <content tag="label.for">contact.firstName</content>
                                <g:textField class="field" name="contact.firstName" value="${params['contact.firstName']}" />
                            </g:applyLayout>

                            <g:applyLayout name="form/input">
                                <content tag="label"><g:message code="prompt.last.name"/><span id="mandatory-meta-field">*</span></content>
                                <content tag="label.for">contact.lastName</content>
                                <g:textField class="field" name="contact.lastName" value="${params['contact.lastName']}" />
                            </g:applyLayout>

                            <g:applyLayout name="form/text">
                                <content tag="label"><g:message code="prompt.phone.number"/><span id="mandatory-meta-field">*</span></content>
                                <content tag="label.for">contact.phoneCountryCode</content>
                                <span>
                                    <g:textField class="field" id="contact.phoneCountryCode1" name="contact.phoneCountryCode1" value="${params['contact.phoneCountryCode']}" maxlength="3" size="2"/>
                                    -
                                    <g:textField class="field" name="contact.phoneAreaCode" value="${params['contact.phoneAreaCode']}" maxlength="5" size="3"/>
                                    -
                                    <g:textField class="field" name="contact.phoneNumber" value="${params['contact.phoneNumber']}" maxlength="10" size="8"/>
                                    <g:hiddenField id="contact.phoneCountryCode" name="contact.phoneCountryCode" value="${params['contact.phoneCountryCode']}" />
                                </span>
                            </g:applyLayout>

                            <g:applyLayout name="form/input">
                                <content tag="label"><g:message code="prompt.email"/><span id="mandatory-meta-field">*</span></content>
                                <content tag="label.for">contact.email</content>
                                <g:textField class="field" name="contact.email" value="${params['contact.email']}" />
                            </g:applyLayout>

                            <g:applyLayout name="form/select">
                                <content tag="label"><g:message code="prompt.user.language"/></content>
                                <content tag="label.for">languageId</content>
                                <g:select name="languageId"
                                          from="${LanguageDTO.list()}"
                                          optionKey="id"
                                          optionValue="description"
                                          value="${params['languageId']}"  />
                            </g:applyLayout>

                            <g:applyLayout name="form/select">
                                <content tag="label"><g:message code="prompt.user.currency"/></content>
                                <content tag="label.for">currencyId</content>
                                <g:select name="currencyId"
                                          from="${CurrencyDTO.list()}"
                                          optionKey="id"
                                          optionValue="description"
                                          value="${params['currencyId']}" />
                            </g:applyLayout>
                        </div>

                        <!-- company information column -->
                        <div class="column">
                            <sec:ifLoggedIn>
                            	<%
                                def loggedInUserCompany = CompanyDTO.load(sec.loggedInUserInfo(field: 'companyId').toInteger())
                            	%>
                        		<g:applyLayout name="form/text">
                            		<content tag="label"><g:message code="prompt.organization.parent"/></content>
                                	<h>${loggedInUserCompany?.description}</h>
                        		</g:applyLayout>
                            </sec:ifLoggedIn>
                            
                            <g:applyLayout name="form/input">
                                <content tag="label"><g:message code="prompt.organization.name"/><span id="mandatory-meta-field">*</span></content>
                                <content tag="label.for">contact.organizationName</content>
                                <g:textField class="field" name="contact.organizationName" value="${params['contact.organizationName']}" />
                            </g:applyLayout>
                            
                            <sec:ifLoggedIn>
                            	<g:applyLayout name="form/checkbox">
                            		<content tag="label"><g:message code="prompt.organization.invoice.as.reseller"/></content>
                            		<content tag="label.for">contact.invoiceAsReseller</content>
                            		<g:checkBox class="cb checkbox" name="contact.invoiceAsReseller" value="${params['contact.invoiceAsReseller']}"/>
                        		</g:applyLayout>
                        	</sec:ifLoggedIn>

                            <g:applyLayout name="form/input">
                                <content tag="label"><g:message code="prompt.address1"/><span id="mandatory-meta-field">*</span></content>
                                <content tag="label.for">contact.address1</content>
                                <g:textField class="field" name="contact.address1" value="${params['contact.address1']}" />
                            </g:applyLayout>

                            <g:applyLayout name="form/input">
                                <content tag="label"><g:message code="prompt.address2"/></content>
                                <content tag="label.for">contact.address2</content>
                                <g:textField class="field" name="contact.address2" value="${params['contact.address2']}" />
                            </g:applyLayout>

                            <g:applyLayout name="form/input">
                                <content tag="label"><g:message code="prompt.city"/></content>
                                <content tag="label.for">contact.city</content>
                                <g:textField class="field" name="contact.city" value="${params['contact.city']}" />
                            </g:applyLayout>

                            <g:applyLayout name="form/input">
                                <content tag="label"><g:message code="prompt.state"/><span id="mandatory-meta-field">*</span></content>
                                <content tag="label.for">contact.stateProvince</content>
                                <g:textField class="field" name="contact.stateProvince" value="${params['contact.stateProvince']}" />
                            </g:applyLayout>

                            <g:applyLayout name="form/select">
                                <content tag="label"><g:message code="prompt.country"/><span id="mandatory-meta-field">*</span></content>
                                <content tag="label.for">contact.countryCode</content>

                                <g:select name="contact.countryCode"
                                          from="${CountryDTO.list()}"
                                          optionKey="code"
                                          optionValue="description"
                                          noSelection="['': message(code: 'default.no.selection')]"
                                          value="${params['contact.countryCode']}"/>
                            </g:applyLayout>

                            <g:applyLayout name="form/input">
                                <content tag="label"><g:message code="prompt.zip"/><span id="mandatory-meta-field">*</span></content>
                                <content tag="label.for">contact.postalCode</content>
                                <g:textField class="field" name="contact.postalCode" value="${params['contact.postalCode']}" />
                            </g:applyLayout>
                        </div>
                    </div>

                    <!-- spacer -->
                    <div>
                        <br/>&nbsp;
                    </div>

                    <div class="buttons">
                        <ul>
                            <li>
                                <a onclick="replacePhoneCountryCodePlusSign(document.getElementById('contact.phoneCountryCode1').value); $('#company-edit-form').submit()" class="submit save"><span><g:message code="button.save"/></span></a>
                            </li>
                            <li>
                                <g:link controller="login" class="submit cancel"><span><g:message code="button.cancel"/></span></g:link>
                            </li>
                        </ul>
                    </div>

                </fieldset>
            </g:form>
        </div>
    </div>
</body>
</html>
