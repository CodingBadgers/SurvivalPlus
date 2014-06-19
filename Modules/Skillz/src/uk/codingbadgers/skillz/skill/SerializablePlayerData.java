/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.skillz.skill;

import uk.thecodingbadgers.bDatabaseManager.DatabaseTable.DatabaseTableData;

/**
 *
 * @author n3wton
 */
public class SerializablePlayerData extends DatabaseTableData {
    
    /**
     * The players UUID
     */
    public String PlayerUUID = null;
    
    /**
     * The amount of xp for this player
     */
    public Long Xp = 0L;
    
}
