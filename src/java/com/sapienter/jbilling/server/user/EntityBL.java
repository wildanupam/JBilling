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

package com.sapienter.jbilling.server.user;

import java.sql.SQLException;
import java.util.Locale;
import java.util.List;

import javax.naming.NamingException;
import javax.sql.rowset.CachedRowSet;

import com.sapienter.jbilling.common.SessionInternalError;
import com.sapienter.jbilling.server.list.ResultList;
import com.sapienter.jbilling.server.user.contact.db.ContactDAS;
import com.sapienter.jbilling.server.user.contact.db.ContactDTO;
import com.sapienter.jbilling.server.user.db.CompanyDAS;
import com.sapienter.jbilling.server.user.db.CompanyDTO;
import com.sapienter.jbilling.server.user.permisson.db.RoleDAS;
import com.sapienter.jbilling.server.user.permisson.db.RoleDTO;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.audit.EventLogger;
import com.sapienter.jbilling.server.util.db.CurrencyDAS;
import com.sapienter.jbilling.server.util.db.LanguageDAS;
import com.sapienter.jbilling.server.util.db.LanguageDTO;

import java.util.ArrayList;

/**
 * @author Emil
 */
public class EntityBL extends ResultList 
        implements EntitySQL {
    private CompanyDAS das = null;
    private CompanyDTO entity = null;
    private EventLogger eLogger = null;
    
    public EntityBL()  {
        init();
    }
    
    public EntityBL(Integer id)  {
        init();
        entity = das.find(id);
    }

    /*
    public EntityBL(String externalId) 
            throws FinderException, NamingException {
        init();
        entity = entityHome.findByExternalId(externalId);
    }
    */
    
    public static final CompanyWS getCompanyWS(CompanyDTO companyDto) {
    	
    	CompanyWS ws = new CompanyWS();
        ws.setId(companyDto.getId());
        ws.setCurrencyId(companyDto.getCurrencyId());
        ws.setLanguageId(companyDto.getLanguageId());
        ws.setDescription(companyDto.getDescription());

        ContactDTO contact = new EntityBL(new Integer(ws.getId())).getContact();

        if (contact != null) {
            ws.setContact(new ContactWS(contact.getId(),
                                         contact.getAddress1(),
                                         contact.getAddress2(),
                                         contact.getCity(),
                                         contact.getStateProvince(),
                                         contact.getPostalCode(),
                                         contact.getCountryCode(),
                                         contact.getDeleted()));
        }
        return ws;
    }
    
    public static final  CompanyDTO getDTO(CompanyWS ws){
        CompanyDTO dto = new CompanyDAS().find(new Integer(ws.getId()));
        dto.setCurrency(new CurrencyDAS().find(ws.getCurrencyId()));
        dto.setLanguage(new LanguageDAS().find(ws.getLanguageId()));
        dto.setDescription(ws.getDescription());
        return dto;
    }
    
    
    private void init() {
        das = new CompanyDAS();
        eLogger = EventLogger.getInstance();
    }
    
    public CompanyDTO getEntity() {
        return entity;
    }
    
    public Locale getLocale()  {
        Locale retValue = null;
        // get the language first
        Integer languageId = entity.getLanguageId();
        LanguageDTO language = new LanguageDAS().find(languageId);
        String languageCode = language.getCode();
        
        // now the country
        ContactBL contact = new ContactBL();
        contact.setEntity(entity.getId());
        String countryCode = contact.getEntity().getCountryCode();
        
        if (countryCode != null) {
            retValue = new Locale(languageCode, countryCode);
        } else {
            retValue = new Locale(languageCode);
        }

        return retValue;
    }

    public ContactDTO getContact() {
        //get company contact
        ContactBL contact = new ContactBL();
        contact.setEntity(entity.getId());
        return contact.getEntity();
    }
    
    public Integer[] getAllIDs() 
            throws SQLException, NamingException {
        List list = new ArrayList();
        
        prepareStatement(EntitySQL.listAll);
        execute();
        conn.close();
        
        while (cachedResults.next()) {
            list.add(new Integer(cachedResults.getInt(1)));
        } 
        
        Integer[] retValue = new Integer[list.size()];
        list.toArray(retValue);
        return retValue;
    }
    
    public CachedRowSet getTables() 
            throws SQLException, NamingException {
        prepareStatement(EntitySQL.getTables);
        execute();
        conn.close();
        
        return cachedResults;
    }
    
    public Integer getRootUser(Integer entityId) {
        try {
        	RoleDTO rootRole = new RoleDAS().findByRoleTypeIdAndCompanyId(Constants.TYPE_ROOT, entityId);
            prepareStatement(EntitySQL.findRoot);
            cachedResults.setInt(1, entityId);
            cachedResults.setInt(2, rootRole.getId());

            execute();
            conn.close();
            
            cachedResults.next();
            return cachedResults.getInt(1);
        } catch (Exception e) {
            throw new SessionInternalError("Finding root user for entity " + 
                    entity.getId(), EntityBL.class, e);
        } 
    }
    
    public void updateEntityAndContact(CompanyWS companyWS, Integer entityId, Integer userId) {
        CompanyDTO dto= EntityBL.getDTO(companyWS);
            ContactWS contactWs= companyWS.getContact();
            ContactBL contactBl= new ContactBL();
            contactBl.setEntity(entityId);
            ContactDTO contact= contactBl.getEntity();
            contact.setAddress1(contactWs.getAddress1());
            contact.setAddress2(contactWs.getAddress2());
            contact.setCity(contactWs.getCity());
            contact.setCountryCode(contactWs.getCountryCode());
            contact.setPostalCode(contactWs.getPostalCode());
            contact.setStateProvince(contactWs.getStateProvince());
            contact.setCountryCode(contactWs.getCountryCode());
            new ContactDAS().save(contact);
            eLogger.auditBySystem(entityId,
                    userId, Constants.TABLE_CONTACT,
                    contact.getId(),
                    EventLogger.MODULE_WEBSERVICES,
                    EventLogger.ROW_UPDATED, null, null, null);
        new CompanyDAS().save(dto);
        eLogger.auditBySystem(entityId,
                userId, Constants.TABLE_ENTITY,
                entityId,
                EventLogger.MODULE_WEBSERVICES,
                EventLogger.ROW_UPDATED, null, null, null);
    }
}
