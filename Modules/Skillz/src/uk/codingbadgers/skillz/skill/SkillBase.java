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
package uk.codingbadgers.skillz.skill;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.module.Module;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;
import uk.codingbadgers.SurvivalPlus.player.PlayerData;
import uk.codingbadgers.skillz.metakey.BlockMeta;

/**
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
     * @return
     */
    public abstract String getAbilityName();

    /**
     * @param event
     * @return
     */
    public abstract boolean canActivateAbility(PlayerInteractEvent event);

    /**
     * @param player
     * @param data
     * @param event
     */
    protected void onPlayerDamageBlock(FundamentalPlayer player, PlayerSkillData data, BlockDamageEvent event, boolean placedByPlayer) {
    }

    /**
     * @param player
     * @param data
     * @param event
     */
    protected void onPlayerBreakBlock(FundamentalPlayer player, PlayerSkillData data, BlockBreakEvent event, boolean placedByPlayer) {
    }

    /**
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

            if (!canActivateAbility(event)) {
                return;
            }

            if (!data.enableAbility()) {
                final Long timeTillUse = data.getTimeUntilAbilityRefresh();
                player.sendMessage(ChatColor.RED + SurvivalPlus.formatTime(timeTillUse) + " until you can use");
                player.sendMessage(ChatColor.RED + "the " + ChatColor.GOLD + this.getAbilityName() + ChatColor.RED + " ability...");
            } else {
                player.sendMessage(ChatColor.GOLD + this.getAbilityName() + ChatColor.YELLOW + " activated for " + SurvivalPlus.formatTime(data.getAbilityLength()) + ".");

                Long lengthInTicks = (long) ((data.getAbilityLength() * 0.001) * 20L);

                Bukkit.getScheduler().scheduleSyncDelayedTask(SurvivalPlus.getInstance(),
                        new Runnable() {
                            @Override
                            public void run() {
                                player.sendMessage(ChatColor.GOLD + getAbilityName() + ChatColor.YELLOW + " has ended.");
                            }
                        },
                        lengthInTicks
                );
            }
        }

    }

    /**
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

        final Block block = event.getBlock();
        final boolean placedByPlayer = block.hasMetadata(BlockMeta.PlacedBy);

        this.onPlayerDamageBlock(player, data, event, placedByPlayer);

    }

    /**
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

        final Block block = event.getBlock();
        final boolean placedByPlayer = block.hasMetadata(BlockMeta.PlacedBy);

        this.onPlayerBreakBlock(player, data, event, placedByPlayer);

    }

    /**
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {

        final Block block = event.getBlock();
        final Player player = event.getPlayer();
        final Plugin plugin = SurvivalPlus.getInstance();

        // Set who the block was place by
        block.setMetadata(BlockMeta.PlacedBy, new FixedMetadataValue(plugin, player.getName()));

    }
}
