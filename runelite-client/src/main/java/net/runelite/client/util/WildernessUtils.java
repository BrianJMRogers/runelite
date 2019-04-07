package net.runelite.client.util;

import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.Player;
import net.runelite.api.WorldType;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WildernessUtils {
    public static final int LEVEL_ONE_WILDERNESS_Y = 53;
    public static final int LEVEL_TWO_WILDERNESS_Y = 56;
    public static final int LEVEL_THREE_WILDERNESS_Y = 64;

    private static int getWildernessLevel(Client client)
    {
        final Pattern WILDERNESS_LEVEL_PATTERN = Pattern.compile("^Level: (\\d+).*$");
        final Widget wildernessLevelWidget = client.getWidget(WidgetInfo.PVP_WILDERNESS_LEVEL);
        if (wildernessLevelWidget == null)
        {
            return 0;
        }

        final String wildernessLevelText = wildernessLevelWidget.getText();
        final Matcher m = WILDERNESS_LEVEL_PATTERN.matcher(wildernessLevelText);

        if (!m.matches() ||
                WorldType.isPvpWorld(client.getWorldType()))
        {
            return 0;
        }

        return Integer.parseInt(m.group(1));
    }

    public static int isHittable(Player player, Client client)
    {

        if (player.getLocalLocation().getSceneY() < LEVEL_ONE_WILDERNESS_Y)
        {
            return 0;
        }
        int wildernessLevel = getWildernessLevel(client);
        final int MIN_COMBAT_LEVEL = 3;
        int hittableDistance = 0;
        if (wildernessLevel != 0)
        {
            int combatLevel = client.getLocalPlayer().getCombatLevel();
            int minHittableLevel = Math.max(MIN_COMBAT_LEVEL, combatLevel - wildernessLevel);
            int maxHittableLevel = Math.min(Experience.MAX_COMBAT_LEVEL, combatLevel + wildernessLevel);
            int actorCb = player.getCombatLevel();

            if (actorCb >= minHittableLevel && actorCb <= maxHittableLevel)
            {
                hittableDistance = Math.abs(player.getCombatLevel() - client.getLocalPlayer().getCombatLevel());
            }
        }

        return hittableDistance;
    }

    private static int getWildernessLevelOfPlayer(Player player)
    {
        if (player.getLocalLocation().getSceneY() < LEVEL_ONE_WILDERNESS_Y)
        {
            return 0;
        }
        else if (player.getLocalLocation().getSceneY() < LEVEL_TWO_WILDERNESS_Y)
        {
            return 1;
        }

        // the player is at least in level 2 wilderness. At this point, every
        // 8 tiles they go, they'll go one level wilderness higher
        return Math.floorDiv(player.getLocalLocation().getSceneY() - 56, 8) + 2;
    }
}
