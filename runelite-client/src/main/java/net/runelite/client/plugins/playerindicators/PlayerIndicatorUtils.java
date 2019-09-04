package net.runelite.client.plugins.playerindicators;

import net.runelite.api.Client;
import net.runelite.api.Item;
import net.runelite.api.ItemID;
import net.runelite.api.InventoryID;
import net.runelite.api.ItemComposition;

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
      final Item[] equipment = client.getItemContainer(InventoryID.EQUIPMENT).getItems();
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
