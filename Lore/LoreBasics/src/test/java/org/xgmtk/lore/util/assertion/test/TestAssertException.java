/*
 * Copyright 2016 Inuyama-ya sanp <develop@xgmtk.org>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xgmtk.lore.util.assertion.test;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.xgmtk.lore.util.assertion.AssertException.assertException;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class TestAssertException {
    private static final Logger LOGGER = Logger.getLogger(TestAssertException.class.getName());
    
    @Rule
    public TestName testName = new TestName();
    
    @Before
    public void setup(){
        LOGGER.log(Level.INFO, ()->"Initialized and Going to start "+testName.getMethodName());
    }
    
    @Test
    public void testAssertException(){
        assertException(()->{throw new IllegalStateException("Expected");}, IllegalStateException.class, (ex)->assertThat(ex.getMessage(), is("Expected")));
    }
    
    @Test(expected=NullPointerException.class)
    public void testAssertExceptionErrorInCheck(){
        assertException(()->{throw new IllegalStateException("Expected");}, IllegalStateException.class, (ex)->{throw new NullPointerException("Not thrown.");});
    }
    
    @Test
    public void testAssertExceptionUnexpected(){
        try{
            assertException(()->{throw new IllegalArgumentException("Unexpected");}, IllegalStateException.class, (ex)->fail());
        }catch(AssertionError ex){
            assertThat(ex.getCause(), instanceOf(IllegalArgumentException.class));
        }
    }
    
    @Test(expected=AssertionError.class)
    @SuppressWarnings("empty-statement")
    public void testAssertExceptionNoException(){
        assertException(()->{;}, IllegalStateException.class, (ex)->{throw new NullPointerException("Not thrown.");});
    }
}
