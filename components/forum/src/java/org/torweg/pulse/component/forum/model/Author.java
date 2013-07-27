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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.torweg.pulse.accesscontrol.User;
import org.torweg.pulse.util.entity.AbstractNamedEntity;

/**
 * represents an {@code Author} of either {@code ForumPost} or
 * {@code ForumThread}.
 * 
 * @author Thomas Weber, Daniel Dietz
 * @version $Revision$
 */
@Entity
@XmlRootElement(name = "author")
@XmlAccessorOrder(XmlAccessOrder.UNDEFINED)
@XmlAccessorType(XmlAccessType.NONE)
public class Author extends AbstractNamedEntity<Author> {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5207861245925543769L;

	/**
	 * optional user id.
	 */
	@Basic
	private Long userId;

	/**
	 * the email of the author.
	 */
	@Basic
	@Column(unique = true)
	private String email;

	/**
	 * JAXB and Hibernate.
	 */
	@Deprecated
	protected Author() {
		super();
	}

	/**
	 * creates a new {@code Author} from the given name and e-mail.
	 * 
	 * @param nm
	 *            the name
	 * @param eml
	 *            the email
	 */
	public Author(final String nm, final String eml) {
		super();
		setName(nm);
		this.email = eml;
	}

	/**
	 * creates a new {@code Author} from the given {@code User}.
	 * 
	 * @param usr
	 *            the User
	 */
	public Author(final User usr) {
		super();
		this.userId = usr.getId();
		this.email = usr.getEmail();
		setName(usr.getName());
	}

	/**
	 * @return the email
	 */
	@XmlElement(name = "email")
	public final String getEmail() {
		return this.email;
	}

	/**
	 * @param eml
	 *            the email to set
	 */
	public final void setEmail(final String eml) {
		this.email = eml;
	}

	/**
	 * @return the userId
	 */
	@XmlAttribute(name = "user-id")
	public final Long getUserId() {
		return this.userId;
	}

}
