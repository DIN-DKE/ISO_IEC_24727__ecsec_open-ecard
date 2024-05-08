/****************************************************************************
 * Copyright (C) 2013 ecsec GmbH.
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

package org.openecard.crypto.tls;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import javax.annotation.Nonnull;


/**
 * Wrapper class to simulate socket for TLS in- and output streams.
 * BouncyCastle is only able to emit InputStream and OutputStream classes which represent the tunneled channel. If it is
 * desirable to operate on a socket, say because some API needs a socket, then this class can be used to bring the
 * tunneled streams and the originating socket together.
 *
 * @author Tobias Wich
 */
public class SocketWrapper extends Socket {

    private final Socket parent;
    private final InputStream in;
    private final OutputStream out;

    /**
     * Creates an instance of a SocketWrapper binding the given streams to the socket.
     * The socket must be opened and the streams must belong to the socket. The latter requirement is not checked.
     *
     * @param parent Connected socket which should be wrapped.
     * @param in Input stream belonging to the socket.
     * @param out Output stream belonging to the socket.
     * @throws IOException Thrown in case the socket is not connected.
     */
    public SocketWrapper(@Nonnull Socket parent, @Nonnull InputStream in, @Nonnull OutputStream out)
	    throws IOException {
	this.parent = parent;
	this.in = in;
	this.out = out;
	// assert that the socket is really open. An unconnected socket can not have open streams
	if (! parent.isConnected()) {
	    throw new IOException("Socket is not connected.");
	}
    }


    @Override
    public InputStream getInputStream() throws IOException {
	// call original to check if the socket is closed already
	parent.getInputStream();
	return in;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
	// call original to check if the socket is closed already
	parent.getInputStream();
	return out;
    }


    @Override
    public void bind(SocketAddress bindpoint) throws IOException {
	parent.bind(bindpoint);
    }

    @Override
    public synchronized void close() throws IOException {
	parent.close();
    }

    @Override
    public void connect(SocketAddress endpoint) throws IOException {
	parent.connect(endpoint);
    }

    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
	parent.connect(endpoint, timeout);
    }

    @Override
    public SocketChannel getChannel() {
	return parent.getChannel();
    }

    @Override
    public InetAddress getInetAddress() {
	return parent.getInetAddress();
    }

    @Override
    public boolean getKeepAlive() throws SocketException {
	return parent.getKeepAlive();
    }

    @Override
    public InetAddress getLocalAddress() {
	return parent.getLocalAddress();
    }

    @Override
    public int getLocalPort() {
	return parent.getLocalPort();
    }

    @Override
    public SocketAddress getLocalSocketAddress() {
	return parent.getLocalSocketAddress();
    }

    @Override
    public boolean getOOBInline() throws SocketException {
	return parent.getOOBInline();
    }

    @Override
    public int getPort() {
	return parent.getPort();
    }

    @Override
    public synchronized int getReceiveBufferSize() throws SocketException {
	return parent.getReceiveBufferSize();
    }

    @Override
    public SocketAddress getRemoteSocketAddress() {
	return parent.getRemoteSocketAddress();
    }

    @Override
    public boolean getReuseAddress() throws SocketException {
	return parent.getReuseAddress();
    }

    @Override
    public synchronized int getSendBufferSize() throws SocketException {
	return parent.getSendBufferSize();
    }

    @Override
    public int getSoLinger() throws SocketException {
	return parent.getSoLinger();
    }

    @Override
    public synchronized int getSoTimeout() throws SocketException {
	return parent.getSoTimeout();
    }

    @Override
    public boolean getTcpNoDelay() throws SocketException {
	return parent.getTcpNoDelay();
    }

    @Override
    public int getTrafficClass() throws SocketException {
	return parent.getTrafficClass();
    }

    @Override
    public boolean isBound() {
	return parent.isBound();
    }

    @Override
    public boolean isClosed() {
	return parent.isClosed();
    }

    @Override
    public boolean isConnected() {
	return parent.isConnected();
    }

    @Override
    public boolean isInputShutdown() {
	return parent.isInputShutdown();
    }

    @Override
    public boolean isOutputShutdown() {
	return parent.isOutputShutdown();
    }

    @Override
    public void sendUrgentData(int data) throws IOException {
	parent.sendUrgentData(data);
    }

    @Override
    public void setKeepAlive(boolean on) throws SocketException {
	parent.setKeepAlive(on);
    }

    @Override
    public void setOOBInline(boolean on) throws SocketException {
	parent.setOOBInline(on);
    }

    @Override
    public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
	parent.setPerformancePreferences(connectionTime, latency, bandwidth);
    }

    @Override
    public synchronized void setReceiveBufferSize(int size) throws SocketException {
	parent.setReceiveBufferSize(size);
    }

    @Override
    public void setReuseAddress(boolean on) throws SocketException {
	parent.setReuseAddress(on);
    }

    @Override
    public synchronized void setSendBufferSize(int size) throws SocketException {
	parent.setSendBufferSize(size);
    }

    @Override
    public void setSoLinger(boolean on, int linger) throws SocketException {
	parent.setSoLinger(on, linger);
    }

    @Override
    public synchronized void setSoTimeout(int timeout) throws SocketException {
	parent.setSoTimeout(timeout);
    }

    @Override
    public void setTcpNoDelay(boolean on) throws SocketException {
	parent.setTcpNoDelay(on);
    }

    @Override
    public void setTrafficClass(int tc) throws SocketException {
	parent.setTrafficClass(tc);
    }

    @Override
    public void shutdownInput() throws IOException {
	parent.shutdownInput();
    }

    @Override
    public void shutdownOutput() throws IOException {
	parent.shutdownOutput();
    }

    @Override
    public String toString() {
	return parent.toString();
    }

}
