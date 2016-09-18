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
import org.xgmtk.lore.util.ArrayMatrix;
import org.xgmtk.lore.util.ImmutableMatrix;
import java.util.Iterator;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.xgmtk.lore.util.ImmutableVector;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.xgmtk.lore.util.assertion.AssertException.assertException;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class TestArrayMatrix {
    private static final Logger LOGGER = Logger.getLogger(TestArrayMatrix.class.getName());
    
    @Rule
    public TestName testName = new TestName();
    
    @Before
    public void setup(){
        LOGGER.log(Level.INFO, ()->"Initialized and Going to start "+testName.getMethodName());
    }
    
    @Test
    public void testMatrix_0_0(){
        ImmutableMatrix<String> m = new ArrayMatrix<>(new ArrayList<>());
        assertThat(m.numberOfRows(), is(0));
        assertThat(m.numberOfColumns(), is(0));
        assertException(()->m.get(0, 0), IndexOutOfBoundsException.class);
        Iterator<ImmutableVector<String>> it = m.iterator();
        assertThat(it, notNullValue());
        assertThat(it.hasNext(), is(false));
        assertException(()->it.next(), IndexOutOfBoundsException.class);
    }
    
    @Test
    public void testMatrix_3_2(){
        String[][] expected = {{"a", "b", "c"},
                                {"d", "e", "f"}};
        ImmutableMatrix<String> v = new ArrayMatrix<>(Arrays.asList(Arrays.asList(expected[0]), Arrays.asList(expected[1])));

        assertThat(v.numberOfRows(), is(2));
        assertThat(v.numberOfColumns(), is(3));

        for(int row = 0; row < v.numberOfRows(); ++row){
            for(int col = 0; col < v.numberOfColumns(); ++col){
                assertThat(v.get(row, col), is(expected[row][col]));
            }
        }
        
        Iterator<ImmutableVector<String>> itRow = v.iterator();
        assertThat(itRow, notNullValue());
        int row1 = 0;
        while(itRow.hasNext()){
            int col = 0;
            Iterator<String> itCol = itRow.next().iterator();
            while(itCol.hasNext()){
                assertThat(itCol.next(), is(expected[row1][col]));
                ++col;
            }
            assertThat(itCol.hasNext(), is(false));
            assertException(()->itCol.next(), IndexOutOfBoundsException.class);
            ++row1;
        }
        assertThat(itRow.hasNext(), is(false));
        assertException(()->itRow.next(), IndexOutOfBoundsException.class);
        
        int row2 = 0;
        for(ImmutableVector<String> rowVec : v){
            int col = 0;
            for(String e : rowVec){
                assertThat(e, is(expected[row2][col]));
                ++col;
            }
            ++row2;
        }
        
        for(int row = 0; row < v.numberOfRows(); ++row){
            int col = 0;
            for(String e : v.get(row)){
                assertThat(e, is(expected[row][col]));
                ++col;
            }
        }
    }
}
