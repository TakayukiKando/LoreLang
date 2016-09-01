/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author kando
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
