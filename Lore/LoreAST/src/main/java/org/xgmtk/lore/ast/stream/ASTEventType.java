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

/**
 *
 * @author Takayuki,Kando <develop@xgmtk.org>
 */
public enum ASTEventType {
    /**
     * This indicates that stream is just initialized.
     */
    INITIAL,
    /**
     * This indicates the entering to the node.
     */
    ENTER,
    /**
     * This indicates the leaving from the node.
     */
    LEAVE,
    /**
     * This indicates that stream is already finished.
     */
    FINISH
}
