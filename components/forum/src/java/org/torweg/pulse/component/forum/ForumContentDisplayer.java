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
package org.torweg.pulse.component.forum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.torweg.pulse.annotations.Action;
import org.torweg.pulse.annotations.Permission;
import org.torweg.pulse.annotations.Action.Security;
import org.torweg.pulse.component.cms.CMSContentDisplayerResult;
import org.torweg.pulse.component.store.StoreContentDisplayer;
import org.torweg.pulse.service.request.Command;
import org.torweg.pulse.service.request.ServiceRequest;
import org.torweg.pulse.site.content.AbstractContentDisplayer;


/**
 * @author Thomas Weber, Zach Metcalf
 * @version $Revision$
 */
public class ForumContentDisplayer extends AbstractContentDisplayer {
	
	/**
	 * the logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ForumContentDisplayer.class);
	
	/**
	 * loads a given {@code ForumContent}, either by the {@code Command} 's
	 * <em>sitemapID</em> (see: {@link Command#getSitemapID()}) or by the
	 * {@code <em>contentId</em>} {@code Parameter}, while the {@code
	 * <em>contentId</em>} {@code Parameter} takes precedence over the {@code
	 * Command}'s <em>sitemapId</em>.
	 * <p>
	 * If the <em>sitemapID</em> denotes a {@code SitemapNode} without a {@code
	 * View}, the first {@code ForumContent} in the ancestor axis is loaded
	 * instead.
	 * </p>
	 * 
	 * @param request
	 *            the current {@code ServiceRequest}
	 * @return a {@code ForumContentDisplayerResult} for the requested {@code
	 *         ForumContent}
	 * @see ForumContentDisplayerResult
	 */
	@Action(value = "displayForum", security = Security.NEVER)
	@Permission("displayForum")
	public final ForumContentDisplayerResult display(final ServiceRequest request) {
		return null;
	}
}