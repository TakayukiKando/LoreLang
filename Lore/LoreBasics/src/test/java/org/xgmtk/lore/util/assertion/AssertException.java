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

import static org.junit.Assert.fail;

import java.util.function.Consumer;
import org.xgmtk.lore.util.RunnableWithException;

/**
 * Collection of utility methods for unit test.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * 
 */
public class AssertException {
    /**
     * The utility method for unit test.
     * 
     * @param <EX> Type of the expected exception, which would be thrown.
     * @param <R> Type of the action, which is a target of assertion.
     * @param <C> Type of consumer function which would assert a thrown excption.
     * @param action The action, which is a target of assertion.
     * @param cls The expected exception, which would be thrown.
     * @param checker The consumer function which would assert a thrown excption.
     */
    @SuppressWarnings("unchecked")
    public static <EX extends Throwable, R extends RunnableWithException<EX>, C extends Consumer<EX>> void assertException(R action, Class<EX> cls, C checker){
        try{
            action.run();
            fail("The expected exception is not thrown.");
        }catch(AssertionError e){
            throw e;
        }catch(Throwable ex){
            try{
                checker.accept((EX)ex);
            }catch(ClassCastException e){
                throw new AssertionError("Unexpected exception is thrown.", ex);
            }
        }
    }
    
    /**
     * The utility method for unit test.
     * 
     * @param <EX> Type of the expected exception, which would be thrown.
     * @param <R> Type of the action, which is a target of assertion.
     * @param action The action, which is a target of assertion.
     * @param cls The expected exception, which would be thrown.
     */
    public static <EX extends Throwable, R extends RunnableWithException<EX>> void assertException(R action, Class<EX> cls){
        assertException(action, cls, (EX ex)->{});
    }
}
