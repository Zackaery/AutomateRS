package net.automaters.script;

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.automaters.account_builds.build_executor.BuildExecutor;
import net.automaters.gui.GUI;
import net.automaters.script.panel.AutomateRSPanel;
import net.automaters.util.file_managers.ImageManager;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.World;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.PluginChanged;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.unethicalite.api.events.LobbyWorldSelectToggled;
import net.unethicalite.api.events.LoginStateChanged;
import net.unethicalite.api.events.WorldHopped;
import net.unethicalite.api.game.Game;
import net.unethicalite.api.game.Worlds;
import net.unethicalite.api.plugins.Task;
import net.unethicalite.api.plugins.TaskPlugin;
import net.unethicalite.api.script.blocking_events.BlockingEventManager;
import net.unethicalite.api.script.blocking_events.LoginEvent;
import net.unethicalite.api.script.blocking_events.WelcomeScreenEvent;
import net.unethicalite.api.script.paint.ExperienceTracker;
import net.unethicalite.api.widgets.Widgets;
import org.pf4j.Extension;

import javax.annotation.Nullable;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ScheduledExecutorService;

import net.automaters.script.panel.auto_login.ProfilePanel;

import static net.automaters.gui.GUI.*;
import static net.automaters.script.panel.AutomateRSPanel.*;
import static net.automaters.script.panel.auto_login.ProfilePanel.init;
import static net.unethicalite.api.input.Keyboard.sendEnter;

@PluginDescriptor(name = "AutomateRS", description = "RuneScape - Automated")
@Extension
@Slf4j
public class AutomateRS extends TaskPlugin {

	@Inject
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
	public static boolean sent;
	public static int currentWorld;

	@Subscribe
	private void onWidgetHiddenChanged(WidgetLoaded e)
	{
		int group = e.getGroupId();
		if (group == 378 || group == 413)
		{
			Widget playButton = WelcomeScreenEvent.getPlayButton();
			if (Widgets.isVisible(playButton))
			{
				client.invokeWidgetAction(1, playButton.getId(), -1, -1, "");
			}
		}
	}
	@Subscribe
	private void onLobbyWorldSelectToggled(LobbyWorldSelectToggled e) {
		if (e.isOpened()) {
			client.setWorldSelectOpen(false);
		}
		if (selectWorldBool) {
			World selectedWorld = Worlds.getFirst(useWorld);
			if (selectedWorld != null) {
				client.changeWorld(selectedWorld);
			}
		}

		client.promptCredentials(false);
		init(client);
	}



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

	@Override
	protected int loop()  {
		if (scriptStarted) {
			if (started) {
				new BuildExecutor();
				debug("--- Initiating loop sequence ---\n\n");
				return 600;
			}
			return 600;
		}
		return 600;
	}

	public static void debug(String message) {
		if (debugEnabled) {
			System.out.println("[AutomateRS] - " + message);
		}
	}
}
