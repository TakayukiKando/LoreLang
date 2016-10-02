/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xgmtk.lore.graph.iterator;

import java.util.ArrayDeque;
import java.util.Collections;
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
 * @param <N> Data type of a graph node contents.
 * @param <E> Data type of a graph edge contents.
 */
public final class DepthFirstIterator<N, E> implements GraphIterator<N, E> {

    private final Graph<N, E> graph;

    private static class State<N, E>{
        public final Graph.Node<N> node;
        public final Iterator<Graph.Edge<N,E>> edges;
        
        public State(Graph.Node<N> currentNode, Iterator<Graph.Edge<N,E>> edges){
            this.node = currentNode;
            this.edges = edges;
        }
    }

    private static class Record<N, E>{
        public boolean visited = false;
        public Graph.Edge<N,E> stepBefore = null;
    }
    
    private final Graph.Node<N> start;
    private final int depth;
    private final Deque<State<N, E>> states;
    private final ImmutableVector<Record<N, E>> visitedRocords;

    /**
     * 
     * @param graph
     * @param start
     * @param depth 
     */
    public DepthFirstIterator(Graph<N, E> graph, Graph.Node<N> start, int depth){
        super();
        this.graph = graph;
        this.depth = 0;
        this.start = start;
        this.states = new ArrayDeque<>();
        this.visitedRocords = new ArrayVector<>(IntStream.range(0, graph.size()).mapToObj(i-> new Record<>()));
        this.states.push(new State<>(start, graph.getEdgeIterator(start)));
    }
    
    /**
     * 
     * @param graph
     * @param start 
     */
    public DepthFirstIterator(Graph<N, E> graph, Graph.Node<N> start){
        this(graph, start, Integer.MAX_VALUE);
    }
    
    @Override
    public Graph.Node<N> startNode() {
        return this.start;
    }
    
    @Override
    public boolean hasNext() {
        return !this.states.isEmpty();
    }

    @Override
    public Graph.Node<N> next() {
        if(!this.hasNext()){
            throw new IndexOutOfBoundsException("All reachable nodes has been visited already.");
        }
        State<N, E> current = this.states.peek();
        final Graph.Node<N> currentNode = current.node;
        final Record<N, E> currentRecord = this.visitedRocords.get(currentNode.index());
        Graph.Node<N> node = null;
        if(!currentRecord.visited){
            currentRecord.visited = true;
            node = currentNode;
        }
        for(;;){
            while(!current.edges.hasNext()){
                this.states.pop();
                if(this.states.isEmpty()){
                    return node;
                }
                current = this.states.peek();
            }
            final Graph.Edge<N, E> nextEdge = current.edges.next();
            final Graph.Node<N> next = nextEdge.terminalNode();
            if(this.visitedRocords.get(next.index()).visited){
                continue;
            }
            this.visitedRocords.get(next.index()).stepBefore = nextEdge;
            System.err.println("<Node next class: \""+next.getClass().getName()+"\">");
            this.states.push(new State<>(next, graph.getEdgeIterator(next)));
            return node;
        }
    }

    @Override
    public boolean isVisited(final Graph.Node<N> node){
        return visitedRocords.get(node.index()).visited;
    }
    
    @Override
    public GraphPath<N, E> getPath(final Graph.Node<N> goal) {
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
