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

import net.sf.json.JSONObject;

import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdom.Element;
import org.torweg.pulse.annotations.Action;
import org.torweg.pulse.annotations.Permission;
import org.torweg.pulse.annotations.RequireToken;
import org.torweg.pulse.bundle.Bundle;
import org.torweg.pulse.component.forum.model.ForumContent;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;
import org.torweg.pulse.service.PulseException;
import org.torweg.pulse.service.event.XSLTOutputEvent;
import org.torweg.pulse.service.request.ServiceRequest;
import org.torweg.pulse.site.content.ContentFolderNode;
import org.torweg.pulse.site.content.ContentGroup;
import org.torweg.pulse.site.content.ContentLocalizationMap;
import org.torweg.pulse.site.content.ContentNode;
import org.torweg.pulse.site.content.RegistryLocaleNode;
import org.torweg.pulse.site.content.admin.AbstractBasicContentEditor;
import org.torweg.pulse.site.content.admin.AbstractContentRegistryEditor;
import org.torweg.pulse.util.adminui.JSONCommunicationUtils;
import org.torweg.pulse.util.adminui.RegistryEditorResult;
import org.torweg.pulse.util.adminui.RightsCheckUtils;

/**
 * the {@code ForumContentRegistryEditor} of the Forum-{@code Bundle}.
 * <p>
 * This controller corresponds to the Forum-branch of the content-registry shown
 * in the west-panel of the website-administration and provides editing
 * functions for the Forum-branch of {@code ContentRegistry}.
 * </p>
 * 
 * @author Daniel Dietz, Zach Metcalf
 * @version $Revision$
 */
public class ForumContentRegistryEditor extends AbstractContentRegistryEditor {

	/**
	 * starts create-new-({@code ForumContent}, {@code ContentGroup},
	 * {@code ContentFolder})-editor.
	 * 
	 * @param bundle
	 *            the current {@code Bundle}
	 * @param request
	 *            the current {@code ServiceRequest}
	 * 
	 * @return an AJAX-result
	 */
	@RequireToken
	@Action(value = "contentRegistryCreate")
	@Permission("useContentRegistry")
	@Override
	public final RegistryEditorResult create(final Bundle bundle, final ServiceRequest request) {

		// retrieve id from request
		Long id = Long.parseLong(request.getCommand().getParameter("id")
				.getFirstValue());

		// setup
		Session s = Lifecycle.getHibernateDataSource().createNewSession();
		Transaction tx = s.beginTransaction();
		RegistryEditorResult result = new RegistryEditorResult();

		try {

			// load the contentNode that has requested a create as
			// RegistryLocaleNode
			RegistryLocaleNode regLocNode = (RegistryLocaleNode) s.get(
					RegistryLocaleNode.class, id);

			if (regLocNode.getClass().equals(ContentFolderNode.class)) {
				// this is a ContentFolderNode
				// -> add create-folder-URL & create-content-URL &
				// create-content-group-URL

				// folder
				result.setCreateURL(ContentFolderNode.class.getCanonicalName(),
						bundle.getViewTypes(new ContentFolderNode(""))
								.getViewByType("save").getCommand(
										request.getCommand().createCopy(false))
								.addHttpParameter("id", id.toString())
								.toCommandURL(request));

				// content
				result
						.setCreateURL(ForumContent.class.getCanonicalName(),
								bundle
										.getViewTypes(
												new ForumContent(regLocNode
														.getLocale(), bundle))
										.getViewByType("create").getCommand(
												request.getCommand()
														.createCopy(false))
										.addHttpParameter("id", id.toString())
										.toCommandURL(request));

				// content-group
				result
						.setCreateURL(ContentGroup.class.getCanonicalName(),
								bundle
										.getViewTypes(
												new ContentGroup(regLocNode
														.getLocale(), bundle))
										.getViewByType("create").getCommand(
												request.getCommand()
														.createCopy(false))
										.addHttpParameter("id", id.toString())
										.toCommandURL(request));

			} else {
				// this is a RegistryLocaleNode
				// -> add create-folder-URL only

				// folder
				result.setCreateURL(ContentFolderNode.class.getCanonicalName(),
						bundle.getViewTypes(new ContentFolderNode(""))
								.getViewByType("save").getCommand(
										request.getCommand().createCopy(false))
								.addHttpParameter("id", id.toString())
								.toCommandURL(request));

			}

			tx.commit();

		} catch (Exception e) {
			tx.rollback();
			throw new PulseException(
					"Forum.ContentRegistryEditor.doContentRegistryCreate.failed: "
							+ e.getLocalizedMessage(), e);
		} finally {
			s.close();
		}

		// output
		XSLTOutputEvent event = new XSLTOutputEvent(getConfig()
				.getAjaxCreateXSL());
		event.setStopEvent(true);
		request.getEventManager().addEvent(event);
		return result;
	}

	/**
	 * creates and saves a new {@code ForumContent}.
	 * 
	 * @param bundle
	 *            the current {@code Bundle}
	 * @param request
	 *            the current {@code ServiceRequest}
	 */
	@RequireToken
	@Action(value = "createForumContent")
	@Permission("useContentRegistry")
	public final void createForumContent(final Bundle bundle,
			final ServiceRequest request) {

		// retrieve parent-id/name from request
		Long id = Long.parseLong(request.getCommand().getParameter("id")
				.getFirstValue());
		String name = request.getCommand().getParameter("name").getFirstValue();

		// setup
		Session s = Lifecycle.getHibernateDataSource().createNewSession();
		Transaction tx = s.beginTransaction();
		ContentNode newContentNode = null;
		// error : userHasNoEditRightsForLocale (if occurs)
		JSONObject error = null;

		try {

			// load the parent-content-node that has requested a save as
			// RegistryLocaleNode
			RegistryLocaleNode parent = (RegistryLocaleNode) s.get(
					RegistryLocaleNode.class, id, LockOptions.UPGRADE);

			// user check
			error = RightsCheckUtils.checkUserAgainstLocale(bundle, request,
					parent.getLocale());

			if (error == null) {

				// create new content
				ForumContent newContent = new ForumContent(parent.getLocale(),
						parent.getBundle());

				// set name
				newContent.setName(name);

				// set (default-)suffix
				newContent.setSuffix(name);

				// set empty summary
				newContent.setSummary(new Element("body"));

				// set empty description
				newContent.setDescription(new Element("body"));

				// create & add content-localization-map
				ContentLocalizationMap clMap = new ContentLocalizationMap(
						parent.getLocale(), newContent);
				newContent.setLocalizationMap(clMap);

				// set creator
				newContent.setCreator(request.getUser());

				// save
				s.saveOrUpdate(newContent);

				// create content-node
				newContentNode = new ContentNode(newContent, parent.getBundle());
				newContentNode.setLocale(parent.getLocale());

				// add new content-node to parent
				parent.addChild(newContentNode);

				// persist
				s.saveOrUpdate(parent);

				tx.commit();

				// TODO : check if this necessary
				AbstractBasicContentEditor
						.initHibernateSearchFix(s, newContent);
			}

		} catch (Exception e) {
			tx.rollback();
			throw new PulseException(
					"Forum.ContentRegistryEditor.createForumContent.failed: "
							+ e.getLocalizedMessage(), e);
		} finally {
			s.close();
		}

		// output
		if (error == null) {
			JSONCommunicationUtils.jsonSuccessMessage(request, "id",
					newContentNode.getId().toString());
		} else {
			JSONCommunicationUtils.jsonErrorMessage(request, error);
		}
	}

}
