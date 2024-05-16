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

package org.openecard.addons.cg.ex;

import org.openecard.addon.bind.AuxDataKeys;
import org.openecard.addon.bind.BindingResult;
import org.openecard.addon.bind.BindingResultCode;
import org.openecard.common.I18nKey;


/**
 * Exception indicating that a redirect of the caller will be performed.
 *
 * @author Tobias Wich
 */
public abstract class RedirectionBaseError extends ActivationError {

    public RedirectionBaseError(String errorUrl, String msg) {
	super(makeBindingResult(errorUrl), msg);
    }

    public RedirectionBaseError(String errorUrl, String msg, Throwable ex) {
	super(makeBindingResult(errorUrl), msg, ex);
    }

    public RedirectionBaseError(String errorUrl, Throwable ex) {
        super(makeBindingResult(errorUrl), ex);
    }

    public RedirectionBaseError(String errorUrl, I18nKey key, Object... params) {
	super(makeBindingResult(errorUrl), key, params);
    }

    public RedirectionBaseError(String errorUrl, I18nKey key, Throwable cause, Object... params) {
	super(makeBindingResult(errorUrl), key, cause, params);
    }

    private static BindingResult makeBindingResult(String errorUrl) {
	BindingResult result = new BindingResult(BindingResultCode.REDIRECT);
	return result.addAuxResultData(AuxDataKeys.REDIRECT_LOCATION, errorUrl);
    }

}
