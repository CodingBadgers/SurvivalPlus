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
public class BlockData {
	
	/**
	 * 
	 */
	private final Long m_xp;
	
	/**
	 * 
	 */
	private final int m_minLevel;
	
	public BlockData(Long xp, int minLevel) {
		m_xp = xp;
		m_minLevel = minLevel;
	}
	
	/**
	 * 
	 * @return 
	 */
	public Long getXp() {
		return m_xp;
	}
	
	/**
	 * 
	 * @return 
	 */
	public int getMinimumLevel() {
		return m_minLevel;
	}
}
