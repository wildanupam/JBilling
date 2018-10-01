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

package com.sapienter.jbilling.server.order.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * IsNotEmptyOrDeleted
 * This annotation checks to see if the order lines list is empty and if it's not it checks to see if every element on
 * the list is deleted. In either case it returns <b>false</b>, otherwise <b>true</b>.
 *
 * @author Juan Vidal
 * @since 04/01/2012
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsNotEmptyOrDeletedValidator.class)
@Documented

public @interface IsNotEmptyOrDeleted {

    String message() default "validation.error.empty.lines";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
