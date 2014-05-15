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
package uk.thecodingbadgers.SurvivalPlus.messaging;

import org.bukkit.ChatColor;
import org.junit.Test;

import uk.codingbadgers.SurvivalPlus.message.Message;

import static org.junit.Assert.*;

public class MessagingTest {

    @Test(expected = IllegalArgumentException.class)
    public void testMessageColor() {
        Message message = new Message("Hello world");
        message.setColor(ChatColor.BOLD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMessageStyle1() {
        Message message = new Message("Hello world");
        message.addStyle(ChatColor.GREEN);
    }

    @Test
    public void testMessageStyle2() {
        Message message = new Message("Hello world");
        message.addStyle(ChatColor.BOLD);
        message.addStyle(ChatColor.UNDERLINE);
        message.addStyle(ChatColor.STRIKETHROUGH);
        message.addStyle(ChatColor.ITALIC);
        message.addStyle(ChatColor.MAGIC);

        assertTrue(message.hasStyle(ChatColor.BOLD));
        assertTrue(message.hasStyle(ChatColor.UNDERLINE));
        assertTrue(message.hasStyle(ChatColor.STRIKETHROUGH));
        assertTrue(message.hasStyle(ChatColor.ITALIC));
        assertTrue(message.hasStyle(ChatColor.MAGIC));
    }

    @Test
    public void testMessageReset() {
        Message message = new Message("Hello world");
        message.addStyle(ChatColor.BOLD);
        message.addStyle(ChatColor.UNDERLINE);
        message.addStyle(ChatColor.STRIKETHROUGH);
        message.addStyle(ChatColor.ITALIC);
        message.addStyle(ChatColor.MAGIC);
        message.setColor(ChatColor.GOLD);

        message.addStyle(ChatColor.RESET);

        assertTrue(!message.hasStyle(ChatColor.BOLD));
        assertTrue(!message.hasStyle(ChatColor.UNDERLINE));
        assertTrue(!message.hasStyle(ChatColor.STRIKETHROUGH));
        assertTrue(!message.hasStyle(ChatColor.ITALIC));
        assertTrue(!message.hasStyle(ChatColor.MAGIC));
        assertTrue(message.getColor() == ChatColor.WHITE);
    }

}
