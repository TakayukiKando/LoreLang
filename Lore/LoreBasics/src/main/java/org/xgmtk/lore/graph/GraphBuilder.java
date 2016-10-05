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

/**
 * The graph builder interface.
 * Because a graph builder itself is also a graph,
 * the graph builder can be used as a mutable graph.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <N> Data type of a node contents.
 * @param <E> Data type of an edge contents.
 */
public interface GraphBuilder<N, E> extends Graph<N, E> {
    
    /**
     * Return a built graph.
     * 
     * @return A built graph.
     * @throws NullPointerException A graph has not built yet.
     */
    public Graph<N, E> getGraph() throws NullPointerException;
}
