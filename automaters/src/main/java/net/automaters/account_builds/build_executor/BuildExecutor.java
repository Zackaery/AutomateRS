package net.automaters.account_builds.build_executor;

import net.automaters.account_builds.testing.AlphaTester;
import net.automaters.account_builds.testing.BetaTester;

import static net.automaters.gui.GUI.selectedBuild;

public class BuildExecutor {
    public BuildExecutor() {
        executeBuild();
    }

    public void executeBuild() {
        switch (selectedBuild) {
            case "ALPHA_TESTER":
                new AlphaTester();
                break;
            case "BETA_TESTER":
                new BetaTester();
                break;
        }

    }
}
