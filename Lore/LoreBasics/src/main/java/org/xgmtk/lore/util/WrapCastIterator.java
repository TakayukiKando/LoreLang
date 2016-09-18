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

/**
 * Type conversion iterator adopter.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <D> Domain of type conversion.
 * @param <R> Range of type conversion.
 */
public class WrapCastIterator<D,R> implements Iterator<R> {
    private final Iterator<D> inner;
    
    /**
     * Initializer.
     * 
     * @param wrappedIterator source.
     */
    public WrapCastIterator(Iterator<D> wrappedIterator){
        this.inner = wrappedIterator;
    }

    @Override
    public boolean hasNext() {
        return this.inner.hasNext();
    }

    @Override
    @SuppressWarnings("unchecked")
    public R next() {
        return (R)this.inner.next();
    }
}
