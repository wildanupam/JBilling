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
package com.sapienter.jbilling.server.process.db;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.util.Constants;
import com.sapienter.jbilling.server.util.db.AbstractDAS;

public class BillingProcessFailedUserDAS extends AbstractDAS<BillingProcessFailedUserDTO> {

    //private static final FormatLogger LOG = new FormatLogger(Logger.getLogger(ProcessRunDAS.class));

	public BillingProcessFailedUserDTO create(BatchProcessInfoDTO batchProcessDTO, UserDTO userDTO) {
		BillingProcessFailedUserDTO dto = new BillingProcessFailedUserDTO();
		dto.setBatchProcess(batchProcessDTO);
		dto.setUser(userDTO);
        dto = save(dto);
        return dto;
    }
	
	 public List<BillingProcessFailedUserDTO> getEntitiesByBatchProcessId(Integer entityId) {
	        final String hql =
	            "select a " +
	            "  from BillingProcessFailedUserDTO a " +
	            " where a.batchProcess.id = :entity " +
	            " order by a.id desc ";
	       
	        Query query = getSession().createQuery(hql);
	        query.setParameter("entity", entityId);
	        return (List<BillingProcessFailedUserDTO>) query.list();
	}
	 
	public void removeFailedUsersForBatchProcess(Integer batchProcessId) {
	        String hql = "DELETE FROM " + BillingProcessFailedUserDTO.class.getSimpleName() +
	                " WHERE batchProcess.id = :batchProcessId";
	        Query query = getSession().createQuery(hql);
	        query.setParameter("batchProcessId", batchProcessId);
	        query.executeUpdate();
	}
	
	public List<BillingProcessFailedUserDTO> getEntitiesByExecutionId(Integer executionId) {
		   final String hql =
	            "select a " +
	            "  from BillingProcessFailedUserDTO a " +
	            " where a.batchProcess.jobExecutionId = :entity " +
	            " order by a.id desc ";
	       
	        Query query = getSession().createQuery(hql);
	        query.setParameter("entity", executionId);
	        return (List<BillingProcessFailedUserDTO>) query.list();
	}
}
