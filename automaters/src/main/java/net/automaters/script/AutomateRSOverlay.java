package net.automaters.script;

import com.google.inject.Singleton;
import com.openosrs.client.ui.overlay.components.table.TableAlignment;
import com.openosrs.client.ui.overlay.components.table.TableComponent;
import net.automaters.util.file_managers.ImageManager;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.reportbutton.TimeFormat;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.ColorUtil;
import net.unethicalite.api.widgets.Widgets;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static java.awt.Transparency.TRANSLUCENT;
import static net.automaters.api.utils.Debug.debug;
import static net.automaters.api.utils.Debug.displayMessage;
import static net.automaters.gui.GUI.started;
import static net.automaters.script.AutomateRS.*;

@Singleton
class AutomateRSOverlay extends OverlayPanel {

	private final Client client;
	private final AutomateRS automate;

	private final Font font = new Font("Arial", Font.BOLD, 12);

	final Image title = ImageManager.getInstance().loadImage("panel/AutomateRS.png");


	@Inject
	private AutomateRSOverlay(Client client, AutomateRS automate)
	{
		this.client = client;
		this.automate = automate;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D g) {
		Widget chatbox = Widgets.get(WidgetInfo.CHATBOX);
		if (!started || scriptTimer == -1 || title == null || chatbox == null) {
			return null;
		}

		g.setFont(font);

		int paintY = chatbox.getBounds().y;
		g.drawImage(title, 0, paintY-80, null);

		g.setColor(Color.WHITE);
		g.drawString("Runtime: " + timeFormat(), 10, paintY-20);
		g.drawString("Status: " + displayMessage, 10, paintY-5);
		return super.render(g);
	}

	public String timeFormat() {
		long totalElapsedTime = scriptStarted ? System.currentTimeMillis() - scriptTimer : elapsedTime;
		Duration duration = Duration.ofMillis(totalElapsedTime);
		long hours = duration.toHours();
		long minutes = duration.toMinutesPart();
		long seconds = duration.toSecondsPart();
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
}