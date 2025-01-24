/****************************************************************************
 * Copyright (C) 2012-2018 ecsec GmbH.
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

package org.openecard.transport.dispatcher;

import java.util.List;
import org.openecard.common.event.EventObject;
import org.openecard.common.event.EventType;
import org.openecard.common.interfaces.CIFProvider;
import org.openecard.common.interfaces.CardRecognition;
import org.openecard.common.interfaces.Dispatchable;
import org.openecard.common.interfaces.Dispatcher;
import org.openecard.common.interfaces.Environment;
import org.openecard.common.interfaces.EventCallback;
import org.openecard.common.interfaces.EventDispatcher;
import org.openecard.common.interfaces.EventFilter;
import org.openecard.common.interfaces.SalSelector;
import org.openecard.gui.UserConsent;
import org.openecard.ws.IFD;
import org.openecard.ws.Management;
import org.openecard.ws.SAL;


/**
 * Test environment.
 * The getIFD method has the Dispatchable annotation.
 *
 * @author Tobias Wich
 */
public class TestEnv1 implements Environment {

    private IFD ifd;

    @Override
    public void setIfd(IFD ifd) {
	this.ifd = ifd;
    }

    @Override
    @Dispatchable(interfaceClass = IFD.class)
    public IFD getIfd() {
	return ifd;
    }

    @Override
    public void setSal(SAL sal) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SAL getSal() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setEventDispatcher(EventDispatcher manager) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public EventDispatcher getEventDispatcher() {
	return new EventDispatcher() {
	    @Override
	    public void start() {
	    }

	    @Override
	    public void terminate() {
	    }

	    @Override
	    public EventCallback add(EventCallback cb) {
		throw new UnsupportedOperationException("Not supported yet.");
	    }

	    @Override
	    public EventCallback add(EventCallback cb, EventType... eventTypes) {
		throw new UnsupportedOperationException("Not supported yet.");
	    }

	    @Override
	    public EventCallback add(EventCallback cb, EventFilter filter) {
		throw new UnsupportedOperationException("Not supported yet.");
	    }

	    @Override
	    public EventCallback del(EventCallback cb) {
		throw new UnsupportedOperationException("Not supported yet.");
	    }
	    @Override
	    public void notify(EventType t, EventObject o) {
	    }
	};
    }

    @Override
    public void setDispatcher(Dispatcher dispatcher) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Dispatcher getDispatcher() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setGenericComponent(String id, Object component) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getGenericComponent(String id) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setManagement(Management m) {
	throw new UnsupportedOperationException("Not supported yet.");

    }

    @Override
    public Management getManagement() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRecognition(CardRecognition recognition) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CardRecognition getRecognition() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCifProvider(CIFProvider provider) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CIFProvider getCifProvider() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSalSelector(SalSelector salSelect) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SalSelector getSalSelector() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addIfdCtx(byte[] ctx) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeIfdCtx(byte[] ctx) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<byte[]> getIfdCtx() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setGui(UserConsent gui) {
	throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UserConsent getGui() {
	throw new UnsupportedOperationException("Not supported yet.");
    }

}
