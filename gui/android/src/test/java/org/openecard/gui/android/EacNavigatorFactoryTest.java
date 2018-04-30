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

package org.openecard.gui.android;

import java.util.Arrays;
import java.util.List;
import mockit.Expectations;
import mockit.Mocked;
import org.openecard.gui.UserConsentNavigator;
import org.openecard.gui.android.eac.EacNavigator;
import org.openecard.gui.definition.Step;
import org.openecard.gui.definition.UserConsentDescription;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;


/**
 *
 * @author Neil Crossley
 */
public class EacNavigatorFactoryTest {
    
    @Mocked
    UserConsentDescription ucd;

    @Test
    public void testGivenCorrectValuesThenCreateFromShouldCreateCorrectInstance() {
	final List<Step> expectedSteps = createInitialSteps();
	new Expectations() {{
	    ucd.getDialogType(); result = "EAC";
	    ucd.getSteps(); result = expectedSteps;
	}};

	EacNavigatorFactory sut = new EacNavigatorFactory();
	final UserConsentNavigator result = sut.createFrom(ucd);

	assertEquals(result.getClass(), EacNavigator.class);
    }
    
    @Test
    public void testGivenCorrectValuesThenCreateFromShouldBeCorrect() {
	final List<Step> expectedSteps = createInitialSteps();
	new Expectations() {{
	    ucd.getDialogType(); result = "EAC";
	    ucd.getSteps(); result = expectedSteps;
	    
	}};
	
	EacNavigatorFactory sut = new EacNavigatorFactory();
	final UserConsentNavigator result = sut.createFrom(ucd);
	
	assertNotNull(result);
	assertTrue(result.hasNext());
    }

    private List<Step> createInitialSteps() {
	return Arrays.asList(new Step("Some text"), new Step("Some text"), new Step("Some text"), new Step("Some text"));
    }
    
}
