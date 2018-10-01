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

package com.sapienter.jbilling.server.process;

import java.util.*;
import org.apache.log4j.Logger;


import com.sapienter.jbilling.common.FormatLogger;
import com.sapienter.jbilling.server.process.db.*;
import com.sapienter.jbilling.server.user.db.UserDAS;
import com.sapienter.jbilling.server.user.db.UserDTO;
import com.sapienter.jbilling.server.list.ResultList;

public class BillingProcessFailedUserBL  extends ResultList {
	private BillingProcessFailedUserDAS bpFailedUsersDas = null;
    private BatchProcessInfoDAS processInfoDas = null;
    private UserDAS userDas = null;
    private UserDTO user = null;
    private BatchProcessInfoDTO processInfo = null;
    private BillingProcessFailedUserDTO bPFailedUsers = null;
    
    public BillingProcessFailedUserBL(Integer processInfoId) {
        init();
        set(processInfoId);
    }
    
    public BillingProcessFailedUserBL() {
        init();
    }
    
    private void init() {
       userDas = new UserDAS();
       processInfoDas = new BatchProcessInfoDAS();
       bpFailedUsersDas = new BillingProcessFailedUserDAS();
    }

    public BillingProcessFailedUserDTO getEntity() {
        return bPFailedUsers;
    }
    
    public void set(Integer id) {
        bPFailedUsers = bpFailedUsersDas.find(id);
    }
    
    public void create(Integer batchProcessId, Integer userId) {
    	user = userDas.find(userId);
    	processInfo = processInfoDas.find(batchProcessId);
    	bpFailedUsersDas.create(processInfo, user);
    }
    
    public List<BillingProcessFailedUserDTO> findByBatchProcessId (Integer batchProcessId) {
    	List<BillingProcessFailedUserDTO> list = bpFailedUsersDas.getEntitiesByBatchProcessId(batchProcessId);
    	if(list !=null) {
    		return list;
    	}
    	return null;
    }
    
    public List<UserDTO> getUsersByExecutionId (Integer executionId) {
    	List<UserDTO> users = new ArrayList<UserDTO>();
    	for(BillingProcessFailedUserDTO dto : bpFailedUsersDas.getEntitiesByExecutionId(executionId)) {
    		users.add(dto.getUser());
    	}
    	return users;
    }
}
