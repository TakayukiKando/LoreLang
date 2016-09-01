/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author kando
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
