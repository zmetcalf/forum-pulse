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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Test;
import org.torweg.pulse.TestingEnvironment;
import org.torweg.pulse.TestingHttpServletRequest;
import org.torweg.pulse.TestingHttpServletResponse;
import org.torweg.pulse.TestingServiceRequest;
import org.torweg.pulse.accesscontrol.User;
import org.torweg.pulse.accesscontrol.authentication.AuthenticationAdapter;
import org.torweg.pulse.bundle.Bundle;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;
import org.torweg.pulse.util.HibernateDataSource;

/**
 * @author Zach Metcalf
 * @version $Revision$
 * 
 */
public class TestAuthor extends TestCase {
	
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
		Session sess = dataSource.createNewSession();
		Transaction trans = sess.beginTransaction();
		Bundle bundle = new Bundle(new File("test"));
		User usr = new User("TestUser", "test@email.com", "password");
		usr.setSuperuser(true);
		
		Session s = this.dataSource.createNewSession();
		Transaction tx = s.beginTransaction();

		s.saveOrUpdate(usr);

		tx.commit();
		s.close();
		
		TestingServiceRequest testRequest = new TestingServiceRequest();
		
		//HttpServletRequest request = testRequest.getHttpServletRequest();
		//HttpServletResponse response = testRequest.getHttpServletResponse();
		
		HttpServletRequest request = new TestingHttpServletRequest();
		HttpServletResponse response = new TestingHttpServletResponse();
		
		AuthenticationAdapter.authenticate(request, response, "TestUser", "password");
		
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
		
		
		Session s = this.dataSource.createNewSession();
		Transaction tx = s.beginTransaction();
		
		s = this.dataSource.createNewSession();
		tx = s.beginTransaction();
		User usr = new User("TestUser", "test@email.com", "password");
		
		s.delete(usr);

		tx.commit();
		s.close();
	}
	
	@Test
	public void testCreateAuthor() {

	}
}