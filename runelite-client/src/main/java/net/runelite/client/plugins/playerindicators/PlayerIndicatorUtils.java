package net.runelite.client.plugins.playerindicators;

import net.runelite.api.*;

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

}
