/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
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
package net.runelite.client.plugins.chatcommands;

import java.awt.event.KeyEvent;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.inject.Provides;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.ScriptID;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.VarClientStr;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyListener;

import static net.runelite.api.ScriptID.CHATBOX_INPUT;

@Singleton
@Slf4j
public class ChatKeyboardListener implements KeyListener
{
	@Inject
	private ChatCommandsConfig chatCommandsConfig;

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private ChatCommandsConfig config;

	@Provides
	ChatCommandsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ChatCommandsConfig.class);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{

	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (!config.shortcuts().equals("") && e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			String input = client.getVar(VarClientStr.CHATBOX_TYPED_TEXT);
			if (!config.shortcuts().equals(""))
			{
				String[] pairs = config.shortcuts().split(",");
				String[] shortcut;
				for (String pair : pairs)
				{
					shortcut = pair.split("=");
					if (shortcut.length != 2)
						continue;

					if (shortcut[0].equals(input) || (input.length() > 1 && input.charAt(0) == '/' && shortcut[0].equals(input.substring(1)))) {
						final String phrase = shortcut[1];
						client.setVar(VarClientStr.CHATBOX_TYPED_TEXT, phrase);

						if (input.charAt(0) == '/')
						{
							clientThread.invoke(() ->
									client.runScript(133377, "/" + phrase));
						} else {
							clientThread.invoke(() ->
									client.runScript(CHATBOX_INPUT, 0, phrase));
						}

						client.setVar(VarClientStr.CHATBOX_TYPED_TEXT, "");
					}
				}
			}
		}

		if (chatCommandsConfig.clearSingleWord().matches(e))
		{
			String input = client.getVar(VarClientStr.CHATBOX_TYPED_TEXT);
			if (input != null)
			{
				// remove trailing space
				while (input.endsWith(" "))
				{
					input = input.substring(0, input.length() - 1);
				}
				
				// find next word
				int idx = input.lastIndexOf(' ');
				final String replacement;
				if (idx != -1)
				{
					replacement = input.substring(0, idx);
				}
				else
				{
					replacement = "";
				}

				clientThread.invoke(() ->
				{
					client.setVar(VarClientStr.CHATBOX_TYPED_TEXT, replacement);
					client.runScript(ScriptID.CHAT_PROMPT_INIT);
				});
			}
		}
		else if (chatCommandsConfig.clearChatBox().matches(e))
		{
			clientThread.invoke(() ->
			{
				client.setVar(VarClientStr.CHATBOX_TYPED_TEXT, "");
				client.runScript(ScriptID.CHAT_PROMPT_INIT);
			});
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{

	}
}
