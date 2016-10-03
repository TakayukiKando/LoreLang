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

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import org.xgmtk.lore.graph.Graph;
import org.xgmtk.lore.util.ArrayVector;
import org.xgmtk.lore.util.ImmutableVector;

/**
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <N>
 * @param <E> 
 */
public abstract class AbstractGraphIterator<N, E> implements GraphIterator<N, E> {
    protected static class State<N, E>{
        public final Graph.Node<N> node;
        public final Iterator<Graph.Edge<N,E>> edges;
        
        public State(Graph.Node<N> currentNode, Iterator<Graph.Edge<N,E>> edges){
            this.node = currentNode;
            this.edges = edges;
        }
    }
    
    protected static class Record<N, E>{
        public boolean visited = false;
        public Graph.Edge<N,E> stepBefore = null;
    }
    
    protected final Graph<N, E> graph;
    protected final Graph.Node<N> start;
    protected final int maxDepth;
    protected final Deque<State<N, E>> states;
    protected final ImmutableVector<Record<N, E>> visitedRocords;

    /**
     * 
     * @param graph
     * @param start
     * @param maxDepth 
     */
    protected AbstractGraphIterator(Graph<N, E> graph, Graph.Node<N> start, int maxDepth){
        super();
        this.graph = graph;
        this.maxDepth = maxDepth;
        this.start = start;
        this.states = new ArrayDeque<>();
        this.visitedRocords = new ArrayVector<>(IntStream.range(0, graph.size()).mapToObj(i-> new Record<>()));
    }
    
    @Override
    public final Graph.Node<N> startNode() {
        return this.start;
    }
    
    @Override
    public final boolean hasNext() {
        return !this.states.isEmpty();
    }
    
    
    @Override
    public final boolean isVisited(final Graph.Node<N> node){
        return visitedRocords.get(node.index()).visited;
    }
    
    @Override
    public final GraphPath<N, E> getPath(final Graph.Node<N> goal) {
        Objects.requireNonNull(goal);
        Record<N, E> record = visitedRocords.get(goal.index());
        if(!record.visited){//Not reacheable.
            System.err.print("(Not reacheable.)");
            return new GraphPath<>(this.startNode(), goal);
        }
        if(record.stepBefore == null){//The goal node is same as the start node.
            System.err.print("(The goal node is same as the start node.)");
            return new GraphPath<>(this.startNode());
        }
        //Ordinary path.
        System.err.print("(Ordinary path.)");
        List<Graph.Edge<N, E>> steps = new LinkedList<>();
        while(record.stepBefore != null){
            System.err.println("("+record.stepBefore.terminalNode().index()+"<-"+record.stepBefore.initialNode().index()+")");
            steps.add(0, record.stepBefore);
            record = visitedRocords.get(record.stepBefore.initialNode().index());
        }
        return new GraphPath<>(new ArrayVector<>(steps));
    }
}
