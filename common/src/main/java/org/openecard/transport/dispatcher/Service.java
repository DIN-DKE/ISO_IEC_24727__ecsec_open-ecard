/****************************************************************************
 * Copyright (C) 2012-2014 ecsec GmbH.
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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import javax.xml.transform.TransformerException;
import org.openecard.common.interfaces.DispatcherException;
import org.openecard.common.interfaces.Publish;
import org.openecard.ws.ECardApiMethod;
import org.openecard.ws.marshal.WSMarshaller;
import org.openecard.ws.marshal.WSMarshallerException;
import org.openecard.ws.marshal.WSMarshallerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Service class encapsulating one webservice for the {@link MessageDispatcher}.
 * This class takes care of the actual interface analysis and reflection part.
 *
 * @author Tobias Wich
 */
class Service implements Comparable<Service> {

    private static final Logger LOG = LoggerFactory.getLogger(Service.class);

    private final Class<?> iface;
    private final Class<?> impl;
    private final ArrayList<Class<?>> requestClasses;
    private final TreeMap<String, Method> requestMethods;
    private final HashMap<Class<?>, MessageLogger> objectLoggers;
    private final List<String> actions;
    private final boolean isFilter;

    /**
     * Creates a new Service instance and initializes it with the given webservice interface class.
     *
     * @param iface The webservice interface class.
     */
    public Service(Class<?> iface, Class<?> impl) {
	this(iface, impl, false);
    }

    protected Service(Class<?> iface, Class<?> impl, boolean isFilter) {
	this.iface = iface;
	this.impl = impl;

	requestClasses = new ArrayList<>();
	requestMethods = new TreeMap<>();
	objectLoggers = new HashMap<>();
	actions = new ArrayList<>();
	this.isFilter = isFilter;

	init();
    }

    private void init() {
	Method[] methods = impl.getDeclaredMethods();
	for (Method m : methods) {
	    ECardApiMethod webAnnotation = getAnnotation(m, ECardApiMethod.class);
	    if (isReqParam(m) &&  webAnnotation != null) {
		Class<?> reqClass = getReqParamClass(m);
		if (requestMethods.containsKey(reqClass.getName())) {
		    String msg = "Omitting method {} in service interface {}, because its parameter type is ";
		    msg += "already associated with another method.";
		    LOG.warn(msg, m.getName(), impl.getName());
		} else {
		    String action = webAnnotation.action();
		    if (isFilter) {
			if (getAnnotation(m, Publish.class) != null) {
			    requestClasses.add(reqClass);
			    requestMethods.put(reqClass.getName(), m);
			    actions.add(action);
			}
		    } else {
			requestClasses.add(reqClass);
			requestMethods.put(reqClass.getName(), m);
			actions.add(action);
		    }
		}
	    }
	}
    }

    private static <A extends Annotation> A getAnnotation(Method m, final Class<? extends A> aClass) {
	// direct lookup
	A a = m.getAnnotation(aClass);
	if (a != null) {
	    return a;
	} else {
	    // try interfaces and superclass
	    List<Class<?>> children = new ArrayList<>();
	    Class<?> declaringClass = m.getDeclaringClass();
	    // find all interfaces and the super class
	    children.addAll(Arrays.asList(declaringClass.getInterfaces()));
	    if (declaringClass.getSuperclass() != null) {
		children.add(declaringClass.getSuperclass());
	    }

	    // try to find annotation in any of the childs
	    for (Class<?> c : children) {
		try {
		    m = c.getDeclaredMethod(m.getName(), m.getParameterTypes());
		} catch (NoSuchMethodException | SecurityException ex) {
		    continue;
		}
		a = getAnnotation(m, aClass);
		if (a != null) {
		    return a;
		}
	    }
	    // nothing found
	    return null;
	}
    }


    /**
     * Gets the webservice interface class this instance is initialized with.
     *
     * @return The webservice interface belonging to this instance.
     */
    public Class<?> getServiceInterface() {
	return iface;
    }

    /**
     * Gets the logger for the given object.
     * This method creates a new logger if none is present yet. After the logger is created, always the same logger is
     * returned. This method is thread safe.
     *
     * @param ifaceImpl Implementation for which the logger is requested.
     * @return The requested logger.
     */
    private MessageLogger getLogger(Object ifaceImpl) {
	Class<?> implClass = ifaceImpl.getClass();
	if (objectLoggers.containsKey(implClass)) {
	    return objectLoggers.get(implClass);
	} else {
	    synchronized (this) {
		MessageLogger implLogger = new MessageLogger(ifaceImpl.getClass());
		objectLoggers.put(implClass, implLogger);
		return implLogger;
	    }
	}
    }

    /**
     * Invokes the webservice method related to the request object in the given webservice class instance.
     *
     * @param ifaceImpl The instance implementing the webservice interface this instance is responsible for.
     * @param req The request object to dispatch.
     * @return The result of the method invocation.
     * @throws DispatcherException In case an error happens in the reflections part of the dispatcher.
     * @throws InvocationTargetException In case the dispatched method throws en exception.
     */
    public Object invoke(Object ifaceImpl, Object req) throws DispatcherException, InvocationTargetException {
	try {
	    MessageLogger l = getLogger(ifaceImpl);
	    Class<?> reqClass = req.getClass();
	    Method m = getMethod(reqClass.getName());
	    // invoke method
	    l.logRequest(req);
	    Object res = m.invoke(ifaceImpl, req);
	    l.logResponse(res);
	    return res;
	} catch (IllegalAccessException | NoSuchMethodException | IllegalArgumentException ex) {
	    throw new DispatcherException(ex);
	}
    }


    private Class<?> getReqParamClass(Method m) {
	// get parameters of this method
	Class[] params = m.getParameterTypes();
	// methods must have exactly one parameter
	if (params.length != 1) {
	    return null;
	}
	// TODO: add other checks

	return params[0];
    }

    private boolean isReqParam(Method m) {
	return getReqParamClass(m) != null;
    }

    public List<Class<?>> getRequestClasses() {
	return Collections.unmodifiableList(requestClasses);
    }

    private Method getMethod(String paramClass) throws NoSuchMethodException {
	Method m = requestMethods.get(paramClass);
	if (m == null) {
	    String msg = "Method containing parameter with class '" + paramClass + "' does not exist in interface '";
	    msg += iface.getName() + "'.";
	    throw new NoSuchMethodException(msg);
	}
	return m;
    }

    /**
     * Get a list with all the action names of this service.
     *
     * @return An unmodifiable list containing all the action names of this service.
     */
    protected List<String> getActionList() {
	return Collections.unmodifiableList(actions);
    }


    /**
     * Internal logger class for request and response objects.
     * It only logs
     */
    private class MessageLogger {

	private final Logger l;

	private final String reqLogMsg;
	private final String resLogMsg;

	public MessageLogger(Class<?> receiverClass) {
	    this.l = LoggerFactory.getLogger(receiverClass);

	    this.reqLogMsg = String.format("Delivering request object to %s:", receiverClass.getName());
	    this.resLogMsg = "Returning response object:";
	}

	public void logRequest(Object msgObj) {
	    logObject(l, reqLogMsg, msgObj);
	}
	public void logResponse(Object msgObj) {
	    logObject(l, resLogMsg, msgObj);
	}

	private void logObject(Logger l, String msg, Object msgObj) {
	    try {
		if (l.isTraceEnabled()) {
		    WSMarshaller m = WSMarshallerFactory.createInstance();
		    String msgObjStr = m.doc2str(m.marshal(msgObj));
		    l.trace("{}\n{}", msg, msgObjStr);
		} else if (LOG.isTraceEnabled()) {
		    // check if the message needs to be logged in the dispatcher class
		    WSMarshaller m = WSMarshallerFactory.createInstance();
		    String msgObjStr = m.doc2str(m.marshal(msgObj));
		    LOG.trace("{}\n{}", msg, msgObjStr);
		}
	    } catch (TransformerException | WSMarshallerException ex) {
		LOG.error("Failed to log message.", ex);
	    }
	}

    }

    @Override
    public int compareTo(Service o) {
	return this.iface.toString().compareTo(o.iface.toString());
    }

}
