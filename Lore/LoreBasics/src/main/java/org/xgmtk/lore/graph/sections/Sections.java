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
package org.xgmtk.lore.graph.sections;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.stream.Stream.concat;
import org.xgmtk.lore.graph.Graph;
import org.xgmtk.lore.util.ArrayVector;
import org.xgmtk.lore.util.ImmutableVector;

/**
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 * @param <N>
 * @param <E>
 * @param <D> 
 */
public class Sections<N, E, D extends Direction> implements SectionGraph<N, E, D>{

    /**
     * 
     */
    public class Section extends Graph.ConcreteNode<N> implements SectionGraph.Section<N, D>{
        private final int barriers;
        private final Coordinate position;
        
        private Section(int index, N data, Coordinate position, int barriers){
            super(index, data);
            this.position = position;
            this.barriers = barriers;
        }
        
        @Override
        public int getBarriers(){
            return this.barriers;
        }
        
        @Override
        public boolean isBarriered(D direction){
            return direction.isMatch(this.barriers);
        }
        
        @Override
        public Stream<Direction> directions(){
            return barrierDefault.directions().stream().filter(d->!d.isMatch(barriers));
        }
        
        @Override
        public Coordinate position(){
            return this.position;
        }
    }
    
    public class Line implements SectionGraph.Line<N, D>{
        private final int offsetX;
        private final ImmutableVector<Sections<N, E, D>.Section> sections;
        
        private Line(int offsetX, Stream<Sections<N, E, D>.Section> sections) {
            this.offsetX = offsetX;
            this.sections = new ArrayVector<>(sections);
        }
        
        @Override
        public int getOffsetX(){
            return this.offsetX;
        }
            
        @Override
        public int numberOfSections(){
            return this.sections.size();
        }
        
        @Override
        public Stream<SectionGraph.Section<N, D>> sections(){
            return this.sections.stream().map(s->(SectionGraph.Section<N, D>)s);
        }

        @Override
        public Optional<SectionGraph.Section<N, D>> getNthSection(final int index) {
            if(index < 0 || this.sections.size() <= index){
                return Optional.empty();
            }
            return Optional.of(sections.get(index));
        }
        
        @Override
        public Optional<SectionGraph.Section<N, D>> getSection(int x){
            return getNthSection(x - this.offsetX);
        }
    }
    
    public static class Builder<N, E, D extends Direction> implements SectionGraph.Builder<N, E, D>{

        public static final class ModifiableSection<N, E, D extends Direction> implements SectionGraph.Section<N, D>{
            private int index;
            private final N data;
            private int barriers;
            private Coordinate position;
            private D barrierDefault;

            ModifiableSection(N data, List<D> barrieredDirections){
                this.index = 0;
                this.data = data;
                this.barriers = 0;
                this.position = Coordinate.origin;
                this.addBarriers(barrieredDirections);
                this.barrierDefault = null;
            }
            
            private void initSection(int index, int x, int y, D barrierDefault){
                this.index = index;
                this.position = new Coordinate(x, y);
                this.barrierDefault = barrierDefault;
            }
            
            void addBarriers(List<D> directions){
                OptionalInt optBarriers = directions.stream().mapToInt(d->d.getMask()).reduce((a, b)->a | b);
                if(optBarriers.isPresent()){
                    this.barriers |= optBarriers.getAsInt();
                }
            }
            
            @Override
            public int getBarriers(){
                return this.barriers;
            }

            @Override
            public int index(){
                return this.index;
            }
            
            @Override
            public N getData(){
                return this.data;
            }

            @Override
            public boolean isBarriered(D direction){
                return direction.isMatch(this.barriers);
            }

            
            @Override
            public Stream<Direction> directions(){
                return barrierDefault.directions().stream().filter(d->!d.isMatch(barriers));
            }
            
            @Override
            public Iterator<Direction> directionsIterator(){
                return directions().iterator();
            }

            @Override
            public Coordinate position(){
                return this.position;
            }

            @SuppressWarnings("unchecked")
            private void initSection(int x, int y, Sections.Builder<N, E, D> graph) {
                final int newIndex = graph.getNewIndex();
                System.err.print("initSection() for #"+newIndex+", (x, y): ("+x+", "+y+")");
                this.initSection(newIndex, x, y, graph.getDefaultBarrieredDirection());
                List<D> bs = this.directions()
                        .filter(d->!graph.getSection(d.getCoordinate(this.position())).isPresent())
                        .map(d->(D)d)
                        .collect(Collectors.toList());
                this.addBarriers(bs);
                System.err.println(", no barriers: { "+this.directions().map(d->d.toString()).collect(Collectors.joining(", "))+" }");
            }
            

        }
        
        @SuppressWarnings("unchecked")
        public static <N, E, D extends Direction> ModifiableSection<N, E, D> section(N data, D...barrieredDirections){
            return new ModifiableSection<>(data, Arrays.asList(barrieredDirections));
        }
        
        public static class ModifiableLine<N, E, D extends Direction> implements SectionGraph.Line<N, D>{
            private final int offsetX;
            private final List<SectionGraph.Section<N, D>> sections;

            ModifiableLine(int offsetX, List<SectionGraph.Section<N, D>> sections) {
                this.offsetX = offsetX;
                this.sections = Collections.unmodifiableList(sections);
            }
            
            @Override
            public int getOffsetX(){
                return this.offsetX;
            }
            
            @Override
            public int numberOfSections(){
                return this.sections.size();
            }

            @Override
            public Stream<SectionGraph.Section<N, D>> sections(){
                return this.sections.stream();
            }
            
            @Override
            public Optional<SectionGraph.Section<N, D>> getSection(final int x){
                return getNthSection(x - this.offsetX);
            }

            @Override
            public Optional<SectionGraph.Section<N, D>> getNthSection(final int index) {
                if(index < 0 || this.sections.size() <= index){
                    return Optional.empty();
                }
                return Optional.of(sections.get(index));
            }

            @SuppressWarnings("unchecked")
            private void initSections(int y, Sections.Builder<N, E, D> graph) {
                Iterator<SectionGraph.Section<N, D>> it = this.sectionsIterator();
                int x = this.offsetX;
                while(it.hasNext()){
                    ((ModifiableSection<N, E, D>)it.next()).initSection(x++, y, graph);
                }
            }
        }

        @SuppressWarnings("unchecked")
        public static <N, E, D extends Direction> ModifiableLine<N, E, D> line(int offsetX, SectionGraph.Section<N, D>...sections){
            return new ModifiableLine<>(offsetX, Arrays.asList(sections));
        }
        
        private final D barrierDefault;
        private final AtomicInteger nodeCounter;
        private final int offsetY;
        private final List<SectionGraph.Line<N, D>> lines;
        private final EdgeDataSupplier<N, E> edgeSupplier;
        
        public Builder(D barrierDefault, int offsetY, List<SectionGraph.Line<N, D>> lines, EdgeDataSupplier<N, E> edgeSupplier){
            this.barrierDefault = barrierDefault;
            this.nodeCounter = new AtomicInteger(0);
            this.offsetY = offsetY;
            this.lines = Collections.unmodifiableList(lines);
            this.edgeSupplier = edgeSupplier;
            this.initLines();
        }
        
        @SuppressWarnings("unchecked")
        private void initLines(){
            Iterator<SectionGraph.Line<N, D>> it = this.linesIterator();
            int y = this.offsetY;
            while(it.hasNext()){
                ((ModifiableLine<N, E, D>)it.next()).initSections(y++, this);
            }
        }
        
        private int getNewIndex(){
            return this.nodeCounter.getAndIncrement();
        }
        
        private D getDefaultBarrieredDirection(){
            return this.barrierDefault;
        }
        
        @Override
        public int getOffsetY(){
            return this.offsetY;
        }
         
        @Override
        public int numberOfLines(){
            return this.lines.size();
        }
        
        @Override
        public Stream<SectionGraph.Line<N, D>> lines(){
            return this.lines.stream();
        }

        @Override
        public Optional<Line<N, D>> getNthLine(final int index) {
            if(index < 0 || this.lines.size() <= index){
                return Optional.empty();
            }
            return Optional.of(lines.get(index));
        }
        
        @Override
        public Optional<SectionGraph.Line<N, D>> getLine(int y){
            return getNthLine(y - this.offsetY);
        }
        
        public Optional<SectionGraph.Section<N, D>> getSection(int x, int y){
            Optional<SectionGraph.Line<N, D>> optLine = this.getLine(y);
            if(!optLine.isPresent()){
                return Optional.empty();
            }
            return optLine.get().getSection(x);
        }
        
        public Optional<SectionGraph.Section<N, D>> getSection(Coordinate c){
            return this.getSection(c.x, c.y);
        }
        
        @Override
        public SectionGraph.Section<N, D> getNthSection(int index) throws IndexOutOfBoundsException {
            Iterator<SectionGraph.Line<N, D>> it = this.lines().iterator();
            while(it.hasNext()){
                SectionGraph.Line<N, D> l = it.next();
                int remain = index - l.numberOfSections();
                System.err.println("Builder::getSection() index: "+index+", remain: "+remain+", line length: "+l.numberOfSections());
                if(remain < 0){
                    final Optional<SectionGraph.Section<N, D>> optSect = l.getNthSection(index);
                    if(!optSect.isPresent()){
                        throw new IndexOutOfBoundsException();
                    }
                    final SectionGraph.Section<N, D> sect = optSect.get();
                    System.err.println("\tsect.index(): "+sect.index());
                    return sect;
                }
                index = remain;
            }
            throw new IndexOutOfBoundsException();
        }
        
        @Override
        public SectionGraph<N, E, D> getGraph() throws NullPointerException {
            return new Sections<>(size(), getDefaultBarrieredDirection(), this.offsetY, this.lines(), edgeSupplier);
        }

        @Override
        public int size() {
            return this.lines.stream().mapToInt(l->l.numberOfSections()).sum();
        }

        @Override
        public Node<N> getNode(int index) throws IndexOutOfBoundsException {
            return this.getNthSection(index);
        }

        @Override
        public Iterator<Node<N>> getNodeIterator() {
            return this.getNodeStream().iterator();
        }

        @Override
        public Stream<Node<N>> getNodeStream() {
            return lines.stream().map(l->l.sections().map(s->(Node<N>)s)).reduce((before, after)->concat(before, after)).get();
        }

        @SuppressWarnings("unchecked")
        @Override
        public int numberOfEdges(Node<N> node) {
            return (int)((SectionGraph.Section<N, D>)node).directions().count();
        }

        @Override
        public Iterator<Edge<N, E>> getEdgeIterator(Node<N> node) {
            return getEdgeStream(node).iterator();
        }

        private Edge<N, E> suppyEdge(Node<N> initial, Node<N> terminal){
            return new ConcreteEdge<>(initial, edgeSupplier.get(initial.getData(), terminal.getData()), terminal);
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public Stream<Edge<N, E>> getEdgeStream(Node<N> node) {
            SectionGraph.Section<N, D> initial = ((SectionGraph.Section<N, D>)node);
            return initial.directions()
                    .map(d->suppyEdge(initial,
                            getSection(d.getCoordinate(initial.position())).get()));
        }
    }

    @SuppressWarnings("unchecked")
    public static <N, E, D extends Direction> Builder<N, E, D> builder(
            D barrierDefault, int offsetY,
            EdgeDataSupplier<N, E> edgeSupplier,
            Builder.Line<N, D>...lines){
        return new Builder<>(barrierDefault, offsetY, Arrays.asList(lines), edgeSupplier);
    }
    
    private final int size;
    private final D barrierDefault;
    private final int offsetY;
    private final ImmutableVector<SectionGraph.Line<N, D>> lines;
    private final EdgeDataSupplier<N, E> edgeSupplier;

    private Sections(int size, D barrierDefault, int offsetY, Stream<SectionGraph.Line<N, D>> lines, EdgeDataSupplier<N, E> edgeSupplier){
        this.size = size;
        this.barrierDefault = barrierDefault;
        this.offsetY = offsetY;
        this.lines = new ArrayVector<>(lines.map(s->getFixedLine(s)));
        this.edgeSupplier = edgeSupplier;
    }
    
    private Sections<N, E, D>.Section getFixedSection(SectionGraph.Section<N, D> sect){
        return new Section(sect.index(), sect.getData(), sect.position(), sect.getBarriers());
    }
    
    private Sections<N, E, D>.Line getFixedLine(SectionGraph.Line<N, D> line){
        return new Line(line.getOffsetX(), line.sections().map(s->getFixedSection(s)));
    }
    
    @Override
    public int getOffsetY(){
        return this.offsetY;
    }
    
    @Override
    public int numberOfLines(){
        return this.lines.size();
    }
    
    @Override
    public Stream<SectionGraph.Line<N, D>> lines(){
        return this.lines.stream();
    }

    @Override
    public Optional<SectionGraph.Line<N, D>> getNthLine(final int index) {
        if(index < 0 || this.lines.size() <= index){
            return Optional.empty();
        }
        return Optional.of(lines.get(index));
    }

    @Override
    public Optional<SectionGraph.Line<N, D>> getLine(int y){
        final int index = y - this.offsetY;
        return getNthLine(index);
    }
    
    public Optional<SectionGraph.Section<N, D>> getSection(int x, int y){
        Optional<SectionGraph.Line<N, D>> optLine = this.getLine(y);
        if(!optLine.isPresent()){
            return Optional.empty();
        }
        return optLine.get().getSection(x);
    }
    
    public Optional<SectionGraph.Section<N, D>> getSection(Coordinate c){
        return this.getSection(c.x, c.y);
    }
    
    @Override
    public int size() {
        return this.size;
    }

    @Override
    public Node<N> getNthSection(int index) throws IndexOutOfBoundsException {
        Iterator<SectionGraph.Line<N, D>> it = this.lines().iterator();
        while(it.hasNext()){
            SectionGraph.Line<N, D> l = it.next();
            int remain = index - l.numberOfSections();
            if(remain < 0){
                final Optional<SectionGraph.Section<N, D>> optSect = l.getNthSection(index);
                if(!optSect.isPresent()){
                    throw new IndexOutOfBoundsException();
                }
                SectionGraph.Section<N, D> sect = optSect.get();
                return sect;
            }
            index = remain;
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public Node<N> getNode(int index) throws IndexOutOfBoundsException {
        return getNthSection(index);
    }
    
    @Override
    public Iterator<Node<N>> getNodeIterator() {
        return this.getNodeStream().iterator();
    }

    @Override
    public Stream<Node<N>> getNodeStream() {
        return lines.stream().map(l->l.sections()
                .map(s->(Node<N>)s))
                .reduce((before, after)->concat(before, after))
                .get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int numberOfEdges(Node<N> node) {
        return (int)((SectionGraph.Section<N, D>)node).directions().count();
    }

    @Override
    public Iterator<Edge<N, E>> getEdgeIterator(Node<N> node) {
        return this.getEdgeStream(node).iterator();
    }

    private Edge<N, E> suppyEdge(Node<N> initial, Node<N> terminal){
        return new ConcreteEdge<>(initial, edgeSupplier.get(initial.getData(), terminal.getData()), terminal);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public Stream<Edge<N, E>> getEdgeStream(Node<N> node) {
        SectionGraph.Section<N, D> initial = (SectionGraph.Section<N, D>)node;
        return ((Section)node).directions()
                .map(d->suppyEdge(initial,
                    getSection(d.getCoordinate((initial).position())).get()));
    }
}
