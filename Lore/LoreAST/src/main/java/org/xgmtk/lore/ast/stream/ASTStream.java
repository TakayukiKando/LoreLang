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
package org.xgmtk.lore.ast.stream;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.xgmtk.lore.ast.tree.ASTNode;
import org.xgmtk.lore.ast.tree.ASTVisitor;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class ASTStream {
    public static final int DEFAULT_BUFFER_QUEUE_LENGTH = 128;
    private static final Logger LOGGER = Logger.getLogger(ASTStream.class.getName());

    private ASTEvent current;
    private final BlockingQueue<ASTEvent> queue;
    private final ASTNode root;
    private final ASTVisitor visitor;
    private final ExecutorService executor;
    private boolean finished;

    private class SpecialEvent implements ASTEvent{
        final ASTEventType type;
        
        public SpecialEvent(ASTEventType type){
            super();
            Objects.requireNonNull(type, "Argument \"type\" should not be null.");
            this.type = type;
        }
        
        @Override
        public final ASTEventType getEventType(){
            return this.type;
        }
        
        @Override
        public ASTNode getNode(){
            throw new UnsupportedOperationException("Event type "+this.getEventType()+"doesn't have a node.");
        }
        
        @Override
        public String toString(){
            return "Class: "+this.getClass().getName()+", Type: "+this.type;
        }
    }
    
    private class NodeEvent extends SpecialEvent{
        final ASTNode node;
        
        public NodeEvent(ASTEventType type, ASTNode node) {
            super(type);
            Objects.requireNonNull(node, "Argument \"node\" should not be null.");
            this.node = node;
        }
        
        @Override
        public boolean hasNode(){
            return true;
        }
        
        @Override
        public ASTNode getNode(){
            return this.node;
        }
        
        @Override
        public String toString(){
            return super.toString()+", Node: {"+
                this.node.attributes().entrySet().stream()
                    .map(e->"<"+e.getKey()+": \""+e.getValue()+"\">")
                    .collect(Collectors.joining(", "))
                +"}";
        }
    }

    private void putToQueue(ASTEventType type){
        try {
            this.queue.put(new SpecialEvent(type));
        } catch (InterruptedException ex) {
            final String msg = "(Internal error) Failed to put an event "
                    +type+" to buffer queue.";
            LOGGER.log(Level.SEVERE, msg);
            throw new IllegalStateException(msg, ex);
        }
    }

    private void putToQueue(ASTEventType type, ASTNode n){
        try {
            this.queue.put(new NodeEvent(type, n));
        } catch (InterruptedException ex) {
            final String msg = "(Internal error) Failed to put an event "
                    +type+" to buffer queue.";
            LOGGER.log(Level.SEVERE, msg);
            throw new IllegalStateException(msg, ex);
        }
    }

    /**
     * TODO write JavaDoc comment.
     * 
     * @param root
     * @param queueLength 
     */
    public ASTStream(ASTNode root, int queueLength) {
        this.current = new SpecialEvent(ASTEventType.INITIAL);
        this.queue = new ArrayBlockingQueue<>(queueLength);
        this.root = root;
        
        this.visitor = new ASTVisitor(
             n->this.putToQueue(ASTEventType.ENTER, n),
             n->this.putToQueue(ASTEventType.LEAVE, n)
        );
        
        this.executor = Executors.newFixedThreadPool(1);
    }
    
    /**
     * TODO write JavaDoc comment.
     * 
     * @return 
     */
    public Future<Boolean> start(){
        return this.executor.submit(()->{
            root.startVisit(visitor);
            putToQueue(ASTEventType.FINISH);
            return true;
        });
    }
    
    /**
     * TODO write JavaDoc comment.
     * 
     * @param root 
     */
    public ASTStream(ASTNode root){
        this(root, DEFAULT_BUFFER_QUEUE_LENGTH);
    }
    
    /**
     * TODO write JavaDoc comment.
     * 
     * @return 
     */
    public ASTEvent getEvent(){
        return this.current; //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * TODO write JavaDoc comment.
     * 
     * @return
     * @throws IllegalStateException 
     */
    public ASTNode getNode() throws IllegalStateException{
        if(!this.getEvent().hasNode()){
            throw new IllegalStateException();
        }
        return this.getEvent().getNode();
    }

    /**
     * TODO write JavaDoc comment.
     * 
     * @throws IllegalStateException 
     */
    public void next() throws IllegalStateException {
        if(this.finished){
            throw new IllegalStateException("ASTStream is already finished.");
        }
        try {
            this.current = this.queue.take();
            if(this.is(EventMatcher.match(ASTEventType.FINISH))){
                this.finished = true;
            }
        } catch (InterruptedException ex) {
            final String msg = "(Internal error) Failed to take an event "
                    +" from buffer queue.";
            LOGGER.log(Level.SEVERE, msg);
            throw new IllegalStateException(msg, ex);
        }
    }

    /**
     * TODO write JavaDoc comment.
     * 
     * @param predicate 
     * @return  
     */
    public boolean is(Predicate<ASTEvent> predicate) {
        return predicate.test(this.getEvent());
    }

    /**
     * 
     * @param predicate 
     * @throws org.xgmtk.lore.ast.stream.ASTStreamInvalidException 
     */
    public void require(Predicate<ASTEvent> predicate) throws ASTStreamInvalidException {
        if(!this.is(predicate)){
            throw new ASTStreamInvalidException(this.getEvent());
        }
    }

    /**
     * Skip events untill the matched event.
     * 
     * @param predicate 
     */
    public void skipTo(Predicate<ASTEvent> predicate) {
        while(!is(predicate)){
            this.next();
        }
    }
}
