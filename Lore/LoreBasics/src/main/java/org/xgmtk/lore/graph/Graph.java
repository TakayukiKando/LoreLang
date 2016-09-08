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

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A directed graph in graph theory.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <N> Data type of a node contents.
 * @param <E> Data type of an edge contents.
 */
public interface Graph<N, E> {
    /**
     * 
     * Node type of the graph.
     * 
     * @param <N> Data type of a node contents.
     */
    public static class Node<N>{
        private final int index;
        private final N data;
        
        /**
         * Initializer.
         * 
         * @param index 
         */
        public Node(int index, N data){
            this.index = index;
            this.data = data;
        }

        /**
         * Get the index of the node.
         * 
         * @return The index of the node.
         */
        public final int index(){
            return index;
        }
        
        /**
         * Get the data of the node.
         * 
         * @return The data of the node.
         */
        public final N getData(){
            return this.data;
        }

        @Override
        public boolean equals(Object o){
            if(!(o instanceof Node)){
                return false;
            }
            Node n = (Node)o;
            return Objects.equals(this.index, n.index) && 
                    Objects.equals(this.data, n.data);
        }

        @Override
        public int hashCode(){
            return Objects.hash(this.index, this.data);
        }
        
        @Override
        public String toString(){
            return "{#"+this.index()+", data: "+this.getData()+"}";
        }
    }
    
    /**
     * 
     * @author Takayuki,Kando <develop@xgmtk.org>
     * 
     * @param <N> Data type of a node contents.
     * @param <E> Data type of an edge contents.
     */
    public static abstract class Edge<N,E> {
        private final E data;
        
        /**
         * Initializer.
         * 
         * @param data The data of the edge.
         */
        public Edge(E data){
            this.data = data;
        }
        
        /**
         * Get the initial node of the edge.
         * 
         * @return The initial node of the edge.
         */
        public abstract Node<N> initialNode();
        
        /**
         * Get the terminal node of the edge.
         * 
         * @return The terminal node of the edge.
         */
        public abstract Node<N> terminalNode();
        
        /**
         * Get the data of the edge.
         * 
         * @return The data of the edge.
         */
        public final E getData(){
            return this.data;
        }
        
        @Override
        public String toString(){
            return "{data: "+this.getData()+"}";
        }
    }
    
    /**
     * Concrete edge class.
     * 
     * @param <N> Data type of a node contents.
     * @param <E> Data type of an edge contents.
     */
    public static class ConcreteEdge<N,E> extends Edge<N,E>{
        private final Node<N> initialNode;
        private final Node<N> terminalNode;
        
        /**
         * Initializer.
         * 
         * @param initialNode
         * @param edgeData
         * @param terminalNode 
         */
        public ConcreteEdge(Node<N> initialNode, E edgeData, Node<N> terminalNode){
            super(edgeData);
            this.initialNode = initialNode;
            this.terminalNode = terminalNode;
        }

        @Override
        public Node<N> initialNode() {
            return this.initialNode;
        }

        @Override
        public Node<N> terminalNode() {
            return this.terminalNode;
        }
    }
    
    /**
     * Size of the graph.
     * 
     * @return Number of nodes of the graph.
     */
    public int size();
    
    /**
     * Get a node.
     * 
     * @param index An index number of the node.
     * @return A specified node.
     */
    public Node<N> getNode(int index);
    
    /**
     * Get a stream of nodes of the graph.
     * 
     * @return A stream of nodes of the graph.
     */
    public Stream<Node<N>> getNodeStream();
    
    /**
     * Get a list of nodes of the graph.
     * 
     * @return A list of nodes of the graph.
     */
    public default List<Node<N>> getNodes(){
        return this.getNodeStream().collect(Collectors.toList());
    }
    
    /**
     * Find a node.
     * 
     * @param predicate
     * @return 
     */
    public default Optional<Node<N>> findNodeFirst(Predicate<Node<N>> predicate){
        return this.getNodeStream().filter(predicate).findFirst();
    }
    
    /**
     * Get number of edges started from specified node.
     * 
     * @param node The initial node.
     * @return Number of edges started from specified node.
     */
    public abstract int numberOfEdges(Node<N> node);
    
    /**
     * Get an edge started from specified node.
     * 
     * @param node The initial node.
     * @param index The index of the edge.
     * @return An edge started from specified node
     */
    public abstract Edge<N,E> getEdge(Node<N> node, int index);
    
    /**
     * Get a stream of edges started from specified node.
     * 
     * @param node The initial node.
     * @return A stream of edges started from specified node.
     */
    public default Stream<Edge<N, E>> getEdgeStream(Node<N> node) {
        return IntStream.range(0, this.numberOfEdges(node)).mapToObj(i->this.getEdge(node, i));
    }
}
