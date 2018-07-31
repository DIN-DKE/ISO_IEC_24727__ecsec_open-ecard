/****************************************************************************
 * Copyright (C) 2016 ecsec GmbH.
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

package org.openecard.control.binding.http.interceptor;

import java.io.IOException;
import org.openecard.apache.http.HttpException;
import org.openecard.apache.http.HttpResponse;
import org.openecard.apache.http.HttpResponseInterceptor;
import org.openecard.apache.http.protocol.HttpContext;


/**
 * HttpResponseInterceptor implementation which adds security related headers to all responses sent by the HTTP Binding.
 *
 * @author Tobias Wich
 */
public class SecurityHeaderResponseInterceptor implements HttpResponseInterceptor {

    @Override
    public void process(HttpResponse hr, HttpContext hc) throws HttpException, IOException {
	hr.addHeader("X-XSS-Protection", "1");
	hr.addHeader("Content-Security-Policy", "default-src 'none'; script-src 'none'; style-src 'self'; img-src 'self'");
	hr.addHeader("X-Content-Type-Options", "nosniff");
	hr.addHeader("X-Frame-Options",  "SAMEORIGIN");
    }

}
