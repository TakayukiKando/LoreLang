/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xgmtk.lore.ast.stream;

import org.xgmtk.lore.ast.ASTException;

/**
 *
 * @author kando
 */
public class ASTStreamInvalidException extends ASTException {

    private final ASTEvent current;

    /**
     * 
     * @param current 
     */
    ASTStreamInvalidException(ASTEvent current) {
        super("Current event type "+current.getEventType()+"(Node: \""+current.getNode()+"\" is not saticefy requirement.");
        this.current = current;
    }
    
    /**
     * 
     * @return 
     */
    public ASTEvent getCurrentEvent(){
        return this.current;
    }
}
