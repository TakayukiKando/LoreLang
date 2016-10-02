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
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Implementation of immutable 2D array.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <T> Type of elements.
 */
public final class ArrayMatrix<T> implements ImmutableMatrix<T> {
    /**
     * Inner use only.
     * 
     * @param <T> 
     */
    private final class ColIterator<T> implements Iterator<T> {
        private int index;
        private final int sup;
        
        /**
         * Initializer.
         * 
         * @param offset 
         */
        public ColIterator(int offset) {
            this.index = offset;
            this.sup = offset+cols;
        }

        @Override
        public boolean hasNext() {
            return this.index < this.sup;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            if(!hasNext()){
                throw new IndexOutOfBoundsException();
            }
            return (T)elements[this.index++];
        }
    }
    
    /**
     * Inner use only.
     * 
     * @param <T> 
     */
    private final class RowVector<T> implements ImmutableVector<T> {
        private final int rowIndex;
        private final int offset;
        
        /**
         * Initializer.
         * 
         * @param rowIndex
         * @param offset 
         */
        public RowVector(int rowIndex, int offset) {
            this.rowIndex = rowIndex;
            this.offset = offset;
        }

        @Override
        public int size() {
            return cols;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T get(int i) {
            if(i >= cols){
                throw new IndexOutOfBoundsException();
            }
            return (T)elements[this.rowIndex * cols + i];
        }

        @Override
        public Iterator<T> iterator() {
            return new ColIterator<>(this.offset);
        }
    }
    
    /**
     * Inner use only.
     * 
     * @param <T> 
     */
    private class RowVectorIterator<T> implements Iterator<ImmutableVector<T>> {
        private int index;
        private int rowIndex;
        private final int rowSup;

        /**
         * Initializer.
         */
        public RowVectorIterator() {
            this.index = 0;
            this.rowSup = rows*cols;
        }

        @Override
        public boolean hasNext() {
            return this.index < this.rowSup;
        }

        @Override
        public ImmutableVector<T> next() {
            if(!hasNext()){
                throw new IndexOutOfBoundsException();
            }
            int ix = this.index;
            this.index+=cols;
            return new RowVector<>(this.rowIndex++, ix);
        }
    }
    
    private final int rows;
    private final int cols;
    private final Object[] elements;
    
    public ArrayMatrix(List<List<T>> elements) {
        super();
        this.rows = elements.size();
        this.cols = this.rows == 0? 0: elements.get(0).size();
        this.elements = new Object[rows*cols];
        int i = 0;
        for(List<T> row : elements){
            for(T v : row){
                this.elements[i] = v;
                ++i;
            }
        }
    }
    
    /**
     * Copy initializer.
     * 
     * @param src A source matrix to copying.
     * @param elementCopier element copier
     */
    public ArrayMatrix(ImmutableMatrix<T> src, Function<T, T> elementCopier) {
        super();
        this.rows = src.numberOfRows();
        this.cols = src.numberOfColumns();
        this.elements = new Object[rows*cols];
        int i = 0;
        for(ImmutableVector<T> row : src){
            for(T v : row){
                this.elements[i] = elementCopier.apply(v);
                ++i;
            }
        }
    }

    @Override
    public int numberOfRows() {
        return this.rows;
    }

    @Override
    public int numberOfColumns() {
        return this.cols;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int indexRow, int indexColumn) {
        if(indexRow >= this.rows || indexColumn >= this.cols){
            throw new IndexOutOfBoundsException();
        }
        return (T)this.elements[indexRow * this.cols+ indexColumn];
    }
    
    @Override
    public ImmutableVector<T> get(int indexRow) {
        if(indexRow >= this.rows){
            throw new IndexOutOfBoundsException();
        }
        return new RowVector<>(indexRow, indexRow*this.cols);
    }

    @Override
    public Iterator<ImmutableVector<T>> iterator() {
        return new RowVectorIterator<>();
    }
    
    @Override
    public boolean equals(Object o){
        if(!(o instanceof ArrayMatrix<?>)){
            return false;
        }
        ArrayMatrix<?> v = (ArrayMatrix<?>)o;
        if(this.numberOfRows() != v.numberOfRows() || this.numberOfColumns() != v.numberOfColumns()){
            return false;
        }
        return Arrays.deepEquals(this.elements, v.elements);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.rows;
        hash = 47 * hash + this.cols;
        hash = 47 * hash + Arrays.deepHashCode(this.elements);
        return hash;
    }
}
