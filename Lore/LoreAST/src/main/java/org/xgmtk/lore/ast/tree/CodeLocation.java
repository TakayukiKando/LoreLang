/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xgmtk.lore.ast.tree;

import java.net.URI;
import java.util.Objects;

/**
 * An information of location that the node is derived.
 * 
 * @author kando
 */
public final class CodeLocation {
    /**
     * The URI of the source text that the node is derived.
     */
    public final URI source;
    /**
     * The line number of line that the node is derived.
     */
    public final int line;
    /**
     * The location of column that the node is derived.
     */
    public final int column;
    
    /**
     * Initializer of an information of location that the node is derived.
     * 
     * @param source The URI of the source text that the node is derived.
     * @param line The line number of line that the node is derived.
     * @param column The location of column that the node is derived.
     */
    public CodeLocation(URI source, int line, int column){
        super();
        this.source = source;
        this.line = line;
        this.column = column;
    }
    
    /**
     * Initializer of an information of location that the node is derived.
     * Its line and column equal 0.
     * 
     * @param source The URI of the source text that the node is derived.
     */
    public CodeLocation(URI source){
        this(source, 0, 0);
    }
    
    /**
     * Create a new CodeLocation object.
     * The new location has same source as this location.
     * But, its line and column are specified via arguments of the method.
     * 
     * @param line The line number of line that the node is derived.
     * @param column The location of column that the node is derived.
     * @return 
     */
    public final CodeLocation locate(int line, int column){
        return new CodeLocation(this.source, line, column);
    }
    
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof CodeLocation)){
            return false;
        }
        CodeLocation loc = (CodeLocation)obj;
        return Objects.equals(this.source, loc.source)
                && this.line == loc.line
                && this.column == loc.column;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.source);
        hash = 59 * hash + this.line;
        hash = 59 * hash + this.column;
        return hash;
    }
}
