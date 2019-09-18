package net.runelite.client.plugins.playerindicators;

import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemComposition;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Collection;

public class PlayerIndicatorUtils {
    private static ConcurrentHashMap<String,String> callerTargets = new ConcurrentHashMap<String,String>();
    public static boolean isCaller(PlayerIndicatorsConfig config, String playerName)
    {
        String[] callers = config.getCallerNames().split(",");
        for (String caller : callers)
        {
            if (caller.trim().equalsIgnoreCase(playerName)) return true;
        }
        return false;
    }

    public static boolean playerIsWearingUnchargedDragonstone(Client client)
    {
    	final ItemContainer con = client.getItemContainer(InventoryID.EQUIPMENT);
    	if (con == null)
	    {
	    	return false;
	    }

        final Item[] equipment = con.getItems();
		for (Item item : equipment) {
			if (item.getId() == ItemID.AMULET_OF_GLORY ||
	            item.getId() == ItemID.AMULET_OF_GLORY_T ||
	            item.getId() == ItemID.RING_OF_WEALTH ||
	            item.getId() == ItemID.RING_OF_WEALTH_I ||
	            item.getId() == ItemID.COMBAT_BRACELET ||
	            item.getId() == ItemID.SKILLS_NECKLACE)
			{
				return true;
			}
		}
      return false;
    }

    public static void removeTarget(String caller)
    {
      callerTargets.remove(caller);
    }

    public static void addTarget(String caller, String target)
    {
      callerTargets.put(caller, target.toLowerCase());
    }

    public static Collection<String> getTargets()
    {
      return callerTargets.values();
    }
}
