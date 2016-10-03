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

import org.xgmtk.lore.graph.Graph;

/**
 * An iterator for the breadth first serch on a Graph&lt;N, E&gt;.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <N>
 * @param <E> 
 */
public class BreadthFirstIterator<N, E> extends AbstractGraphIterator<N, E> {
    /**
     * 
     * @param graph
     * @param start
     * @param maxDepth 
     */
    public BreadthFirstIterator(Graph<N, E> graph, Graph.Node<N> start, int maxDepth){
        super(graph, start, maxDepth);
        this.visitedRocords.get(start.index()).visited = true;
        this.states.offer(new State<>(start, graph.getEdgeIterator(start)));
    }

    /**
     * 
     * @param graph
     * @param start 
     */
    public BreadthFirstIterator(Graph<N, E> graph, Graph.Node<N> start){
        this(graph, start, Integer.MAX_VALUE);
    }

    @Override
    public Graph.Node<N> next() {
        if(!this.hasNext()){
            throw new IndexOutOfBoundsException("All reachable nodes has been visited already.");
        }
        AbstractGraphIterator.State<N, E> current = this.states.peek();
        final Graph.Node<N> currentNode = current.node;
        final AbstractGraphIterator.Record<N, E> currentRecord = this.visitedRocords.get(currentNode.index());
        final Graph.Node<N> node = currentNode;
        //System.err.println("Visit #"+node.index());
        while(current.edges.hasNext()){
            Graph.Edge<N, E> nextEdge = current.edges.next();
            Graph.Node<N> next = nextEdge.terminalNode();
            //System.err.print("\tGoing to #"+next.index()+" ...");
            if(this.visitedRocords.get(next.index()).visited){
                //System.err.println("already visited. skip.");
                continue;
            }
            //System.err.println("not yet visited. offer to queue.");
            final Record<N, E> nextRecord = this.visitedRocords.get(next.index());
            nextRecord.stepBefore = nextEdge;
            nextRecord.visited = true;
            this.states.offer(new AbstractGraphIterator.State<>(next, graph.getEdgeIterator(next)));
        }
        this.states.poll();
        return node;
    }
}
