/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.jboss.stm.annotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.TestCase;

/**
 * Unit tests for the Class class.
 * 
 * @author Mark Little
 */

public class BasicUnitTest extends TestCase
{

    @Transactional
    public class TestObject
    {
        @ReadLock
        public void doSomeWork ()
        {

        }

        @WriteLock
        public boolean doSomeOtherWork ()
        {
            return true;
        }
        
        @TransactionFree
        public void notTransactionalWork ()
        {
        }

        @State
        @SuppressWarnings(value={"unused"})
        private int _isState;

        @SuppressWarnings(value={"unused"})
        private Integer _isNotState;
    }

    public void testLockableClass ()
    {
        TestObject tester = new TestObject();
        
        assertTrue(tester.getClass().isAnnotationPresent(Transactional.class));
    }
    
    public void testNotLockableClass ()
    {
        BasicUnitTest tester = new BasicUnitTest();
        
        assertFalse(tester.getClass().isAnnotationPresent(Transactional.class));
    }

    public void testMethodTypes () throws Exception
    {
        TestObject tester = new TestObject();
        Method[] methods = tester.getClass().getDeclaredMethods();

        assertNotNull(methods);

        Method someWork = tester.getClass().getDeclaredMethod("doSomeWork", (Class[]) null);
        
        assertNotNull(someWork);
        assertTrue(someWork.isAnnotationPresent(ReadLock.class));
        
        Method someOtherWork = tester.getClass().getDeclaredMethod("doSomeOtherWork", (Class[]) null);
        
        assertNotNull(someOtherWork);
        assertTrue(someOtherWork.isAnnotationPresent(WriteLock.class));
        
        Method someBasicWork = tester.getClass().getDeclaredMethod("notTransactionalWork", (Class[]) null);
        
        assertNotNull(someBasicWork);
        assertFalse(someBasicWork.isAnnotationPresent(WriteLock.class));
        assertFalse(someBasicWork.isAnnotationPresent(ReadLock.class));
    }
    
    public void testFields () throws Exception
    {
        TestObject tester = new TestObject();
        Field[] fields = tester.getClass().getDeclaredFields(); // get all fields including private
        
        assertNotNull(fields);
        
        for (Field afield : fields)
        {
            if (afield.getName().equals("_isState"))
            {
                assertTrue(afield.isAnnotationPresent(State.class));
                assertTrue(afield.getType().equals(Integer.TYPE));
            }
            
            if (afield.getName().equals("_isNotState"))
            {
                assertFalse(afield.isAnnotationPresent(State.class));
                assertTrue(afield.getType().equals(Integer.class));
            }
        }
    }
    
}
