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
package org.xgmtk.lore.util;

import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Interface of immutable 2D array.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <T> Type of elements.
 */
public interface ImmutableMatrix<T> extends Iterable<ImmutableVector<T>>{
    /**
     * 
     * @return 
     */
    public int numberOfRows();
    
    /**
     * 
     * @return 
     */
    public int numberOfColumns();
    
    /**
     * Get (indexRow,indexColumn) element of the matrix.
     * 
     * This is suitable for random access use.
     * This is NOT efficent in sequential access.
     * 
     * @param indexRow
     * @param indexColumn
     * @return 
     */
    public T get(int indexRow, int indexColumn);
    
    /**
     * Get indexRow-th row vector of the matrix.
     * 
     * This is suitable for random access use.
     * This is NOT efficent in sequential access.
     * 
     * @param indexRow
     * @return 
     */
    public ImmutableVector<T> get(int indexRow);
    
    /**
     * Get stream of elements of the vector.
     * 
     * @return The stream of elements of the vector.
     */
    public default Stream<ImmutableVector<T>> stream(){
        return StreamSupport.stream(
            Spliterators.spliterator(
                this.iterator(),
                this.numberOfRows(),
                Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.SIZED
                ),
            false);
    }
}
