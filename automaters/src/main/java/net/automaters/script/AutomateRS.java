package net.automaters.script;

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.automaters.account_builds.build_executor.BuildExecutor;
import net.automaters.gui.GUI;
import net.automaters.gui.utils.EventDispatchThreadRunner;
import net.automaters.script.panel.AutomateRSPanel;
import net.automaters.util.file_managers.ImageManager;
import net.runelite.api.Client;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.unethicalite.api.plugins.Task;
import net.unethicalite.api.plugins.TaskPlugin;
import net.unethicalite.api.script.blocking_events.BlockingEventManager;
import net.unethicalite.api.script.blocking_events.LoginEvent;
import net.unethicalite.api.script.paint.ExperienceTracker;
import org.pf4j.Extension;

import javax.annotation.Nullable;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ScheduledExecutorService;

import static net.automaters.gui.GUI.*;

@PluginDependency(XpTrackerPlugin.class)

@Extension
@PluginDescriptor(
		name = "AutomateRS",
		description = "RuneScape - Automated")

@Slf4j
public class AutomateRS extends TaskPlugin {
	@Inject
	@Nullable
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AutomateRSOverlay automateRSOverlay;

	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private AutomateRSConfig config;

	@Inject
	private ClientThread clientThread;

	@Inject
	private BlockingEventManager blockingEventManager;

	@Getter
	private final LoginEvent loginEvent = new LoginEvent(this.blockingEventManager);

	@Getter(AccessLevel.PROTECTED)

	@Inject
	private ScheduledExecutorService executorService;

	private AutomateRSPanel panel;
	private NavigationButton navButton;

	@Provides
	AutomateRSConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutomateRSConfig.class);
	}

	private GUI GUI;
	public static ExperienceTracker xpTracker;
	private final Task[] tasks = new Task[] {};
	public static boolean debugEnabled = true;
	public static boolean scriptStarted;

	@Override
	protected void startUp() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
		panel = injector.getInstance(AutomateRSPanel.class);
		panel.init();

		navButton = NavigationButton.builder()
				.tooltip("AutomateRS")
				.icon(ImageManager.getInstance().loadImage("resources/net.automaters.script/panel/navButton.png"))
				.priority(0)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navButton);
		blockingEventManager.remove(LoginEvent.class);

		overlayManager.add(automateRSOverlay);
	}

	public Task[] getTasks() { return new Task[0]; }

	@Override
	public void stop() {
		super.stop();
		debug("AutomateRS has stopped.");
		if (GUI != null && GUI.isOpen()) { GUI.close();	}
		overlayManager.remove(automateRSOverlay);
	}

//	@Subscribe
//	public void onConfigButtonPressed(ConfigButtonClicked event) throws InterruptedException {
//
//		if (event.getGroup().contains("automaters")) {
//			if (event.getKey().toLowerCase().contains("start")) {
//				var local = Players.getLocal();
//				if (local == null) {
//					debug("Local Player not located");
//					return;
//				}
//				if (!started) {
//					selectedBuild = loadBuildFromGUI();
//				} else {
//					this.scriptStarted = true;
//					debug("Started - AutomateRS");
//				}
//			} else if (event.getKey().toLowerCase().contains("pause")) {
//				scriptStarted = false;
//				debug("Paused - AutomateRS");
//			} else {
//				started = false;
//				scriptStarted = false;
//				debug("Stopped - AutomateRS");
//			}
//		}
//	}

	@Override
	protected int loop()  {
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
