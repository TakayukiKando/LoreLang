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

import java.util.Objects;
import java.util.Optional;
import org.xgmtk.lore.graph.Graph;
import org.xgmtk.lore.util.ArrayVector;
import org.xgmtk.lore.util.ImmutableVector;

/**
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <N> Data type of a graph node contents.
 * @param <E> Data type of a graph edge contents.
 */
public final class GraphPath<N, E> {
    private final ImmutableVector<Graph.Edge<N, E>> steps;
    private final Graph.Node<N> start;
    private final Graph.Node<N> goal;
    
    /**
     * Initializer for a non-existent path.
     * 
     * @param start start node
     * @param goal goal node
     */
    public GraphPath(Graph.Node<N> start, Graph.Node<N> goal){
        super();
        Objects.requireNonNull(start);
        Objects.requireNonNull(goal);
        this.steps = null;
        this.start = start;
        this.goal = goal;
    }
    
    /**
     * Initializer for a zero-length path.
     * 
     * @param startAndGoal start and goal node
     */
    public GraphPath(Graph.Node<N> startAndGoal){
        super();
        Objects.requireNonNull(startAndGoal);
        this.steps = ArrayVector.zeroLengthVector();
        this.start = startAndGoal;
        this.goal = startAndGoal;
    }
    
    /**
     * Initializer.
     * 
     * @param steps steps of the path.
     */
    public GraphPath(ImmutableVector<Graph.Edge<N, E>> steps){
        super();
        Objects.requireNonNull(steps);
        this.steps = steps;
        this.start = this.steps.get(0).initialNode();
        int last = this.steps.size() - 1;
        System.err.println("(last: "+last+")");
        this.goal = this.steps.get(last).terminalNode();
    }

    /**
     * Get the steps from start node to goal node.
     * Method isPresent() of the Optional object would return false, if the path is non-existent.
     * 
     * @return 
     */
    public Optional<ImmutableVector<Graph.Edge<N, E>>> steps(){
        return Optional.ofNullable(this.steps);
    }
    
    /**
     * Get the start node of the path.
     * 
     * @return The start node.
     */
    public Graph.Node<N> startNode(){
        return this.start;
    }

    /**
     * Get the goal node of the path.
     * 
     * @return The goal node.
     */
    public Graph.Node<N> goalNode(){
        return this.goal;
    }
}
