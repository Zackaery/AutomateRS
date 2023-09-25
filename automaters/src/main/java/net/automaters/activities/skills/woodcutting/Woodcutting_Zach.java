package net.automaters.activities.skills.woodcutting;

import net.runelite.api.Client;
import net.runelite.api.mixins.Inject;
import net.unethicalite.api.coords.Area;
import net.unethicalite.api.plugins.LoopedPlugin;

import static net.automaters.api.entities.LocalPlayer.canInteract;

@SuppressWarnings({"ConstantConditions","unused"})
public class Woodcutting_Zach extends LoopedPlugin {

    @Inject
    private Client client;

    Area LUMBRIDGE;

    @Override
    public int loop() {

        if (!canInteract()) { return -1; }

       // if (!inArea(LUMBRIDGE)) {

      //  }



        return 100;
    }


}

