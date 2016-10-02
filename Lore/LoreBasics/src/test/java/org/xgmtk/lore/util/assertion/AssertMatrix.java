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
import org.xgmtk.lore.util.ArrayMatrix;
import org.xgmtk.lore.util.ImmutableMatrix;
import org.xgmtk.lore.util.ImmutableVector;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class AssertMatrix {
        
    public static <T> void assertCopying(ImmutableMatrix<T> m, Function<T, T> copier) {
        ImmutableMatrix<T> mCopy = new ArrayMatrix<>(m, copier);
        assertThat(mCopy, not(sameInstance(m)));
        assertThat(mCopy.numberOfRows(), is(m.numberOfRows()));
        assertThat(mCopy.numberOfColumns(), is(m.numberOfColumns()));
        assertThat(mCopy, is(m));
        Iterator<ImmutableVector<T>> mit = m.iterator();
        Iterator<ImmutableVector<T>> mCopyIt = mCopy.iterator();
        for(int i = 0; i < m.numberOfRows(); ++i){
            Iterator<T> rowIt = mit.next().iterator();
            Iterator<T> rowCopyIt = mCopyIt.next().iterator();
            for(int j = 0; j < m.numberOfColumns(); ++j){
                T e = rowIt.next();
                T eCopy = rowCopyIt.next();
                assertThat(eCopy, not(sameInstance(e)));
                assertThat(eCopy, is(e));
            }
        }
    }
}
