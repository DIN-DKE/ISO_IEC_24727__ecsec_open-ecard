/****************************************************************************
 * Copyright (C) 2012-2017 ecsec GmbH.
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

package org.openecard.gui.definition;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * GUI component class which represents an image.
 * In order to use the component, at least an image must be set. The MIME type is optional, but may be needed by some
 * GUI implementations.
 *
 * @author Tobias Wich
 */
public final class ImageBox extends IDTrait implements InputInfoUnit {

    private static final Logger LOG = LoggerFactory.getLogger(ImageBox.class);

    private Document doc;

    /**
     * Get the raw data of the image.
     *
     * @see #setImageData(byte[])
     * @return The raw image data.
     */
    public byte[] getImageData() {
	byte[] imageData = doc.getValue();
	return Arrays.copyOf(imageData, imageData.length);
    }
    /**
     * Sets the raw image data for this instance.
     * The image must be given in the serialized form of an image container format such as PNG, JPEG, etc.
     *
     * @param imageData The raw image data.
     */
    public void setImageData(byte[] imageData) {
	this.doc.setValue(Arrays.copyOf(imageData, imageData.length));
    }

    /**
     * Gets the MIME type for the image represented by this instance.
     *
     * @see #setMimeType(java.lang.String)
     * @return String containing the MIME type.
     */
    public String getMimeType() {
	return doc.getMimeType();
    }
    /**
     * Sets the MIME type for the image represented by this instance.
     *
     * @see <a href="https://www.iana.org/assignments/media-types/">IANA Registered MIME Types</a>
     * @see <a href="https://www.iana.org/assignments/media-types/image/">IANA Registered Image MIME Types</a>
     * @param mimeType MIME type describing the image type.
     */
    public void setMimeType(String mimeType) {
	this.doc.setMimeType(mimeType);
    }

    public Document getDocument() {
	return doc;
    }

    public void setDocument(Document doc) {
	this.doc = doc;
    }


    @Override
    public InfoUnitElementType type() {
	return InfoUnitElementType.IMAGE_BOX;
    }

    @Override
    public void copyContentFrom(InfoUnit origin) {
	if (! (this.getClass().equals(origin.getClass()))) {
	    LOG.warn("Trying to copy content from type {} to type {}.", origin.getClass(), this.getClass());
	    return;
	}
	ImageBox other = (ImageBox) origin;
	// copy document
	if (other.getDocument() != null) {
	    try {
		this.setDocument(other.getDocument().clone());
	    } catch (CloneNotSupportedException ex) {
		throw new AssertionError("Clone not implemented correctly in Document class.");
	    }
	}
    }

}
