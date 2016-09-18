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

import java.util.Iterator;
import java.util.List;

/**
 * Implementation of immutable 1D array.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <T> Type of elements.
 */
public final class ArrayVector<T> implements ImmutableVector<T> {
    private class InnerIterator<T> implements Iterator<T>{
        /**
         * Current index;
         */
        private int index;
        private final int sup;
        
        /**
         * Initializer.
         * 
         * @param size 
         */
        private InnerIterator(){
            super();
            this.index = 0;
            this.sup = elements.length;
        }
        
        @Override
        public final boolean hasNext() {
            return index < sup;
        }

        @Override
        @SuppressWarnings("unchecked")
        public final T next() {
            return (T)elements[(this.index++)];
        }
    }
    
    private final Object[] elements;
    
    /**
     * Initializer.
     * 
     * @param elements 
     */
    public ArrayVector(List<T> elements) {
        super();
        this.elements = elements.toArray();
    }

    @Override
    public int size() {
        return this.elements.length;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public T get(int i) {
        return (T)this.elements[i];
    }

    @Override
    public Iterator<T> iterator() {
        return new InnerIterator<>();
    }
}
