package net.automaters.api.game;

import net.automaters.api.walking.Position;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.rs.api.RSTile;

import java.util.List;

import static net.runelite.api.Constants.CHUNK_SIZE;

public class Tile implements RSTile {
    final Tile tile;

    Tile(Tile tile) {
        this.tile = tile;
    }

    //    @Override
//    public static Game game() {
////        return Game;
//    }

    public Client client() {
        return Game.client;
    }

    public Position position() {
        return new Position(tile.getSceneLocation().getX(), tile.getSceneLocation().getY(), tile.getPlane());
    }

    public Position templatePosition() {
        if (client().isInInstancedRegion()) {
            LocalPoint localPoint = client().getLocalPlayer().getLocalLocation();
            int[][][] instanceTemplateChunks = client().getInstanceTemplateChunks();
            int z = client().getPlane();
            int chunkData = instanceTemplateChunks[z][localPoint.getSceneX() / CHUNK_SIZE][localPoint.getSceneY() / CHUNK_SIZE];

            int rotation = chunkData >> 1 & 0x3; //TODO
            int chunkX = (chunkData >> 14 & 0x3FF) * CHUNK_SIZE + (localPoint.getSceneX() % CHUNK_SIZE);
            int chunkY = (chunkData >> 3 & 0x7FF) * CHUNK_SIZE + (localPoint.getSceneY() % CHUNK_SIZE);
            int chunkZ = (chunkData >> 24 & 0x3);

            return new Position(chunkX, chunkY, chunkZ);
        }
        return null;
    }

    public Point getSceneLocation()
    {
        return new Point(getX(), getY());
    }

    @Override
    public List<net.runelite.api.Tile> pathTo(net.runelite.api.Tile other) {
        return null;
    }

    @Override
    public List<TileItem> getGroundItems() {
        return null;
    }

    @Override
    public void walkHere() {

    }

    @Override
    public int getWorldX() {
        return 0;
    }

    @Override
    public int getWorldY() {
        return 0;
    }

    @Override
    public WorldPoint getWorldLocation() {
        return null;
    }

    @Override
    public LocalPoint getLocalLocation() {
        return null;
    }

    @Override
    public GameObject[] getGameObjects() {
        return new GameObject[0];
    }

    @Override
    public ItemLayer getItemLayer() {
        return null;
    }

    @Override
    public DecorativeObject getDecorativeObject() {
        return null;
    }

    @Override
    public void setDecorativeObject(DecorativeObject object) {

    }

    @Override
    public GroundObject getGroundObject() {
        return null;
    }

    @Override
    public void setGroundObject(GroundObject object) {

    }

    @Override
    public WallObject getWallObject() {
        return null;
    }

    @Override
    public void setWallObject(WallObject object) {

    }

    @Override
    public SceneTilePaint getSceneTilePaint() {
        return null;
    }

    @Override
    public void setSceneTilePaint(SceneTilePaint paint) {

    }

    @Override
    public SceneTileModel getSceneTileModel() {
        return null;
    }

    @Override
    public int getX() {
        return 0;
    }

    @Override
    public int getY() {
        return 0;
    }

    @Override
    public int getPlane() {
        return 0;
    }

    @Override
    public int getRenderLevel() {
        return 0;
    }

    @Override
    public int getPhysicalLevel() {
        return 0;
    }

    @Override
    public int getFlags() {
        return 0;
    }

    @Override
    public RSTile getBridge() {
        return null;
    }

    @Override
    public boolean isDraw() {
        return false;
    }

    @Override
    public void setDraw(boolean draw) {

    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void setVisible(boolean visible) {

    }

    @Override
    public void setDrawEntities(boolean drawEntities) {

    }

    @Override
    public void setWallCullDirection(int wallCullDirection) {

    }
}
