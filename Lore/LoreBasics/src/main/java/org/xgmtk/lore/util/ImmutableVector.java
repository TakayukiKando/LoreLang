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
 * Interface of immutable 1D array.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <T> Type of elements.
 */
public interface ImmutableVector<T> extends Iterable<T> {
    /**
     * Get size of the vector.
     * 
     * @return Size of the vector.
     */
    public int size();
    
    /**
     * Get i-th element.
     * This is suitable for random access use.
     * This is NOT efficent in sequential access.
     * 
     * @param i index.
     * @return i-th element.
     */
    public T get(int i);
    
    /**
     * Get stream of elements of the vector.
     * 
     * @return The stream of elements of the vector.
     */
    public default Stream<T> stream(){
        return StreamSupport.stream(
            Spliterators.spliterator(
                this.iterator(),
                this.size(),
                Spliterator.IMMUTABLE | Spliterator.NONNULL | Spliterator.ORDERED | Spliterator.SIZED
                ),
            false);
    }
}
