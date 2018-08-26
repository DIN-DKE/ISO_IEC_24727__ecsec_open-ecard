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

package org.openecard.sal.protocol.eac.gui;

import iso.std.iso_iec._24727.tech.schema.DIDAuthenticationDataType;
import iso.std.iso_iec._24727.tech.schema.EstablishChannel;
import iso.std.iso_iec._24727.tech.schema.EstablishChannelResponse;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.openecard.binding.tctoken.TR03112Keys;
import org.openecard.common.DynamicContext;
import org.openecard.common.ECardConstants;
import org.openecard.common.I18n;
import org.openecard.common.WSHelper;
import org.openecard.common.WSHelper.WSException;
import org.openecard.common.anytype.AuthDataMap;
import org.openecard.common.anytype.AuthDataResponse;
import org.openecard.common.interfaces.Dispatcher;
import org.openecard.common.util.ByteUtils;
import org.openecard.gui.StepResult;
import org.openecard.gui.definition.PasswordField;
import org.openecard.gui.executor.ExecutionResults;
import org.openecard.gui.executor.StepAction;
import org.openecard.gui.executor.StepActionResult;
import org.openecard.gui.executor.StepActionResultStatus;
import org.openecard.sal.protocol.eac.EACData;
import org.openecard.sal.protocol.eac.EACProtocol;
import org.openecard.sal.protocol.eac.anytype.PACEInputType;
import org.openecard.sal.protocol.eac.anytype.PasswordID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * StepAction for capturing the user PIN on the EAC GUI.
 *
 * @author Tobias Wich
 * @author Hans-Martin Haase
 */
public class PINStepAction extends StepAction {

    private static final Logger LOG = LoggerFactory.getLogger(PINStepAction.class);

    private static final String PIN_ID_CAN = "2";

    // Translation constants
    private static final String ERROR_CARD_REMOVED = "action.error.card.removed";
    private static final String ERROR_INTERNAL = "action.error.internal";
    private static final String ERROR_TITLE = "action.error.title";
    private static final String ERROR_UNKNOWN = "action.error.unknown";

    // did translations
    private final String pin;
    private final String puk;

    private final EACData eacData;
    private final boolean capturePin;
    private final byte[] slotHandle;
    private final Dispatcher dispatcher;
    private final PINStep step;
    private final I18n lang = I18n.getTranslation("pace");
    private final I18n langPin = I18n.getTranslation("pinplugin");
    private final DynamicContext ctx;

    private int retryCounter;

    public PINStepAction(EACData eacData, boolean capturePin, byte[] slotHandle, Dispatcher dispatcher, PINStep step,
	    EacPinStatus status) {
	super(step);
	this.eacData = eacData;
	this.capturePin = capturePin;
	this.slotHandle = slotHandle;
	this.dispatcher = dispatcher;
	this.step = step;
	this.ctx = DynamicContext.getInstance(TR03112Keys.INSTANCE_KEY);

	switch (status) {
	    case RC3:
		retryCounter = 0;
		break;
	    case RC2:
		retryCounter = 1;
		step.updateAttemptsDisplay(2);
		break;
	    case RC1:
		retryCounter = 2;
		step.updateAttemptsDisplay(1);
		if (capturePin) {
		    step.addCANEntry();
		} else {
		    step.addNativeCANNotice();
		}
		break;
	}

	// get some important translations
	pin = lang.translationForKey("pin");
	puk = lang.translationForKey("puk");
    }

    @Override
    public StepActionResult perform(Map<String, ExecutionResults> oldResults, StepResult result) {
	if (retryCounter == 2) {
	    try {
		EstablishChannelResponse response = performPACEWithCAN(oldResults);
		if (response == null) {
		    LOG.debug("The CAN does not meet the format requirements.");
		    step.setStatus(EacPinStatus.RC1);
		    return new StepActionResult(StepActionResultStatus.REPEAT);
		}

		if (response.getResult().getResultMajor().equals(ECardConstants.Major.ERROR)) {
		    if (response.getResult().getResultMinor().equals(ECardConstants.Minor.IFD.AUTHENTICATION_FAILED)) {
			LOG.error("Failed to authenticate with the given CAN.");
			step.setStatus(EacPinStatus.RC1);
			return new StepActionResult(StepActionResultStatus.REPEAT);
		    } else {
			WSHelper.checkResult(response);
		    }
		}
	    } catch (WSException ex) {
		// This is for PIN Pad Readers in case the user pressed the cancel button on the reader.
		if (ex.getResultMinor().equals(ECardConstants.Minor.IFD.CANCELLATION_BY_USER)) {
		    LOG.error("User canceled the authentication manually.", ex);
		    ctx.put(EACProtocol.PACE_EXCEPTION, ex);
		    return new StepActionResult(StepActionResultStatus.CANCEL);
		}

		// for people which think they have to remove the card in the process
		if (ex.getResultMinor().equals(ECardConstants.Minor.IFD.INVALID_SLOT_HANDLE)) {
		    LOG.error("The SlotHandle was invalid so probably the user removed the card or an reset occurred.", ex);
		    ctx.put(EACProtocol.PACE_EXCEPTION, ex);
		    return new StepActionResult(StepActionResultStatus.REPEAT, 
			    new ErrorStep(lang.translationForKey(ERROR_TITLE), langPin.translationForKey(ERROR_CARD_REMOVED)));
		}
	    }
	}

	try {
	    EstablishChannelResponse establishChannelResponse = performPACEWithPIN(oldResults);

	    if (establishChannelResponse.getResult().getResultMajor().equals(ECardConstants.Major.ERROR)) {
		if (establishChannelResponse.getResult().getResultMinor().equals(ECardConstants.Minor.IFD.PASSWORD_ERROR)) {
		    // increase counters and the related displays
		    retryCounter++;
		    step.updateAttemptsDisplay(3 - retryCounter);
		    // repeat the step
		    LOG.info("Wrong PIN entered, trying again (try number {}).", retryCounter);
		    this.step.setStatus(EacPinStatus.RC2);
		    return new StepActionResult(StepActionResultStatus.REPEAT);
		} else if (establishChannelResponse.getResult().getResultMinor().equals(ECardConstants.Minor.IFD.PASSWORD_SUSPENDED)) {
		    // increase counters and the related displays
		    retryCounter++;
		    step.updateAttemptsDisplay(3 - retryCounter);
		    LOG.info("Wrong PIN entered, trying again (try number {}).", retryCounter);
		    step.setStatus(EacPinStatus.RC1);

		    if (capturePin) {
			step.addCANEntry();
		    } else {
			step.addNativeCANNotice();
		    }
		    return new StepActionResult(StepActionResultStatus.REPEAT);
		} else if (establishChannelResponse.getResult().getResultMinor().equals(ECardConstants.Minor.IFD.PASSWORD_BLOCKED)) {
		    LOG.warn("Wrong PIN entered. The PIN is blocked.");
		    ctx.put(EACProtocol.PIN_STATUS, EacPinStatus.BLOCKED);
		    return new StepActionResult(StepActionResultStatus.REPEAT,
			    new ErrorStep(lang.translationForKey("step_error_title_blocked", pin),
				    lang.translationForKey("step_error_pin_blocked", pin, pin, puk, pin),
				    WSHelper.createException(establishChannelResponse.getResult())));

		} else {
		    WSHelper.checkResult(establishChannelResponse);
		}
	    }

	    eacData.paceResponse = establishChannelResponse;
	    // PACE completed successfully, proceed with next step
	    ctx.put(EACProtocol.PACE_EXCEPTION, null);
	    return new StepActionResult(StepActionResultStatus.NEXT);
	} catch (WSException ex) {
	    // This is for PIN Pad Readers in case the user pressed the cancel button on the reader.
	    if (ex.getResultMinor().equals(ECardConstants.Minor.IFD.CANCELLATION_BY_USER)) {
		LOG.error("User canceled the authentication manually.", ex);
		ctx.put(EACProtocol.PACE_EXCEPTION, ex);
		return new StepActionResult(StepActionResultStatus.CANCEL);
	    }

	    // for people which think they have to remove the card in the process
	    if (ex.getResultMinor().equals(ECardConstants.Minor.IFD.INVALID_SLOT_HANDLE)) {
		LOG.error("The SlotHandle was invalid so probably the user removed the card or an reset occurred.", ex);
		return new StepActionResult(StepActionResultStatus.REPEAT,
			new ErrorStep(lang.translationForKey(ERROR_TITLE),
				langPin.translationForKey(ERROR_CARD_REMOVED), ex));
	    }

	    // repeat the step
	    LOG.error("An unknown error occured while trying to verify the PIN.");
	    return new StepActionResult(StepActionResultStatus.REPEAT,
		    new ErrorStep(langPin.translationForKey(ERROR_TITLE),
			    langPin.translationForKey(ERROR_UNKNOWN), ex));
	}
    }

    private EstablishChannelResponse performPACEWithPIN(Map<String, ExecutionResults> oldResults) {
	DIDAuthenticationDataType protoData = eacData.didRequest.getAuthenticationProtocolData();
	AuthDataMap paceAuthMap;
	try {
	    paceAuthMap = new AuthDataMap(protoData);
	} catch (ParserConfigurationException ex) {
	    LOG.error("Failed to read EAC Protocol data.", ex);
	    return null;
	}
	AuthDataResponse paceInputMap = paceAuthMap.createResponse(protoData);

	if (capturePin) {
	    ExecutionResults executionResults = oldResults.get(getStepID());
	    PasswordField p = (PasswordField) executionResults.getResult(PINStep.PIN_FIELD);
	    char[] pinIn = p.getValue();
	    // let the user enter the pin again, when there is none entered
	    // TODO: check pin length and possibly allowed charset with CardInfo file
	    if (pinIn.length == 0) {
		return null;
	    } else {
		// NOTE: saving pin as string prevents later removal of the value from memory !!!
		paceInputMap.addElement(PACEInputType.PIN, new String(pinIn));
	    }
	}

	// perform PACE
	paceInputMap.addElement(PACEInputType.PIN_ID, PasswordID.parse(eacData.pinID).getByteAsString());
	paceInputMap.addElement(PACEInputType.CHAT, eacData.selectedCHAT.toString());
	String certDesc = ByteUtils.toHexString(eacData.rawCertificateDescription);
	paceInputMap.addElement(PACEInputType.CERTIFICATE_DESCRIPTION, certDesc);
	EstablishChannel eChannel = createEstablishChannelStructure(paceInputMap);
	return (EstablishChannelResponse) dispatcher.safeDeliver(eChannel);
    }

    private EstablishChannelResponse performPACEWithCAN(Map<String, ExecutionResults> oldResults) {
	DIDAuthenticationDataType paceInput = new DIDAuthenticationDataType();
	paceInput.setProtocol(ECardConstants.Protocol.PACE);
	AuthDataMap tmp;
	try {
	    tmp = new AuthDataMap(paceInput);
	} catch (ParserConfigurationException ex) {
	    LOG.error("Failed to read empty Protocol data.", ex);
	    return null;
	}

	AuthDataResponse paceInputMap = tmp.createResponse(paceInput);
	if (capturePin) {
	    ExecutionResults executionResults = oldResults.get(getStepID());
	    PasswordField canField = (PasswordField) executionResults.getResult(PINStep.CAN_FIELD);
	    String canValue = new String(canField.getValue());

	    if (canValue.length() != 6) {
		// let the user enter the can again, when input verification failed
		return null;
	    } else {
		paceInputMap.addElement(PACEInputType.PIN, canValue);
	    }
	}
	paceInputMap.addElement(PACEInputType.PIN_ID, PIN_ID_CAN);

	// perform PACE by EstablishChannelCommand
	EstablishChannel eChannel = createEstablishChannelStructure(paceInputMap);
	return (EstablishChannelResponse) dispatcher.safeDeliver(eChannel);
    }

    private EstablishChannel createEstablishChannelStructure(AuthDataResponse paceInputMap) {
	// EstablishChannel
	EstablishChannel establishChannel = new EstablishChannel();
	establishChannel.setSlotHandle(slotHandle);
	establishChannel.setAuthenticationProtocolData(paceInputMap.getResponse());
	establishChannel.getAuthenticationProtocolData().setProtocol(ECardConstants.Protocol.PACE);
	return establishChannel;
    }

}
