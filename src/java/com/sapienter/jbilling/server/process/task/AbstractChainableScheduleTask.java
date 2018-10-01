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

package com.sapienter.jbilling.server.process.task;


import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import org.quartz.Trigger;

/**
 * This abstract class is used to create tasks to be executed in a chain.
 * No parameter is required, all the configuration must be done in the first task of the chain,
 * and it shouldn't extend from this class.
 *
 * @author Oscar Bidabehere
 * @since 03-09-2012
 */
public abstract class AbstractChainableScheduleTask extends ScheduledTask {
    @Override
    public String getScheduleString () {
        return null;
    }

    public Trigger getTrigger () throws PluggableTaskException {
        return null;
    }
}
