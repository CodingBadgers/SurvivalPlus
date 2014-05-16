/*
 * Copyright (C) 2012 mewin <mewin001@hotmail.de>
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
package uk.codingbadgers.customflags;

import com.sk89q.util.yaml.YAMLFormat;
import com.sk89q.util.yaml.YAMLProcessor;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import uk.codingbadgers.SurvivalPlus.module.Module;
import uk.codingbadgers.customflags.data.FlagSaveHandler;
import uk.codingbadgers.customflags.data.JDBCSaveHandler;
import uk.codingbadgers.customflags.data.YAMLSaveHandler;
import uk.codingbadgers.customflags.util.ClassHacker;

/**
 * The WGCustomFlagsPlugin that allows you to hook into WorldGuard and setup custom flags
 * @author mewin <mewin001@hotmail.de>
 */
public class CustomFlags extends Module {

    private static final String DEFAULT_CONFIG = "# WGCustomFlags default config\r\n"
                                               + "# If you edit this make sure not to use tabulators, but spaces.\r\n"
                                               + "\r\n"
                                               + "# name: save-handler\r\n"
                                               + "# default: auto\r\n"
                                               + "# description: determines whether a database or a flat file is used to save the flags\r\n"
                                               + "# values: auto - detect which option WorldGuard uses, flat - force flat file usage\r\n"
                                               + "save-handler: auto\r\n"
                                               + "# name: flag-saving\r\n"
                                               + "# default: unload,save\r\n"
                                               + "# description: determines when to save the custom flag values\r\n"
                                               + "# values: one or multiple values of [unload, save, change] seperated with a comma\r\n"
                                               + "# unload - saves the flags when a world is unloaded (highly recommended as the flags are not saved for world if it is unloaded before the server shuts down)\r\n"
                                               + "# save - saves the flags everytime a world is saved (can cause lags if you have a plugin like MultiVerse the auto-saves the worlds very often)\r\n"
                                               + "# change - saves the flags everytime any flag has changed in the world\r\n"
                                               + "flag-saving: unload,save\r\n"
                                               + "# name: flag-logging\r\n"
                                               + "# default: true\r\n"
                                               + "# values: true, false\r\n"
                                               + "# set to false if you want to disable the logging of flag loading/saving and adding\r\n"
                                               + "flag-logging: true\r\n";
                                               /*+ "# name: tab-completions\r\n"
                                               + "# default: false\r\n"
                                               + "# description: enable/disable experimental tab completions for the region command of WorldGuard\r\n"
                                               + "# values: true,false\r\n"
                                               + "tab-completions: false";*/
    
    private JDBCSaveHandler jdbcConnector = null;
    private WGCustomFlagsListener listener;
    private PluginListener plListener;

    public static WorldGuardPlugin wgPlugin;

    private File configFile;
    private YAMLProcessor config;
    private boolean flagLogging = true;

    /**
     * sets up the plugin and creates connection to the WorldGuard plugin
     */
    private void setupWgPlugin() {
        Plugin plug = m_plugin.getServer().getPluginManager().getPlugin("WorldGuard");
        wgPlugin = (WorldGuardPlugin) plug;
    }

    private void loadConfig() {
        getLogger().info("Loading configuration...");
        if (!configFile.exists()) {
            try {
                getLogger().info("No configuration found, writing defaults.");
                writeDefaultConfig();
            } catch(IOException ex) {
                getLogger().log(Level.SEVERE, "Could not create default configuration.", ex);

                return;
            }
        }

        config = new YAMLProcessor(configFile, true, YAMLFormat.EXTENDED);

        try {
            config.load();
        }
        catch(IOException ex) {
            getLogger().log(Level.SEVERE, "Could read configuration.", ex);
            return;
        }

        getLogger().info("Configuration loaded.");
    }

    private void writeDefaultConfig() throws IOException {
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdirs();
        }

        if (!this.configFile.exists()) {
            this.configFile.createNewFile();
        }

        BufferedWriter out = new BufferedWriter(new FileWriter(configFile));

        out.write(DEFAULT_CONFIG);

        out.close();
    }

    @Override
    public void onEnable() {
        FlagManager.setWGCFInstance(this);
        configFile = new File(getDataFolder(), "config.yml");

        setupWgPlugin();
        if (wgPlugin.getGlobalStateManager().useSqlDatabase) {
            jdbcConnector = new JDBCSaveHandler(wgPlugin.getGlobalStateManager().sqlDsn,
                    wgPlugin.getGlobalStateManager().sqlUsername,
                    wgPlugin.getGlobalStateManager().sqlPassword, this);
        }

        listener = new WGCustomFlagsListener(this);
        plListener = new PluginListener(this);
        
        this.register(listener);
        this.register(plListener);

        loadConfig();
        
        if (config.getBoolean("tab-completions", false))
        {
            this.register(new TabCompleteListener(wgPlugin));
        }
        
        flagLogging = config.getBoolean("flag-logging", true);

        ClassHacker.setPrivateValue(wgPlugin.getDescription(), "version", wgPlugin.getDescription().getVersion() + " with custom flags plugin.");
    }
    
    public boolean isFlagLogging()
    {
        return this.flagLogging;
    }

    @Override
    public void onDisable()
    {
        saveAllWorlds(false);
        if (jdbcConnector != null)
        {
            jdbcConnector.close();
        }
    }
    /**
     * loads the flag values for all worlds
     * should not be called manually
     */
    public void loadAllWorlds()
    {
        Iterator<World> itr = m_plugin.getServer().getWorlds().iterator();

        while(itr.hasNext()) {
            loadFlagsForWorld(itr.next());
        }
    }

    /**
     * loads the flags for a single world
     * should not be called manually
     * @param world the world to load the flags for
     */
    public void loadFlagsForWorld(World world)
    {
        //getLogger().log(Level.INFO, "Loading flags for world {0}", world.getName());
        FlagSaveHandler handler = getSaveHandler();

        handler.loadFlagsForWorld(world);
    }

    /**
     * saves all custom flags to YAML file or database
     * should not be called manually
     */
    public void saveAllWorlds(boolean asynchron)
    {
        Iterator<World> itr = m_plugin.getServer().getWorlds().iterator();

        while(itr.hasNext()) {
            saveFlagsForWorld(itr.next(), asynchron);
        }
    }

    /**
     * saves the flag values for a single world
     * should not be called manually
     * @param world the world to save the flags for
     */
    public void saveFlagsForWorld(final World world, boolean asynchron)
    {
        getLogger().log(Level.FINEST, "Saving flags for world {0}", world.getName());
        final FlagSaveHandler handler = getSaveHandler();

        if (asynchron)
        {
            m_plugin.getServer().getScheduler().runTaskAsynchronously(m_plugin, new Runnable()
            {
                @Override
                public void run()
                {
                    handler.saveFlagsForWorld(world);
                }
            });
        }
        else
        {
            handler.saveFlagsForWorld(world);
        }
    }
    
    

    /**
     * adds a custom flag and hooks it into WorldGuard
     * @param flag the flag to add
     */
    public void addCustomFlag(Flag flag)
    {
        FlagManager.addCustomFlag(flag);
    }
    
    /**
     * adds flags for all public and static fields of a class that extend Flag
     * @param clazz the class that contains the flags
     */
    public void addCustomFlags(Class clazz) throws Exception
    {
        FlagManager.addCustomFlags(clazz);
    }
    
    /**
     * retrieves the custom flag configuration
     * @return a YAMLProcessor representing the configuration of the plugin
     */
    public YAMLProcessor getConf()
    {
        return this.config;
    }

    private FlagSaveHandler getSaveHandler()
    {
        if (config.getString("save-handler", "auto").equalsIgnoreCase("auto") && wgPlugin.getGlobalStateManager().useSqlDatabase)
        {
            return jdbcConnector;
        }
        else
        {
            return new YAMLSaveHandler(this, wgPlugin);
        }
    }
    
    public Object getFlag(Player player, Flag flag)
    {
        RegionManager regionManager = wgPlugin.getRegionManager(player.getWorld());
        if (regionManager == null) {
            return null;
        }
        
        final Location location = player.getLocation();
        final int px = location.getBlockX();       
        final int py = location.getBlockY();
        final int pz = location.getBlockZ();

        List<ProtectedRegion> playerRegions = new ArrayList<ProtectedRegion>();
        
        for (Entry<String, ProtectedRegion> regionEntry : regionManager.getRegions().entrySet()) {
            ProtectedRegion region = regionEntry.getValue();
            if (region.contains(px, py, pz)) {
                playerRegions.add(region);                
            }            
        }
        
        if (playerRegions.isEmpty()) {
            return null;
        }
                
        ProtectedRegion mainRegion = null;
        if (playerRegions.size() == 1) {
            mainRegion = playerRegions.get(0);
        }
        else {
            // work out the lowest child of the regions
            int childLevel = 0;
            for (ProtectedRegion region : playerRegions)
            {
                if (region.getParent() != null)
                {
                    ProtectedRegion tempRegion = region;
                    int tempChildLevel = 0;
                    while(tempRegion.getParent() != null)
                    {
                        tempRegion = tempRegion.getParent();
                        tempChildLevel++;
                    }

                    if (tempChildLevel > childLevel)
                    {
                        childLevel = tempChildLevel;
                        mainRegion = region;
                    }
                }
            }
        }
        
        if (mainRegion == null) {
            return null;
        }

        return mainRegion.getFlag(flag);
    }
}