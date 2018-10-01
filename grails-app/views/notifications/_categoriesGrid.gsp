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

<g:set var="updateColumn" value="column1"/>

<!-- table tag will hold our grid
    The updateColumn variable will allow us to identify whether this table
    is for showing entities (column1) or their children (column2)
-->

<table id="data_grid_${updateColumn}" class="scroll jqTable" cellpadding="0" cellspacing="0"></table>
<!-- pager will hold our paginator -->
<div id="data_grid_pager_${updateColumn}" class="scroll" style="text-align:center;"></div>

<div class="btn-box">
    <g:link controller="notifications" action="editCategory" class="submit add" params="${[add: true]}">
        <span><g:message code="button.create.notification.category"/></span>
    </g:link>

    <a href="#" onclick="return editCategory();" class="submit edit"><span>
        <g:message code="button.edit.notification.category"/></span></a>
</div>

<div id="listLink" style="display: none;">
    <g:remoteLink class="cell" action="list" params="['template': 'list', categoryId:_id_]" id="_id_" before="register(this);" onSuccess="render(data, next); animateToTheTop();">

    </g:remoteLink>
</div>

<div id="execListLink" style="display: none;">
</div>

<script type="text/javascript">// <![CDATA[
/* when the page has finished loading.. execute the follow */
var gLastSel = -1;
var jqTableGrid = $('#data_grid_${updateColumn}');
var jqTablePager = $('#data_grid_pager_${updateColumn}');
$(document).ready(function () {
    $(jqTableGrid).jqGrid({
        url:'<g:createLink action="findCategories"/>',
        datatype: "json",
        colNames:[
            '<g:message code="title.notification.category"/>'
        ],
        colModel:[
            { name: 'categoryId', editable: false, width: 150, formatter: notificationCategoryFormatter }
        ],
        sortname: 'categoryId',
        sortorder: 'desc',
        autowidth: true,
        height: 'auto',
        rowNum: 20,
        rowList: [10,20,50],
        pager: $(jqTablePager),
        viewrecords: true,
        gridview: true,
        onSelectRow: function(id){
            if(id && id!==gLastSel){
                var content = $('#listLink').clone().html().replace(/_id_/g, id);
                $("#execListLink").html(content);
                $("#execListLink > a").click();
                gLastSel=id;
            }
        }
    }).navGrid('#data_grid_pager_${updateColumn}',
            {   add:false,edit:false,del:false,search:false,refresh:true,csv:false
            }, // which buttons to show?
            // edit options
            {},
            // add options
            {},
            // delete options
            {}
    );

    $(jqTableGrid).jqGrid('filterToolbar',{autosearch:true});

});

function notificationCategoryFormatter (cellvalue, options, rowObject) {
    var categoryIdDisplay = '<em><g:message code="table.id.format" args="['_categoryId_']"/></em>'
    var content = '<div class="medium">' + rowObject.description + '</div>' + categoryIdDisplay;
    return content.replace(/_categoryId_/g, cellvalue)
}

function isRowSelected(id) {
    //console.log("id:"+id+" gLastSel:"+gLastSel);
    return gLastSel == id;
}

function animateToTheTop(){
    $('html, body').animate({ scrollTop: 0 }, 'fast');
}
// ]]></script>
