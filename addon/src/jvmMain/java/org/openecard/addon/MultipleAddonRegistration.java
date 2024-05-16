/****************************************************************************
 * Copyright (C) 2015 ecsec GmbH.
 * All rights reserved.
 * Contact: ecsec GmbH (info@ecsec.de)
 *
 * This file is part of the Open eCard App.
 *
 * GNU General Public License Usage
 * This file may be used under the terms of the GNU General Public
 * License version 3.0 as published by the Free Software Foundation
 * and appearing in the file LICENSE.GPL included in the packaging of
 * this file. Please review the following information to ensure the
 * GNU General Public License version 3.0 requirements will be met:
 * http://www.gnu.org/copyleft/gpl.html.
 *
 * Other Usage
 * Alternatively, this file may be used in accordance with the terms
 * and conditions contained in a signed written agreement between
 * you and ecsec GmbH.
 *
 ***************************************************************************/

package org.openecard.addon;

import javax.annotation.Nonnull;
import org.openecard.addon.manifest.AddonSpecification;


/**
 * Exception indicating that an addon is already registered.
 *
 * @author Tobias Wich
 */
public class MultipleAddonRegistration extends Exception {

    private final AddonSpecification spec;

    public MultipleAddonRegistration(String msg, @Nonnull AddonSpecification spec) {
	super(msg);
	this.spec = spec;
    }

    public AddonSpecification getAddonSpec() {
	return spec;
    }

}
