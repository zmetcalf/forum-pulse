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

import java.io.File;
import java.util.Locale;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jdom.Element;
import org.junit.Test;
import org.torweg.pulse.TestingEnvironment;
import org.torweg.pulse.bundle.Bundle;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;
import org.torweg.pulse.util.HibernateDataSource;

/**
 * @author Zach Metcalf
 * @version $Revision$
 * 
 */
public class TestForumThread extends TestCase {
	
	/**
	 * The HibernateDataSource.
	 */
	private HibernateDataSource dataSource = null;
	
	/**
	 * set up the test.
	 * 
	 * @throws Exception
	 *             on errors
	 */
	@Override
	protected final void setUp() throws Exception {
		super.setUp();
		new TestingEnvironment();
		this.dataSource = Lifecycle.getHibernateDataSource();
		
		Session s = dataSource.createNewSession();
		Transaction tx = s.beginTransaction();
		Locale locale = new Locale("en", "EN");
		Bundle bundle = new Bundle(new File("test"));
		ForumContent content = new ForumContent(locale, bundle);
		content.setName("forumtest");
		content.setSummary(new Element("testsummary")
			.setText("This is the test summary."));
		content.setDescription(new Element("description")
			.setText("This is the test description."));
		Author athr = new Author("Test Author", "test@case.com");
		ForumThread frmThrd = new ForumThread(content, athr);
		frmThrd.setTitle("Title of Post");
		frmThrd.setPost(new Element("post")
			.setText("This is a post for testing purposes."));
		
		s.saveOrUpdate(bundle);
		s.saveOrUpdate(content);
		s.saveOrUpdate(athr);
		s.saveOrUpdate(frmThrd);

		tx.commit();
		s.close();
	}
	
	/**
	 * tear down the test.
	 * 
	 * @throws Exception
	 *             on errors
	 */
	@Override
	protected final void tearDown() throws Exception {
		super.tearDown();
	}
	
	@Test
	public void testCreateForumThread() {
		//org.junit.Assert.assertNotNull(frmThrd.getPost());	
	}
	
	@Test
	public void testAssociatedWithForumContent() {
		
	}
	
}