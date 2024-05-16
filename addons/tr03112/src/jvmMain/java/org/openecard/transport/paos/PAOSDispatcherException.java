/****************************************************************************
 * Copyright (C) 2014 ecsec GmbH.
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

package org.openecard.transport.paos;

import org.openecard.common.I18n;
import org.openecard.common.I18nKey;
import org.openecard.common.interfaces.DispatcherException;


/**
 * Localized DispatcherException.
 *
 * @author Tobias Wich
 */
public class PAOSDispatcherException extends DispatcherException {

    private static final long serialVersionUID = 1L;
    private static final I18n lang = I18n.getTranslation("tr03112");

    /**
     * Creates an instance and initializes the exception with a localized message.
     *
     * @param key Translation key.
     * @param params Parameters adding values into the translation.
     */
    public PAOSDispatcherException(I18nKey key, Object... params) {
	super(lang, key, params);
    }

    /**
     * Creates an instance and initializes the exception with a localized message.
     *
     * @param key Translation key.
     * @param cause The exception causing the error.
     * @param params Parameters adding values into the translation.
     */
    public PAOSDispatcherException(I18nKey key, Throwable cause, Object... params) {
	super(lang, key, cause, params);
    }

}
