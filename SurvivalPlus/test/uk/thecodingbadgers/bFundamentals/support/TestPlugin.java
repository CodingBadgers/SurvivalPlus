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
package uk.thecodingbadgers.bFundamentals.support;

import org.bukkit.Achievement;
import org.bukkit.inventory.ItemStack;

import com.google.gson.GsonBuilder;

import uk.codingbadgers.SurvivalPlus.SurvivalPlus;
import uk.codingbadgers.SurvivalPlus.message.ClickEventType;
import uk.codingbadgers.SurvivalPlus.message.HoverEventType;
import uk.codingbadgers.SurvivalPlus.message.Message;
import uk.codingbadgers.SurvivalPlus.serialization.AchievementSerializer;
import uk.codingbadgers.SurvivalPlus.serialization.ItemStackSerializer;

public class TestPlugin extends SurvivalPlus {

	public TestPlugin() {
		m_gson = new GsonBuilder()
						.registerTypeAdapter(Message.class, new Message.MessageSerializer())
						.registerTypeAdapter(ClickEventType.class, new ClickEventType.ClickEventSerializer())
						.registerTypeAdapter(HoverEventType.class, new HoverEventType.HoverEventSerializer())
						.registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
						.registerTypeAdapter(Achievement.class, new AchievementSerializer())
						.create();
	}
	
}
