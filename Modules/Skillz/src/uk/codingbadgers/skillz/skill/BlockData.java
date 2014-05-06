/**
 * SurvivalPlus 1.0
 * Copyright (C) 2014 CodingBadgers <plugins@mcbadgercraft.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
