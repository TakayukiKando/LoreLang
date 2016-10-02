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
package org.xgmtk.lore.graph.iterator.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import static org.xgmtk.lore.graph.iterator.AssertGraphIterater.assertGraphIteratorAndPath;
import org.xgmtk.lore.graph.iterator.DepthFirstIterator;
import org.xgmtk.lore.graph.test.SampleGenericGraphs;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class TestDepthFirstIterator {
    private static final Logger LOGGER  = Logger.getLogger(TestDepthFirstIterator.class.getName());
    
    private static final int[] EXPECTED_ITERATION = {0, 1, 2, 3, 4, 5, 6};
    private static final int[][] EXPECTED_PATHS = {
        {0},
        {0, 1},
        {0, 1, 2},
        {0, 1, 2, 3},
        {0, 1, 2, 3, 4},
        {0, 1, 2, 3, 4, 5},
        {0, 1, 2, 6},
        null,
        null
    };
        
    @Rule
    public TestName testName = new TestName();
    
    @Before
    public void setup(){
        LOGGER.log(Level.INFO, ()->"Initialized and Going to start "+testName.getMethodName());
    }
    
    @Test
    public void testAdjacencyListGraphIteratorAndPath(){
        LOGGER.log(Level.INFO, ()->"Start "+this.testName.getMethodName()+".");
        assertGraphIteratorAndPath(SampleGenericGraphs.createAdjacencyListGraphBuilder(),
                (g)->new DepthFirstIterator<>(g, g.getNode(0)),
        EXPECTED_ITERATION, EXPECTED_PATHS);
    }
    
    @Test
    public void testAdjacencyMatrixGraphIteratorAndPath(){
        LOGGER.log(Level.INFO, ()->"Start "+this.testName.getMethodName()+".");
        assertGraphIteratorAndPath(SampleGenericGraphs.createAdjacencyMatrixGraphBuilder(),
                (g)->new DepthFirstIterator<>(g, g.getNode(0)),
        EXPECTED_ITERATION, EXPECTED_PATHS);
    }
}
