package net.runelite.client.plugins.playerindicators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlayerIndicatorUtils {

    public static boolean isCaller(PlayerIndicatorsConfig config, String playerName)
    {
        String[] callers = config.getCallerNames().split(",");
        for (String caller : callers)
        {
            if (caller.trim().equalsIgnoreCase(playerName)) return true;
        }
        return false;
    }

}
