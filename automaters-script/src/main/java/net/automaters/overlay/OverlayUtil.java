package net.automaters.overlay;

import net.automaters.activities.skills.firemaking.DynamicFiremaking;
import net.runelite.api.Client;
import net.runelite.api.Tile;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import net.unethicalite.api.scene.Tiles;

import javax.inject.Inject;
import java.awt.*;
import java.util.List;

import static net.automaters.activities.skills.firemaking.DynamicFiremaking.firemaking;
import static net.automaters.api.entities.LocalPlayer.localPlayer;
import static net.automaters.tasks.Task.objectToRender;
public class OverlayUtil extends OverlayPanel {

    private final Client client;
    private final ModelOutlineRenderer modelOutlineRenderer;

    public static final Color ORANGE = new Color(255, 109, 0);
    public static int HEX_OPACITY_ORANGE = 0x96FF5700;
    public static int HEX_ORANGE_COLOR = 0xD2FF5700;
    public static int HEX_MATERIAL_DARK = 0xD2121212;
    public static Color OPACITY_ORANGE = new Color(HEX_OPACITY_ORANGE, true);
    public static Color MATERIAL_DARK = new Color(HEX_MATERIAL_DARK, true);
    public static Color ORANGE_COLOR = new Color(HEX_ORANGE_COLOR);

    @Inject
    private OverlayUtil(Client client, ModelOutlineRenderer modelOutlineRenderer) {
        this.client = client;
        this.modelOutlineRenderer = modelOutlineRenderer;
        setLayer(OverlayLayer.UNDER_WIDGETS);
        setPosition(OverlayPosition.DYNAMIC);
    }

    @Override
    public Dimension render(Graphics2D g) {

        Tile localTile = Tiles.getAt(localPlayer.getWorldLocation());
        localTile.getWorldLocation().outline(client, g, ORANGE_COLOR);

        if (objectToRender != null) {
            modelOutlineRenderer.drawOutline(objectToRender, 12, OPACITY_ORANGE, 4);
        }

        if (firemaking) {
            objectToRender = null;
			List<Tile> fireArea = DynamicFiremaking.getFireArea();
			if (fireArea == null || fireArea.isEmpty()) {
				return null;
			}

            for (Tile t : DynamicFiremaking.getFireArea()) {
                Tile tile = Tiles.getAt(t.getWorldLocation());
                if (DynamicFiremaking.isEmptyTile(tile)) {
                    tile.getWorldLocation().outline(client, g, OPACITY_ORANGE);
                }
            }
        }

        return null;
    }
}
