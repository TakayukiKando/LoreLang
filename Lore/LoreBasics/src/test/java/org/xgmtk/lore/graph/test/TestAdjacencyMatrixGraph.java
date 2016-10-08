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

import java.util.logging.Level;
import java.util.logging.Logger;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.xgmtk.lore.graph.AdjacencyMatrixGraph;
import org.xgmtk.lore.graph.Graph;
import static org.xgmtk.lore.graph.test.SampleGenericGraphs.SAMPLE0_NODES;
import static org.xgmtk.lore.graph.test.SampleGenericGraphs.SAMPLE0_LINKS;

/**
 *
 * @author kando
 */
public class TestAdjacencyMatrixGraph {
    private static final Logger LOGGER = Logger.getLogger(TestAdjacencyMatrixGraph.class.getName());
    
    @Rule
    public TestName testName = new TestName();
    
    @Before
    public void setup(){
        LOGGER.log(Level.INFO, ()->"Initialized and Going to start "+testName.getMethodName());
    }
    
    @Test
    public void testGraphBuild(){
        LOGGER.log(Level.INFO, ()->"Start "+this.testName.getMethodName()+".");
        final AdjacencyMatrixGraph.Builder<SampleNodeData, SampleEdgeData> builder = new AdjacencyMatrixGraph.Builder<>();
        createGraph(builder, SAMPLE0_NODES, SAMPLE0_LINKS);
        assertGraph(SAMPLE0_NODES, SAMPLE0_LINKS, builder);
        builder.getNodeStream().forEach(n->{
            assertThat(n, instanceOf(Graph.ConcreteNode.class));
            builder.getEdgeStream(n).forEach(e->{
                assertThat(e.initialNode(), sameInstance(n));
                assertThat(e.terminalNode(), instanceOf(Graph.ConcreteNode.class));
            });
        });
        builder.getNodeIterator().forEachRemaining(n->{
            assertThat(n, instanceOf(Graph.ConcreteNode.class));
            builder.getEdgeIterator(n).forEachRemaining(e->{
                assertThat(e.initialNode(), sameInstance(n));
                assertThat(e.terminalNode(), instanceOf(Graph.ConcreteNode.class));
            });
        });
        Graph<SampleNodeData, SampleEdgeData> graph = builder.getGraph();
        assertGraph(SAMPLE0_NODES, SAMPLE0_LINKS, graph);
        graph.getNodeStream().forEach(n->{
            assertThat(n, instanceOf(Graph.ConcreteNode.class));
            graph.getEdgeStream(n).forEach(e->{
                assertThat(e.initialNode(), sameInstance(n));
                assertThat(e.terminalNode(), instanceOf(Graph.ConcreteNode.class));
            });
        });
        graph.getNodeIterator().forEachRemaining(n->{
            assertThat(n, instanceOf(Graph.ConcreteNode.class));
            graph.getEdgeIterator(n).forEachRemaining(e->{
                assertThat(e.initialNode(), sameInstance(n));
                assertThat(e.terminalNode(), instanceOf(Graph.ConcreteNode.class));
            });
        });
    }
}
