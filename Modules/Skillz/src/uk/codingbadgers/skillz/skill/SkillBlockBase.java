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

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.player.FundamentalPlayer;

/**
 * @author n3wton
 */
public abstract class SkillBlockBase extends SkillBase {

    /**
     * The blocks that are used by this skill
     */
    private final Map<Material, Map<Byte, BlockData>> m_blocks = new EnumMap<Material, Map<Byte, BlockData>>(Material.class);

    /**
     * The tools that have the mining ability
     */
    private final Map<Material, ToolData> m_tools = new EnumMap<Material, ToolData>(Material.class);

    /**
     *
     */
    private final Map<Block, Material> m_blocksToRegen = new HashMap<Block, Material>();
    
    /**
     * 
     */
    private final Material m_regenBlockType;
        
    /**
     * 
     * @param regenBlockType 
     */
    public SkillBlockBase(Material regenBlockType) {
        m_regenBlockType = regenBlockType;
    }
    
    /**
     * Register a block with this skill and the amount of xp gained for breaking said block
     *
     * @param block The material of the block to register
     * @param blockData    The block data associated with this block
     */
    protected void RegisterBlock(Material block, BlockData blockData) {
        Map<Byte, BlockData> mapBlockData = new HashMap<Byte, BlockData>();
        mapBlockData.put((byte)0, blockData);
        m_blocks.put(block, mapBlockData);
    }

    /**
     * Register a block with this skill and the amount of xp gained for breaking said block
     *
     * @param block The material of the block to register
     * @param blockData    The block data associated with this block
     * @param dataId    The data id of the block
     */
    protected void RegisterBlock(Material block, BlockData blockData, byte dataId) {
        Map<Byte, BlockData> mapBlockData = null;
        
        if (m_blocks.containsKey(block)) {
            mapBlockData = m_blocks.get(block);
        }
        else {
            mapBlockData = new HashMap<Byte, BlockData>();
        }
        
        mapBlockData.put(dataId, blockData);
        m_blocks.put(block, mapBlockData);
    }
    
    /**
     * Register a block with this skill and the amount of xp gained for breaking said block
     *
     * @param block The material of the block to register
     * @param blockData    The block data associated with this block
     */
    protected void RegisterBlock(Material block, Map<Byte, BlockData> blockData) {
        m_blocks.put(block, blockData);
    }

    /**
     * Register a tool which can be used with this skills ability
     *
     * @param tool The material of the tool to register
     * @param toolData The tool data associated with this block
     */
    protected void RegisterTool(Material tool, ToolData toolData) {
        m_tools.put(tool, toolData);
    }

    /**
     * @return
     */
    public Map<Material, Map<Byte, BlockData>> getBlocks() {
        return m_blocks;
    }

    /**
     * Called when a player breaks a block
     *
     * @param player         The player who broke a block
     * @param data           The players skill data for this skill
     * @param event          The Bukkit block break event
     */
    protected void onPlayerBreakBlock(FundamentalPlayer player, PlayerSkillData data, BlockBreakEvent event) {}

    /**
     * Handle block based skill ability
     *
     * @param player         The player who damaged the block
     * @param data           The data for the player for this skill
     * @param event          The Bukkit block damage event
     */
    protected void onPlayerDamageBlock(FundamentalPlayer player, PlayerSkillData data, BlockDamageEvent event) {}

    /**
     * @param player
     * @param block
     * @return
     */
    public boolean canBreakBlock(FundamentalPlayer player, Block block, PlayerSkillData data, ItemStack tool) {

        final Map<Byte, BlockData> allBlockData = m_blocks.get(block.getType());
        if (!allBlockData.containsKey(block.getData())) {
            return false;
        }
        
        final BlockData blockData = allBlockData.get(block.getData());        
        final PlayerSkillData playerData = (PlayerSkillData) player.getPlayerData(this.getPlayerDataClass());

        if (playerData.getLevel() < blockData.getMinimumLevel()) {
            if (block.getType() != m_regenBlockType) {
                player.sendMessage("You need to be level '" + blockData.getMinimumLevel() + "' to break that block...");
                return false;
            }
        }
                
        ToolData toolData = m_tools.get(tool.getType());
        if (data.getLevel() < toolData.getMinimumLevel()) {
            player.sendMessage("You need to be level '" + toolData.getMinimumLevel() + "' " + this.getName() + " to use that tool...");
            return false;
        }
        
        return true;
    }
    
    /**
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockDamage(BlockDamageEvent event) {
        
        // If the player is in creative, let them do what they want
        final Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        
        // Already canceled, don't do anything
        if (event.isCancelled()) {
            return;
        }
               
        FundamentalPlayer fundamentalPlayer = SurvivalPlus.Players.getPlayer(event.getPlayer());
        if (fundamentalPlayer == null) {
            return;
        }

        PlayerSkillData data = (PlayerSkillData) fundamentalPlayer.getPlayerData(this.getPlayerDataClass());
        if (data == null) {
            return;
        }

        final Block block = event.getBlock();        
        if (!m_blocks.containsKey(block.getType())) {
            return;
        }
        
        Map<Byte, BlockData> blockData = m_blocks.get(block.getType());
        if (!blockData.containsKey(block.getData())) {
            return;
        }
        
        ItemStack tool = player.getPlayer().getItemInHand();
        if (tool == null || !m_tools.containsKey(tool.getType())) {
            return;
        }        

        if (!canBreakBlock(fundamentalPlayer, block, data, tool)) {
            event.setCancelled(true);
            return;
        }

        this.onPlayerDamageBlock(fundamentalPlayer, data, event);

    }

    /**
     * 
     * @param event 
     * @return True if this skill handled the block regen
     */
    protected void handleBlockBreakResourceRegen(BlockBreakEvent event) {
        
        final Player player = event.getPlayer();
        final Block block = event.getBlock();
        final Material type = block.getType();
        final Byte dataId = block.getData();
        
        // Give the player the drops directly
        final Inventory playerInvent = player.getInventory();
        Collection<ItemStack> drops = block.getDrops();
        for (ItemStack drop : drops) {
            playerInvent.addItem(drop);
        }
        
        // Set the block to bedrock
        block.setType(m_regenBlockType);
        m_blocksToRegen.put(block, type);

        // Start a timer for when the block should regen
        Bukkit.getScheduler().scheduleSyncDelayedTask(
            m_plugin,
            new Runnable() {
                @Override
                public void run() {
                    block.setType(type);
                    m_blocksToRegen.remove(block);
                }
            },
            m_blocks.get(type).get(dataId).getRegenTime()
        );
        
    }
    
    /**
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        
        // If the player is in creative, let them do what they want
        final Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        // Cancel block breaks
        event.setCancelled(true);
        
        final Block block = event.getBlock();
        final Material material = block.getType();
        final Byte dataId = block.getData();
        
        // If this block isn't registered with this skill
        if (!m_blocks.containsKey(material)) {
            return;
        }        
        
        Map<Byte, BlockData> blockData = m_blocks.get(block.getType());
        if (!blockData.containsKey(dataId)) {
            return;
        }
        
        // Always cancel an event if not in creative
        event.setCancelled(true);
        
        FundamentalPlayer fundamentalPlayer = SurvivalPlus.Players.getPlayer(player);
        if (fundamentalPlayer == null) {
            return;
        }

        PlayerSkillData data = (PlayerSkillData) fundamentalPlayer.getPlayerData(this.getPlayerDataClass());
        if (data == null) {
            return;
        }
        
        ItemStack tool = player.getPlayer().getItemInHand();
        if (tool == null || !m_tools.containsKey(tool.getType())) {
            return;
        }        

        if (!canBreakBlock(fundamentalPlayer, block, data, tool)) {
            return;
        }
                
        // Call the notify for skills
        this.onPlayerBreakBlock(fundamentalPlayer, data, event);
        
        // Give the xp
        data.addXP(m_blocks.get(material).get(dataId).getXp());
        
        // Handle resource regening
        handleBlockBreakResourceRegen(event);
    }

    /**
     * @param event
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {

        // If the player is in creative, let them do what they want
        final Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        
        event.setCancelled(true);

    }
}
