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
package org.xgmtk.lore.graph.iterator;

import java.util.function.Function;
import org.xgmtk.lore.graph.GenericGraphBuilder;
import org.xgmtk.lore.graph.Graph;
import org.xgmtk.lore.util.ImmutableVector;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class AssertGraphIterater {
           
    public static <N, E> void assertIteration(int[] expectedOrderOfIteration, GraphIterator<N, E> iterator) {
        int i = 0;
        while(iterator.hasNext()){
            int expectedIndex = expectedOrderOfIteration[i++];
            Graph.Node<N> actual = iterator.next();
            //System.err.println("* Iterator returns node["+actual.index()+"]");
            assertThat(actual.index(), is(expectedIndex));
        }
        assertThat(i, is(expectedOrderOfIteration.length));
    }
    
    public static <N, E> void assertPaths(int[][] expectedPaths, Graph<N, E> graph, GraphIterator<N, E> iterator) {
        Graph.Node<N> start = iterator.startNode();
        for(int ipath = 0; ipath < expectedPaths.length; ++ipath){
            //System.err.print("assert path["+ipath+"]: ");
            final Graph.Node<N> goal = graph.getNode(ipath);
            GraphPath<N, E> actualPath = iterator.getPath(goal);
            int[] expectedPathIndices = expectedPaths[ipath];
            assertThat(actualPath.goalNode(), is(goal));
            assertThat(actualPath.startNode(), is(start));
            assertThat(iterator.isVisited(goal), is(expectedPathIndices != null));
            if(expectedPathIndices == null){//Not visited case.
                //System.err.println("Not reacheable.");
                assertThat(actualPath.steps().isPresent(), is(false));

                continue;
            }
            if(expectedPathIndices.length == 1){//Start == Goal case.
                //System.err.println("The goal node is same as the start node.");
                assertThat(actualPath.steps().isPresent(), is(true));
                assertThat(actualPath.steps().get().size(), is(0));
                assertThat(actualPath.startNode(), is(actualPath.goalNode()));
                continue;
            }
            //System.err.println("Ordinaly path.");
            assertThat(expectedPathIndices[expectedPathIndices.length-1], is(goal.index()));
            assertThat(expectedPathIndices[0], is(start.index()));
            final ImmutableVector<Graph.Edge<N, E>> steps = actualPath.steps().get();
            assertThat(steps.size(), is(expectedPathIndices.length - 1));
            int expectedInitialNodeIndex = expectedPathIndices[0];
            for(int iexpected = 1; iexpected < expectedPathIndices.length; ++iexpected){
                int expectedTerminalNodeIndex = expectedPathIndices[iexpected];
                Graph.Edge<N, E> step = steps.get(iexpected - 1);
                assertThat(step.initialNode().index(), is(expectedInitialNodeIndex));
                assertThat(step.terminalNode().index(), is(expectedTerminalNodeIndex));
                expectedInitialNodeIndex = expectedTerminalNodeIndex;
            }
        }
    }

    public static <N, E> void assertIteratorAndPath(final Graph<N, E> graph,
            Function<Graph<N, E>, GraphIterator<N, E>> iteratorSuppier,
            final int[] expectedIteration, final int[][] expectedPaths) {
        final GraphIterator<N, E> it = iteratorSuppier.apply(graph);
        assertIteration(expectedIteration, it);
        assertPaths(expectedPaths, graph, it);
    }
    
    public static <N, E> void assertGraphIteratorAndPath(GenericGraphBuilder<N, E> builder,
            Function<Graph<N, E>, GraphIterator<N, E>> iteratorSuppier,
            final int[] expectedIteration, final int[][] expectedPaths){
        assertIteratorAndPath(builder, iteratorSuppier, expectedIteration, expectedPaths);
        Graph<N, E> graph = builder.getGraph();
        assertIteratorAndPath(graph, iteratorSuppier, expectedIteration, expectedPaths);
    }
}
