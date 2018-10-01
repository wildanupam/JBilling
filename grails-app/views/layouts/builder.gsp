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


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--[if lt IE 7]>      <html xmlns="http://www.w3.org/1999/xhtml" class="lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html xmlns="http://www.w3.org/1999/xhtml" class="lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html xmlns="http://www.w3.org/1999/xhtml" class="lt-ie9"> <![endif]-->
<!--[if IE 9]>         <html xmlns="http://www.w3.org/1999/xhtml" class="ie9"> <![endif]-->
<!--[if gt IE 9]><!--> <html xmlns="http://www.w3.org/1999/xhtml" > <!--<![endif]-->
<head>
    <g:render template="/layouts/includes/head" model="[ajaxListeners: false]"/>
    <g:layoutHead/>
    <r:layoutResources/>
</head>
<body>
<div id="wrapper">
    <g:render template="/layouts/includes/header"/>

    <div id="main">
        <g:render template="/layouts/includes/breadcrumbs"/>

        <!-- optional top section -->
        <g:if test="${pageProperty(name: 'page.top')}">
            <div class="top-hold">
                <g:pageProperty name="page.top"/>
            </div>
        </g:if>

        <!-- content columns -->
        <div class="columns-holder">
            <div class="column panel">
                <div class="column-hold" id="column1">
                    <g:pageProperty name="page.builder"/>
                </div>
            </div>

            <div class="column panel">
                <div class="column-hold" id="column2">
                    <g:pageProperty name="page.review"/>
                </div>
            </div>
        </div>
    </div>
</div>

<r:layoutResources/>

</body>
</html>
