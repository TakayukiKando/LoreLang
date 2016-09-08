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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <N> Data type of a node contents.
 * @param <E> Data type of an edge contents.
 */
public class AdjacencyListGraph<N, E> implements GenericGraph<N, E> {
    /**
     * 
     * @param <N>
     * @param <E> 
     */
    protected static class ConcreteNode<N,E> extends Node<N>{
        private final Object[] edges;
        
        public ConcreteNode(int index,N nodeData, List<OptimizedEdge<N, E>> edges){
            super(index, nodeData);
            this.edges = new Object[edges.size()];
            Iterator<OptimizedEdge<N, E>> it = edges.iterator();
            for(int i = 0; i < this.edges.length; ++i){
                this.edges[i] = it.next();
            }
        }
        
        public final int numberOfEdges(){
            return this.edges.length;
        }
        
        public final OptimizedEdge<N, E> getEdge(int index){
            return (OptimizedEdge<N, E>)this.edges[index];
        }
        
        public final Stream<OptimizedEdge<N, E>> getEdgeStream(){
            return Arrays.stream(this.edges).map(e->(OptimizedEdge<N, E>)e);
        }
    }
    
    /**
     * 
     * @param <N> Data type of a node contents.
     * @param <E> Data type of an edge contents.
     */
    protected static class OptimizedEdge<N, E> extends Edge<N,E>{
        private final Node<N> terminal;
        
        /**
         * Initializer.
         * 
         * @param terminal
         * @param data 
         */
        public OptimizedEdge(E data, Node<N> terminal){
            super(data);
            this.terminal = terminal;
        }

        @Override
        public Node<N> initialNode() {
            throw new UnsupportedOperationException("Not supported.");
        }

        @Override
        public Node<N> terminalNode() {
            return this.terminal;
        }
    }
    
    public static class Builder<N, E> implements GenericGraphBuilder<N, E>{

        private final AtomicInteger nodeCount;
        protected final Map<Node<N>, List<OptimizedEdge<N,E>>> nodes;
        
        public Builder(){
            super();
            this.nodeCount = new AtomicInteger(0);
            this.nodes = new LinkedHashMap<>();
        }

        @Override
        public int size() {
            return this.nodes.size();
        }

        @Override
        public int numberOfEdges() {
            return this.nodes.values().stream().collect(Collectors.summingInt(l->l.size()));
        }

        @Override
        public Node<N> getNode(int index) {
            Iterator<Node<N>> it = this.nodes.keySet().iterator();
            for(int i = 0; i < index; ++i){
                it.next();
            }
            return it.next();
        }

        @Override
        public Stream<Node<N>> getNodeStream() {
            return this.nodes.keySet().stream();
        }

        @Override
        public int numberOfEdges(Node<N> node) {
            return this.nodes.get(node).size();
        }

        @Override
        public Edge<N, E> getEdge(Node<N> node, int index) {
            return this.nodes.get(node).get(index);
        }
        
        @Override
        public void addNode(N nodeData) {
            this.nodes.put(new Node<>(this.nodeCount.getAndIncrement(), nodeData), new ArrayList<>());
        }

        @Override
        public void addEdge(Node<N> initial, E edgeData, Node<N> terminal) {
            final List<OptimizedEdge<N,E>> edges = this.nodes.get(initial);
            if(edges == null){
                throw new IllegalStateException("Given node has not registered yet.");
            }
            edges.add(new OptimizedEdge<>(edgeData, terminal));
        }

        @Override
        public AdjacencyListGraph<N, E> getGraph(){
            int numberOfEdges = this.numberOfEdges();
            List<ConcreteNode<N, E>> concreteNodes = this.nodes.entrySet().stream()
                    .map(e->new ConcreteNode<>(e.getKey().index(), e.getKey().getData(), e.getValue()))
                    .collect(Collectors.toList());
            return new AdjacencyListGraph<>(numberOfEdges, concreteNodes);
        }
    }
    
    private final int numberOfEdges;
    private final Object[] nodes;
    
    protected AdjacencyListGraph(int numberOfEdges, List<ConcreteNode<N, E>> nodes){
        super();
        this.numberOfEdges = numberOfEdges;
        this.nodes = new Object[nodes.size()];
        Iterator<ConcreteNode<N, E>> it = nodes.iterator();
        for(int i = 0; i < this.nodes.length; ++i){
            this.nodes[i] = it.next();
        }
    }

    @Override
    public int size() {
        return this.nodes.length;
    }

    @Override
    public ConcreteNode<N, E> getNode(int index) {
        return (ConcreteNode<N, E>)this.nodes[index];
    }

    @Override
    public Stream<Node<N>> getNodeStream() {
        return Arrays.stream(this.nodes).map(o->(ConcreteNode<N, E>)o);
    }
    
    @Override
    public int numberOfEdges() {
        return this.numberOfEdges;
    }

    @Override
    public int numberOfEdges(Node<N> node) {
        return ((ConcreteNode<N, E>)node).numberOfEdges();
    }

    @Override
    public Edge<N, E> getEdge(Node<N> node, int index) {
        return ((ConcreteNode<N, E>)node).getEdge(index);
    }
    
    @Override
    public Stream<Edge<N, E>> getEdgeStream(Node<N> node) {
        return ((ConcreteNode<N,E>)node).getEdgeStream().map(e->(Edge<N, E>)e);
    }
    
    @Override
    public String toString(){
        return this.getNodeStream()
                .map(n->"{"+n+"["+this.getEdgeStream(n)
                        .map(e->"<"+e+">")
                        .collect(Collectors.joining(","))+"]"+"}")
                .collect(Collectors.joining("\n"));
    }
}
