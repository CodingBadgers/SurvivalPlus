/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.skillz.skill;

/**
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

    /**
     *
     */
    private final Long m_regenTime;

    public BlockData(Long xp, int minLevel, Long regenTime) {
        m_xp = xp;
        m_minLevel = minLevel;
        m_regenTime = regenTime;
    }

    /**
     * @return
     */
    public Long getXp() {
        return m_xp;
    }

    /**
     * @return
     */
    public int getMinimumLevel() {
        return m_minLevel;
    }

    /**
     * @return
     */
    public Long getRegenTime() {
        return m_regenTime;
    }
}
