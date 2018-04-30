/****************************************************************************
 * Copyright (C) 2016-2017 ecsec GmbH.
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

package org.openecard.common.event;

import iso.std.iso_iec._24727.tech.schema.ConnectionHandleType;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


/**
 *
 * @author Tobias Wich
 */
public class IfdEventObject extends EventObject {

    private final String ifaceProtocol;

    // TODO: move this class to IFD package, but this needs refactoring of StateMap as well

    public IfdEventObject(@Nonnull ConnectionHandleType handle) {
	this(handle, null);
    }

    public IfdEventObject(@Nonnull ConnectionHandleType handle, @Nullable String ifaceProtocol) {
	super(handle);
	this.ifaceProtocol = ifaceProtocol;
    }

    @Override
    @Nonnull
    @SuppressWarnings("null")
    public ConnectionHandleType getHandle() {
	return super.getHandle();
    }

    @Nullable
    public String getIfaceProtocol() {
	return ifaceProtocol;
    }

}
