/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.skillz.skill;

/**
 *
 * @author n3wton
 */
public class ToolData {
    
    /**
     * 
     */
    private final int m_minLevel;
    
    /**
     * 
     * @param minLevel 
     */
    public ToolData(int minLevel) {
        m_minLevel = minLevel;
    }
    
    /**
     * 
     * @return 
     */
    public int getMinimumLevel() {
        return m_minLevel;
    }
    
}
