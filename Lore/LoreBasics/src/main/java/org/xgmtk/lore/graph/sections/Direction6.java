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
import java.util.List;
import java.util.stream.Collectors;
import static org.xgmtk.lore.graph.sections.Coordinate.coordinate;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public enum Direction6 implements Direction{
    Origine(0x0, 0, 0),
    UpperRight(0x1, 0, -1),
    Right(0x2, 1, 0),
    LowerRight(0x4, 0, 1),
    LowerLeft(0x8, -1, 1),
    Left(0x10, -1, 0),
    UpperLeft(0x20, -1, -1);
    
    private final int mask;
    private final Coordinate offset;
    
    Direction6(int mask, int offsetX, int offsetY){
        this.mask = mask;
        this.offset = coordinate(offsetX, offsetY);
    }
    
    @Override
    public final int getMask(){
        return this.mask;
    }
    
    @Override
    public final boolean isMatch(int mask){
        return (this.getMask() & mask) != 0;
    }
    
    @Override
    public final Coordinate getCoordinate(Coordinate origin){
        return this.offset.add(origin);
    }
    
    @Override
    public List<Direction> directions(){
        return Arrays.stream(Direction6.values()).skip(1L).collect(Collectors.toList());
    }
    
    public static Direction6 none(){
        return Direction6.Origine;
    }
}
