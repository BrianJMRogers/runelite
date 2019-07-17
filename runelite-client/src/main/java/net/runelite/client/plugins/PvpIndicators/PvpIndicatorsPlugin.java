/*
 * Copyright (c) 2018, Tomas Slusny <slusnucky@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.pvpindicators;

import com.google.inject.Provides;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import net.runelite.api.events.MenuOptionClicked;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatCommandManager;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.chat.QueuedMessage;
import net.runelite.api.ChatMessageType;
import java.util.concurrent.ScheduledExecutorService;
import net.runelite.http.api.chat.ChatClient;
import net.runelite.api.Client;
import net.runelite.client.util.Text;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.LocalPlayerDeath;


@PluginDescriptor(
	name = "PvP Indicators",
	description = "Indicators for PvP situations",
	tags = {"highlight", "overlay"},
	enabledByDefault = false
)



@Slf4j
public class PvpIndicatorsPlugin extends Plugin
{
	private Map<String,String> callerTargets = new HashMap<String,String>();
	private String DEATH_MESSAGE = "!Oh Dear, I Am Dead!";

	@Inject
	private Client client;

	private final ChatClient chatClient = new ChatClient();

	@Inject
	private ChatMessageManager chatMessageManager;

	@Inject
	private PvpIndicatorsConfig config;

	@Inject
	private ScheduledExecutorService executor;


	@Provides
	PvpIndicatorsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PvpIndicatorsConfig.class);
	}

	@Subscribe
	public void onLocalPlayerDeath(LocalPlayerDeath death)
	{
		if (config.iAmCaller())
		{
			client.runScript(133377, "/" + DEATH_MESSAGE);
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		if (chatMessage.getType() == ChatMessageType.FRIENDSCHAT &&
			  config.listenForCalls())
		{
			String target = chatMessage.getMessage();
			if (target.charAt(0) == '!')
			{
				// if death message, remove call
				if (target.equals(DEATH_MESSAGE))
				{
					log.debug(target + "==" + DEATH_MESSAGE);
					callerTargets.remove(chatMessage.getName());
				}

				// else add call
				else
				{
					callerTargets.put(chatMessage.getName(), target.substring(1));
					log.debug("Target list: ");
					for (String t : this.getTargets())
					{
						log.debug("\ttarget: " + t);
					}
				}
			}
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		log.debug("Menu option clicked...");
		if (!config.iAmCaller())
		{
			log.debug("Not a caller...");
			return;
		}

		if (event.getMenuOption().equals("Attack"))
		{
			String target = Text.removeTags(event.getMenuTarget());
			target = target.substring(0, target.indexOf("(") - 1);
			client.runScript(133377, "/!" + target);
			log.debug("Clicked attack!");
		}
		return;
	}

	public Collection<String> getTargets()
	{
		return callerTargets.values();
	}

}
