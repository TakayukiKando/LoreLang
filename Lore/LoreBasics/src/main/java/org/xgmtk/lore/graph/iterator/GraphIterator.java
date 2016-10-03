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

import java.util.Iterator;
import org.xgmtk.lore.graph.Graph;

/**
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <N> Data type of a graph node contents.
 * @param <E> Data type of a graph edge contents.
 */
public interface GraphIterator<N, E> extends Iterator<Graph.Node<N>> {
    /**
     * Get the start node of the iterator.
     * 
     * @return The start node of the iterator.
     */
    public Graph.Node<N> startNode();
    
    /**
     * Get the path from the start node to the specified goal node.
     * 
     * @param node
     * @return 
     */
    public boolean isVisited(Graph.Node<N> node);
    
    /**
     * Get the path from the start node to the specified goal node.
     * 
     * @param goal
     * @return 
     */
    public GraphPath<N,E> getPath(Graph.Node<N> goal);
}