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

<%@ page import="com.sapienter.jbilling.server.util.db.CurrencyDTO; com.sapienter.jbilling.server.util.db.LanguageDTO;" %>

<%--
  Editor form for price model attributes.

  This template is not the same as the attribute UI in the plan builder. The plan builder
  uses remote AJAX calls that can only be used in a web-flow. This template is to be used
  for standard .gsp pages.

  @author Brian Cowdery
  @since  02-Feb-2011
--%>

<script type="text/javascript">
    function addNewProductDescription(){
        var languageId = $('#newDescriptionLanguage').val();
        var previousDescription = $("#descriptions div:hidden .descLanguage[value='"+languageId+"']");
        if(previousDescription.size()){
            previousDescription.parents('.row:first').show();
            previousDescription.parents('.row:first').find(".descDeleted").val(false);
            previousDescription.parents('.row:first').find(".descContent").val('');
        }else{
            var languageDescription = $('#newDescriptionLanguage option:selected').text();
            var clone = $('#descriptionClone').children().clone();
            var languagesCount = $('#descriptions').children().size();
            var newName = '${itemName}.descriptions['+languagesCount+']';
            clone.find("label").attr('for', newName+'.content');
            var label = clone.find('label').html();
            clone.find('label').html(label.replace('{0}', languageDescription));
            if(languageDescription=="English"){
                clone.find('label').append("<span id='mandatory-meta-field'>*</span>");
            }

            clone.find(".descContent").attr('id',newName+'.content');
            clone.find(".descContent").attr('name',newName+'.content');

            clone.find(".descLanguage").attr('id',newName+'.languageId');
            clone.find(".descLanguage").attr('name',newName+'.languageId');
            clone.find(".descLanguage").val(languageId);

            clone.find(".descDeleted").attr('id',newName+'.deleted');
            clone.find(".descDeleted").attr('name',newName+'.deleted');

            $('#descriptions').append(clone);
        }
        if(languageId==1){
            $('#newDescriptionLanguage').closest("div").find("label span").remove();
        }
        removeProductSelectedLanguage();
        updateItemWithDescriptions();
    }

    function removeProductDescription(elm){
        var div = $(elm).parents('.row:first');
        //set 'deleted'=true;
        div.find('.descDeleted').val(true);
        div.hide();

        if($("#addDescription").is(':hidden')){
            $("#addDescription").show();
        }
        var langId = div.find(".descLanguage").val();
        var langValue = getValueForLangId(langId);
        if(langValue){
            $("#newDescriptionLanguage").append("<option value='"+langId+"'>"+langValue+"</option>");
            if(langId==1){
                $("#newDescriptionLanguage").closest("div").find('label').append(
                        "<span id='mandatory-meta-field'>*</span>");
            }
        }
        updateItemWithDescriptions();
    }

    function loadAvailableDecLang(){
        var languages = $('#availableDescriptionLanguages').val().split(',');
        if(languages[0]!=''){
            $.each(languages,function(i,lang){
                var lang = lang.split('-');
                $("#newDescriptionLanguage").append("<option value='"+lang[0]+"'>"+lang[1]+"</option>");
                if(lang[0]==1){
                    $("#newDescriptionLanguage").closest("div").find('label').append("<span id='mandatory-meta-field'>*</span>");
                }
            });
        }else{
            $('#addDescription').hide();
        }
    }

    function getValueForLangId(langId){
        var languages = $('#allDescriptionLanguages').val().split(',')
        if(languages[0]!=''){
            var value = false;
            $.each(languages,function(i,lang){
                var lang = lang.split('-');
                if(lang[0] == langId){
                    value = lang[1];
                }
            });
            return value;
        }else{
            return false;
        }
        return false;
    }

    function removeProductSelectedLanguage(){
        $('#newDescriptionLanguage option:selected').remove();
        if(!$('#newDescriptionLanguage option').size()){
            $('#addDescription').hide();
        }
    }


    function getSelectValues(select) {
        var result = [];
        var options = select && select.options;
        var opt;

        for (var i=0, iLen=options.length; i!=iLen; i++) {
            opt = options[i];

            if (opt.selected) {
                result.push(opt.value || opt.text);
                result.push(",")
            }
        }
        return result;
    }

    $(document).ready(function() {
        loadAvailableDecLang();
    })

    function updateItemWithDescriptions() {
        $('#${form_to_submit_on_update}').submit();
    }

</script>
<div class="row" id='addDescription'>
    <div class="add-desc">
        <label><g:message code='product.detail.description.add.title'/></label>
        <select name="newDescriptionLanguage" id="newDescriptionLanguage"></select>
        <a onclick="addNewProductDescription()">
            <img src="${resource(dir:'images', file:'add.png')}" alt="remove"/>
        </a>
    </div>
</div>

<div id="descriptionClone" style="display: none">
    <g:applyLayout name="form/description">
        <content tag="label"><g:message code="product.detail.description.label"/></content>
        <content tag="label.for">desCloneContent</content>

        <input type="text" id="desCloneContent" class="descContent field" size="26" value="" name="desCloneContent" onchange="updateItemWithDescriptions()">
        <input type="hidden" id="desCloneLangId" class="descLanguage" value="" name="desCloneLangId">
        <input type="hidden" id="desCloneDeleted" class="descDeleted" value="" name="desCloneDeleted">
        <a onclick="removeProductDescription(this)">
            <img src="${resource(dir:'images', file:'cross.png')}" alt="remove"/>
        </a>
    </g:applyLayout>
</div>
<g:set var="availableDescriptionLanguages" value="${LanguageDTO.list().collect {it.id+'-'+it.description}}"></g:set>

<div id="descriptions">
    <g:each in="${product?.descriptions}" var="description" status="index">
        <g:if test="${description?.languageId}">
            <g:applyLayout name="form/description">
                <g:set var="currentLang" value="${LanguageDTO.get(product?.descriptions[index]?.languageId)}"></g:set>
                <g:set var="availableDescriptionLanguages" value="${availableDescriptionLanguages - (currentLang?.id+'-'+currentLang?.description)}"></g:set>
                <content tag="label"><g:message code="product.detail.description.label" args="${[currentLang?.description]}"/>
                    <g:if test="${description?.languageId==1}">
                        <span id="mandatory-meta-field">*</span>
                    </g:if>
                </content>

                <content tag="label.for">product?.descriptions[${index}]?.content</content>

                <g:textField name="${itemName}.descriptions[${index}].content" class="descContent field" value="${product?.descriptions[index]?.content}" onchange="updateItemWithDescriptions()"/>
                <g:hiddenField name="${itemName}.descriptions[${index}].languageId" class="descLanguage" value="${currentLang?.id}"/>
                <g:hiddenField name="${itemName}.descriptions[${index}].deleted" value="" class="descDeleted"/>
                <a onclick="removeProductDescription(this)">
                    <img src="${resource(dir:'images', file:'cross.png')}" alt="remove"/>
                </a>
            </g:applyLayout>
        </g:if>
    </g:each>
</div>

<g:set var="allDescriptionLanguages" value="${LanguageDTO.list().collect {it.id+'-'+it.description}}"></g:set>
<g:hiddenField name="allDescriptionLanguages" value="${allDescriptionLanguages?.join(',')}"/>
<g:hiddenField name="availableDescriptionLanguages" value="${availableDescriptionLanguages?.join(',')}"/>

