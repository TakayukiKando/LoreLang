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

/**
 * The coordinate of sections graph.
 * 
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class Coordinate {
    public static Coordinate origin = new Coordinate(0, 0);
    /**
     * The position of a point with reference to the x-axis
     */
    public final int x;
    /**
     * The position of a point with reference to the y-axis
     */
    public final int y;

    /**
     * Initializer.
     * 
     * @param x
     * @param y 
     */
    public Coordinate(int x, int y){
        super();
        this.x = x;
        this.y = y;
    }
    
    /**
     * Copy initializer.
     * 
     * @param c 
     */
    public Coordinate(Coordinate c){
        this(c.x, c.y);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Coordinate)){
            return false;
        }
        Coordinate c = (Coordinate)o;
        return this.x == c.x && this.y == c.y;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.x;
        hash = 53 * hash + this.y;
        return hash;
    }
    
    @Override
    public String toString(){
        return "("+x+", "+y+")";
    }

    /**
     * Add a coordinate and this.
     * 
     * @param c
     * @return 
     */
    public Coordinate add(Coordinate c){
        return new Coordinate(this.x + c.x, this.y + c.y);
    }
    
    /**
     * Subtract a coordinate from this.
     * 
     * @param c
     * @return 
     */
    public Coordinate sub(Coordinate c){
        return new Coordinate(this.x - c.x, this.y - c.y);
    }
    
    /**
     * negate cordinate.
     * 
     * @return 
     */
    public Coordinate negate(){
        return new Coordinate(-this.x, -this.y);
    }
    
    /**
     * A factory method of cordinate object.
     * 
     * @param x
     * @param y
     * @return 
     */
    public static Coordinate coordinate(int x, int y){
        return new Coordinate(x, y);
    }
    
    /**
     * Get geographic distance between the two coordinates.
     * 
     * @param a
     * @param b
     * @return 
     */
    public static double distance(Coordinate a, Coordinate b){
        int x = a.x - b.x;
        int y = a.y - b.y;
        return Math.sqrt((double)x * x + (double)y * y);
    }
}
