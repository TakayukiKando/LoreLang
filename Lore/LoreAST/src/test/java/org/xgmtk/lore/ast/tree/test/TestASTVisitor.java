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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.xgmtk.lore.ast.tree.ASTAttributes;
import org.xgmtk.lore.ast.tree.ASTNode;
import org.xgmtk.lore.ast.tree.ASTVisitor;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class TestASTVisitor {
    
    @Rule
    public TestName testName = new TestName();
    
    private ASTNode root;
    private String expected;
    private static final Logger LOGGER = Logger.getLogger(TestASTVisitor.class.getName());
    
    @Before
    public void setup(){
        this.root = SampleAST.SIMPLE_TREE;
        this.expected = SampleAST.SIMPLE_TREE_DUMP;
    }

    @Test
    public void testSetAndVisit(){
        LOGGER.log(Level.INFO, ()->"Start test \""+testName.getMethodName()+"\".");
        final StringBuilder builder = new StringBuilder();
        ASTVisitor visitor = new ASTVisitor();
        visitor.setEnterAction(n->builder.append(String.format("<%s>", n.getAttribute(ASTAttributes.SYMBOL))));
        visitor.setLeaveAction(n->builder.append(String.format("</%s>", n.getAttribute(ASTAttributes.SYMBOL))));
        this.root.startVisit(visitor);
        assertThat(builder.toString(), is(expected));
    }
    
    @Test
    public void testConstructAndVisit(){
        LOGGER.log(Level.INFO, ()->"Start test \""+testName.getMethodName()+"\".");
        final StringBuilder builder = new StringBuilder();
        ASTVisitor visitor = new ASTVisitor(
                n->builder.append(String.format("<%s>", n.getAttribute(ASTAttributes.SYMBOL))),
                n->builder.append(String.format("</%s>", n.getAttribute(ASTAttributes.SYMBOL)))
        );
        this.root.startVisit(visitor);
        assertThat(builder.toString(), is(expected));
    }
}
