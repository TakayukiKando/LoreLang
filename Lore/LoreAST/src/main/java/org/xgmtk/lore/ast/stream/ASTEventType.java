/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.xgmtk.lore.ast.stream;

/**
 *
 * @author kando
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
