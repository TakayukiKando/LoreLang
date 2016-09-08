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
package org.xgmtk.lore.graph.test;

import static org.xgmtk.lore.graph.test.SampleGenericGraphs.assertGraph;
import static org.xgmtk.lore.graph.test.SampleGenericGraphs.createGraph;
import static org.xgmtk.lore.graph.test.SampleGenericGraphs.sample0Links;
import static org.xgmtk.lore.graph.test.SampleGenericGraphs.sample0Nodes;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.xgmtk.lore.graph.AdjacencyListGraph;
import org.xgmtk.lore.graph.Graph;

/**
 *
 * @author kando
 */
public class TestAdjacencyListGraph {
    private static Logger logger;
    
    @Rule
    public TestName testName = new TestName();
    
    @BeforeClass
    public static void initClass(){
        logger = Logger.getLogger(TestAdjacencyListGraph.class.getName());
    }
    
    @Test
    public void testGraphBuild(){
        logger.log(Level.INFO, ()->"Start "+this.testName.getMethodName()+".");
        final AdjacencyListGraph.Builder<SampleNodeData, SampleEdgeData> builder = new AdjacencyListGraph.Builder<>();
        createGraph(builder, sample0Nodes, sample0Links);
        assertGraph(sample0Nodes, sample0Links, builder);
        Graph<SampleNodeData, SampleEdgeData> graph = builder.getGraph();
        assertGraph(sample0Nodes, sample0Links, graph);
    }
}
