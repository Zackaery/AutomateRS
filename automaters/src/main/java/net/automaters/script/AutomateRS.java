package net.automaters.script;

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.automaters.account_builds.build_executor.BuildExecutor;
import net.automaters.gui.GUI;
import net.automaters.gui.utils.EventDispatchThreadRunner;
import net.runelite.api.Client;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.ui.overlay.OverlayManager;
import net.unethicalite.api.entities.Players;
import net.unethicalite.api.plugins.Task;
import net.unethicalite.api.plugins.TaskPlugin;
import net.unethicalite.api.script.paint.ExperienceTracker;
import org.pf4j.Extension;

import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static net.automaters.gui.GUI.*;

@PluginDependency(XpTrackerPlugin.class)

@Extension
@PluginDescriptor(
		name = "AutomateRS",
		description = "RuneScape - Automated")

@Slf4j
public class AutomateRS extends TaskPlugin {
	@Inject
	private AutomateRSConfig config;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private AutomateRSOverlay automateRSOverlay;
	@Inject
	private Client client;
	@Provides
	AutomateRSConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutomateRSConfig.class);
	}
	@Getter(AccessLevel.PROTECTED)

	private GUI GUI;
	public static ExperienceTracker xpTracker;
	private final Task[] tasks = new Task[] {};
	public static boolean debugEnabled = true;
	public static boolean scriptStarted;

	@Override
	protected void startUp() { overlayManager.add(automateRSOverlay); }

	public Task[] getTasks() { return new Task[0]; }

	@Override
	public void stop() {
		super.stop();
		debug("AutomateRS has stopped.");
		if (GUI != null && GUI.isOpen()) { GUI.close();	}
		overlayManager.remove(automateRSOverlay);
	}

	@Subscribe
	public void onConfigButtonPressed(ConfigButtonClicked event) throws InterruptedException {

		if (event.getGroup().contains("automaters")) {
			if (event.getKey().toLowerCase().contains("start")) {
				var local = Players.getLocal();
				if (local == null) {
					debug("Local Player not located");
					return;
				}
				if (!started) {
					selectedBuild = loadBuildFromGUI();
				} else {
					this.scriptStarted = true;
					debug("Started - AutomateRS");
				}
			} else if (event.getKey().toLowerCase().contains("pause")) {
				scriptStarted = false;
				debug("Paused - AutomateRS");
			} else {
				started = false;
				scriptStarted = false;
				debug("Stopped - AutomateRS");
			}
		}
	}

	@Override
	protected int loop() throws InterruptedException {
		if (scriptStarted) {
			if (started) {
				new BuildExecutor();
				debug("--- Initiating loop sequence ---\n\n");
				return 600;
			}
		}
		return 600;
	}

	private String loadBuildFromGUI() throws InterruptedException {
		try {
			EventDispatchThreadRunner.runOnDispatchThread(() -> {
				try {
					GUI = new GUI();
					GUI.open();
					debug("Launching AutomateRS - GUI");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}, true);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}

		if (started) {
			debug("SELECTED BUILD = " + selectedBuild);
			return selectedBuild;
		} else {
			return null;
		}
	}

	public static void debug(String message) {
		if (debugEnabled) {
			System.out.println("[AutomateRS] - " + message);
		}
	}
}
