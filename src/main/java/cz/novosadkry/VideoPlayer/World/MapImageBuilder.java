package cz.novosadkry.VideoPlayer.World;

import cz.novosadkry.VideoPlayer.Video.VideoMode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MapImageBuilder extends ImageBuilder {

    static class Renderer extends MapRenderer {
        private BufferedImage image;
        private int offsetX;
        private int offsetY;

        public BufferedImage getImage() {
            return image;
        }

        public void setImage(BufferedImage image) {
            this.image = image;
        }

        public int getOffsetX() {
            return offsetX;
        }

        public void setOffsetX(int offsetX) {
            this.offsetX = offsetX;
        }

        public int getOffsetY() {
            return offsetY;
        }

        public void setOffsetY(int offsetY) {
            this.offsetY = offsetY;
        }

        @Override
        public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
            if (image == null)
                return;

            mapCanvas.drawImage(offsetX, offsetY, image);
        }
    }

    private ArrayList<ItemFrame> mapFrames = new ArrayList<>();

    public MapImageBuilder(Location start, int width, int height) {
        super(start, width, height);
    }

    public MapImageBuilder(Location start, int width, int height, VideoMode mode) {
        super(start, width, height, mode);
    }

    @Override
    public void destroy() {
        for (ItemFrame mapFrame : mapFrames) {
            mapFrame.remove();
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Location pos = start.clone().add(x, y, 0);
                pos.getBlock().setType(Material.AIR, false);
            }
        }
    }

    @Override
    public void build() {
        for (int x = 0; x < width / 128; x++) {
            for (int y = 0; y < height / 128; y++) {
                Location pos = start.clone().add(x, y, 0);
                pos.getBlock().setType(Material.STONE, false);

                pos = pos.add(x, y, -1);
                ItemFrame mapFrame = pos.getWorld().spawn(pos, ItemFrame.class);

                ItemStack map = new ItemStack(Material.FILLED_MAP);
                MapMeta mapMeta = (MapMeta) map.getItemMeta();
                MapView mapView = Bukkit.createMap(pos.getWorld());

                mapView.setTrackingPosition(false);
                for (MapRenderer renderer : mapView.getRenderers())
                    mapView.removeRenderer(renderer);

                Renderer mapRenderer = new Renderer();
                mapRenderer.setOffsetX(x * 128);
                mapRenderer.setOffsetY(y * 128);

                mapView.addRenderer(mapRenderer);
                mapMeta.setMapView(mapView);

                map.setItemMeta(mapMeta);
                mapFrame.setItem(map);

                mapFrames.add(mapFrame);
            }
        }
    }

    @Override
    public void build(BufferedImage image) {
        for (ItemFrame mapFrame : mapFrames) {
            ItemStack map = mapFrame.getItem();
            MapMeta mapMeta = (MapMeta) map.getItemMeta();

            Renderer renderer = (Renderer) mapMeta.getMapView().getRenderers().get(0);
            renderer.setImage(image);
        }
    }
}
