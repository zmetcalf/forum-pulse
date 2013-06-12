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
package org.torweg.pulse.component.forum.model;

import java.util.HashSet;
import java.util.Locale;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.torweg.pulse.accesscontrol.User;
import org.torweg.pulse.bundle.Bundle;
import org.torweg.pulse.bundle.ExtendedJDOMable;
import org.torweg.pulse.component.store.model.StoreContent;
import org.torweg.pulse.component.store.model.StoreVariant;
import org.torweg.pulse.site.content.AbstractBasicContent;
import org.torweg.pulse.site.content.Content;
import org.torweg.pulse.site.content.Variant;
import org.torweg.pulse.vfs.VirtualFile;
import org.torweg.pulse.vfs.VirtualFileSystem;

/**
 * @author Thomas Weber, Daniel Dietz, Zach Metcalf
 * @version $Revision$
 */
@Entity
@XmlRootElement(name = "forum-content")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.FIELD)
public class ForumContent extends AbstractBasicContent implements
		ExtendedJDOMable {
	
	/**
	 * the logger for {@code ForumContent}.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ForumContent.class);
	
	/**
	 * Hibernate<sup>TM</sup>.
	 */
	@Deprecated
	public ForumContent() {
		super();
	}
	
	/**
	 * The constructor that sets the {@code Locale} and the {@code Bundle} of
	 * the {@code StoreContent}.
	 * 
	 * @param pLocale
	 *            the {@code Locale} to be set.
	 * @param pBundle
	 *            the {@code Bundle} to be set.
	 */
	public ForumContent(final Locale pLocale, final Bundle pBundle) {
		super();
		setLocale(pLocale);
		setBundle(pBundle);
	}
	
	/**
	 * Creates a non-persistent (therefore id = null) copy of the current
	 * {@code ForumContent} with the given {@code Locale}.
	 * 
	 * @param l
	 *            the {@code Locale} to be used.
	 * @param u
	 *            the {@code User} to be used as the creator/last modifier of
	 *            the copied {@code Content}
	 * @return a copy of the {@code ForumContent}.
	 */
	@Override
	public ForumContent createCopy(final Locale l, final User u) {
		// Create copy of ForumContent
		ForumContent copy = new ForumContent(l, getBundle());

		addValuesToCopy(copy, u);

		// Update virtual files
		copy.updateAssociatedVirtualFiles();

		// Return copy
		return copy;
	}
	
	/**
	 * returns the {@code StoreContent}'s textual information as it is supposed
	 * to be supplied for the index.
	 * 
	 * @return the {@code StoreContent}'s textual information
	 * @see org.torweg.pulse.site.content.AbstractBasicContent#getFullTextValue()
	 */
	@Override
	public StringBuilder getFullTextValue() {
		StringBuilder builder = super.getFullTextValue();

		return builder;
	}
	/**
	 * returns {@code false}.
	 * 
	 * @see Content#isGroup()
	 * @return {@code false}.
	 */
	@Override
	public final boolean isGroup() {
		return false;
	}
	
	/**
	 * updates the {@code CMSContent}s HTML, if associated {@code VirtualFile}s
	 * have been moved in the {@code VirtualFileSystem}.
	 * <p>
	 * This method is executed by the {@code VirtualFileSystem}
	 * </p>
	 * 
	 * @param file
	 *            the moved files
	 */
	@Override
	public void onVirtualFileSystemChange(final VirtualFile file) {
		setSummary(Content.updateHTML(getSummaryElement(), file));
	}
	
	/**
	 * Is called by the editors upon save actions to update the list of
	 * associated {@code VirtualFile}s.
	 */
	@Override
	public void updateAssociatedVirtualFiles() {
		HashSet<VirtualFile> files = new HashSet<VirtualFile>();
		VirtualFileSystem fileSystem = VirtualFileSystem.getInstance();
		Element element = getSummaryElement();
		Content.processHTML(element, files, fileSystem);
		setSummary(element);
		setAssociatedVirtualFiles(files);
	}
}