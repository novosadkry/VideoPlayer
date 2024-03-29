package cz.novosadkry.VideoPlayer.World;

import cz.novosadkry.VideoPlayer.Main;
import cz.novosadkry.VideoPlayer.Video.VideoMode;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
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

        int w = width / 128;
        int h = height / 128;

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Location pos = start.clone().add(x, y, 0);
                pos.getBlock().setType(Material.AIR, false);
            }
        }
    }

    @Override
    public void build() {
        int w = width / 128;
        int h = height / 128;

        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                Location pos = start.clone().add(x, y, 0);
                pos.getBlock().setType(Material.STONE, false);

                pos = pos.add(0, 0, -1);
                ItemFrame mapFrame = pos.getWorld().spawn(pos, ItemFrame.class);

                ItemStack map = new ItemStack(Material.FILLED_MAP);
                MapMeta mapMeta = (MapMeta) map.getItemMeta();
                MapView mapView = Bukkit.createMap(pos.getWorld());

                mapView.setTrackingPosition(false);
                for (MapRenderer renderer : mapView.getRenderers())
                    mapView.removeRenderer(renderer);

                Renderer mapRenderer = new Renderer();
                mapRenderer.setOffsetX((w - 1) * 128 - x * 128);
                mapRenderer.setOffsetY((h - 1) * 128 - y * 128);

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

            Renderer renderer = (Renderer) mapMeta
                    .getMapView()
                    .getRenderers()
                    .get(0);

            DataBuffer data = image.getRaster().getDataBuffer();
            byte[] pixels = ((DataBufferByte) data).getData();

            int ox = renderer.getOffsetX();
            int oy = renderer.getOffsetY();

            byte[] colorIds = new byte[128 * 128];

            for (int y = 0; y < 128; y++) {
                for (int x = 0; x < 128; x++) {
                    int i = x + y * 128;
                    int p = (x + ox) + (y + oy) * width;

                    int r = pixels[p * 3 + 2];
                    int g = pixels[p * 3 + 1];
                    int b = pixels[p * 3    ];

                    int v = ((r & 0xFF) | ((g & 0xFF) << 8) | ((b & 0xFF) << 16));

                    colorIds[i] = Main.cachedColorIds[v];
                }
            }

            var packet = new ClientboundMapItemDataPacket(
                    mapMeta.getMapId(),
                    (byte)0, false,
                    new ArrayList<>(),
                    new MapItemSavedData.MapPatch(
                            0, 0,
                            128, 128,
                            colorIds
                    )
            );

            for (Player player : Bukkit.getOnlinePlayers())
                ((CraftPlayer) player).getHandle().connection.send(packet);
        }
    }
}
