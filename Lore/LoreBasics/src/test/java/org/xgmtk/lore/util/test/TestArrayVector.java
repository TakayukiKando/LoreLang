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
package org.xgmtk.lore.util.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.xgmtk.lore.util.ArrayVector;
import org.xgmtk.lore.util.ImmutableVector;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.xgmtk.lore.util.assertion.AssertException.assertException;
import static org.xgmtk.lore.util.assertion.AssertVector.assertCopying;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class TestArrayVector {
    private static final Logger LOGGER = Logger.getLogger(TestArrayVector.class.getName());
    
    @Rule
    public TestName testName = new TestName();
    
    @Before
    public void setup(){
        LOGGER.log(Level.INFO, ()->"Initialized and Going to start "+testName.getMethodName());
    }
    
    @Test
    public void testVector_zero(){
        ImmutableVector<String> v = ArrayVector.zeroLengthVector();
        assertThat(v.size(), is(0));
        assertException(()->v.get(0), IndexOutOfBoundsException.class);
        Iterator<String> it = v.iterator();
        assertThat(it, notNullValue());
        assertThat(it.hasNext(), is(false));
        assertException(()->it.next(), IndexOutOfBoundsException.class);
        
        //@SuppressWarnings("RedundantStringConstructorCall")
        assertCopying(v, String::new);
    }
    
    @Test
    public void testVector_0(){
        ImmutableVector<String> v = new ArrayVector<>(new ArrayList<>());
        assertThat(v.size(), is(0));
        assertException(()->v.get(0), IndexOutOfBoundsException.class);
        Iterator<String> it = v.iterator();
        assertThat(it, notNullValue());
        assertThat(it.hasNext(), is(false));
        assertException(()->it.next(), IndexOutOfBoundsException.class);
        
        //@SuppressWarnings("RedundantStringConstructorCall")
        assertCopying(v, String::new);
    }
    
    @Test
    public void testVector_3(){
        String[] expected = {"a", "b", "c"};
        ImmutableVector<String> v = new ArrayVector<>(Arrays.asList(expected));
        
        assertThat(v.size(), is(3));
        for(int i = 0; i < v.size(); ++i){
            assertThat(v.get(i), is(expected[i]));
        }
        
        Iterator<String> it = v.iterator();
        assertThat(it, notNullValue());
        int index = 0;
        while(it.hasNext()){
            assertThat(it.next(), is(expected[index]));
            ++index;
        }
        assertThat(it.hasNext(), is(false));
        assertException(()->it.next(), IndexOutOfBoundsException.class);
        
        int index2 = 0;
        for(String e : v){
            assertThat(e, is(expected[index2]));
            ++index2;
        }
        
        //@SuppressWarnings("RedundantStringConstructorCall")
        assertCopying(v, String::new);
    }


}
