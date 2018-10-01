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


package com.sapienter.jbilling.server.invoice.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sapienter.jbilling.server.invoice.InvoiceLineComparator;
import org.apache.log4j.Logger;

import com.sapienter.jbilling.common.FormatLogger;
import com.sapienter.jbilling.common.Util;
import com.sapienter.jbilling.server.invoice.NewInvoiceEvent;
import com.sapienter.jbilling.server.invoice.db.InvoiceDTO;
import com.sapienter.jbilling.server.invoice.db.InvoiceLineDTO;
import com.sapienter.jbilling.server.pluggableTask.PluggableTask;
import com.sapienter.jbilling.server.pluggableTask.admin.ParameterDescription;
import com.sapienter.jbilling.server.pluggableTask.admin.PluggableTaskException;
import com.sapienter.jbilling.server.system.event.Event;
import com.sapienter.jbilling.server.system.event.task.IInternalEventsTask;
import com.sapienter.jbilling.server.user.ContactBL;
import com.sapienter.jbilling.server.user.UserBL;
import org.joda.time.format.DateTimeFormat;

/**
 *
 * @author emilc
 */
public class FileInvoiceExportTask extends PluggableTask implements IInternalEventsTask {
    
    private static final Class<Event> events[] = new Class[] { NewInvoiceEvent.class };

    private static final FormatLogger LOG = new FormatLogger(Logger.getLogger(FileInvoiceExportTask.class));

    // Required parameters
    private static final ParameterDescription PARAMETER_FILE = new ParameterDescription("file", true, ParameterDescription.Type.STR);

    { 
        descriptions.add(PARAMETER_FILE);
    }
    
    public void process(Event event) throws PluggableTaskException {
        NewInvoiceEvent myEvent = (NewInvoiceEvent) event;
        if (myEvent.getInvoice().getIsReview() != null && myEvent.getInvoice().getIsReview() == 1) {
            return;
        }

        LOG.debug("Exporting invoice %s", myEvent.getInvoice().getId());

        // get filename
        String filename = (String) parameters.get(PARAMETER_FILE.getName());
        if (!(new File(filename)).isAbsolute()) {
            // prepend the default directory if file path is relative
            String defaultDir = Util.getSysProp("base_dir");
            filename = defaultDir + filename;
        }

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
            List<InvoiceLineDTO> ordInvoiceLines = new ArrayList<InvoiceLineDTO>(myEvent.getInvoice().getInvoiceLines());
            Collections.sort(ordInvoiceLines, new InvoiceLineComparator());
            for (InvoiceLineDTO line : ordInvoiceLines) {
                out.write(composeLine(myEvent.getInvoice(), line, myEvent.getUserId()));
                out.newLine();
            }
            out.close();
        } catch (IOException e) {
            LOG.error("Can not write invoice to export file", e);
            throw new PluggableTaskException("Can not write invoice to export file" + e.getMessage());
        }

    }

    public Class<Event>[] getSubscribedEvents() {
        return events;
    }

    private String composeLine(InvoiceDTO invoice, InvoiceLineDTO invoiceLine, Integer userId) {
        StringBuffer line = new StringBuffer();
        ContactBL contact = new ContactBL();
        contact.set(userId);

        // cono                                                         
        line.append("\"" + emptyIfNull(contact.getEntity().getPostalCode()) + "\"");
        line.append(',');
        // custno
        line.append("\"" + userId + "\"");
        line.append(',');
        // naddrcode
        line.append("\"" + "000" + "\"");
        line.append(',');
        // lookupnm
        line.append("\"" + emptyIfNull(contact.getEntity().getOrganizationName()) + "\"");
        line.append(',');
        // totallineamt
        line.append("\"" + invoiceLine.getAmount() + "\"");
        line.append(',');
        // period
        line.append("\"" + DateTimeFormat.forPattern("yyyyMM").print(invoice.getCreateDatetime().getTime()) + "\"");
        line.append(',');
        // name
        line.append("\"" + emptyIfNull(contact.getEntity().getOrganizationName()) + "\"");
        line.append(',');
        // deliveryaddr
        line.append("\"" + emptyIfNull(contact.getEntity().getAddress1()) + "\"");
        line.append(',');
        // city
        line.append("\"" + emptyIfNull(contact.getEntity().getCity()) + "\"");
        line.append(',');
        // state
        line.append("\"" + emptyIfNull(contact.getEntity().getStateProvince()) + "\"");
        line.append(',');
        // zip5
        line.append("\"" + emptyIfNull(contact.getEntity().getPostalCode()) + "\"");
        line.append(',');
        // totdue - round to two decimals
        line.append("\"" + UserBL.getBalance(userId).round(new MathContext(2)) + "\"");
        line.append(',');
        // qty
        line.append("\"" + invoiceLine.getQuantity() + "\"");
        line.append(',');
        // description
        line.append("\"" + invoiceLine.getDescription() + "\"");
        line.append(',');
        // invoiceno
        line.append("\"" + invoice.getNumber() + "\"");
        line.append(',');
        // custstatus
        line.append("\"" + "TRUE" + "\"");

        LOG.debug("Line to export: %s", line);
        return line.toString();
    }

    private String emptyIfNull(String str) {
        if (str == null) {
            return "";
        } else {
            return str;
        }
    }

}
