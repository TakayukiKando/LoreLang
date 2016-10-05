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

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Implementation of immutable 1D array.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <T> Type of elements.
 */
public final class ArrayVector<T> implements ImmutableVector<T> {
    /**
     * Get zero-length vector.
     * 
     * @param <T> Type of elements.
     * @return Zero-length vector.
     */
    public static <T> ArrayVector<T> zeroLengthVector() {
        return new ArrayVector<>(Collections.emptyList());
    }
    
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
    public ArrayVector(Stream<T> elements) {
        super();
        this.elements = elements.toArray();
    }

    /**
     * Initializer.
     * 
     * @param elements 
     */
    public ArrayVector(List<T> elements) {
        super();
        this.elements = elements.toArray();
    }
      
    /**
     * Copy initializer.
     * 
     * @param src A source matrix to copying.
     * @param elementCopier element copier
     */
    public ArrayVector(ImmutableVector<T> src, Function<T, T> elementCopier) {
        super();
        this.elements = new Object[src.size()];
        int i = 0;
        for(T v : src){
            this.elements[i] = elementCopier.apply(v);
            ++i;
        }
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
    
    @Override
    public boolean equals(Object o){
        if(!(o instanceof ArrayVector<?>)){
            return false;
        }
        ArrayVector<?> v = (ArrayVector<?>)o;
        return Arrays.deepEquals(this.elements, v.elements);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Arrays.deepHashCode(this.elements);
        return hash;
    }
}
