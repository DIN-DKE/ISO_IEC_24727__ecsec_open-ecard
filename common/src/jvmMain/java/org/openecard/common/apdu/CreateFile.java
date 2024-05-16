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

package org.openecard.common.apdu;

import org.openecard.common.apdu.common.CardCommandAPDU;


/**
 * CREATE FILE Command.
 * See ISO/IEC 7816-9 Section 9.2.3.
 *
 * @author Hans-Martin Haase
 */
public class CreateFile extends CardCommandAPDU {

    /**
     * Instruction byte for the CREATE FILE command.
     */
    private static final byte CREATE_FILE_INS = (byte) 0xE0;

    /**
     * Creates a new CREATE FILE APDU.
     * APDU: 0x00 0XE0 0x00 0x00 Lc metaData
     *
     * @param metaData The metaData variable has to contain a FCP template with tag 0x62 or it is empty and the file
     * gets default file control parameters.
     */
    public CreateFile(byte[] metaData) {
	super(x00, CREATE_FILE_INS, (byte) 0x00, (byte) 0x00, metaData);
    }

    /**
     * Creates a new CREATE FILE APDU.
     * APDU: 0x00 0xE0 fileDescriptorByte p2 metaData
     *
     * p2 is constructed from the shortEfFid variable.
     *
     * @param fileDescriptorByte File descriptor byte which indicates at least whether the new file is an DF or an EF.
     * @param shortEfFid A short EF identifier for the file to create.
     * @param metaData The metaData variable has to contain a FCP template with tag 0x62 or it is empty and the file
     * gets default file control parameters.
     */
    public CreateFile(byte fileDescriptorByte, byte shortEfFid, byte[] metaData) {
	super(x00, CREATE_FILE_INS, fileDescriptorByte, (byte) (shortEfFid * 8), metaData);
    }

    /**
     * Creates a new CREATE FILE APDU.
     * APDU: 0x00 0xE0 fileDescriptorByte p2 metaData
     *
     * p2 is created from the shortEfFid variable and the proprietaryPartOfP2 variable.
     *
     * @param fileDescriptorByte File descriptor byte which indicates at least whether the new file is an DF or an EF.
     * @param shortEfFid A short EF identifier for the file to create.
     * @param proprietaryPartOfP2 3 least significant bits which encode proprietary commands/behavior.
     * @param metaData The metaData variable has to contain a FCP template with tag 0x62 or it is empty and the file
     * gets default file control parameters.
     */
    public CreateFile(byte fileDescriptorByte, byte shortEfFid, byte proprietaryPartOfP2, byte[] metaData) {
	super(x00, CREATE_FILE_INS, fileDescriptorByte, (byte) ((shortEfFid * 8) + proprietaryPartOfP2), metaData);
    }
}
