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
@XmlRootElement(name = "forum-post")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.NONE)
public class ForumPost extends AbstractBasicEntity<ForumPost> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -24899912922469722L;

	/**
	 * the logger for {@code ForumPost}.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ForumPost.class);
	
	/**
	 * the classification of the comment.
	 */
	@Enumerated(EnumType.STRING)
	private Classification classification;
	
	/**
	 * the forum content or forum topic
	 */
	@ManyToOne
	private ForumThread forumThread;	
	
	/**
	 * the author of the post.
	 */
	@ManyToOne(optional = false)
	private Author author;
	
	/**
	 * the post mark-up as a string.
	 */
	@Lob
	@Column(length = 65535, nullable=false)
	private String post;
	
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
	public ForumPost() {
		super();
	}
	
	/**
	 * creates a new {@code Forum Post} with the given post mark-up, forum
	 * thread, and author.
	 * 
	 * @param post
	 *            the post mark-up as a string
	 * @param thread
	 *            the forum thread
	 * @param athr
	 *            the author
	 */
	public ForumPost(final String post, final ForumThread thread,
			final Author athr) {
		super();
		this.post = post;
		this.forumThread = thread;
		this.author = athr;
	}
	
	/**
	 * @return the classification
	 */
	public final Classification getClassification() {
		return this.classification;
	}
	
	/**
	 * @return the forumThread
	 */
	public final ForumThread getForumThread() {
		return this.forumThread;
	}

	/**
	 * @param clssfctn
	 *            the classification to set
	 */
	public final void setClassification(final Classification clssfctn) {
		this.classification = clssfctn;
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
	 * post classification.
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