/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.skill;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author n3wton
 */
public class FishingConfig {

    public class FishType {

        public class FishMaterial {
            public String name;
            public Byte dataid;            
        }
        
        public String name;
        public String displayname;
        public long xp;
        public int requiredlevel;
        public FishMaterial material;        
        
        private ItemStack fish = null;
        
        public boolean setup() {
            Material material = Material.matchMaterial(this.material.name);
            if (material == null) {
                return false;                
            }
            
            fish = new ItemStack(material, 1, (short)this.material.dataid, this.material.dataid);
            ItemMeta fishData = fish.getItemMeta();
            fishData.setDisplayName(this.displayname);
            fish.setItemMeta(fishData);
            return true;
        }
        
        public ItemStack getFish() {
            return fish;
        }
        
    }
    
    public ArrayList<FishType> fishtypes;
    
}
