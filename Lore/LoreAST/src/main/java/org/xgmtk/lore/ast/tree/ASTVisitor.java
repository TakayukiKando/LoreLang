/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xgmtk.lore.ast.tree;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;

/**
 * AST(Abstract Syntax Tree) visitor class.
 * This visitor visits nodes of the specified subtree of the AST,
 * and it visits node with depth-first manner.
 * User can register 2 actions.
 * The 1st action is called in the time of the entering to a new node.
 * The 2nd action is called in the time of the leaving from the node.
 * 
 * @author kando
 */
public class ASTVisitor {
    /**
     * This class is used to an entry of stack.
     */
    private class Entry{
        public final ASTNode node;
        public int index;
        
        public Entry(ASTNode node){
            this.node = node;
            this.index = 0;
        }
    }
    
    /**
     * Default action for entering/leaving the node.
     * This action does nothing.
     */
    public static final Consumer<ASTNode> DEFAULT_ACTION = n->{};
    
    private Consumer<ASTNode> enterAction;
    private Consumer<ASTNode> leaveAction;
    private Deque<Entry> stack;
    
    /**
     * Initialize AST visitor.
     * 
     * @param enterAction The action is called in the time of the entering to a new node.
     * @param leaveAction The action is called in the time of the leaving from the node.
     */
    public ASTVisitor(Consumer<ASTNode> enterAction, Consumer<ASTNode> leaveAction){
        this.setEnterAction(enterAction);
        this.setLeaveAction(leaveAction);
        this.stack = new ArrayDeque<>();
    }
    
    /**
     * Initialize AST visitor with default actions.
     */
    public ASTVisitor(){
        this(DEFAULT_ACTION, DEFAULT_ACTION);
    }
    
    /**
     * Set the action which is called in the time of the entering to a new node.
     * 
     * @param action The action is called in the time of the entering to a new node.
     */
    public final void setEnterAction(Consumer<ASTNode> action){
        this.enterAction = action;
    }
    
    /**
     * Set the action which is called in the time of the leaving from the node.
     * 
     * @param action The action is called in the time of the leaving from the node.
     */
    public final void setLeaveAction(Consumer<ASTNode> action){
        this.leaveAction = action;
    }
    
    /**
     * Start visiting.
     * 
     * @param root The root node of the visiting subtree.
     */
    void start(ASTNode root) {
        Entry current = new Entry(root);
        this.enterAction.accept(current.node);
        for(;;){
            if(current.index < current.node.children().size()){
                Entry next = new Entry(current.node.children().get(current.index));
                ++current.index;
                this.stack.push(current);
                current = next;
                this.enterAction.accept(current.node);
                continue;
            }
            this.leaveAction.accept(current.node);
            if(this.stack.isEmpty()){
                break;
            }
            current = this.stack.pop();
        }
    }
}
