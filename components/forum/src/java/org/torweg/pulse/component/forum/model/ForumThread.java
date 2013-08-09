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

import java.io.IOException;
import java.io.StringReader;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.hibernate.LazyInitializationException;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.torweg.pulse.service.PulseException;
import org.torweg.pulse.util.entity.AbstractBasicEntity;
import org.torweg.pulse.util.time.CalendarJAXBOutputWrapper;
import org.torweg.pulse.util.xml.XMLConverter;
import org.torweg.pulse.util.xml.bind.ElementXmlAdapter;

/**
 * @author Thomas Weber, Daniel Dietz, Zach Metcalf
 * @version $Revision$
 */

@Entity
@XmlRootElement(name = "forum-thread")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.NONE)
public class ForumThread extends AbstractBasicEntity<ForumThread> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1736392909176235324L;

	/**
	 * the logger for {@code ForumPost}.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ForumThread.class);
	
	/**
	 * the classification of the comment.
	 */
	@Enumerated(EnumType.STRING)
	private Classification classification;
	
	/**
	 * the forum content or forum topic
	 */
	@ManyToOne
	private ForumContent forumContent;
	
	/**
	 * the author of the post.
	 */
	@ManyToOne(optional = false)
	private Author author;
	
	/**
	 * the thread post mark-up as a string.
	 */
	@Lob
	@Column(length = 65535, nullable=false)
	private String post;
	
	/**
	 * the thread title mark-up as a string.
	 */
	@Lob
	@Column(length = 140, nullable=false)
	private String title;
	
	/**
	 * the post as a JDOM element.
	 */
	@Transient
	private Element postElement;
	
	/**
	 * the created timestamp.
	 */
	@Basic
	private long createdOn = System.currentTimeMillis();
	
	/**
	 * JAXB and Hibernate.
	 */
	@Deprecated
	public ForumThread() {
		super();
	}
	
	/**
	 * creates a new {@code Forum Thread} with the given post mark-up, forum
	 * thread, and author.
	 * 
	 * @param post
	 *            the post mark-up as a string
	 * @param cnt
	 *            the forum content
	 * @param athr
	 *            the author
	 */
	public ForumThread(final ForumContent cnt, final Author athr) {
		super();
		this.forumContent = cnt;
		this.author = athr;
		post = "";
	}

	/**
	 * @return the classification
	 */
	public final Classification getClassification() {
		return this.classification;
	}

	/**
	 * @param clssfctn
	 *            the classification to set
	 */
	public final void setClassification(final Classification clssfctn) {
		this.classification = clssfctn;
	}
	
	/**
	 * @return the forumContent
	 */
	public final ForumContent getForumContent() {
		return this.forumContent;
	}

	/**
	 * returns the title of post
	 * 
	 * @return the title
	 */
	public final String getTitle() {
		return this.title;
	}

	/**
	 * sets the title of post
	 * 
	 * @param t
	 *            the title to set
	 */
	public final void setTitle(final String t) {
		this.title = t;
	}
	
	/**
	 * Returns the post of the {@code ForumPost}.
	 * 
	 * @return the post of the {@code ForumPost}
	 */
	public final String getPost() {
		return this.post;
	}

	/**
	 * Sets the post of the {@code ForumPost} from a JDOM {@code Element}.
	 * 
	 * @param cmmnt
	 *            the JDOM {@code Element} representing the post to be set.
	 */
	public final void setPost(final Element cmmnt) {
		this.post = XMLConverter.getRawString(cmmnt, true);
		this.postElement = cmmnt;
	}

	/**
	 * Returns the post of the {@code ForumPost} as a JDOM {@code Element}.
	 * 
	 * @return the post of the {@code ForumPost} as a JDOM {@code Element}.
	 */
	public final Element getPostElement() {
		if (getPost() == null) {
			return null;
		}
		if (this.postElement == null) {
			try {
				this.postElement = new SAXBuilder().build(
						new StringReader(getPost())).getRootElement();
			} catch (JDOMException e) {
				throw new PulseException("Error parsing XML for description: "
						+ e.getLocalizedMessage(), e);
			} catch (IOException e) {
				throw new PulseException("Error reading XML for description: "
						+ e.getLocalizedMessage(), e);
			}
		}
		return (Element) this.postElement.clone();
	}

	/**
	 * For JAXB only.
	 * 
	 * @return this.getDescriptionElement()
	 */
	@XmlElement(name = "description-element")
	@XmlJavaTypeAdapter(value = ElementXmlAdapter.class)
	@SuppressWarnings("unused")
	@Deprecated
	private Element getPostElementJAXB() {
		try {
			return getPostElement();
		} catch (LazyInitializationException e) {
			LOGGER.trace("ignored: {}", e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * @return the author
	 */
	public final Author getAuthor() {
		return this.author;
	}

	/**
	 * @return the createdOn
	 */
	public final long getCreatedOn() {
		return this.createdOn;
	}

	/**
	 * for JAXB.
	 * 
	 * @return the created on date
	 */
	@XmlElement(name = "created-on")
	@SuppressWarnings("unused")
	private CalendarJAXBOutputWrapper getCreatedOnJAXB() {
		return new CalendarJAXBOutputWrapper(this.createdOn);
	}
	
	/**
	 * thread classification.
	 */
	public enum Classification {
		/**
		 * spam.
		 */
		SPAM,
		/**
		 * not rated/unclear rating.
		 */
		UNDEFINED,
		/**
		 * Reported by other users as SPAM or abusive.
		 */
		REPORTED,
		/**
		 * ham.
		 */
		HAM;
	}	
}