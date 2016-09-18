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
package org.xgmtk.lore.graph;

/**
 * The generic graph builder interface.
 * Because a graph builder itself is also a graph,
 * the graph builder can be used as a mutable graph.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <N> Data type of a node contents.
 * @param <E> Data type of an edge contents.
 */
public interface GenericGraphBuilder<N, E> extends Graph<N,E>, GenericGraph<N, E> {    
    /**
     * Add a node to the graph via supplier.
     * 
     * @param nodeData 
     */
    public void addNode(N nodeData);
    
    /**
     * Add an edge to the graph.
     * 
     * @param initialNode
     * @param edgeData 
     * @param terminalNode
     */
    public void addEdge(Node<N> initialNode, E edgeData, Node<N> terminalNode);
    
    /**
     * The specified graph's nodes and edges are added to the currently building graph.
     * 
     * @param source A source graph.
     */
    public default void loadGraph(Graph<N,E> source){
        source.getNodeStream()
                .forEach(n->this.addNode(n.getData()));

        source.getNodeStream().forEach(n->{
            source.getEdgeStream(n)
                .forEach(e->this.addEdge(n, e.getData(), e.terminalNode()));
        });
    }
    
    /**
     * Return a built graph.
     * 
     * @return A built graph.
     * @throws NullPointerException A graph has not built yet.
     */
    public Graph<N, E> getGraph() throws NullPointerException;
}
