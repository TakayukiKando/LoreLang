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
package org.xgmtk.lore.ast.stream.test;

import java.util.logging.Level;
import java.util.logging.Logger;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.xgmtk.lore.ast.stream.ASTStream;
import org.xgmtk.lore.ast.stream.ASTEventType;
import org.xgmtk.lore.ast.stream.ASTStreamInvalidException;
import static org.xgmtk.lore.ast.stream.EventMatcher.enter;
import static org.xgmtk.lore.ast.stream.EventMatcher.leave;
import org.xgmtk.lore.ast.tree.ASTAttributes;
import org.xgmtk.lore.ast.tree.ASTNode;
import org.xgmtk.lore.ast.tree.test.SampleAST;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;
import static org.xgmtk.lore.ast.stream.EventMatcher.match;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class TestTreeNodeStream {
        
    @Rule
    public TestName testName = new TestName();
    
    private ASTNode root;
    private static final Logger LOGGER =
            Logger.getLogger(TestTreeNodeStream.class.getName());
    
    @Before
    public void setup(){
        this.root = SampleAST.SIMPLE_TREE;
    }

    
    private void assertAndRequire(ASTStream stream, final ASTEventType t, final ASTAttributes.StringAttr s, final String name) throws ASTStreamInvalidException {
        try{
            assertThat(stream.is(match(t, s, name)), is(true));
            stream.require(match(t, s, name));
        }catch(Throwable ex){
            LOGGER.log(Level.INFO, ()->"Current event{ "+stream.getEvent()+" }");
            throw ex;
        }
    }

    private void assertAndRequire(ASTStream stream, final ASTEventType t) throws ASTStreamInvalidException {
        try{
            assertThat(stream.is(match(t)), is(true));
            stream.require(match(t));
        }catch(Throwable ex){
            LOGGER.log(Level.INFO, ()->"Current event{ "+stream.getEvent()+" }");
            throw ex;
        }
    }
    
    @Test
    public void testTreeNodeStream() throws ASTStreamInvalidException{
        LOGGER.log(Level.INFO,
                ()->"Start test \""+testName.getMethodName()+"\".");
        ASTStream stream = new ASTStream(this.root);
        stream.start();
        assertAndRequire(stream, ASTEventType.INITIAL);

        stream.next();
        assertAndRequire(stream, ASTEventType.ENTER, ASTAttributes.SYMBOL, "root");    
        stream.next();
        assertAndRequire(stream, ASTEventType.ENTER, ASTAttributes.SYMBOL, "child0");
        stream.next();
        assertAndRequire(stream, ASTEventType.LEAVE, ASTAttributes.SYMBOL, "child0");
        stream.next();
        assertAndRequire(stream, ASTEventType.ENTER, ASTAttributes.SYMBOL, "child1");
        stream.next();
        assertAndRequire(stream, ASTEventType.ENTER, ASTAttributes.SYMBOL, "child1_0");
        stream.next();
        assertAndRequire(stream, ASTEventType.ENTER, ASTAttributes.SYMBOL, "child1_0_0");
        stream.next();
        assertAndRequire(stream, ASTEventType.LEAVE, ASTAttributes.SYMBOL, "child1_0_0");
        stream.next();
        assertAndRequire(stream, ASTEventType.LEAVE, ASTAttributes.SYMBOL, "child1_0");
        stream.next();
        assertAndRequire(stream, ASTEventType.ENTER, ASTAttributes.SYMBOL, "child1_1");
        stream.next();
        assertAndRequire(stream, ASTEventType.LEAVE, ASTAttributes.SYMBOL, "child1_1");
        stream.next();
        assertAndRequire(stream, ASTEventType.LEAVE, ASTAttributes.SYMBOL, "child1");
        stream.next();
        assertAndRequire(stream, ASTEventType.LEAVE, ASTAttributes.SYMBOL, "root");
        
        stream.next();
        assertAndRequire(stream, ASTEventType.FINISH);

        try{
            stream.next();
            fail();
        }catch(IllegalStateException e){
            //Expected. Do nothing.
        }
    }

    @Test
    public void testTreeNodeStreamSkip() throws ASTStreamInvalidException{
        LOGGER.log(Level.INFO,
                ()->"Start test \""+testName.getMethodName()+"\".");
        ASTStream stream = new ASTStream(this.root);
        stream.start();
        
        try{
            stream.skipTo(leave(stream.getNode()));
            fail();
        }catch(IllegalStateException e){
            //Expected. Do nothing.
        }
        
        try {
            stream.skipTo(enter(ASTAttributes.SYMBOL, "child1_0"));
            assertThat(stream.is(match(ASTEventType.ENTER, ASTAttributes.SYMBOL, "child1_0")), is(true));
        } catch (Throwable ex) {
            LOGGER.log(Level.INFO, () -> "Current event{ " + stream.getEvent() + " }");
            throw ex;
        }
        
        try {
            stream.skipTo(leave(stream.getNode()));
            assertThat(stream.is(match(ASTEventType.LEAVE, ASTAttributes.SYMBOL, "child1_0")), is(true));
        } catch (Throwable ex) {
            LOGGER.log(Level.INFO, () -> "Current event{ " + stream.getEvent() + " }");
            throw ex;
        }

        try {
            stream.skipTo(match(ASTEventType.FINISH));
            assertAndRequire(stream, ASTEventType.FINISH);
        } catch (Throwable ex) {
            LOGGER.log(Level.INFO, () -> "Current event{ " + stream.getEvent() + " }");
            throw ex;
        }
        
        try{
            stream.skipTo(leave(stream.getNode()));
            fail();
        }catch(IllegalStateException e){
            //Expected. Do nothing.
        }

        try{
            stream.next();
            fail();
        }catch(IllegalStateException e){
            //Expected. Do nothing.
        }
    }
}
