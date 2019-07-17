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
package net.runelite.client.plugins.playerindicators;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("playerindicators")
public interface PlayerIndicatorsConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName = "drawOwnName",
		name = "Highlight own player",
		description = "Configures whether or not your own player should be highlighted"
	)
	default boolean highlightOwnPlayer()
	{
		return false;
	}

	@ConfigItem(
		position = 1,
		keyName = "ownNameColor",
		name = "Own player color",
		description = "Color of your own player"
	)
	default Color getOwnPlayerColor()
	{
		return new Color(0, 184, 212);
	}

	@ConfigItem(
		position = 2,
		keyName = "drawFriendNames",
		name = "Highlight friends",
		description = "Configures whether or not friends should be highlighted"
	)
	default boolean highlightFriends()
	{
		return true;
	}

	@ConfigItem(
		position = 3,
		keyName = "friendNameColor",
		name = "Friend color",
		description = "Color of friend names"
	)
	default Color getFriendColor()
	{
		return new Color(0, 200, 83);
	}

	@ConfigItem(
		position = 4,
		keyName = "ignoreClanMember",
		name = "Ignore clan members",
		description = "Configures whether or not clan members should be ignored"
	)
	default boolean ignoreClanMembers()
	{
		return false;
	}

	@ConfigItem(
		position = 5,
		keyName = "drawClanMemberNames",
		name = "Highlight clan members",
		description = "Configures whether or clan members should be highlighted"
	)
	default boolean drawClanMemberNames()
	{
		return true;
	}

	@ConfigItem(
		position = 6,
		keyName = "clanMemberColor",
		name = "Clan member color",
		description = "Color of clan members"
	)
	default Color getClanMemberColor()
	{
		return new Color(170, 0, 255);
	}


	@ConfigItem(
		position = 7,
		keyName = "ignoreTeamMembers",
		name = "Ignore team members",
		description = "Configures whether or not team members should be ignored"
	)
	default boolean ignoreTeamMembers()
	{
		return false;
	}

	@ConfigItem(
		position = 8,
		keyName = "drawTeamMemberNames",
		name = "Highlight team members",
		description = "Configures whether or not team members should be highlighted"
	)
	default boolean highlightTeamMembers()
	{
		return true;
	}

	@ConfigItem(
		position = 9,
		keyName = "teamMemberColor",
		name = "Team member color",
		description = "Color of team members"
	)
	default Color getTeamMemberColor()
	{
		return new Color(19, 110, 247);
	}

	@ConfigItem(
		position = 10,
		keyName = "drawNonClanMemberNames",
		name = "Highlight non-clan members",
		description = "Configures whether or not non-clan members should be highlighted"
	)
	default boolean highlightNonClanMembers()
	{
		return false;
	}

	@ConfigItem(
			position = 11,
		keyName = "nonClanMemberColor",
		name = "Non-clan member color",
		description = "Color of non-clan member names"
	)
	default Color getNonClanMemberColor()
	{
		return Color.RED;
	}

	@ConfigItem(
			position = 12,
			keyName = "highlightHittablePlayers",
			name = "Highlight hittable players",
			description = "Highlight hittable players in the wilderness"
	)
	default boolean highlightHittablePlayers()
	{
		return true;
	}

	@ConfigItem(
			position = 13,
			keyName = "getHittablePlayerColor",
			name = "Hittable player color",
			description = "Color of the hittable player"
	)
	default Color getHittablePlayerColor()
	{
		return Color.RED;
	}

	@ConfigItem(
			position = 14,
			keyName = "highlightPlayerClumps",
			name = "Highlight player clumps",
			description = "Draw tiles around players in potential clumps. Highlight hittable players must be enabled"
	)
	default boolean highlightPlayerClumps()
	{
		return true;
	}

	@ConfigItem(
			position = 15,
			keyName = "clumpablePlayerColors",
			name = "clumpable players color",
			description = "Color of the clumpable players"
	)
	default Color getClumpablePlayerColor()
	{
		return Color.RED;
	}

	@ConfigItem(
			position = 16,
			keyName = "highlightCallers",
			name = "Highlight callers",
			description = "Highlight callers"
	)
	default boolean highlightCallers()
	{
		return true;
	}

	@ConfigItem(
			position = 17,
			keyName = "getCallerColor",
			name = "Caller player color",
			description = "Color of the callers"
	)
	default Color getCallerColor()
	{
		return Color.RED;
	}

	@ConfigItem(
			keyName = "callerNames",
			name = "caller names",
			description = "Configures specifically highlighted ground items. Format: (item), (item)",
			position = 18
	)
	default String getCallerNames()
	{
		return "";
	}

	@ConfigItem(
			keyName = "callerNames",
			name = "",
			description = "",
			position = 19
	)
	void setCallerNames(String key);

	@ConfigItem(
		position = 20,
		keyName = "listenForTargetsFromCaller",
		name = "Listen for Targets From Caller",
		description = "Check this to highlight players your caller(s) attacks"
	)
	default boolean listenForCalls()
	{
		return false;
	}

	@ConfigItem(
			position = 21,
			keyName = "callerTargetColor",
			name = "Caller target color",
			description = "Color of the caller(s)'s target'"
	)
	default Color getCallerTargetColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		position = 22,
		keyName = "iAmCaller",
		name = "I Am Caller",
		description = "Check this for your attacks to me called out"
	)
	default boolean iAmCaller()
	{
		return false;
	}

	@ConfigItem(
		position = 23,
		keyName = "unchargedDragonstoneJewellery",
		name = "Warn Uncharged Dragonstone",
		description = "Draw overlay yourself if you're wearing uncharged dragonstone jewellery - \"draw tiles under players\" MUST be enabled"
	)
	default boolean warnUnchargedDragonstone()
	{
		return false;
	}

	@ConfigItem(
			position = 24,
			keyName = "unchargedDragonstoneWarningColor",
			name = "Uncharged Dragonstone Warning Color",
			description = "Color the overlay on your character when you wear uncharged dragonstone jewellery"
	)
	default Color getUnchargedDragonstoneColor()
	{
		return Color.RED;
	}

	@ConfigItem(
		position = 25,
		keyName = "drawPlayerTiles",
		name = "Draw tiles under players",
		description = "Configures whether or not tiles under highlighted players should be drawn"
	)
	default boolean drawTiles()
	{
		return false;
	}

	@ConfigItem(
		position = 26,
		keyName = "playerNamePosition",
		name = "Name position",
		description = "Configures the position of drawn player names, or if they should be disabled"
	)
	default PlayerNameLocation playerNamePosition()
	{
		return PlayerNameLocation.ABOVE_HEAD;
	}

	@ConfigItem(
		position = 27,
		keyName = "drawMinimapNames",
		name = "Draw names on minimap",
		description = "Configures whether or not minimap names for players with rendered names should be drawn"
	)
	default boolean drawMinimapNames()
	{
		return false;
	}

	@ConfigItem(
		position = 28,
		keyName = "colorPlayerMenu",
		name = "Colorize player menu",
		description = "Color right click menu for players"
	)
	default boolean colorPlayerMenu()
	{
		return true;
	}

	@ConfigItem(
		position = 29,
		keyName = "clanMenuIcons",
		name = "Show clan ranks",
		description = "Add clan rank to right click menu and next to player names"
	)
	default boolean showClanRanks()
	{
		return true;
	}

	@ConfigItem(
			position = 30,
			keyName = "showWildernessThreshold",
			name = "Show wilderness Threshold",
			description = "Draw the wilderness level above which both players must be in order to be hittable"
	)
	default boolean showWildernessThreshold()
	{
		return true;
	}
}
