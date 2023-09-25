package net.automaters.script;

import com.google.inject.Provides;
import com.openosrs.client.events.OPRSPluginChanged;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.automaters.account_builds.build_executor.BuildExecutor;
import net.automaters.gui.GUI;
import net.automaters.gui.utils.EventDispatchThreadRunner;
import net.automaters.script.panel.AutomateRSPanel;
import net.automaters.util.file_managers.ImageManager;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.World;
import net.runelite.api.events.ConfigButtonClicked;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.unethicalite.api.events.LobbyWorldSelectToggled;
import net.unethicalite.api.game.Worlds;
import net.unethicalite.api.plugins.Task;
import net.unethicalite.api.plugins.TaskPlugin;
import net.unethicalite.api.script.blocking_events.BlockingEventManager;
import net.unethicalite.api.script.blocking_events.LoginEvent;
import net.unethicalite.api.script.blocking_events.WelcomeScreenEvent;
import net.unethicalite.api.widgets.Widgets;
import org.pf4j.Extension;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.gui.GUI.*;
import static net.automaters.script.panel.AutomateRSPanel.*;
import static net.automaters.script.panel.auto_login.ProfilePanel.init;

@SuppressWarnings("ALL")
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
	ConfigManager configManager;

	@Inject
	private BlockingEventManager blockingEventManager;

	@Getter
	private final LoginEvent loginEvent = new LoginEvent(this.blockingEventManager);

	@Getter(AccessLevel.PROTECTED)

	private AutomateRSPanel panel;
	private NavigationButton navButton;

	@Provides
	AutomateRSConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutomateRSConfig.class);
	}

	public static long scriptTimer;
	public static long elapsedTime;
	private GUI GUI;
	private final Task[] tasks = new Task[] {};
	public static boolean scriptStarted;
	private boolean hotswapEnabled = true;
	private ExecutorService executorService = Executors.newFixedThreadPool(1);

	@Subscribe
	private void onExternalPluginChanged(OPRSPluginChanged e)
	{
		if (e.getPlugin() != this)
		{
			debug("return");
			return;
		}

		if (e.isAdded())
		{
			debug("client.getcallbacks");
			client.getCallbacks();
		}
	}

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
		debug(String.valueOf(e));
		if (e.isOpened()) {
			client.setWorldSelectOpen(false);

			if (boolWorld) {
				World selectedWorld = Worlds.getFirst(useWorld);
				if (selectedWorld != null) {
					client.changeWorld(selectedWorld);
				}
			}
			client.promptCredentials(false);
			init(client);
		}
	}



	@Override
	protected void startUp() throws IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {


		configManager.setConfiguration("unethicalite", "avoidWilderness", true);
		panel = injector.getInstance(AutomateRSPanel.class);
		try {EventDispatchThreadRunner.runOnDispatchThread(() -> {
			debug("panel.init");
			panel.refreshPanel();
			}, true);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		navButton = NavigationButton.builder()
				.tooltip("AutomateRS")
				.icon(ImageManager.getInstance().loadImage("panel/navButton.png"))
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

	@Subscribe
	public void onConfigButtonPressed(ConfigButtonClicked event) throws InterruptedException {
			if (event.getGroup().contains("automaters")) {
				if (event.getKey().toLowerCase().contains("start")) {
					if (client != null && client.getGameState() == GameState.LOGGED_IN) {
						if (localPlayer == null) {
							debug("Local Player not located");
							return;
						} else if (!started) {
//							selectedBuild = loadBuildFromGUI();
							selectedBuild = "ALPHA_TESTER";
							started = true;
							this.scriptStarted = true;
							scriptTimer = (System.currentTimeMillis() - elapsedTime);
							debug("Started - AutomateRS");
						} else {
							this.scriptStarted = true;
							scriptTimer = (System.currentTimeMillis() - elapsedTime);
							debug("Started - AutomateRS");
						}
					} else {
						JOptionPane.showMessageDialog(null, "You're not logged in, please login before starting the plugin.", "Starting... AutomateRS", JOptionPane.ERROR_MESSAGE);
						debug("You're not logged in.");
					}
				} else if (event.getKey().toLowerCase().contains("pause")) {
						scriptStarted = false;
						elapsedTime = System.currentTimeMillis() - scriptTimer;
						debug("Paused - AutomateRS");
				} else {
						scriptTimer = 0;
						elapsedTime = 0;
						started = false;
						scriptStarted = false;
						debug("Stopped - AutomateRS");
				}
			}
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


}
