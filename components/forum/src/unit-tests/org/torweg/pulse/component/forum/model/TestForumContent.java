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

import org.torweg.pulse.TestingEnvironment;
import org.torweg.pulse.bundle.Bundle;
import org.torweg.pulse.component.forum.model.ForumContent;
import org.torweg.pulse.invocation.lifecycle.Lifecycle;
import org.torweg.pulse.util.HibernateDataSource;


/**
 * Test case for {@code ForumContent}.
 * 
 * @author Thomas Weber, Zach Metcalf
 * @version $Revision$
 * 
 */

public class TestForumContent extends TestCase {
	
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
	
	public void testCreateContent() throws Exception {
		ForumContent content = new ForumContent(new Locale("xx_YY"), new Bundle(
				new File("test")));
		
	}
}