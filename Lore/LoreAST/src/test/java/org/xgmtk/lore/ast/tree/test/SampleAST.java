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
package org.xgmtk.lore.ast.tree.test;

import org.xgmtk.lore.ast.tree.ASTAttributes;
import org.xgmtk.lore.ast.tree.ASTNode;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
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
