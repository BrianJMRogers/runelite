package net.runelite.client.plugins.playerindicators;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PlayerIndicatorUtils {

    public static boolean isCaller(PlayerIndicatorsConfig config, String playerName)
    {
        log.debug(playerName + " checking if caller");
        String[] callers = config.getCallerNames().split(",");
        log.debug("CallerNames: " + config.getCallerNames());
        for (String caller : callers)
        {
            log.debug("compairing " + playerName + " with " + caller);
            if (caller.equalsIgnoreCase(playerName)) return true;
        }
        return false;
    }

}
