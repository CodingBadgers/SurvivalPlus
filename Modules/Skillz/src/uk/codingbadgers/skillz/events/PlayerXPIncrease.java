/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.skillz.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.skillz.skill.PlayerSkillData;

/**
 *
 * @author n3wton
 */
public class PlayerXPIncrease extends Event {
    
    private static final HandlerList handlers = new HandlerList();     
    private final FundamentalPlayer m_player;    
    private final PlayerSkillData m_data;
    
    public PlayerXPIncrease(FundamentalPlayer player, PlayerSkillData data)
    {
        m_player = player;
        m_data = data;
    }
    
    public FundamentalPlayer getPlayer()
    {
        return m_player;
    }
    
    public PlayerSkillData getData()
    {
        return m_data;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
     
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}
