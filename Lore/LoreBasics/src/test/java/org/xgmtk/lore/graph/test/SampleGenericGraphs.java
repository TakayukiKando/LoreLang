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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import static org.xgmtk.lore.graph.test.SampleNodeData.nameMatch;


import java.util.stream.Collectors;

import java.util.stream.IntStream;
import org.xgmtk.lore.graph.GenericGraphBuilder;
import org.xgmtk.lore.graph.Graph;
import org.xgmtk.lore.graph.iterator.GraphIterator;
import org.xgmtk.lore.graph.iterator.GraphPath;
import org.xgmtk.lore.util.ImmutableVector;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import org.xgmtk.lore.graph.AdjacencyListGraph;
import org.xgmtk.lore.graph.AdjacencyMatrixGraph;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class SampleGenericGraphs{
    public static Graph.Node<SampleNodeData> findNodeFirst(GenericGraphBuilder<SampleNodeData, SampleEdgeData> builder, String name){
        return builder.findNodeFirst(nameMatch(name)).get();
    }
    
    public static void link(GenericGraphBuilder<SampleNodeData, SampleEdgeData> builder, String initialNodeName, int cost, String terminalNodeName){
        final Graph.Node<SampleNodeData> initialNode = findNodeFirst(builder, initialNodeName);
        final Graph.Node<SampleNodeData> terminalNode = findNodeFirst(builder, terminalNodeName);
        final SampleEdgeData edgeData = new SampleEdgeData(initialNodeName+"->"+terminalNodeName, cost);
        builder.addEdge(initialNode, edgeData,terminalNode);
    }
    
    /**
     * Link of the sample graph for testing.
     */
    public static class Link{
        private static String toString(Link[] links) {
            return Arrays.stream(links).map(l->"["+l+"]").collect(Collectors.joining("\n"));
        }
        
        public final String initial;
        public final int cost;
        public final String terminal;
        
        public Link(String initial, int cost, String terminal){
            super();
            this.initial = initial;
            this.cost = cost;
            this.terminal = terminal;
        }
        
        @Override
        public String toString(){
            return "\""+this.initial+"\"--("+this.cost+")->\""+this.terminal+"\"";
        }
    }
    
    /**
     * Number of nodes of graph sample0.
     */
    public static final int SAMPLE0_NODES = 9;
    /**
     * Array of edges of graph sample0.
     */
    public static final Link[] SAMPLE0_LINKS = {
        new Link("#0", 1, "#1"),
        new Link("#1", 1, "#2"),
        new Link("#2", 1, "#3"),
        new Link("#3", 1, "#4"),
        new Link("#4", 1, "#5"),
        new Link("#2", 1, "#6"),
        new Link("#0", 1, "#4"),
        new Link("#7", 1, "#8")
    };

    /**
     * Diagram<br>
     * 
     * 1---2-6<br>
     * |...|..<br>
     * |.5.|.8<br>
     * |.|.|.|<br>
     * 0-4-3.7<br>
     * 
     * @param builder
     * @param nodes
     * @param links
     * @return 
     */
    public static Graph<SampleNodeData, SampleEdgeData> createGraph(GenericGraphBuilder<SampleNodeData, SampleEdgeData> builder, int nodes, Link[] links){
        IntStream.range(0, nodes).forEach(ix->builder.addNode(new SampleNodeData("#"+ix)));
        for(Link link : links){
            link(builder, link.initial, link.cost, link.terminal);
        }
        return builder.getGraph();
    }
    
    public static GenericGraphBuilder<SampleNodeData, SampleEdgeData> createAdjacencyListGraphBuilder(){
        AdjacencyListGraph.Builder<SampleNodeData, SampleEdgeData> builder = new AdjacencyListGraph.Builder<>();
        createGraph(builder, SAMPLE0_NODES, SAMPLE0_LINKS);
        return builder;
    }
    
    public static GenericGraphBuilder<SampleNodeData, SampleEdgeData> createAdjacencyMatrixGraphBuilder(){
        AdjacencyMatrixGraph.Builder<SampleNodeData, SampleEdgeData> builder = new AdjacencyMatrixGraph.Builder<>();
        createGraph(builder, SAMPLE0_NODES, SAMPLE0_LINKS);
        return builder;
    }
    
    public static void assertGraph(int expectedNodes, Link[] expectedLinks, Graph<SampleNodeData, SampleEdgeData> actual){
//        System.err.println("Expected: number of nodes:"+expectedNodes+", links:\n"+Link.toString(expectedLinks));
//        System.err.println();
//        System.err.println("Actual: "+actual.toString());
//        System.err.println();
        assertThat(actual.size(), is(expectedNodes));
        List<Graph.Node<SampleNodeData>> unmachedNodes = actual.getNodeStream()
                .filter(n->!n.getData().name.equals("#"+n.index()))
                .collect(Collectors.toList());
        assertThat(unmachedNodes.size(), is(0));
        for(int i = 0; i < expectedNodes; ++i){
            final int initial = i;
            List<Link> linksMatcedInitial = Arrays.stream(expectedLinks)
                    .filter(l->l.initial.equals("#"+initial))
                    .collect(Collectors.toList());
            Graph.Node<SampleNodeData> node = actual.getNode(i);
            int edges = actual.numberOfEdges(node);
//            System.err.println("i: "+i+", node: "+node+", edges: "+edges);
            assertThat(edges, is(linksMatcedInitial.size()));
            Iterator<Graph.Edge<SampleNodeData, SampleEdgeData>> itEdge = actual.getEdgeIterator(node);
            while(itEdge.hasNext()){
                Graph.Edge<SampleNodeData, SampleEdgeData> edge = itEdge.next();
                List<Link> linksMatched = linksMatcedInitial.stream()
                        .filter(l->l.terminal.equals(edge.terminalNode().getData().name))
                        .collect(Collectors.toList());
                Link link = linksMatched.get(0);
                assertThat(edge.getData().name, is(link.initial+"->"+link.terminal));
                assertThat(edge.getData().cost, is(linksMatched.get(0).cost));
                assertThat(linksMatched.size(), is(1));
            }
        }
    }

 
}
