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

package org.openecard.binding.tctoken.ex;

import org.openecard.common.I18nKey;


/**
 * Exception indicating a security violation.
 * This can be a violation of the same origin policy or something similar.
 *
 * @author Tobias Wich
 */
public class SecurityViolationException extends RedirectionBaseError {

    public SecurityViolationException(String errorUrl, String msg) {
	super(errorUrl, msg);
    }

    public SecurityViolationException(String errorUrl, String msg, Throwable ex) {
	super(errorUrl, msg, ex);
    }

    public SecurityViolationException(String errorUrl, I18nKey key, Object... params) {
	super(errorUrl, key, params);
    }

    public SecurityViolationException(String errorUrl, I18nKey key, Throwable cause, Object... params) {
	super(errorUrl, key, cause, params);
    }

}
