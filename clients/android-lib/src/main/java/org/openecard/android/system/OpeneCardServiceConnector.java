/****************************************************************************
 * Copyright (C) 2017 ecsec GmbH.
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

package org.openecard.android.system;

import org.openecard.android.ServiceResponse;
import org.openecard.android.ServiceResponseStatusCodes;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import org.openecard.android.OpeneCardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Mike Prechtl
 */
public class OpeneCardServiceConnector {

    private static final Logger LOG = LoggerFactory.getLogger(OpeneCardServiceConnector.class);

    private ConnectionHandler connectionHandler;

    public static OpeneCardServiceConnector connection;

    private final Context ctx;

    private OpeneCardService mService;
    private boolean isConnected = false;
    private boolean isServiceBinded = false;

    private OpeneCardServiceConnector(Context ctx) {
	this.ctx = ctx;
    }

    public static OpeneCardServiceConnector createConnection(Context ctx) {
	synchronized (OpeneCardServiceConnector.class) {
	    if (connection == null) {
		connection = new OpeneCardServiceConnector(ctx);
	    }
	}
	return connection;
    }

    public synchronized void setConnectionHandler(ConnectionHandler handler) {
	connectionHandler = handler;
    }

    ///
    /// Service connect and disconnect
    ///

    private final ServiceConnection serviceConnection = new ServiceConnection() {
	@Override
	public void onServiceConnected(ComponentName componentName, IBinder service) {
	    LOG.info("Service binded!");
	    mService = OpeneCardService.Stub.asInterface(service);
	    startOpeneCardService();
	    isServiceBinded = true;
	}

	@Override
	public void onServiceDisconnected(ComponentName componentName) {
	    mService = null;
	    stopOpeneCardService();
	    isServiceBinded = false;
	}
    };

    ///
    /// Build App responses
    ///

    private ServiceErrorResponse buildErrorResponse(Exception ex) {
	return new ServiceErrorResponse(ServiceResponseStatusCodes.INTERNAL_ERROR, ex.getMessage());
    }

    ///
    /// Public methods
    ///

    public synchronized void startService() {
	if (isServiceBinded && ! isConnected) {
	    startOpeneCardService();
	} else if (! isServiceBinded) {
	    Intent i = createOpeneCardIntent();
	    LOG.info("Starting service…");
	    ctx.startService(i);
	    LOG.info("Binding service…");
	    ctx.bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
	} else if (isServiceBinded && isConnected) {
	    connectionHandler.onConnectionSuccess();
	}
    }

    public synchronized void stopService() {
	if (isServiceBinded) {
	    Intent i = createOpeneCardIntent();
	    ctx.stopService(i);
	    ctx.unbindService(serviceConnection);
	} else {
	    throw new IllegalStateException("Service already stopped...");
	}
    }

    public synchronized boolean isConnected() {
	return isConnected;
    }

    private Intent createOpeneCardIntent() {
	return new Intent(ctx, OpeneCardServiceImpl.class);
    }

    private void startOpeneCardService() {
	OpeneCardContext.getContext().setApplicationContext(ctx);
	try {
	    ServiceResponse response = mService.start();
	    isConnected = false;
	    switch (response.getResponseLevel()) {
		case INFO:
		    isConnected = true;
		    connectionHandler.onConnectionSuccess();
		    break;
		case WARNING:
		    connectionHandler.onConnectionFailure((ServiceWarningResponse) response);
		    break;
		case ERROR:
		    connectionHandler.onConnectionFailure((ServiceErrorResponse) response);
		    break;
		default:
		    break;
	    }
	} catch (RemoteException ex) {
	    connectionHandler.onConnectionFailure(buildErrorResponse(ex));
	}
    }

    private void stopOpeneCardService() {
	try {
	    ServiceResponse disconnectResponse = mService.stop();
	    isConnected = true;
	    switch (disconnectResponse.getResponseLevel()) {
		case INFO:
		    isConnected = false;
		    connectionHandler.onDisconnectionSuccess();
		    break;
		case WARNING:
		    connectionHandler.onDisconnectionFailure((ServiceWarningResponse) disconnectResponse);
		    break;
		case ERROR:
		    connectionHandler.onDisconnectionFailure((ServiceErrorResponse) disconnectResponse);
		    break;
		default:
		    break;
	    }
	} catch (RemoteException ex) {
	    connectionHandler.onDisconnectionFailure(buildErrorResponse(ex));
	}
    }

}
