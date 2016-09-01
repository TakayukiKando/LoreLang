/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xgmtk.lore.ast.stream;

import org.xgmtk.lore.ast.tree.ASTNode;
import org.xgmtk.lore.ast.tree.ASTAttribute;

/**
 *
 * @author kando
 */
public interface ASTEvent {

    public ASTEventType getEventType();

    public default boolean hasNode(){
        return false;
    }
    
    public ASTNode getNode();

    public default <T> T getAttribute(ASTAttribute<T> attribute) {
        return this.getNode().getAttribute(attribute);
    }
}
