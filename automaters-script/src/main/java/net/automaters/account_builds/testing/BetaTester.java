package net.automaters.account_builds.testing;


import static net.automaters.gui.GUI.selectedBuild;
import static net.automaters.api.utils.Debug.debug;

public class BetaTester {

    public BetaTester() {
        run();
        to();
        me();
    }
    public void run() {
    }
    public void to() {
        selectedBuild = "ALPHA_TESTER";
    }
    public void me() {
    }
}
