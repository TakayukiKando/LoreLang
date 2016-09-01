/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xgmtk.lore.ast.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * AST(Abstract Syntax Tree) node.
 * The node holds attributes and child nodes (subtrees).
 * 
 * @author kando
 */
public final class ASTNode {
    private final Map<ASTAttribute<?>, Object> attrs;
    private final List<ASTNode> children;
    
    /**
     * Initializer of an AST node.
     * 
     * @param children The list of child nodes of this node.
     */
    public ASTNode(List<ASTNode> children){
      super();
      this.attrs = new HashMap<>();
      this.children = new ArrayList<>();
      this.addChildren(children);
    }
    
    /**
     * Initializer of an AST node.
     * 
     * @param children The child nodes of this node.
     */
    public ASTNode(ASTNode...children){
        this(Arrays.asList(children));
    }
    
    /**
     * Set new value to the specified attribute of this node.
     * 
     * @param <T> Type of attribute's value.
     * @param attribute An enum value that specifies attribute name.
     * @param value A new value of the specified attribute.
     */
    public final <T> void setAttribute(ASTAttribute<T> attribute, T value){
        this.attrs.put(attribute, value);
    }
    
    /**
     * Get the value of the specified attribute of this node.
     * 
     * @param <T> Type of attribute's value.
     * @param attribute An enum value that specifies attribute name.
     * @return The specified attribute's value.
     */
    public final <T> T getAttribute(ASTAttribute<T> attribute){
        Object v = this.attrs.get(attribute);
        return (T)v;
    }
    
    /**
     * Get an unmodifiable map of attributes of the node.
     * 
     * @return 
     */
    public Map<ASTAttribute<?>, Object> attributes() {
        return Collections.unmodifiableMap(this.attrs);
    }

    /**
     * Inspect the specified attribute exists or not in the node.
     * 
     * @param <T> Type of attribute's value.
     * @param attribute An enum value that specifies attribute name.
     * @return true, only if the specified attribute exists.
     */
    public final <T> boolean hasAttribute(ASTAttribute<T> attribute){
        return this.attrs.containsKey(attribute);
    }

    /**
     * Add child nodes.
     * 
     * @param children The additional child nodes of this node.
     */
    public final void addChildren(ASTNode...children){
        this.addChildren(Arrays.asList(children));
    }
    
    /**
     * Add child nodes.
     * 
     * @param children The list of additional child nodes of this node.
     */
    public final void addChildren(List<ASTNode> children) {
        this.children.addAll(children);
    }
    
    /**
     * An unmodifiable list of child nodes.
     * 
     * @return 
     */
    public List<ASTNode> children(){
        return Collections.unmodifiableList(this.children);
    }
    
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof ASTNode)){
            return false;
        }
        ASTNode node = (ASTNode)obj;
        return Objects.equals(this.attrs, node.attrs)
                && Objects.equals(this.children, node.children);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.attrs);
        hash = 17 * hash + Objects.hashCode(this.children);
        return hash;
    }

    /**
     * This method starts that the AST visitor visits subtree.
     * The root of the subtree is this node.
     * 
     * @param visitor 
     */
    public final void startVisit(ASTVisitor visitor) {
        visitor.start(this);
    }
}
