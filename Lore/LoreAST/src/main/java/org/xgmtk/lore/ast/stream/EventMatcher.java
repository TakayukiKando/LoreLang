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
package org.xgmtk.lore.ast.stream;

import java.util.Objects;
import java.util.function.Predicate;
import org.xgmtk.lore.ast.tree.ASTNode;
import org.xgmtk.lore.ast.tree.ASTAttribute;

/**
 * Collections of the generating methods
 * of predicates for ASTEvent matching.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class EventMatcher {
    /**
     * TODO write JavaDoc comment.
     * 
     * @param <T>
     * @param type
     * @return 
     */
    public static <T> Predicate<ASTEvent> match(ASTEventType type){
        return e->type==e.getEventType();
    }
    
    /**
     * TODO write JavaDoc comment.
     * 
     * @param <T>
     * @param attribute
     * @param value
     * @return 
     */
    public static <T> Predicate<ASTEvent> match(ASTAttribute<T> attribute, T value){
        return ev->ev.getAttribute(attribute).equals(value);
    }
    
    /**
     * TODO write JavaDoc comment.
     * 
     * @param <T>
     * @param type
     * @param attribute
     * @param value
     * @return 
     */
    public static <T> Predicate<ASTEvent> match(ASTEventType type,
            ASTAttribute<T> attribute, T value){
        return match(type).and(match(attribute, value));
    }
    
    /**
     * TODO write JavaDoc comment.
     * 
     * @param <T>
     * @param attribute
     * @param value
     * @return 
     */
    public static <T> Predicate<ASTEvent> enter(ASTAttribute<T> attribute, T value){
        return match(ASTEventType.ENTER, attribute, value);
    }

    /**
     * TODO write JavaDoc comment.
     * 
     * @param <T>
     * @param n
     * @return 
     */
    public static <T> Predicate<ASTEvent> same(ASTNode n){
        return (ev)->Objects.equals(n, ev.getNode());
    }
    
    /**
     * TODO write JavaDoc comment.
     * 
     * @param <T>
     * @param n
     * @return 
     */
    public static <T> Predicate<ASTEvent> leave(ASTNode n){
        return same(n).and(match(ASTEventType.LEAVE));
    }
}
