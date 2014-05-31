/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.skill;

import java.util.ArrayList;

/**
 *
 * @author n3wton
 */
public class CraftingConfig {

    public class CraftType {

        public class CraftMaterial {
            public String name;
            public Byte dataid;            
        }
        
        public CraftMaterial material;
        public long xp;
        public int requiredlevel;     
                
    }
    
    public ArrayList<CraftType> crafttypes;
    
}