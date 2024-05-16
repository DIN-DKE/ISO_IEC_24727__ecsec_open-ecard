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

package org.openecard.binding.tctoken;

import javax.annotation.Nonnull;
import org.openecard.common.I18n;
import org.openecard.common.I18nException;
import org.openecard.common.I18nKey;


/**
 *
 * @author Tobias Wich
 */
public class ResourceException extends I18nException {

    protected static final I18n lang = I18n.getTranslation("tr03112");

    public ResourceException(@Nonnull String message) {
	super(message);
    }

    public ResourceException(@Nonnull String message, Throwable cause) {
	super(message, cause);
    }

    public ResourceException(I18nKey key, Object... params) {
	super(lang, key, params);
    }

    public ResourceException(I18nKey key, Throwable cause, Object... params) {
	super(lang, key, cause, params);
    }

}
