package main;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger for saving generated maps to files for analysis
 */
public class MapLogger {
    private static final String LOG_DIR = "logs";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Save a generated map to a log file
     * 
     * @param map         The 2D array representing the map
     * @param seed        The seed used for generation
     * @param description Additional description of the generation
     */
    public static void logMap(int[][] map, long seed, String description) {
        try {
            // Create logs directory if it doesn't exist
            File logDir = new File(LOG_DIR);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            // Create filename with timestamp
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            String filename = String.format("map_%s_seed_%d.txt", timestamp, seed);
            File logFile = new File(logDir, filename);

            // Write map data to file
            try (PrintWriter writer = new PrintWriter(new FileWriter(logFile))) {
                writer.println("=== MAP GENERATION LOG ===");
                writer.println("Timestamp: " + LocalDateTime.now());
                writer.println("Seed: " + seed);
                writer.println("Description: " + description);
                writer.println("Map Dimensions: " + map.length + "x" + map[0].length);
                writer.println();

                // Write map legend
                writer.println("=== MAP LEGEND ===");
                writer.println("0 = Floor (Suelo)");
                writer.println("1 = Wall (Pared)");
                writer.println();

                // Write map array
                writer.println("=== MAP ARRAY ===");
                for (int y = 0; y < map[0].length; y++) {
                    StringBuilder row = new StringBuilder();
                    for (int x = 0; x < map.length; x++) {
                        row.append(map[x][y]);
                        if (x < map.length - 1) {
                            row.append(" ");
                        }
                    }
                    writer.println(row.toString());
                }

                // Write statistics
                writer.println();
                writer.println("=== MAP STATISTICS ===");
                int floorCount = 0;
                int wallCount = 0;

                for (int x = 0; x < map.length; x++) {
                    for (int y = 0; y < map[0].length; y++) {
                        if (map[x][y] == 0) {
                            floorCount++;
                        } else {
                            wallCount++;
                        }
                    }
                }

                int totalTiles = floorCount + wallCount;
                double floorPercentage = (double) floorCount / totalTiles * 100;
                double wallPercentage = (double) wallCount / totalTiles * 100;

                writer.println("Total Tiles: " + totalTiles);
                writer.println("Floor Tiles: " + floorCount + " (" + String.format("%.1f", floorPercentage) + "%)");
                writer.println("Wall Tiles: " + wallCount + " (" + String.format("%.1f", wallPercentage) + "%)");

                // Check connectivity
                boolean[][] visited = new boolean[map.length][map[0].length];
                int accessibleCount = 0;

                // Find first floor tile and count accessible tiles
                for (int x = 0; x < map.length; x++) {
                    for (int y = 0; y < map[0].length; y++) {
                        if (map[x][y] == 0) {
                            accessibleCount = floodFillCount(x, y, map, visited);
                            break;
                        }
                    }
                    if (accessibleCount > 0)
                        break;
                }

                double accessibilityPercentage = (double) accessibleCount / floorCount * 100;
                writer.println("Accessible Floor Tiles: " + accessibleCount + " ("
                        + String.format("%.1f", accessibilityPercentage) + "%)");

                if (accessibilityPercentage < 80) {
                    writer.println("WARNING: Low accessibility detected!");
                }

                writer.println();
                writer.println("=== END OF LOG ===");
            }

            System.out.println("Map logged to: " + logFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error logging map: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Count accessible tiles using flood fill
     */
    private static int floodFillCount(int x, int y, int[][] map, boolean[][] visited) {
        if (x < 0 || x >= map.length || y < 0 || y >= map[0].length ||
                map[x][y] != 0 || visited[x][y]) {
            return 0;
        }

        visited[x][y] = true;
        int count = 1;

        count += floodFillCount(x + 1, y, map, visited);
        count += floodFillCount(x - 1, y, map, visited);
        count += floodFillCount(x, y + 1, map, visited);
        count += floodFillCount(x, y - 1, map, visited);

        return count;
    }

    /**
     * Save a visual representation of the map
     */
    public static void logVisualMap(int[][] map, long seed) {
        try {
            File logDir = new File(LOG_DIR);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }

            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
            String filename = String.format("visual_map_%s_seed_%d.txt", timestamp, seed);
            File logFile = new File(logDir, filename);

            try (PrintWriter writer = new PrintWriter(new FileWriter(logFile))) {
                writer.println("=== VISUAL MAP REPRESENTATION ===");
                writer.println("Seed: " + seed);
                writer.println("Legend: . = Floor, # = Wall");
                writer.println();

                for (int y = 0; y < map[0].length; y++) {
                    StringBuilder row = new StringBuilder();
                    for (int x = 0; x < map.length; x++) {
                        if (map[x][y] == 0) {
                            row.append(".");
                        } else {
                            row.append("#");
                        }
                    }
                    writer.println(row.toString());
                }
            }

        } catch (IOException e) {
            System.err.println("Error logging visual map: " + e.getMessage());
        }
    }
}