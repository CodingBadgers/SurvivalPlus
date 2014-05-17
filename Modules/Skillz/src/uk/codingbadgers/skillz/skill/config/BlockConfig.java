/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.codingbadgers.skillz.skill.config;

import java.util.ArrayList;

/**
 *
 * @author n3wton
 */
public class BlockConfig {
    
    public class Block {
        public String material;
        public Byte dataid;
        public Long xp;
        public int requiredlevel;
        public int regentime_minutes;
        public int regentime_seconds;
    }
    
    public ArrayList<Block> blocks;
    
}
