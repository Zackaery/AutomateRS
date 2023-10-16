package net.automaters.overlay;

import com.openosrs.client.ui.overlay.components.table.TableAlignment;
import com.openosrs.client.ui.overlay.components.table.TableComponent;
import lombok.extern.slf4j.Slf4j;
import net.automaters.util.file_managers.ImageManager;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayMenuEntry;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;

import static net.automaters.api.utils.Debug.displayMessage;
import static net.automaters.overlay.OverlayUtil.MATERIAL_DARK;
import static net.automaters.script.Variables.guiStarted;
import static net.automaters.script.Variables.scriptStarted;
import static net.automaters.script.Variables.scriptTimer;
import static net.automaters.script.Variables.elapsedTime;
import static net.automaters.util.file_managers.ImageResizer.resizeImage;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

@Singleton
@Slf4j
public class AutomateRSOverlay extends OverlayPanel {

	private static boolean hideOverlay;

	final Image title = ImageManager.getInstance().loadImage("script\\title.png");
	final Image smallTitle = resizeImage(title, 150, 43);
	@Inject
	private AutomateRSOverlay() {
		setPosition(OverlayPosition.BOTTOM_LEFT);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		getMenuEntries().add(new OverlayMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "AutomateRS overlay"));
	}

	@Override
	public Dimension render(Graphics2D g) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					if (!guiStarted || scriptTimer == -1 || title == null) {
						return;
					}

					TableComponent tableStatusComponent = new TableComponent();
					tableStatusComponent.setColumnAlignments(TableAlignment.LEFT, TableAlignment.RIGHT);
					tableStatusComponent.addRow("Runtime:", timeFormat());
					tableStatusComponent.addRow("Status:", displayMessage);
					if (!tableStatusComponent.isEmpty()) {
						panelComponent.setBackgroundColor(MATERIAL_DARK);
						panelComponent.setPreferredSize(new Dimension(270, 200));
						panelComponent.setBorder(new Rectangle(5, 5, 5, 5));
						panelComponent.getChildren().add(tableStatusComponent);
					}
					int paintY = panelComponent.getBounds().y;
					int paintX = panelComponent.getBounds().width;
					g.drawImage(smallTitle, (paintX / 2) - (smallTitle.getWidth(null) / 2), paintY - smallTitle.getHeight(null)+10, null);
					setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
				}
			});
		} catch (InterruptedException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}

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