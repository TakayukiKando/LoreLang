/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xgmtk.lore.ast;

/**
 * Generic exception concerned with AST tree.
 * 
 * @author kando
 */
public class ASTException extends Exception{
    /**
     * Constructs an {@code ASTTreeException} with the specified detail message
     * and cause.
     * 
     * @param message
     * @param cause 
     */
    public ASTException(String message, Throwable cause){
        super(message, cause);
    }
    
    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message 
     */
    public ASTException(String message){
        super(message);
    }
    
    /**
     * Constructs a new exception with the specified cause.
     * 
     * @param cause 
     */
    public ASTException(Throwable cause){
        super(cause);
    }
}
