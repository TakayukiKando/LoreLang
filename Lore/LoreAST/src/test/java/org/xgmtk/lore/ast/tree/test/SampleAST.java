/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xgmtk.lore.ast.tree.test;

import org.xgmtk.lore.ast.tree.ASTAttributes;
import org.xgmtk.lore.ast.tree.ASTNode;

/**
 *
 * @author kando
 */
public class SampleAST {

    static ASTNode createNode(String symbol, ASTNode... children) {
        ASTNode n = new ASTNode();
        n.setAttribute(ASTAttributes.SYMBOL, symbol);
        n.addChildren(children);
        return n;
    }

    public static ASTNode SIMPLE_TREE =
        createNode("root",
            createNode("child0"),
            createNode("child1",
                createNode("child1_0",
                    createNode("child1_0_0")
                ),
                createNode("child1_1")
            )
        );
    
    public static final String SIMPLE_TREE_DUMP =
        "<root>"
            +"<child0></child0>"
            +"<child1>"
                +"<child1_0>"
                    +"<child1_0_0></child1_0_0>"
                +"</child1_0>"
                +"<child1_1></child1_1>"
            +"</child1>"
        +"</root>";
}
