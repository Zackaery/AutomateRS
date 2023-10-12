package net.automaters.script;

import com.google.inject.Provides;
import com.openosrs.client.events.OPRSPluginChanged;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.automaters.account_builds.build_executor.BuildExecutor;
import net.automaters.gui.GUI;
import net.automaters.gui.utils.EventDispatchThreadRunner;
import net.automaters.overlay.AutomateRSOverlay;
import net.automaters.overlay.OverlayUtil;
import net.automaters.overlay.panel.AutomateRSPanel;
import net.automaters.overlay.panel.auto_login.ProfilePanel;
import net.automaters.util.file_managers.ImageManager;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.unethicalite.api.events.LobbyWorldSelectToggled;
import net.unethicalite.api.game.Worlds;
import net.unethicalite.api.plugins.LoopedPlugin;
import net.unethicalite.api.plugins.Task;
import net.unethicalite.api.script.blocking_events.BlockingEventManager;
import net.unethicalite.api.script.blocking_events.LoginEvent;
import net.unethicalite.api.script.blocking_events.WelcomeScreenEvent;
import net.unethicalite.api.widgets.Widgets;
import org.pf4j.Extension;

import javax.annotation.Nullable;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.inject.Inject;
import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.gui.GUI.*;
import static net.automaters.overlay.panel.AutomateRSPanel.*;
import static net.automaters.overlay.panel.auto_login.ProfilePanel.init;
import static net.runelite.api.GameState.LOGGED_IN;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

@SuppressWarnings("ALL")
@PluginDescriptor(name = "AutomateRS", description = "RuneScape - Automated")
@Extension
@Slf4j
public class AutomateRS extends LoopedPlugin {

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private AutomateRSOverlay automateRSOverlay;

	@Inject
	private OverlayUtil overlayUtil;



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
	private ProfilePanel profilePanel = new ProfilePanel();

	@Getter(AccessLevel.PUBLIC)
	private TileObject interactedObject;
	private NPC interactedNpc;
	@Getter(AccessLevel.PUBLIC)
	boolean attacked;
	private int clickTick;
	@Getter(AccessLevel.PUBLIC)
	private int gameCycle;

	@Provides
	AutomateRSConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutomateRSConfig.class);
	}

	Date currentDate = new Date();

	public static long scriptTimer;
	public static long elapsedTime;
	private GUI GUI;
	private final Task[] tasks = new Task[] {};
	public static boolean scriptStarted;
	private boolean hotswapEnabled = true;
	private ExecutorService executorService = Executors.newFixedThreadPool(1);

	@Subscribe
	private void onGameStateChanged(GameStateChanged e) {
			if (e.getGameState() == GameState.LOADING)
			{
				interactedObject = null;
			}
	}

	@Subscribe
	private void onExternalPluginChanged(OPRSPluginChanged e)
	{
		if (e.getPlugin() != this)
		{
			return;
		}

		if (e.isAdded())
		{
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

		OverlayMenuEntry menuEntry = new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "AutomateRS overlay");
		configManager.setConfiguration("unethicalite", "avoidWilderness", true);
		panel = injector.getInstance(AutomateRSPanel.class);
		try {EventDispatchThreadRunner.runOnDispatchThread(() -> {
			panel.refreshPanel();
			}, true);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		navButton = NavigationButton.builder()
				.tooltip("AutomateRS")
				.icon(ImageManager.getInstance().loadImage("script\\icon.png"))
				.priority(0)
				.panel(panel)
				.build();
		clientToolbar.addNavigation(navButton);
		blockingEventManager.remove(LoginEvent.class);

		overlayManager.add(automateRSOverlay);
		overlayManager.add(overlayUtil);
	}

	public Task[] getTasks() { return new Task[0]; }

	@Override
	public void stop() {
		super.stop();
		debug("AutomateRS has stopped.");
		if (GUI != null && GUI.isOpen()) { GUI.close();	}
		overlayManager.remove(automateRSOverlay);
		overlayManager.remove(overlayUtil);
	}

	@Subscribe
	public void onConfigButtonPressed(ConfigButtonClicked event) throws InterruptedException, IOException {
			if (event.getGroup().contains("automaters")) {
				if (event.getKey().toLowerCase().contains("start")) {
					if (client != null && client.getGameState() == LOGGED_IN) {
						if (localPlayer == null) {
							debug("Local Player not located");
							return;
						} else if (!started) {
							selectedBuild = loadBuildFromGUI();
							selectedBuild = "ALPHA_TESTER";
							started = true;
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
				debug("--- Initiating loop sequence ---\n");
				return 600;
			}
			return 600;
		}
		return 600;
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned)
	{
		if (npcDespawned.getNpc() == interactedNpc)
		{
			interactedNpc = null;
		}
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		if (client.getTickCount() > clickTick && client.getLocalDestinationLocation() == null)
		{
			// when the destination is reached, clear the interacting object
//			interactedObject = null;
//			interactedNpc = null;
		}
	}

	@Subscribe
	public void onInteractingChanged(InteractingChanged interactingChanged)
	{
		if (interactingChanged.getSource() == client.getLocalPlayer()
				&& client.getTickCount() > clickTick && interactingChanged.getTarget() != interactedNpc)
		{
			interactedNpc = null;
			attacked = interactingChanged.getTarget() != null && interactingChanged.getTarget().getCombatLevel() > 0;
		}
	}
	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked menuOptionClicked)
	{
		switch (menuOptionClicked.getMenuAction())
		{
			case WIDGET_TARGET_ON_GAME_OBJECT:
			case GAME_OBJECT_FIRST_OPTION:
			case GAME_OBJECT_SECOND_OPTION:
			case GAME_OBJECT_THIRD_OPTION:
			case GAME_OBJECT_FOURTH_OPTION:
			case GAME_OBJECT_FIFTH_OPTION:
			{
				int x = menuOptionClicked.getParam0();
				int y = menuOptionClicked.getParam1();
				int id = menuOptionClicked.getId();
				interactedObject = findTileObject(x, y, id);
				debug(interactedObject.getName() + " Location: "+interactedObject.getWorldLocation().toString());
				interactedNpc = null;
				clickTick = client.getTickCount();
				gameCycle = client.getGameCycle();
				break;
			}
			case WIDGET_TARGET_ON_NPC:
			case NPC_FIRST_OPTION:
			case NPC_SECOND_OPTION:
			case NPC_THIRD_OPTION:
			case NPC_FOURTH_OPTION:
			case NPC_FIFTH_OPTION:
			{
				interactedObject = null;
				interactedNpc = menuOptionClicked.getMenuEntry().getNpc();
				attacked = menuOptionClicked.getMenuAction() == MenuAction.NPC_SECOND_OPTION ||
						menuOptionClicked.getMenuAction() == MenuAction.WIDGET_TARGET_ON_NPC && WidgetInfo.TO_GROUP(client.getSelectedWidget().getId()) == WidgetID.SPELLBOOK_GROUP_ID;
				clickTick = client.getTickCount();
				gameCycle = client.getGameCycle();
				break;
			}
			// Any menu click which clears an interaction
			case WALK:
			case WIDGET_TARGET_ON_WIDGET:
			case WIDGET_TARGET_ON_GROUND_ITEM:
			case WIDGET_TARGET_ON_PLAYER:
			case GROUND_ITEM_FIRST_OPTION:
			case GROUND_ITEM_SECOND_OPTION:
			case GROUND_ITEM_THIRD_OPTION:
			case GROUND_ITEM_FOURTH_OPTION:
			case GROUND_ITEM_FIFTH_OPTION:
				interactedObject = null;
				interactedNpc = null;
				break;
			default:
				if (menuOptionClicked.isItemOp())
				{

					interactedObject = null;
					interactedNpc = null;
				}
		}
	}

	TileObject findTileObject(int x, int y, int id)
	{
		Scene scene = client.getScene();
		Tile[][][] tiles = scene.getTiles();
		Tile tile = tiles[client.getPlane()][x][y];
		if (tile != null)
		{
			for (GameObject gameObject : tile.getGameObjects())
			{
				if (gameObject != null && gameObject.getId() == id)
				{
					return gameObject;
				}
			}

			WallObject wallObject = tile.getWallObject();
			if (wallObject != null && wallObject.getId() == id)
			{
				return wallObject;
			}

			DecorativeObject decorativeObject = tile.getDecorativeObject();
			if (decorativeObject != null && decorativeObject.getId() == id)
			{
				return decorativeObject;
			}

			GroundObject groundObject = tile.getGroundObject();
			if (groundObject != null && groundObject.getId() == id)
			{
				return groundObject;
			}
		}
		return null;
	}

	@Nullable
	public Actor getInteractedTarget()
	{
		return interactedNpc != null ? interactedNpc : client.getLocalPlayer().getInteracting();
	}

//	@Override
//	protected int loop() {
//		if (scriptStarted) {
//			if (started) {
//				if (client != null && client.getGameState() == GameState.LOGGED_IN) {
//					new BuildExecutor();
//					debug("--- Initiating loop sequence ---\n");
//					return 600;
//				} else {
//					scriptStarted = false;
//					sleep(1800);
//					debug("Logging player back in.");
//					init(client);
//					sleep(1800);
//					return 600;
//				}
//			}
//		} else {
//			if (started) {
//				if (client != null && client.getGameState() == GameState.LOGGED_IN) {
//					scriptStarted = true;
//				}
//			} else {
//				return 600;
//			}
//		}
//		return 1000;
//	}

//	@Override
//	public void onStart(String... args) {
//
//	}


}
