package net.runelite.client.util;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.Player;
import net.runelite.api.WorldType;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

import javax.inject.Inject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class WildernessUtils {
    public static final int LEVEL_ONE_WILDERNESS_Y = 5;
    public static final int LEVEL_TWO_WILDERNESS_Y = 8;
    public static final int MIN_COMBAT_LEVEL = 3;
    public static final int NUM_TILES_PER_WILDY_LEVEL = 8;
    public static final int WILDY_GRID_SIZE = 64;

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

    public static int isHittable(Player opponent, Client client)
    {
        log.debug("determining if " + opponent.getName() + " is hittable");


        // if we aren't in the wild, we can't hit them
        int ourWildernessLevel = getWildernessLevel(client);
        if (ourWildernessLevel == 0)
        {
            log.debug("YAOYAO our wilderness level is zero");
            return 0;
        }
        // we are in the wild. We need to use our wilderness level and our
        // current global y to figure out what 62 tile block we are in
        // if they aren't in the wild, we can't hit them
        if (opponent.getLocalLocation().getSceneY() < LEVEL_ONE_WILDERNESS_Y)
        {
            log.debug("YAOYAO: myY" + client.getLocalPlayer().getLocalLocation().getSceneY());
            log.debug("YAOYAO oppoenent y: " + opponent.getLocalLocation().getSceneY() );
            return 0;
        }
        int hittableDistance = 0;

        // calculate the opponents cb range
        int opponentCb = opponent.getCombatLevel();
        int opponentWildernessLevel = getWildernessLevelOfPlayer(opponent, client);
        int opponentMinHittableLevel = Math.max(MIN_COMBAT_LEVEL, opponentCb - opponentWildernessLevel);
        int opponentMaxHittableLevel = Math.min(Experience.MAX_COMBAT_LEVEL, opponentCb + opponentWildernessLevel);

        int ourCbLevel = client.getLocalPlayer().getCombatLevel();
        int ourMinHittableLevel = Math.max(MIN_COMBAT_LEVEL, ourCbLevel - ourWildernessLevel);
        int ourMaxHittableLevel = Math.min(Experience.MAX_COMBAT_LEVEL, ourCbLevel + ourWildernessLevel);
        log.debug(opponent.getName() + "opponentCb: " + opponentCb + ", opponentWildernessLevel: " + opponentWildernessLevel);
        log.debug(opponent.getName() + "opponentMinHittableLevel: " + opponentMinHittableLevel + ", oppnentMaxHittableLevel: " + opponentMaxHittableLevel);
        log.debug(opponent.getName() + ": " + ourCbLevel + " >= " + opponentMinHittableLevel + " && " + ourCbLevel + " <= " + opponentMaxHittableLevel);
        log.debug(opponent.getName() + ": " + opponentCb + " >= " + ourMinHittableLevel + " && " + opponentCb + " <= " + ourMaxHittableLevel);
        // determine if we are within their cb bracket and vice versa
        if (ourCbLevel >= opponentMinHittableLevel && ourCbLevel <= opponentMaxHittableLevel &&
            opponentCb >= ourMinHittableLevel && opponentCb <= ourMaxHittableLevel)
        {
            log.debug(opponent.getName() + " is returning as hittable");
            return Math.abs(ourCbLevel - opponentCb); // the level above which we can fight
        }
        log.debug(opponent.getName() + " is returning as NOT hittable");

        return hittableDistance;
    }

    private static int getWildernessLevelOfPlayer(Player opponent, Client client)
    {
        int ourWildernessLevel = getWildernessLevel(client);
        int ourYValue = client.getLocalPlayer().getWorldLocation().getRegionY();
        int wildyGridNumber = Math.floorDiv(ourWildernessLevel, NUM_TILES_PER_WILDY_LEVEL);
        int ourNumTilesFromZero = wildyGridNumber * WILDY_GRID_SIZE + ourYValue;

        log.debug(opponent.getName() + " ourwildernesslevel: " + ourWildernessLevel);
        log.debug(opponent.getName() + "ourYvalue: " + ourYValue);
        log.debug(opponent.getName() + "wildygridNumber: " + wildyGridNumber);
        log.debug(opponent.getName() + "ourNumTilesFromZero: " + ourNumTilesFromZero);
        int theirNumTilesFromZero;

        // if they're lower than us
        if (client.getLocalPlayer().getLocalLocation().getSceneY() > opponent.getLocalLocation().getSceneY())
        {
            theirNumTilesFromZero = ourNumTilesFromZero - (client.getLocalPlayer().getLocalLocation().getSceneY() - opponent.getLocalLocation().getSceneY());
            log.debug(opponent.getName() + " is lower than us and their numtilesfromzero: " + theirNumTilesFromZero + " from equation " + ourNumTilesFromZero + " - (" + client.getLocalPlayer().getLocalLocation().getSceneY() + " - " + opponent.getLocalLocation().getSceneY() + ")");
        }
        else // we're lower than them
        {
            theirNumTilesFromZero = ourNumTilesFromZero + (opponent.getLocalLocation().getSceneY() - client.getLocalPlayer().getLocalLocation().getSceneY());
            log.debug(opponent.getName() + " is higher than us and theirnumtilesfromzero: " + theirNumTilesFromZero);
        }

        if (theirNumTilesFromZero < LEVEL_ONE_WILDERNESS_Y)
        {
            log.debug(opponent.getName() + " is less than level 1");
            return 0;
        }

        int retval = Math.floorDiv(theirNumTilesFromZero, 8) + 1;
        log.debug(opponent.getName() + "(" + theirNumTilesFromZero + ", 8) + 1");
        return retval;
    }
}
