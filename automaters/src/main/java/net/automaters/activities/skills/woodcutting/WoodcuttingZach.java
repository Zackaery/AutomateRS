package net.automaters.activities.skills.woodcutting;

import net.automaters.tasks.Task;
import net.automaters.util.items.*;
import static net.automaters.util.items.PrimaryTools.*;

public class WoodcuttingZach extends Task {

    PrimaryTools primaryTools = new PrimaryTools();
    SecondaryTools secondaryTools = new SecondaryTools();
    SkillingOutfits skillingOutfits = new SkillingOutfits();


    private static String task;
    public static int primaryToolID;
    public static String primaryTool = null;
    public static String secondaryTool = null;
    public static String outfit = null;

    public WoodcuttingZach() {
        super();
    }

    @Override
    public void onStart() {
        if (primaryTool == null) {
            getPrimaryTool(WoodcuttingTools.class);

        } else {
            setStarted(true);
        }
    }
    @Override
    public int onLoop() {
        return 0;
    }

    @Override
    public boolean taskFinished() {
        return false;
    }

}
