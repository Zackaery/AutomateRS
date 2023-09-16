package net.automaters.script;

import com.google.inject.Singleton;
import net.unethicalite.api.scene.Tiles;
import net.runelite.api.Client;
import net.runelite.api.Tile;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;

@Singleton
class AutomateRSOverlay extends Overlay
{
	private final Client client;
	private final AutomateRS plugin;
	private final AutomateRSPanel panel;

	@Inject
	private AutomateRSOverlay(Client client, AutomateRS plugin, AutomateRSPanel panel)
	{
		this.client = client;
		this.plugin = plugin;
		this.panel = panel;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics2D)
	{

//		if (!plugin.isScriptStarted())
//		{
//			return null;
//		}


		return null;
	}
}