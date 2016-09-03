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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.xgmtk.lore.ast.tree.ASTAttributes;
import org.xgmtk.lore.ast.tree.CodeLocation;
import org.xgmtk.lore.ast.tree.ASTNode;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class TestASTNode {
    private static final Path USER_DIR = Paths.get(System.getProperty("user.dir"));
    
    @Rule
    public TestName testName = new TestName();
    private static final Logger LOGGER = Logger.getLogger(TestASTNode.class.getName());
    
    @Test
    public void testNode(){
        LOGGER.log(Level.INFO, ()->"Start "+testName.getMethodName());
        ASTNode root = createTree();
        
        assertThat(root.hasAttribute(ASTAttributes.LOCATION), is(true));
        assertThat(root.hasAttribute(ASTAttributes.SYMBOL), is(true));
        assertThat(root.getAttribute(ASTAttributes.LOCATION), is(new CodeLocation(USER_DIR.resolve("source.lore").toUri(), 0, 0)));
        assertThat(root.getAttribute(ASTAttributes.SYMBOL), is("root"));
        
        assertThat(root.children().stream()
                .allMatch(node->node.hasAttribute(ASTAttributes.LOCATION)), is(true));
        assertThat(root.children().stream()
                .allMatch(node->node.hasAttribute(ASTAttributes.SYMBOL)), is(true));
        
        final ASTNode root1 = createTree();
        final ASTNode root2 = createTree();
        assertThat(root1, is(root));
        root1.setAttribute(ASTAttributes.SYMBOL, "ROOT");
        assertThat(root1, is(not(root)));
        root2.addChildren(new ASTNode());
        assertThat(root2, is(not(root)));
    }

    private ASTNode createTree() {
        ASTNode root = new ASTNode();
        final CodeLocation location = new CodeLocation(USER_DIR.resolve("source.lore").toUri(), 0, 0);
        root.setAttribute(ASTAttributes.LOCATION, location);
        root.setAttribute(ASTAttributes.SYMBOL, "root");
        root.addChildren(IntStream.range(0, 1)
                .mapToObj(i->{
                    ASTNode n = new ASTNode();
                    n.setAttribute(ASTAttributes.LOCATION, location.locate(i, 0));
                    n.setAttribute(ASTAttributes.SYMBOL, "child"+i);
                    return n;
                })
                .collect(Collectors.toList()));
        return root;
    }
}
