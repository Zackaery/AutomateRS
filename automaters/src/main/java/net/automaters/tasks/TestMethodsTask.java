package net.automaters.tasks;

import net.unethicalite.api.items.Bank;
import net.unethicalite.api.items.Inventory;
import net.unethicalite.api.plugins.LoopedPlugin;

import static net.automaters.script.AutomateRS.debug;

public class TestMethodsTask extends LoopedPlugin {

    @Override
    protected int loop() {

       if (!Inventory.contains("Knife")) {
           debug("TRUE");
       } else {
           debug("FALSE");
       }
       return 600;
    }
}
