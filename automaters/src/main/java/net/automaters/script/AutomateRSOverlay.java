package net.automaters.script;

import lombok.extern.slf4j.Slf4j;
import net.automaters.util.file_managers.ImageManager;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.*;
import net.unethicalite.api.widgets.Widgets;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import static net.automaters.api.utils.Debug.displayMessage;
import static net.automaters.gui.GUI.started;
import static net.automaters.script.AutomateRS.*;

@Singleton
@Slf4j
public class AutomateRSOverlay extends OverlayPanel {

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
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					Widget chatbox = Widgets.get(WidgetInfo.CHATBOX);
					if (!started || scriptTimer == -1 || title == null || chatbox == null) {
						return;
					}
					g.setFont(font);
					int paintY = chatbox.getBounds().y;
					g.drawImage(title, 0, paintY - 80, null);
					g.setColor(Color.WHITE);
					g.drawString("Runtime: " + timeFormat(), 10, paintY - 20);
					g.drawString("Status: " + displayMessage, 10, paintY - 5);
				}
			});
		} catch (InterruptedException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
        return null;
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