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
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.xgmtk.lore.util.ArrayMatrix;
import org.xgmtk.lore.util.ArrayVector;
import org.xgmtk.lore.util.ImmutableMatrix;
import org.xgmtk.lore.util.ImmutableVector;

/**
 * The generic immutable graph.
 * It is implemented with adjacency matrix.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <N> Data type of a node contents.
 * @param <E> Data type of an edge contents.
 */
public final class AdjacencyMatrixGraph<N, E> implements GenericGraph<N, E> {

    /**
     * The generic graph builder for the generic graph.
     * It is implemented with adjacency matrix.
     * Because a graph builder itself is also a graph,
     * the graph builder can be used as a mutable graph.
     * This object of the class is not thread safe.
     * 
     * @param <N> Data type of a node contents.
     * @param <E> Data type of an edge contents.
     */
    public static final class Builder<N, E> implements GenericGraphBuilder<N, E> {
        /**
         * This indirect reference container enables assignment to adjacency matrix entry.
         * This is should use only in Builder.
         * 
         * @param <E> Data type of an edge contents.
         */
        private static class Cell<E> {
            private E content;
            
            /**
             * Initializer.
             * 
             * The cell is initialized as a empty cell.
             */
            public Cell(){
                this.content = null;
            }

            public final void set(E content){
                this.content = content;
            }

            public final E get(){
                return this.content;
            }
        }
    
        
        private final AtomicInteger nodeCount;
        private final List<Node<N>> nodes;
        private final List<List<Cell<E>>> matrix;
        
        public Builder() {
            this.nodeCount = new AtomicInteger(0);
            this.nodes = new ArrayList<>();
            this.matrix = new ArrayList<>();
        }

        @Override
        public void addNode(N nodeData) {
            this.nodes.add(new Node<>(nodeCount.getAndIncrement(), nodeData));
        }
        
        /**
         * This method makes up for shortage of the matrix entries.
         * The shortage calculates from difference between
         * number of the nodes and number of rows of the current adjacency matrix.
         * The additions are the empty Cell<E> objects, whose contents are initialized by null.
         * 
         * This method is not thread safe.
         */
        private void extendMatrix() {
            int shortage = this.nodes.size() - this.matrix.size();
            if(shortage <= 0){
                return;
            }
            this.matrix.forEach(row->{
                row.addAll(IntStream.range(0, shortage)
                        .mapToObj(i->new Cell<E>())
                        .collect(Collectors.toList()));
            });
            this.matrix.addAll(IntStream.range(0, shortage)
                    .mapToObj(r->IntStream.range(0, this.nodes.size())
                            .mapToObj(c->new Cell<E>())
                            .collect(Collectors.toList()))
                    .collect(Collectors.toList()));
        }
        
        @Override
        public void addEdge(Node<N> initialNode, E edgeData, Node<N> terminalNode) {
            this.extendMatrix();
            this.matrix.get(initialNode.index()).get(terminalNode.index()).set(edgeData);
        }

        @Override
        public Graph<N, E> getGraph() throws NullPointerException {
            return new AdjacencyMatrixGraph<>(this.numberOfEdges(), this.nodes,
                this.matrix.stream()
                    .map(l->l.stream()
                        .map(Cell::get)//Strip a cell container.
                        .collect(Collectors.toList()))
                    .collect(Collectors.toList()));
        }

        @Override
        public final int size() {
            return this.nodes.size();
        }

        @Override
        public final Node<N> getNode(int index) {
            return this.nodes.get(index);
        }

        @Override
        public Iterator<Node<N>> getNodeIterator() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        @Override
        public final Stream<Node<N>> getNodeStream() {
            return this.nodes.stream();
        }

        @Override
        public int numberOfEdges(Node<N> node) {
            return (int)this.matrix.get(node.index()).stream()
                    .filter(n->n.get()!=null)
                    .count();
        }

        @Override
        public int numberOfEdges() {
            return (int)this.matrix.stream()
                    .mapToInt(row->(int)row.stream()
                            .filter(n->n.get()!=null)
                            .count())
                    .sum();
        }


        @Override
        public Iterator<Edge<N, E>> getEdgeIterator(Node<N> node) {
            class WrapIterator<E> implements Iterator<E>{
                private final Iterator<Cell<E>> inner;

                public WrapIterator(final Iterator<Cell<E>> inner){
                    this.inner = inner;
                }
                
                @Override
                public boolean hasNext() {
                    return inner.hasNext();
                }

                @Override
                public E next() {
                    return this.inner.next().get();
                }
            }
            return new NullSkipIterator<>(node, new WrapIterator<>(this.matrix.get(node.index()).iterator()), i->this.nodes.get(i));
        }
        
        @Override
        public Stream<Edge<N, E>> getEdgeStream(Node<N> node){
            return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                    this.getEdgeIterator(node),
                    Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED
                    ),
                false);
        }
    }

    
    /**
     * The iterator is used to iterate with skipping null value.
     */
    private static class NullSkipIterator<N, E> implements Iterator<Edge<N, E>>{
        private final IntFunction<Node<N>> nodeGetter;
        private final Node<N> initial;
        private final Iterator<E> rowIterator;
        private int index;
        private E cash;
        
        /**
         * 
         * @param initial
         * @param rowIterator
         * @param nodeGetter 
         */
        public NullSkipIterator(Node<N> initial, Iterator<E> rowIterator, IntFunction<Node<N>> nodeGetter){
            super();
            this.initial = initial;
            this.rowIterator = rowIterator;
            this.index = -1;
            this.cash = null;
            this.nodeGetter = nodeGetter;
        }

        private boolean skipNulls(){
            for(;;){
                if(this.cash != null){
                    return true;
                }
                if(!this.rowIterator.hasNext()){
                    return false;
                }
                this.cash = this.rowIterator.next();
                ++this.index;
            }
        }
        
        @Override
        public final boolean hasNext() {
            return skipNulls();
        }

        @Override
        public final Edge<N, E> next() {
            skipNulls();
            E e = this.cash;
            this.cash = null;
            final Node<N> terminal = nodeGetter.apply(this.index);
            return new ConcreteEdge<>(this.initial, e, terminal);
        }
    }
    
    private final int numberOfEdges;
    private final ImmutableVector<Node<N>> nodes;
    private final ImmutableMatrix<E> matrix;
    
    protected AdjacencyMatrixGraph(int numberOfEdges, List<Node<N>> nodes, List<List<E>> matrix){
        super();
        this.numberOfEdges = numberOfEdges;
        this.nodes = new ArrayVector<>(nodes);
        this.matrix = new ArrayMatrix<>(matrix);
    }

    
    @Override
    public final int numberOfEdges() {
        return this.numberOfEdges;
    }

    @Override
    public final int size() {
        return this.nodes.size();
    }

    @Override
    public final Node<N> getNode(int index) {
        return this.nodes.get(index);
    }
    
    @Override
    public Iterator<Node<N>> getNodeIterator() {
        return this.nodes.iterator();
    }
    
    @Override
    public final Stream<Node<N>> getNodeStream() {
        return this.nodes.stream();
    }

    @Override
    public int numberOfEdges(Node<N> node) {
        return (int)this.matrix.get(node.index()).stream().filter(e->(e!=null)).count();
    }
    
    @Override
    public Iterator<Edge<N, E>> getEdgeIterator(Node<N> node) {
        return new NullSkipIterator<>(node, this.matrix.get(node.index()).iterator(), i->this.nodes.get(i));
    }
    
    @Override
    public Stream<Edge<N, E>> getEdgeStream(Node<N> node) {
        return StreamSupport.stream(
            Spliterators.spliteratorUnknownSize(
                this.getEdgeIterator(node),
                Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED
                ),
            false);
    }
}
