/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.skillz;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.module.Module;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;

/**
 *
 * @author n3wton
 */
public abstract class SkillBase extends Module implements PlayerData, Listener {
	
	/**
	 * 
	 */
	public SkillBase() {
		register(this);
	}
	
	/**
	 * 
	 * @return 
	 */
	public abstract String getAbilityName();
	
	/**
	 * 
	 * @param player
	 * @param data
	 * @param event 
	 */
	protected void onPlayerDamageBlock(FundamentalPlayer player, PlayerSkillData data, BlockDamageEvent event) {}
	
	/**
	 * 
	 * @param player
	 * @param data
	 * @param event 
	 */
	protected void onPlayerBreakBlock(FundamentalPlayer player, PlayerSkillData data, BlockBreakEvent event) {}
	
	/**
	 * 
	 * @param event 
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) {
	
		final FundamentalPlayer player = SurvivalPlus.Players.getPlayer(event.getPlayer());
		if (player == null) {
			return;
		}
		
		PlayerSkillData data = (PlayerSkillData) player.getPlayerData(this.getPlayerDataClass());
		if (data == null) {
			return;
		}
		
		Action action = event.getAction();
		if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
			if (!data.enableAbility()) {
				final Long timeTillUse = data.getTimeUntilAbilityRefresh();
				player.sendMessage(ChatColor.RED + SurvivalPlus.formatTime(timeTillUse) + " until you can use your mining ability...");
			}
			else {
				player.sendMessage(ChatColor.GOLD + this.getAbilityName() + ChatColor.YELLOW + " activated!");
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(SurvivalPlus.getInstance(), 
				new Runnable() {
					@Override
					public void run() {
						player.sendMessage(ChatColor.GOLD + getAbilityName() + ChatColor.YELLOW + " has ended.");
					}					
				}, 
				data.getAbilityLength());
			}
		}
		
	}
	
	/**
	 * 
	 * @param event 
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockDamage(BlockDamageEvent event) {
		
		FundamentalPlayer player = SurvivalPlus.Players.getPlayer(event.getPlayer());
		if (player == null) {
			return;
		}
		
		PlayerSkillData data = (PlayerSkillData) player.getPlayerData(this.getPlayerDataClass());
		if (data == null) {
			return;
		}
		
		this.onPlayerDamageBlock(player, data, event);
		
	}
	
	/**
	 * 
	 * @param event 
	 */
	@EventHandler(priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event) {
		
		FundamentalPlayer player = SurvivalPlus.Players.getPlayer(event.getPlayer());
		if (player == null) {
			return;
		}
		
		PlayerSkillData data = (PlayerSkillData) player.getPlayerData(this.getPlayerDataClass());
		if (data == null) {
			return;
		}
		
		this.onPlayerBreakBlock(player, data, event);
		
	}
}
