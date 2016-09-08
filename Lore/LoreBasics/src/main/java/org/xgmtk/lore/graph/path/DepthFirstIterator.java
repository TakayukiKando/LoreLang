/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xgmtk.lore.graph.path;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.xgmtk.lore.graph.Graph;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class DepthFirstIterator implements Iterator<Move> {
    private final Deque<Move> visiting;
    private final Set<Move> visited;
    private final Graph.Node start;
    private final int depth;
    
    public DepthFirstIterator(Graph.Node start, int depth){
        super();
        this.depth = 0;
        this.start = start;
        this.visiting = new ArrayDeque<>();
        this.visited = new HashSet<>();
    }
    
    @Override
    public boolean hasNext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Move next() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
