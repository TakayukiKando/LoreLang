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
package org.xgmtk.lore.graph.sections.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.xgmtk.lore.graph.Graph;
import org.xgmtk.lore.graph.sections.Coordinate;
import org.xgmtk.lore.graph.sections.Direction4;
import org.xgmtk.lore.graph.sections.SectionGraph;
import static org.xgmtk.lore.graph.sections.test.SampleSectionGraphs.SAMPLE_SQUARE4_COORDINATES;
import static org.xgmtk.lore.graph.sections.test.SampleSectionGraphs.SAMPLE_SQUARE4_LINKS;
import static org.xgmtk.lore.graph.sections.test.SampleSectionGraphs.SAMPLE_SQUARE4_NODES;
import static org.xgmtk.lore.graph.sections.test.SampleSectionGraphs.SECTIONS4;
import org.xgmtk.lore.graph.test.SampleEdgeData;
import static org.xgmtk.lore.graph.test.SampleGenericGraphs.assertGraph;
import org.xgmtk.lore.graph.test.SampleNodeData;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class TestSectionGraph {
    private static final Logger LOGGER  = Logger.getLogger(TestSectionGraph.class.getName());
    
    @Rule
    public TestName testName = new TestName();
    
    @Before
    public void setup(){
        LOGGER.log(Level.INFO, ()->"Initialized and Going to start "+testName.getMethodName());
    }
    
    @SuppressWarnings("unchecked")
    private static void assertCoordinates(final Coordinate[] expected, SectionGraph graph){
        for(int i = 0; i < expected.length; ++i){
            SectionGraph.Section<SampleNodeData, Direction4> sExpected =
                    (SectionGraph.Section<SampleNodeData, Direction4>)graph.getNode(i);
            System.err.println("assertCoordinates() for #"+i);
            assertThat(sExpected.index(), is(i));
            assertThat(sExpected.position(), is(expected[i]));
        }
    }
    
    @Test
    public void testGraphBuild(){LOGGER.log(Level.INFO, ()->"Start "+this.testName.getMethodName()+".");
        final SectionGraph.Builder<SampleNodeData, SampleEdgeData, Direction4> builder = SECTIONS4;
        assertCoordinates(SAMPLE_SQUARE4_COORDINATES, builder);
        assertGraph(SAMPLE_SQUARE4_NODES, SAMPLE_SQUARE4_LINKS, builder);

        Graph<SampleNodeData, SampleEdgeData> graph = builder.getGraph();
        assertGraph(SAMPLE_SQUARE4_NODES, SAMPLE_SQUARE4_LINKS, graph);
        assertCoordinates(SAMPLE_SQUARE4_COORDINATES, builder);
    }
}
