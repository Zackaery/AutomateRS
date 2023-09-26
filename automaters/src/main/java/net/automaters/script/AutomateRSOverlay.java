package net.automaters.script;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.plugins.unethicaldevtools.InteractionOverlay;
import net.unethicalite.client.managers.InputManager;
import net.automaters.util.file_managers.ImageManager;
import net.runelite.api.Point;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.*;
import net.unethicalite.api.widgets.Widgets;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.time.Duration;
import static net.automaters.api.utils.Debug.displayMessage;
import static net.automaters.gui.GUI.started;
import static net.automaters.script.AutomateRS.*;
import static net.automaters.util.file_managers.IconManager.AUTOMATERS_TITLE;
import static net.automaters.util.file_managers.IconManager.convert;

@Singleton
@Slf4j
public class AutomateRSOverlay extends OverlayPanel {

	@Inject
	private InputManager inputManager;

	private final Font font = new Font("Arial", Font.BOLD, 12);

	final Image title = ImageManager.getInstance().loadImage("script\\title.png");


	@Inject
	private AutomateRSOverlay()
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
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



//		OverlayUtil.renderTextLocation(g, new Point(inputManager.getLastClickX() - (g.getFont().getSize() / 3),
//				inputManager.getLastClickY() + (g.getFont().getSize() / 3)), "X", Color.WHITE);
//
//		OverlayUtil.renderTextLocation(g,
//				new Point(inputManager.getLastMoveX() - (g.getFont().getSize() / 3),
//						inputManager.getLastMoveY() + (g.getFont().getSize() / 3)), "X", Color.ORANGE);

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