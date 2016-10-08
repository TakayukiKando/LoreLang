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
package org.xgmtk.lore.graph.sections;

import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;
import org.xgmtk.lore.graph.Graph;
import org.xgmtk.lore.graph.GraphBuilder;
import org.xgmtk.lore.util.ImmutableVector;

/**
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <N>
 * @param <E>
 * @param <D> 
 */
public interface SectionGraph<N, E, D extends Direction> extends Graph<N, E> {
    /**
     * A section of sectioned graph.
     * 
     * @param <D> 
     */
    public interface Section<N, D extends Direction> extends Node<N>{
        /**
         * Get an integral value,
         * which indicates the barriers information of this section.
         * This method is mainly supposed to be used by
         * the internal of the graph building process.
         * 
         * @return An integral value,
         * which indicates the barriers information
         */
        public int getBarriers();
        
        /**
         * This predicate is true
         * if the given direction is enabled to move from the section.
         * @param direction
         * @return 
         */
        public boolean isBarriered(D direction);
        
        /**
         * Get a Stream of the directions which is enabled to move.
         * @return A Stream of the directions which is enabled to move.
         */
        public Stream<Direction> directions();
        
        /**
         * Get the coordinate of the section.
         * @return The coordinate of the section
         */
        public Coordinate position();
        
        /**
         * Get an Iterator of the directions which is enabled to move.
         * @return An Iterator of the directions which is enabled to move.
         */
        public default Iterator<Direction> directionsIterator(){
            return directions().iterator();
        }
    }
    
    /**
     * 
     * @param <N>
     * @param <D> 
     */
    public interface Line<N, D extends Direction>{
        /**
         * Get the x-axis offset of the line.
         * This method is mainly supposed to be used by
         * the internal of the graph building process.
         * 
         * @return 
         */
        public int getOffsetX();
        
        /**
         * Get a number of the sections in the line.
         * 
         * @return 
         */
        public int numberOfSections();
        
        public Stream<Section<N, D>> sections();
        
                
        /**
         * Get an Iterator of the sections of the line.
         * @return An Iterator of the directions which is enabled to move.
         */
        public default Iterator<Section<N, D>> sectionsIterator(){
            return sections().iterator();
        }
        /**
         * 
         * @param index index of the section in the line.
         * @return 
         */
        public Optional<Section<N, D>> getNthSection(int index);
        
        /**
         * 
         * @param x x-axis coordinate
         * @return 
         */
        public Optional<Section<N, D>> getSection(int x);
    }
    
    public interface Builder<N, E, D extends Direction> extends SectionGraph<N, E, D>, GraphBuilder<N, E>{

    }
    
    /**
     * Get the x-axis offset of the line.
     * This method is mainly supposed to be used by
     * the internal of the graph building process.
     * 
     * @return 
     */
    public int getOffsetY();
    
    public int numberOfLines();
    
    public Stream<Line<N, D>> lines();
    
    /**
     * Get an Iterator of the sections of the line.
     * @return An Iterator of the directions which is enabled to move.
     */
    public default Iterator<Line<N, D>> linesIterator(){
        return lines().iterator();
    }
    
    /**
     * 
     * @param index index of the line in the graph.
     * @return 
     */
    public Optional<Line<N, D>> getNthLine(int index);
    /**
     * 
     * @param y y-axis coordinate of the line
     * @return 
     */
    public Optional<Line<N, D>> getLine(int y);
    
    /**
     * 
     * @param index index of the node in the graph.
     * @return
     * @throws IndexOutOfBoundsException 
     */
    public Node<N> getNthSection(int index) throws IndexOutOfBoundsException;
}
