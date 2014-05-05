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
package uk.thecodingbadgers.bFundamentals.messaging;

import java.util.Arrays;

import org.bukkit.Achievement;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.message.ClickEventType;
import uk.codingbadgers.SurvivalPlus.message.HoverEventType;
import uk.codingbadgers.SurvivalPlus.message.Message;
import uk.thecodingbadgers.bFundamentals.TestContainer;

import static org.junit.Assert.*;

public class SerializationTest extends TestContainer {

	private static Gson GSON = null;
	
	private static final String JSON_MESSAGE_1 = "{\"text\":\"Hello world\",\"color\":\"gold\",\"bold\":true}";
	
	private static final String JSON_ITEMSTACK_1 = "{\"id\":260}";
	private static final String JSON_ITEMSTACK_2 = "{\"id\":260,\"Count\":5}";
	private static final String JSON_ITEMSTACK_3 = "{\"id\":260,\"Count\":5,\"Damage\":7}";
	private static final String JSON_ITEMSTACK_4 = "{\"id\":260,\"Count\":5,\"Damage\":7,\"tag\":{\"Name\":\"Apple of Awsome\",\"Lore\":[\"Its just a apple\"]}}";
	private static final String JSON_ITEMSTACK_5 = "{\"id\":260,\"Count\":5,\"Damage\":7,\"tag\":{\"ench\":[{\"id\":0,\"lvl\":1}]}}";
	
	@Before
	public void setupTest() {
		if (GSON == null) {
			GSON = SurvivalPlus.getGsonInstance();
		}
	}
	
	@Test
	public void testMessageSerialize() {
		Message message = new Message("Hello world");
		message.setColor(ChatColor.GOLD);
		message.addStyle(ChatColor.BOLD);
		
		assertEquals(JSON_MESSAGE_1, GSON.toJson(message));
	}

	@Test
	public void testMessageDeserialize() {
		Message expected = new Message("Hello world");
		expected.setColor(ChatColor.GOLD);
		expected.addStyle(ChatColor.BOLD);
		
		assertEquals(expected, GSON.fromJson(JSON_MESSAGE_1, Message.class));
	}
	
	@Test
	public void testAchievementSerialize() {
		assertEquals("\"achievement.buildWorkBench\"", GSON.toJson(Achievement.BUILD_WORKBENCH));
		assertEquals("\"achievement.bakeCake\"", GSON.toJson(Achievement.BAKE_CAKE));
	}

	@Test
	public void testAchievementDeserialize() {
		assertEquals(Achievement.BUILD_WORKBENCH, GSON.fromJson("\"achievement.buildWorkBench\"", Achievement.class));
		assertEquals(Achievement.BAKE_CAKE,  GSON.fromJson("\"achievement.bakeCake\"", Achievement.class));
	}
	
	@Test
	public void testHoverEventTypeSerialize() {
		assertEquals("\"show_text\"", GSON.toJson(HoverEventType.SHOW_TOOLTIP));
		assertEquals("\"show_item\"", GSON.toJson(HoverEventType.SHOW_ITEM));
		assertEquals("\"show_achievement\"", GSON.toJson(HoverEventType.SHOW_ACHIEVEMENT));
	}

	@Test
	public void testHoverEventTypeDeserialize() {
		assertEquals(HoverEventType.SHOW_TOOLTIP, GSON.fromJson("\"show_text\"", HoverEventType.class));
		assertEquals(HoverEventType.SHOW_ITEM, GSON.fromJson("\"show_item\"", HoverEventType.class));
		assertEquals(HoverEventType.SHOW_ACHIEVEMENT, GSON.fromJson("\"show_achievement\"", HoverEventType.class));
	}

	@Test
	public void testClickEventTypeSerialize() {
		assertEquals("\"open_file\"", GSON.toJson(ClickEventType.OPEN_FILE));
		assertEquals("\"open_url\"", GSON.toJson(ClickEventType.OPEN_URL));
		assertEquals("\"run_command\"", GSON.toJson(ClickEventType.RUN_COMMAND));
		assertEquals("\"suggest_command\"", GSON.toJson(ClickEventType.SUGGEST_COMMAND));
	}

	@Test
	public void testClickEventTypeDeserialize() {
		assertEquals(ClickEventType.OPEN_FILE, GSON.fromJson("\"open_file\"", ClickEventType.class));
		assertEquals(ClickEventType.OPEN_URL, GSON.fromJson("\"open_url\"", ClickEventType.class));
		assertEquals(ClickEventType.RUN_COMMAND, GSON.fromJson("\"run_command\"", ClickEventType.class));
		assertEquals(ClickEventType.SUGGEST_COMMAND, GSON.fromJson("\"suggest_command\"", ClickEventType.class));
	}
	
	@Test
	public void testItemStackSerialize1() {
		ItemStack stack = new ItemStack(Material.APPLE);
		assertEquals(JSON_ITEMSTACK_1, GSON.toJson(stack));
	}

	@Test
	public void testItemStackSerialize2() {
		ItemStack stack = new ItemStack(Material.APPLE, 5);
		assertEquals(JSON_ITEMSTACK_2, GSON.toJson(stack));
	}

	@Test
	public void testItemStackSerialize3() {
		ItemStack stack = new ItemStack(Material.APPLE, 5, (short) 7);
		assertEquals(JSON_ITEMSTACK_3, GSON.toJson(stack));
	}

	@Test
	public void testItemStackSerialize4() {
		ItemStack stack = new ItemStack(Material.APPLE, 5, (short) 7);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("Apple of Awsome");
		meta.setLore(Arrays.asList("Its just a apple"));
		stack.setItemMeta(meta);
		assertEquals(JSON_ITEMSTACK_4, GSON.toJson(stack));
	}

	@Test
	public void testItemStackSerialize5() {
		ItemStack stack = new ItemStack(Material.APPLE, 5, (short) 7);
		ItemMeta meta = stack.getItemMeta();
		meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		stack.setItemMeta(meta);
		assertEquals(JSON_ITEMSTACK_5, GSON.toJson(stack));
	}

	@Test
	public void testItemStackDeserialize1() {
		ItemStack stack = new ItemStack(Material.APPLE);
		assertEquals(stack, GSON.fromJson(JSON_ITEMSTACK_1, ItemStack.class));
	}

	@Test
	public void testItemStackDeserialize2() {
		ItemStack stack = new ItemStack(Material.APPLE, 5);
		assertEquals(stack, GSON.fromJson(JSON_ITEMSTACK_2, ItemStack.class));
	}

	@Test
	public void testItemStackDeserialize3() {
		ItemStack stack = new ItemStack(Material.APPLE, 5, (short) 7);
		assertEquals(stack, GSON.fromJson(JSON_ITEMSTACK_3, ItemStack.class));
	}

	@Test
	public void testItemStackDeserialize4() {
		ItemStack stack = new ItemStack(Material.APPLE, 5, (short) 7);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("Apple of Awsome");
		meta.setLore(Arrays.asList("Its just a apple"));
		stack.setItemMeta(meta);
		assertEquals(stack, GSON.fromJson(JSON_ITEMSTACK_4, ItemStack.class));
	}

	@Test
	public void testItemStackDeserialize5() {
		ItemStack stack = new ItemStack(Material.APPLE, 5, (short) 7);
		ItemMeta meta = stack.getItemMeta();
		meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		stack.setItemMeta(meta);
		assertEquals(stack, GSON.fromJson(JSON_ITEMSTACK_5, ItemStack.class));
	}

}
