/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * An iterator for the depth first serch on a Graph&lt;N, E&gt;.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <N> Data type of a graph node contents.
 * @param <E> Data type of a graph edge contents.
 */
public final class DepthFirstIterator<N, E> extends AbstractGraphIterator<N, E> {
    /**
     * 
     * @param graph
     * @param start
     * @param maxDepth 
     */
    public DepthFirstIterator(Graph<N, E> graph, Graph.Node<N> start, int maxDepth){
        super(graph, start, maxDepth);
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
    public Graph.Node<N> next() {
        if(!this.hasNext()){
            throw new IndexOutOfBoundsException("All reachable nodes has been visited already.");
        }
        State<N, E> current = this.states.peek();
        final Graph.Node<N> currentNode = current.node;
        final Record<N, E> currentRecord = this.visitedRocords.get(currentNode.index());
        currentRecord.visited = true;
        final Graph.Node<N> node = currentNode;

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
}
