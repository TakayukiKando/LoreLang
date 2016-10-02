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
package org.xgmtk.lore.util.assertion;

import java.util.Iterator;
import java.util.function.Function;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import org.xgmtk.lore.util.ArrayVector;
import org.xgmtk.lore.util.ImmutableVector;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class AssertVector {
    /**
     * 
     * @param <T>
     * @param v
     * @param copier 
     */
    public static <T> void assertCopying(ImmutableVector<T> v, Function<T, T> copier) {
        ImmutableVector<T> vCopy = new ArrayVector<>(v, copier);
        assertThat(vCopy, not(sameInstance(v)));
        assertThat(vCopy.size(), is(v.size()));
        assertThat(vCopy, is(v));
        Iterator<T> vit = v.iterator();
        Iterator<T> vCopyIt = vCopy.iterator();
        for(int i = 0; i < v.size(); ++i){
            T e = vit.next();
            T eCopy = vCopyIt.next();
            assertThat(eCopy, not(sameInstance(e)));
            assertThat(eCopy, is(e));
        }
    }
}
