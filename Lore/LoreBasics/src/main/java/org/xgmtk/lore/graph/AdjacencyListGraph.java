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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.xgmtk.lore.util.ArrayVector;
import org.xgmtk.lore.util.ImmutableVector;
import org.xgmtk.lore.util.WrapCastIterator;


/**
 * The generic immutable graph. It is implemented as adjacency list.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <N> Data type of a node contents.
 * @param <E> Data type of an edge contents.
 */
public class AdjacencyListGraph<N, E> implements GenericGraph<N, E> {

    /**
     * Generic graph builder for the generic graph.
     * It is implemented with adjacency list.
     * 
     * @param <N> Data type of a node contents.
     * @param <E> Data type of an edge contents.
     */
    protected static class ConcreteNode<N,E> extends Node<N>{
        private final ImmutableVector<OptimizedEdge<N, E>> edges;
        
        /**
         * 
         * @param index
         * @param nodeData
         * @param edges 
         */
        public ConcreteNode(int index,N nodeData, List<OptimizedEdge<N, E>> edges){
            super(index, nodeData);
            this.edges = new ArrayVector<>(edges);
        }
        
        /**
         * 
         * @return 
         */
        public final int numberOfEdges(){
            return this.edges.size();
        }
        
        /**
         * 
         * @param index
         * @return 
         */
        public final OptimizedEdge<N, E> getEdge(int index){
            return this.edges.get(index);
        }
        
        /**
         * 
         * @return 
         */
        public final Stream<Edge<N, E>> getEdgeStream(){
            return this.edges.stream().map(e->(Edge<N,E>)e);
        }
        
        /**
         * 
         * @return 
         */
        public final Iterator<Edge<N, E>> getEdgeIterator(){
            return new WrapCastIterator<>(this.edges.iterator());
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
    
    /**
     * The generic graph builder for the generic graph.
     * It is implemented with adjacency list.
     * Because a graph builder itself is also a graph,
     * the graph builder can be used as a mutable graph.
     * 
     * This object of the class is not thread safe.
     * 
     * @param <N> Data type of a node contents.
     * @param <E> Data type of an edge contents.
     */
    public static class Builder<N, E> implements GenericGraphBuilder<N, E>{
        private static class WrapEdgesIterator<N, E> implements Iterator<Edge<N, E>>{
            private final Node<N> initial;
            private final Iterator<OptimizedEdge<N, E>> inner;

            public WrapEdgesIterator(Node<N> initial, Iterator<OptimizedEdge<N, E>> inner){
                super();
                this.initial = initial;
                this.inner = inner;
            }

            @Override
            public final boolean hasNext() {
                return this.inner.hasNext();
            }

            @Override
            public final Edge<N, E> next() {
                OptimizedEdge<N, E> optE = this.inner.next();
                return new ConcreteEdge<>(this.initial, optE.getData(), optE.terminal);
            }
        }
        
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
        public Node<N> getNode(int index){
            if(index < 0 || this.size() <= index){
                throw new IndexOutOfBoundsException();
            }
            return this.getNodeStream().filter(n -> n.index() == index).findFirst().get();
        }
        
        @Override
        public Iterator<Node<N>> getNodeIterator() {
            return this.nodes.keySet().iterator();
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
        public Iterator<Edge<N, E>> getEdgeIterator(Node<N> node) {
            final List<OptimizedEdge<N, E>> edges = this.nodes.get(node);
            return new WrapEdgesIterator<>(node, edges.iterator());
        }
        
        @Override
        public Stream<Edge<N, E>> getEdgeStream(Node<N> node){
            final List<OptimizedEdge<N, E>> edges = this.nodes.get(node);
            return edges.stream().map(optE->(Edge<N,E>)(new ConcreteEdge<>(node, optE.getData(), optE.terminal)));
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
    private final ImmutableVector<ConcreteNode<N, E>> nodes;
    
    private AdjacencyListGraph(int numberOfEdges, List<ConcreteNode<N, E>> nodes){
        super();
        this.numberOfEdges = numberOfEdges;
        this.nodes = new ArrayVector<>(nodes);
    }

    @Override
    public final int size() {
        return this.nodes.size();
    }

    @Override
    public final ConcreteNode<N, E> getNode(int index) {
        return this.nodes.get(index);
    }
    
    @Override
    public Iterator<Node<N>> getNodeIterator() {
        return new WrapCastIterator<>(this.nodes.iterator());
    }

    @Override
    public final Stream<Node<N>> getNodeStream() {
        return this.nodes.stream().map(o->(ConcreteNode<N, E>)o);
    }
    
    @Override
    public final int numberOfEdges() {
        return this.numberOfEdges;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final int numberOfEdges(Node<N> node) {
        return ((ConcreteNode<N, E>)node).numberOfEdges();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public final Iterator<Edge<N, E>> getEdgeIterator(Node<N> node) {
        return ((ConcreteNode<N, E>)node).getEdgeIterator();
    }    @Override
    
    @SuppressWarnings("unchecked")
    public final Stream<Edge<N, E>> getEdgeStream(Node<N> node) {
        return ((ConcreteNode<N, E>)node).getEdgeStream();
    }
    
    @Override
    public String toString(){
        return this.getNodeStream()
                .map(n->n+"["+this.getEdgeStream(n).map(e->e.toString())
                        .collect(Collectors.joining(","))+"]")
                .collect(Collectors.joining("\n"));
    }
}
