package com.game.main;

import java.util.*;

/**
 * BSP (Binary Space Partitioning) Dungeon Generator
 * 
 * This generator creates dungeons by:
 * 1. Recursively splitting the space into smaller regions
 * 2. Creating rooms in the leaf nodes
 * 3. Connecting sibling rooms with corridors
 * 
 * Based on the classic BSP algorithm for dungeon generation
 */
public class BSPDungeonGenerator {
    private final Random random;
    private final int width;
    private final int height;
    private int[][] map;
    private long currentSeed;
    private BSPNode rootNode;

    // Generation parameters
    private static final int MIN_ROOM_SIZE = 6;
    private static final int MAX_ROOM_SIZE = 12;
    private static final int MIN_REGION_SIZE = 10; // Minimum size before we stop splitting
    private static final int ROOM_PADDING = 2; // Space around rooms
    private static final double SPLIT_RATIO_MIN = 0.3; // Minimum split ratio (30%)
    private static final double SPLIT_RATIO_MAX = 0.7; // Maximum split ratio (70%)
    private static final int MAX_RECURSION_DEPTH = 6;

    public BSPDungeonGenerator(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        this.currentSeed = seed;
        this.random = new Random(seed);
        this.map = new int[width][height];
    }

    public BSPDungeonGenerator(int width, int height) {
        this(width, height, System.currentTimeMillis());
    }

    /**
     * Generate a complete BSP dungeon
     */
    public int[][] generateMap() {
        System.out.println("Generating BSP dungeon with seed: " + currentSeed);

        // Step 1: Initialize map with walls
        initializeMap();

        // Step 2: Create BSP tree by recursive splitting
        rootNode = new BSPNode(0, 0, width, height);
        splitNode(rootNode, 0);

        // Step 3: Create rooms in leaf nodes
        createRooms(rootNode);

        // Step 4: Connect rooms with corridors
        connectRooms(rootNode);

        // Step 5: Log the generated map
        logGeneratedMap();

        System.out.println("BSP dungeon generated successfully");
        return map;
    }

    /**
     * Initialize map with walls
     */
    private void initializeMap() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = 1; // Wall
            }
        }
    }

    /**
     * Recursively split a node into two children
     */
    private void splitNode(BSPNode node, int depth) {
        // Stop splitting if region is too small or we've reached max depth
        if (node.width < MIN_REGION_SIZE * 2 ||
                node.height < MIN_REGION_SIZE * 2 ||
                depth >= MAX_RECURSION_DEPTH) {
            return;
        }

        // Decide split direction
        boolean splitHorizontal;
        if (node.width > node.height * 1.25) {
            splitHorizontal = false; // Split vertically (width is much larger)
        } else if (node.height > node.width * 1.25) {
            splitHorizontal = true; // Split horizontally (height is much larger)
        } else {
            splitHorizontal = random.nextBoolean(); // Random choice
        }

        // Calculate split position
        int splitPosition;
        if (splitHorizontal) {
            // Horizontal split
            int minSplit = node.y + MIN_REGION_SIZE;
            int maxSplit = node.y + node.height - MIN_REGION_SIZE;
            if (maxSplit <= minSplit)
                return; // Can't split

            splitPosition = minSplit + random.nextInt(maxSplit - minSplit);

            // Create child nodes
            node.leftChild = new BSPNode(node.x, node.y, node.width, splitPosition - node.y);
            node.rightChild = new BSPNode(node.x, splitPosition, node.width,
                    node.y + node.height - splitPosition);
        } else {
            // Vertical split
            int minSplit = node.x + MIN_REGION_SIZE;
            int maxSplit = node.x + node.width - MIN_REGION_SIZE;
            if (maxSplit <= minSplit)
                return; // Can't split

            splitPosition = minSplit + random.nextInt(maxSplit - minSplit);

            // Create child nodes
            node.leftChild = new BSPNode(node.x, node.y, splitPosition - node.x, node.height);
            node.rightChild = new BSPNode(splitPosition, node.y,
                    node.x + node.width - splitPosition, node.height);
        }

        node.splitHorizontal = splitHorizontal;

        // Recursively split children
        splitNode(node.leftChild, depth + 1);
        splitNode(node.rightChild, depth + 1);
    }

    /**
     * Create rooms in all leaf nodes
     */
    private void createRooms(BSPNode node) {
        if (node.isLeaf()) {
            // This is a leaf node, create a room
            createRoomInNode(node);
        } else {
            // Recursively create rooms in children
            if (node.leftChild != null)
                createRooms(node.leftChild);
            if (node.rightChild != null)
                createRooms(node.rightChild);
        }
    }

    /**
     * Create a room within a BSP node
     */
    private void createRoomInNode(BSPNode node) {
        // Calculate room size constraints
        int maxRoomWidth = Math.min(MAX_ROOM_SIZE, node.width - ROOM_PADDING * 2);
        int maxRoomHeight = Math.min(MAX_ROOM_SIZE, node.height - ROOM_PADDING * 2);
        int minRoomWidth = Math.min(MIN_ROOM_SIZE, maxRoomWidth);
        int minRoomHeight = Math.min(MIN_ROOM_SIZE, maxRoomHeight);

        if (maxRoomWidth < minRoomWidth || maxRoomHeight < minRoomHeight) {
            // Node is too small for a room
            return;
        }

        // Generate random room size
        int roomWidth = minRoomWidth + random.nextInt(maxRoomWidth - minRoomWidth + 1);
        int roomHeight = minRoomHeight + random.nextInt(maxRoomHeight - minRoomHeight + 1);

        // Calculate room position (centered in the node with some randomness)
        int maxRoomX = node.x + node.width - roomWidth - ROOM_PADDING;
        int maxRoomY = node.y + node.height - roomHeight - ROOM_PADDING;
        int minRoomX = node.x + ROOM_PADDING;
        int minRoomY = node.y + ROOM_PADDING;

        if (maxRoomX < minRoomX || maxRoomY < minRoomY) {
            return; // Can't fit room
        }

        int roomX = minRoomX + random.nextInt(maxRoomX - minRoomX + 1);
        int roomY = minRoomY + random.nextInt(maxRoomY - minRoomY + 1);

        // Create the room
        Room room = new Room(roomX, roomY, roomWidth, roomHeight);
        node.room = room;

        // Place room on map
        for (int x = roomX; x < roomX + roomWidth; x++) {
            for (int y = roomY; y < roomY + roomHeight; y++) {
                if (x >= 0 && x < width && y >= 0 && y < height) {
                    map[x][y] = 0; // Floor
                }
            }
        }

        System.out.println("Created room: " + room);
    }

    /**
     * Connect rooms with corridors using the BSP tree structure
     */
    private void connectRooms(BSPNode node) {
        if (node.isLeaf()) {
            // Leaf nodes don't need internal connections
            return;
        }

        // Recursively connect children first
        if (node.leftChild != null)
            connectRooms(node.leftChild);
        if (node.rightChild != null)
            connectRooms(node.rightChild);

        // Connect left and right children
        if (node.leftChild != null && node.rightChild != null) {
            Point leftPoint = getConnectionPoint(node.leftChild);
            Point rightPoint = getConnectionPoint(node.rightChild);

            if (leftPoint != null && rightPoint != null) {
                createCorridor(leftPoint, rightPoint);
            }
        }
    }

    /**
     * Get a connection point from a node (center of room or corridor)
     */
    private Point getConnectionPoint(BSPNode node) {
        if (node.room != null) {
            // Return center of room
            return new Point(
                    node.room.x + node.room.width / 2,
                    node.room.y + node.room.height / 2);
        } else if (!node.isLeaf()) {
            // For non-leaf nodes, try to get connection point from children
            Point leftPoint = node.leftChild != null ? getConnectionPoint(node.leftChild) : null;
            Point rightPoint = node.rightChild != null ? getConnectionPoint(node.rightChild) : null;

            if (leftPoint != null)
                return leftPoint;
            if (rightPoint != null)
                return rightPoint;
        }

        // Fallback: return center of node
        return new Point(node.x + node.width / 2, node.y + node.height / 2);
    }

    /**
     * Create a corridor between two points using L-shaped or Z-shaped paths
     */
    private void createCorridor(Point start, Point end) {
        // Create an L-shaped corridor
        if (random.nextBoolean()) {
            // Horizontal first, then vertical
            createHorizontalCorridor(start.x, start.y, end.x);
            createVerticalCorridor(end.x, start.y, end.y);
        } else {
            // Vertical first, then horizontal
            createVerticalCorridor(start.x, start.y, end.y);
            createHorizontalCorridor(start.x, end.y, end.x);
        }
    }

    /**
     * Create a horizontal corridor
     */
    private void createHorizontalCorridor(int startX, int y, int endX) {
        int minX = Math.min(startX, endX);
        int maxX = Math.max(startX, endX);

        for (int x = minX; x <= maxX; x++) {
            if (x >= 0 && x < width && y >= 0 && y < height) {
                map[x][y] = 0; // Floor
            }
        }
    }

    /**
     * Create a vertical corridor
     */
    private void createVerticalCorridor(int x, int startY, int endY) {
        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        for (int y = minY; y <= maxY; y++) {
            if (x >= 0 && x < width && y >= 0 && y < height) {
                map[x][y] = 0; // Floor
            }
        }
    }

    /**
     * Log the generated map for analysis
     */
    private void logGeneratedMap() {
        String description = String.format(
                "BSP dungeon, %dx%d dimensions, seed: %d",
                width, height, currentSeed);

        // Count rooms
        int roomCount = countRooms(rootNode);
        description += String.format(", %d rooms", roomCount);

        // Log the raw map data
        MapLogger.logMap(map, currentSeed, description);

        // Log a visual representation
        MapLogger.logVisualMap(map, currentSeed);
    }

    /**
     * Count total number of rooms in the BSP tree
     */
    private int countRooms(BSPNode node) {
        if (node == null)
            return 0;
        if (node.room != null)
            return 1;
        return countRooms(node.leftChild) + countRooms(node.rightChild);
    }

    /**
     * BSP Tree Node
     */
    private static class BSPNode {
        int x, y, width, height;
        BSPNode leftChild, rightChild;
        Room room;
        boolean splitHorizontal;

        BSPNode(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        boolean isLeaf() {
            return leftChild == null && rightChild == null;
        }

        @Override
        public String toString() {
            return String.format("BSPNode(%d,%d,%dx%d)", x, y, width, height);
        }
    }

    /**
     * Room class
     */
    public static class Room {
        int x, y, width, height;

        Room(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return String.format("Room(%d,%d,%dx%d)", x, y, width, height);
        }
    }

    /**
     * Simple Point class
     */
    private static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Getters
    public long getCurrentSeed() {
        return currentSeed;
    }

    public BSPNode getRootNode() {
        return rootNode;
    }
}