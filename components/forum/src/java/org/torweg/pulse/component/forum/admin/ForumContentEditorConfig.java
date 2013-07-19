/*
 * Copyright 2013 :torweg free software group
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.torweg.pulse.component.forum.admin;

import java.io.File;

import org.jdom.Element;
import org.torweg.pulse.configuration.PoorMansCache;
import org.torweg.pulse.site.content.admin.AbstractBasicContentEditorConfig;
import org.torweg.pulse.util.xml.transform.XSLHandle;

/**
 * the configuration of the {@code ForumContentEditor}.
 * 
 * @author Daniel Dietz
 * @version $Revision$
 */
public class ForumContentEditorConfig extends AbstractBasicContentEditorConfig {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 8167425197180834632L;

	// /**
	// * the AJAX XSL file.
	// */
	// private File ajaxXSLFile;

	/**
	 * the AJAX XSL, that builds the FCK-Editor.
	 */
	private File ajaxDescriptionXSLFile;

	// /**
	// * returns the XSLHandle for AJAX responses.
	// *
	// * @return the XSLHandle for AJAX responses
	// */
	// public final XSLHandle getAjaxXSL() {
	// return PoorMansCache.getXSLHandle(this.ajaxXSLFile);
	// }

	/**
	 * returns the XSLHandle for AJAX responses.
	 * 
	 * @return the XSLHandle for AJAX responses
	 */
	public final XSLHandle getPagesAjaxXSL() {
		return PoorMansCache.getXSLHandle(this.ajaxDescriptionXSLFile);
	}

	/**
	 * initializes the {@code ConfigBean}.
	 * 
	 * @param conf
	 *            the configuration XML
	 * @see org.torweg.pulse.configuration.ConfigBean#init(org.jdom.Element)
	 */
	@Override
	public void init(final Element conf) {

		super.init(conf);

		// if (conf.getChild("ajax") != null) {
		// this.ajaxXSLFile = new File(conf.getChild("ajax")
		// .getAttributeValue("xsl"));
		// }
		if (conf.getChild("ajaxPages") != null) {
			this.ajaxDescriptionXSLFile = new File(conf.getChild("ajaxPages")
					.getAttributeValue("xsl"));
		}
	}

}
