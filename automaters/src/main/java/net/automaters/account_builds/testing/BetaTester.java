package net.automaters.account_builds.testing;


import static net.automaters.gui.GUI.selectedBuild;
import static net.automaters.script.AutomateRS.debug;

public class BetaTester {

    public BetaTester() {
        run();
        to();
        me();
    }
    public void run() {
        debug("BETA_TESTER(RUN)");
    }
    public void to() {
        debug("BETA_TESTER(TO)");
        selectedBuild = "ALPHA_TESTER";
    }
    public void me() {
        debug("BETA_TESTER(ME)");
    }
}
