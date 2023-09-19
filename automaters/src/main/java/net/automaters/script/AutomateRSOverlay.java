package net.automaters.script;

import com.google.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import java.awt.Dimension;
import java.awt.Graphics2D;

@Singleton
class AutomateRSOverlay extends Overlay
{
	private final Client client;
	private final AutomateRS plugin;

	@Inject
	private AutomateRSOverlay(Client client, AutomateRS plugin)
	{
		this.client = client;
		this.plugin = plugin;
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