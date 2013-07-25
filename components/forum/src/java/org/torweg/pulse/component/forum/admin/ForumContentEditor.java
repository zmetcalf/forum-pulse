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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.jdom.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.torweg.pulse.site.content.ContentNode;
import org.torweg.pulse.site.content.admin.AbstractBasicContentEditor;
import org.torweg.pulse.site.content.admin.AbstractBasicContentEditorResult;
import org.torweg.pulse.util.adminui.FCKEditorResult;
import org.torweg.pulse.util.adminui.JSONCommunicationUtils;
import org.torweg.pulse.util.adminui.RightsCheckUtils;
import org.torweg.pulse.util.entity.Node;

/**
 * the editor for the ForumContent as used by the ext-admin.
 * 
 * @see ForumContent
 * @author Daniel Dietz
 * @version $Revision$
 */

public class ForumContentEditor extends AbstractBasicContentEditor {
	
	/**
	 * the logger.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ForumContentEditor.class);
	
	/**
	 * returns the initialized {@code ForumContentEditor} for a {@code Content}
	 * which is determined by a given id in the request.
	 * 
	 * @param bundle
	 *            the current {@code Bundle}
	 * @param request
	 *            the current {@code ServiceRequest}
	 * 
	 * @return an AJAX-result: the initialized {@code ForumContentEditor}
	 */
	@RequireToken
	@Action(value = "initEditor", generate = true)
	@Permission("viewForumContent")
	public final AbstractBasicContentEditorResult initEditor(final Bundle bundle,
			final ServiceRequest request) {

		// get contentId from request
		Long id = Long.parseLong(request.getCommand().getParameter("id")
				.getFirstValue());

		// setup
		Session s = Lifecycle.getHibernateDataSource().createNewSession();
		Transaction tx = s.beginTransaction();
		AbstractBasicContentEditorResult result = new AbstractBasicContentEditorResult();
		ForumContent forumContent = null;

		try {

			// load the requested content
			forumContent = (ForumContent) s.get(ForumContent.class, id);

			/* init lazy fields */
			forumContent.initializeLazyFields();

			// set content for the result
			result.setContent(forumContent);

			tx.rollback();
		} catch (Exception e) {
			tx.rollback();
			throw new PulseException("ForumContentEditor.initEditor.failed: "
					+ e.getLocalizedMessage(), e);
		} finally {
			s.close();
		}

		// output
		XSLTOutputEvent event = new XSLTOutputEvent(getConfig().getAjaxXSL());
		event.setStopEvent(true);
		request.getEventManager().addEvent(event);
		return result;
	}
	
	/**
	 * saves the basic properties of a content (name, suffix, keywords), as well
	 * as the page-order.
	 * 
	 * @param bundle
	 *            the current {@code Bundle}
	 * @param request
	 *            the current {@code ServiceRequest}
	 */
	@RequireToken
	@Action(value = "saveContent", generate = true)
	@Permission("editForumContent")
	public final void saveContent(final Bundle bundle,
			final ServiceRequest request) {

		// retrieve values from request
		Long id = Long.valueOf(request.getCommand().getParameter("id")
				.getFirstValue());

		// setup
		Session s = Lifecycle.getHibernateDataSource().createNewSession();
		Transaction tx = s.beginTransaction();
		// error : userHasNoEditRightsForLocale (if occurs)
		JSONObject error = null;

		try {

			// load the content
			ForumContent forumContent = (ForumContent) s.get(ForumContent.class, id);

			// check user
			error = RightsCheckUtils.checkUserAgainstLocale(error, bundle,
					request, forumContent.getLocale());

			if (error == null) {

				// process name, suffix, keywords, attachments
				error = AbstractBasicContentEditor.setBasics(forumContent,
						request, s);

			}

			if (error == null) {

				// reset the contents' contentNodes' name if it has changed
				ContentNode contentNode = (ContentNode) s
						.createCriteria(ContentNode.class)
						.add(Restrictions.sqlRestriction("{alias}.content_id="
								+ forumContent.getId())).uniqueResult();
				if (!contentNode.getName().equals(forumContent.getName())) {
					contentNode.setName(forumContent.getName());
					s.saveOrUpdate(contentNode);
				}

				// set last modifier
				forumContent.setLastModifier(request.getUser());

				// persist
				s.saveOrUpdate(forumContent);

				// TODO : check if this necessary
				AbstractBasicContentEditor
						.initHibernateSearchFix(s, forumContent);

				tx.commit();
			}

		} catch (Exception e) {
			tx.rollback();
			throw new PulseException("ForumContentEditor.doSaveContent.failed: "
					+ e.getLocalizedMessage(), e);
		} finally {
			s.close();
		}

		// output
		if (error == null) {
			JSONCommunicationUtils.jsonSuccessMessage(request);
		} else {
			JSONCommunicationUtils.jsonErrorMessage(request, error);
		}
	}
	
	/**
	 * creates a copy of a {@code ForumContent} specified by parameter "id" in
	 * request in a folder specified by parameter "toid" in request using the
	 * locale of the folder.
	 * 
	 * @param bundle
	 *            the current {@code Bundle}
	 * @param request
	 *            the current {@code ServiceRequest}
	 */
	@RequireToken
	@Action(value = "copyContent", generate = true)
	@Permission("editForumContent")
	public final void copyContent(final Bundle bundle,
			final ServiceRequest request) {

		// retrieve values from request
		Long id = Long.valueOf(request.getCommand().getParameter("id")
				.getFirstValue());
		Long toId = Long.valueOf(request.getCommand().getParameter("toid")
				.getFirstValue());

		// setup
		Session s = Lifecycle.getHibernateDataSource().createNewSession();
		Transaction tx = s.beginTransaction();
		// error : userHasNoEditRightsForLocale (if occurs)
		JSONObject error = null;
		ForumContent copy = null;
		JSONArray expandPath = new JSONArray();

		try {

			// load the content
			ForumContent forumContent = (ForumContent) s.get(ForumContent.class, id);

			// load the folder
			ContentFolderNode folder = (ContentFolderNode) s.get(
					ContentFolderNode.class, toId);

			// check user
			error = RightsCheckUtils.checkUserAgainstLocale(error, bundle,
					request, folder.getLocale());

			if (error == null) {

				// create copy
				copy = forumContent.createCopy(folder.getLocale(),
						request.getUser());
				if (!copy.getBundle().getName()
						.equals(folder.getBundle().getName())) {
					copy.setBundle(folder.getBundle());
				}

				// create content-node for copy
				ContentNode contentNode = new ContentNode(copy,
						copy.getBundle());
				contentNode.setLocale(folder.getLocale());

				// add content-node to folder
				folder.addChild(contentNode);

				// persist
				s.saveOrUpdate(copy);
				s.saveOrUpdate(contentNode);
				s.saveOrUpdate(folder);

				// build expandPath
				expandPath.add(contentNode.getId());
				Node parent = folder;
				while (parent != null) {
					expandPath.add(0, parent.getId());
					parent = parent.getParent();
				}

				// TODO : check if this necessary
				AbstractBasicContentEditor.initHibernateSearchFix(s, copy);

				tx.commit();
			} else {
				tx.rollback();
			}

		} catch (Exception e) {
			tx.rollback();
			throw new PulseException("ForumContentEditor.doCopyContent.failed: "
					+ e.getLocalizedMessage(), e);
		} finally {
			s.close();
		}

		// output
		if (error == null) {
			JSONCommunicationUtils.jsonSuccessMessage(request, "expandPath",
					expandPath);
		} else {
			JSONCommunicationUtils.jsonErrorMessage(request, error);
		}
	}
	
	/**
	 * starts the FCKEditor for the description of the content determined by the
	 * request.
	 * 
	 * @param bundle
	 *            the current {@code Bundle}
	 * @param request
	 *            the current {@code ServiceRequest}
	 * 
	 * @return the initialized description-editor
	 */
	@RequireToken
	@Action(value = "initDescriptionEditor", generate = true)
	@Permission("editForumContent")
	public final FCKEditorResult initDescriptionEditor(final Bundle bundle,
			final ServiceRequest request) {

		// get contentId from request
		Long id = Long.parseLong(request.getCommand().getParameter("id")
				.getFirstValue());

		// setup
		Session s = Lifecycle.getHibernateDataSource().createNewSession();
		Transaction tx = s.beginTransaction();
		ForumContent forumContent = null;

		try {

			// load the requested content
			forumContent = (ForumContent) s.get(ForumContent.class, id);

			tx.rollback();
		} catch (Exception e) {
			tx.rollback();
			throw new PulseException(
					"ForumContentEditor.initDescriptionEditor.failed: "
							+ e.getLocalizedMessage(), e);
		} finally {
			s.close();
		}

		// output
		FCKEditorResult result = new FCKEditorResult();
		if (forumContent != null) {
			result.setContentLocale(forumContent.getLocale());
			result.setFCKContent(forumContent.getDescription());
		}
		XSLTOutputEvent event = new XSLTOutputEvent(getConfig().getFCKAjaxXSL());
		event.setStopEvent(true);
		request.getEventManager().addEvent(event);
		return result;
	}
	
	/**
	 * 
	 * @param bundle
	 *            the current {@code Bundle}
	 * @param request
	 *            the current {@code ServiceRequest}
	 */
	@RequireToken
	@Action(value = "saveDescription", generate = true)
	@Permission("editForumContent")
	public final void saveDescription(final Bundle bundle,
			final ServiceRequest request) {

		// get contentId from request
		Long id = Long.parseLong(request.getCommand().getParameter("id")
				.getFirstValue());
		String value = request.getCommand().getParameter("value")
				.getFirstValue();

		// setup
		Session s = Lifecycle.getHibernateDataSource().createNewSession();
		Transaction tx = s.beginTransaction();
		ForumContent forumContent = null;
		// error : userHasNoEditRightsForLocale (if occurs)
		JSONObject error = null;

		try {

			// load the requested content
			forumContent = (ForumContent) s.get(ForumContent.class, id);

			// check user
			error = RightsCheckUtils.checkUserAgainstLocale(error, bundle,
					request, forumContent.getLocale());

			if (error == null) {

				Document desc = buildDocument(value);

				// set the new description
				forumContent.setDescription(desc.getRootElement());

				// update associated files
				forumContent.updateAssociatedVirtualFiles();

				// persist
				s.saveOrUpdate(forumContent);
			}

			tx.commit();

		} catch (Exception e) {
			tx.rollback();
			throw new PulseException(
					"ForumContentEditor.saveDescription.failed"
							+ e.getLocalizedMessage(), e);
		} finally {
			s.close();
		}

		// output
		if (error == null) {
			JSONCommunicationUtils.jsonSuccessMessage(request);
		} else {
			JSONCommunicationUtils.jsonErrorMessage(request, error);
		}
	}

	/**
	 * @param bundle
	 *            the current {@code Bundle}
	 * @param request
	 *            the current {@code ServiceRequest}
	 * @return a result representing the description of the current content
	 */
	@RequireToken
	@Action(value = "loadDescription", generate = true)
	@Permission("editForumContent")
	public final AbstractBasicContentEditorResult loadDescription(
			final Bundle bundle, final ServiceRequest request) {

		// get contentId from request
		Long id = Long.parseLong(request.getCommand().getParameter("id")
				.getFirstValue());

		// setup
		Session s = Lifecycle.getHibernateDataSource().createNewSession();
		Transaction tx = s.beginTransaction();
		ForumContent forumContent = null;

		try {

			// load the requested content
			forumContent = (ForumContent) s.get(ForumContent.class, id);

			tx.rollback();

		} catch (Exception e) {
			tx.rollback();
			throw new PulseException(
					"ForumContentEditor.loadDescription.failed"
							+ e.getLocalizedMessage(), e);
		} finally {
			s.close();
		}

		// output
		AbstractBasicContentEditorResult result = new AbstractBasicContentEditorResult();
		if (forumContent != null) {
			result.setContent(forumContent);
		}
		XSLTOutputEvent event = new XSLTOutputEvent(
				((ForumContentEditorConfig) getConfig()).getAjaxDescriptionXSL());
		event.setStopEvent(true);
		request.getEventManager().addEvent(event);
		return result;
	}
	
}