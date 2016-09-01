/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xgmtk.lore.ast.tree;

/**
 * A collection of standard primitive attribute "name".
 * 
 * @author kando
 */
public class ASTAttributes{
    public enum LocationAttr implements ASTAttribute<CodeLocation>{
        LOCATION;
    }
    
    public static final LocationAttr LOCATION = LocationAttr.LOCATION;
    
    public enum StringAttr implements ASTAttribute<String>{
        SYMBOL;
    }
    
    public static final StringAttr SYMBOL = StringAttr.SYMBOL;
}
