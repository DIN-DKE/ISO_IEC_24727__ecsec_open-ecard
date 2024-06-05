/****************************************************************************
 * Copyright (C) 2013-2024 ecsec GmbH.
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

package org.openecard.addon.bind


/**
 * Constants for use in the auxiliary data map.
 * Auxiliary data are open, but some of the sued keys share a common meaning. The constants here help to establish a
 * language common to both bindings and add-ons.
 *
 * @author Tobias Wich
 */
object AuxDataKeys {
    private const val PREFIX = "org.openecard.addon.bind.aux_data."

    const val REDIRECT_LOCATION: String = "${PREFIX}redirect_location"
    const val RESPONSE_HEADERS: String = "${PREFIX}response_headers"
    const val MINOR_PROCESS_RESULT: String = "${PREFIX}minor_result"
}
