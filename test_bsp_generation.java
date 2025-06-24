import main.BSPDungeonGenerator;
import main.MapLogger;

/**
 * Test program for BSP dungeon generation
 */
public class test_bsp_generation {
    public static void main(String[] args) {
        System.out.println("Testing BSP Dungeon Generator");
        System.out.println("=============================");

        // Test different map sizes
        int[][] testSizes = {
                { 32, 24 },
                { 48, 36 },
                { 64, 48 },
                { 80, 60 }
        };

        for (int[] size : testSizes) {
            System.out.println("\nTesting " + size[0] + "x" + size[1] + " map:");
            testMapGeneration(size[0], size[1]);
        }

        System.out.println("\nAll BSP tests completed!");
    }

    private static void testMapGeneration(int width, int height) {
        long seed = System.currentTimeMillis();
        BSPDungeonGenerator generator = new BSPDungeonGenerator(width, height, seed);

        System.out.println("Seed: " + seed);

        long startTime = System.currentTimeMillis();
        int[][] map = generator.generateMap();
        long endTime = System.currentTimeMillis();

        System.out.println("Generation time: " + (endTime - startTime) + "ms");

        // Analyze the map
        analyzeMap(map);

        // Log the map
        String description = String.format("BSP test map %dx%d", width, height);
        MapLogger.logMap(map, seed, description);
        MapLogger.logVisualMap(map, seed);

        // Small delay between tests
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void analyzeMap(int[][] map) {
        int width = map.length;
        int height = map[0].length;
        int wallCount = 0;
        int floorCount = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x][y] == 1) {
                    wallCount++;
                } else {
                    floorCount++;
                }
            }
        }

        double wallPercentage = (double) wallCount / (width * height) * 100;
        double floorPercentage = (double) floorCount / (width * height) * 100;

        System.out.println("Map analysis:");
        System.out.println("  Walls: " + wallCount + " (" + String.format("%.1f", wallPercentage) + "%)");
        System.out.println("  Floors: " + floorCount + " (" + String.format("%.1f", floorPercentage) + "%)");

        // Check connectivity
        boolean[][] visited = new boolean[width][height];
        int accessibleCount = floodFill(map, visited);
        double accessibility = floorCount > 0 ? (double) accessibleCount / floorCount * 100 : 0;

        System.out.println("  Accessible floors: " + accessibleCount + "/" + floorCount +
                " (" + String.format("%.1f", accessibility) + "%)");

        // Check for isolated areas
        int isolatedAreas = countIsolatedAreas(map);
        System.out.println("  Connected regions: " + isolatedAreas);
    }

    private static int floodFill(int[][] map, boolean[][] visited) {
        int width = map.length;
        int height = map[0].length;

        // Find first floor tile
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x][y] == 0) {
                    return floodFillRecursive(map, visited, x, y);
                }
            }
        }
        return 0;
    }

    private static int floodFillRecursive(int[][] map, boolean[][] visited, int x, int y) {
        int width = map.length;
        int height = map[0].length;

        if (x < 0 || x >= width || y < 0 || y >= height ||
                map[x][y] != 0 || visited[x][y]) {
            return 0;
        }

        visited[x][y] = true;
        int count = 1;

        count += floodFillRecursive(map, visited, x + 1, y);
        count += floodFillRecursive(map, visited, x - 1, y);
        count += floodFillRecursive(map, visited, x, y + 1);
        count += floodFillRecursive(map, visited, x, y - 1);

        return count;
    }

    private static int countIsolatedAreas(int[][] map) {
        int width = map.length;
        int height = map[0].length;
        boolean[][] visited = new boolean[width][height];
        int areaCount = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x][y] == 0 && !visited[x][y]) {
                    floodFillRecursive(map, visited, x, y);
                    areaCount++;
                }
            }
        }

        return areaCount;
    }
}