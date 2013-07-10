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
import org.torweg.pulse.component.forum.model.ForumContent;

/**
 * the result for the {@code ForumContentEditor}.
 * 
 * @author Daniel Dietz, Zach Metcalf
 * @version $Revision$
 */
public class ForumContentEditorResult implements Result {

	/**
	 * the {@code ForumContent} of the result.
	 */
	private ForumContent content;

	/**
	 * @return the result as {@code Element}.
	 * 
	 * @see org.torweg.pulse.bundle.JDOMable#deserializeToJDOM()
	 */
	public Element deserializeToJDOM() {
		Element result = new Element("result").setAttribute("class", this
				.getClass().getCanonicalName());
		if (this.content != null) {
			result.addContent(this.content.deserializeToJDOM());
		}
		return result;
	}

	/**
	 * sets the content of the result.
	 * 
	 * @param c
	 *            the {@code ForumContent} to be set
	 */
	public void setContent(final ForumContent c) {
		this.content = c;
	}

}
