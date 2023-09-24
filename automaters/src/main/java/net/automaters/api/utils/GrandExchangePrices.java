package net.automaters.api.utils;

import com.google.gson.*;
import net.runelite.api.Item;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static net.automaters.util.file_managers.FileManager.*;

public class GrandExchangePrices {


    private static AllPricesData data;
    public static void updatePrices() {
        try {
            String urlToDownload = "https://prices.runescape.wiki/api/v1/osrs/latest";
            String directoryPath = PATH_AUTOMATE_RS + "Grand Exchange Prices";
            Path directory = Paths.get(directoryPath);
            Files.createDirectories(directory);
            String filePath = directory.resolve("prices.json").toString();

            URL url = new URL(urlToDownload);
            try (InputStream in = url.openStream();
                 ReadableByteChannel rbc = Channels.newChannel(in);
                 FileOutputStream fos = new FileOutputStream(filePath)) {

                ByteBuffer buffer = ByteBuffer.allocate(1024);

                while (rbc.read(buffer) != -1) {
                    buffer.flip();
                    fos.getChannel().write(buffer);
                    buffer.clear();
                }
                System.out.println("File downloaded successfully to: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            formatJsonFile(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ItemPrice getPrice(int ItemID) {
        String filePath = PATH_GE_PRICES;
        String jsonData = readJsonFromFile(filePath);

        if (jsonData != null) {
            data = new Gson().fromJson(jsonData, AllPricesData.class);
            return data.data.get(ItemID);
        } else {
            return null;
        }
    }

    private static class AllPricesData {
        private final Map<Integer, ItemPrice> data;
        private AllPricesData(Map<Integer, ItemPrice> data) {
            this.data = data;
        }
    }

    public static class ItemPrice {
        public final int high;
        public final int low;
        public final long highTime;
        public final int lowTime;

        private ItemPrice(int high, int low, long highTime, int lowTime) {
            this.high = high;
            this.low = low;
            this.highTime = highTime;
            this.lowTime = lowTime;
        }

        public String toString() {
            return low + "-" + high;
        }
    }
}
