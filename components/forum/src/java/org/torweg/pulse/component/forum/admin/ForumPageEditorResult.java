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

import org.jdom.Element;
import org.torweg.pulse.bundle.Result;

/**
 * the result for the CMSPageEditor.
 * 
 * @author Daniel Dietz, Zach Metcalf
 * @version $Revision$
 */
public class ForumPageEditorResult implements Result {

	/** 
	 * @see org.torweg.pulse.bundle.JDOMable#deserializeToJDOM()
	 * 
	 * @return a result TODO: What is it returning?
	 */
	public Element deserializeToJDOM() {
		Element result = new Element("result").setAttribute("class", this
				.getClass().getCanonicalName());
		return result;
	}
}