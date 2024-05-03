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
 * ERASE RECORD command.
 * See ISO/IEC 7816-4 Section 7.3.8.
 *
 * @author Hans-Martin Haase
 */
public class EraseRecord extends CardCommandAPDU {

    /**
     * Instruction byte for the ERASE RECORD command according to ISO 7816 part 4.
     */
    private static final byte ERASE_RECORD_INS = (byte) 0x0C;


    /**
     * P2 value for erasing the record with the record number from P1.
     */
    public static final byte ERASE_JUST_P1 = (byte) 0x04;

    /**
     * P2 value for erasing the records from P1 up to the last record.
     */
    public static final byte ERASE_P1_UP_TO_LAST = (byte) 0x05;

    /**
     * Creates a new ERASE RECORD APDU.
     * APDU: 0x00 0x0C P1 P2 - - -
     *
     * @param p1 A record number.
     * @param p2 Indicates whether just erase the record with the record number from P1 or erase all record from P1 to
     * the last. There are two public variables in this class for setting p2.
     */
    public EraseRecord(byte p1, byte p2) {
	super(x00, ERASE_RECORD_INS, p1, p2);
    }

    /**
     * Creates a new ERASE RECORD APDU.
     * APDU: 0x00 0x0C P1 P2 - - -
     *
     * @param p1 A record number.
     * @param p2EraseBehavior Indicates whether just erase the record with the record number from P1 or delete all
     * record from P1 to the last. There are two public variables in this class for setting p2.
     * @param shortFID A short file identifier for the file which contains the record to erase.
     */
    public EraseRecord(byte p1, byte p2EraseBehavior, byte shortFID) {
	super(x00, ERASE_RECORD_INS, p1, (byte) (((byte) shortFID * 8) + p2EraseBehavior));
    }
    
}
