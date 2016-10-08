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
package org.xgmtk.lore.graph.sections.test;

import org.xgmtk.lore.graph.sections.Coordinate;
import static org.xgmtk.lore.graph.sections.Coordinate.coordinate;
import org.xgmtk.lore.graph.sections.Direction4;
import org.xgmtk.lore.graph.sections.Sections;
import org.xgmtk.lore.graph.test.SampleEdgeData;
import org.xgmtk.lore.graph.test.SampleNodeData;
import static org.xgmtk.lore.graph.sections.Sections.Builder.line;
import static org.xgmtk.lore.graph.sections.Sections.Builder.section;
import org.xgmtk.lore.graph.test.SampleGenericGraphs.Link;

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public class SampleSectionGraphs {
    static SampleEdgeData edgeSupplier(SampleNodeData initial, SampleNodeData terminal){
        return new SampleEdgeData(initial.name+"->"+terminal.name);
    }
    
    @SuppressWarnings("unchecked")
    public static final Sections.Builder<SampleNodeData, SampleEdgeData, Direction4> SECTIONS4 =
        Sections.builder(Direction4.none(), -2, SampleSectionGraphs::edgeSupplier,
            line(-2,
                section(new SampleNodeData("#0")),
                section(new SampleNodeData("#1"),
                    Direction4.Bottom),
                section(new SampleNodeData("#2")),
                section(new SampleNodeData("#3"))
            ),
            line(-3,
                section(new SampleNodeData("#4")),
                section(new SampleNodeData("#5"),
                    Direction4.Right),
                section(new SampleNodeData("#6"),
                    Direction4.Top,
                    Direction4.Right,
                    Direction4.Bottom,
                    Direction4.Left),
                section(new SampleNodeData("#7"),
                    Direction4.Left)
            ),
            line(-2,
                section(new SampleNodeData("#8")),
                section(new SampleNodeData("#9"), Direction4.Top),
                section(new SampleNodeData("#10")),
                section(new SampleNodeData("#11"))
            ),
            line(1,
                section(new SampleNodeData("#12")),
                section(new SampleNodeData("#13")),
                section(new SampleNodeData("#14")),
                section(new SampleNodeData("#15"))
            )
        );
    
        
    /**
     * Number of nodes of graph sample0.
     */
    public static final int SAMPLE_SQUARE4_NODES = 16;
    
    public static final Coordinate[] SAMPLE_SQUARE4_COORDINATES = {
        //line 0
        coordinate(-2, -2),
        coordinate(-1, -2),
        coordinate(0, -2),
        coordinate(1, -2),
        //line 1
        coordinate(-3, -1),
        coordinate(-2, -1),
        coordinate(-1, -1),
        coordinate(0, -1),
        //line 2
        coordinate(-2, 0),
        coordinate(-1, 0),
        coordinate(0, 0),
        coordinate(1, 0),
        //line 3
        coordinate(1, 1),
        coordinate(2, 1),
        coordinate(3, 1),
        coordinate(4, 1),
    };
    
    /**
     * Array of edges of graph sample0.
     */
    public static final Link[] SAMPLE_SQUARE4_LINKS = {
        new Link("#0", 1, "#1"),
        new Link("#0", 1, "#5"),
        new Link("#1", 1, "#0"),
        new Link("#1", 1, "#2"),
        new Link("#2", 1, "#3"),
        new Link("#2", 1, "#7"),
        new Link("#2", 1, "#1"),
        new Link("#3", 1, "#2"),
        new Link("#4", 1, "#5"),
        new Link("#5", 1, "#0"),
        new Link("#5", 1, "#8"),
        new Link("#5", 1, "#4"),
        new Link("#7", 1, "#2"),
        new Link("#7", 1, "#10"),
        new Link("#8", 1, "#5"),
        new Link("#8", 1, "#9"),
        new Link("#9", 1, "#10"),
        new Link("#9", 1, "#8"),
        new Link("#10", 1, "#7"),
        new Link("#10", 1, "#11"),
        new Link("#10", 1, "#9"),
        new Link("#11", 1, "#12"),
        new Link("#11", 1, "#10"),
        new Link("#12", 1, "#11"),
        new Link("#12", 1, "#13"),
        new Link("#13", 1, "#14"),
        new Link("#13", 1, "#12"),
        new Link("#14", 1, "#15"),
        new Link("#14", 1, "#13"),
        new Link("#15", 1, "#14")
    };
    
    public static final int[] EXPECTED_DEPTHFIRST_ITERATION =
        {0, 1, 2, 3, 7, 10, 11, 12, 13, 14, 15, 9, 8, 5, 4};
    
    public static final int[][] EXPECTED_DEPTHFIRST_PATHS = {
        {0},
        {0, 1},
        {0, 1, 2},
        {0, 1, 2, 3},
        {0, 1, 2, 7, 10, 9, 8, 5, 4},
        {0, 1, 2, 7, 10, 9, 8, 5},
        null,
        {0, 1, 2, 7},
        {0, 1, 2, 7, 10, 9, 8},
        {0, 1, 2, 7, 10, 9},
        {0, 1, 2, 7, 10},
        {0, 1, 2, 7, 10, 11},
        {0, 1, 2, 7, 10, 11, 12},
        {0, 1, 2, 7, 10, 11, 12, 13},
        {0, 1, 2, 7, 10, 11, 12, 13, 14},
        {0, 1, 2, 7, 10, 11, 12, 13, 14, 15},
    };
}
