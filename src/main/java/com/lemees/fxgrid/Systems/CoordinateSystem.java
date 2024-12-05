package com.lemees.fxgrid.Systems;

import com.lemees.fxgrid.Coordinate;
import com.lemees.fxgrid.GridController;

import java.util.*;
import java.util.function.Predicate;

public class CoordinateSystem {

    /**
     * Gets all neighboring tiles of a specified origin.
     * Will not return out of bounds coordinates
     *
     * @param org Origin tile
     * @return Array of neighbors
     */
    public static Coordinate[] getNeighbors(Coordinate org) {
        Coordinate[] coords = {
                new Coordinate(org.getX(), org.getY() + 1),
                new Coordinate(org.getX(), org.getY() - 1),
                new Coordinate(org.getX() + 1, org.getY()),
                new Coordinate(org.getX() - 1, org.getY())
        };

        return Arrays.stream(coords)
                .filter(CoordinateSystem::inBounds)
                .toArray(Coordinate[]::new);
    }

    /**
     * Generates a path between 2 points using the A* algorithm
     * NOTE: If the filter checks for tiles without enemies, and the
     * ending tile is an enemy the path will be incomplete.
     *
     * @param start          The starting tile
     * @param end            The ending tile
     * @param obstacleFilter Filter for tiles that should not be mapped
     * @return The finalized path
     */
    public static Queue<Coordinate> getPath(Coordinate start, Coordinate end, Predicate<Coordinate> obstacleFilter) {
        Map<Coordinate, Integer> steps = new HashMap<>();
        Map<Coordinate, Double> endDistance = new HashMap<>();
        Map<Coordinate, Coordinate> cameFrom = new HashMap<>();

        PriorityQueue<Coordinate> openSet =
                new PriorityQueue<>(Comparator.comparingDouble(c -> steps.get(c) + endDistance.get(c)));

        steps.put(start, 0);
        endDistance.put(start, start.getDistance(end));
        openSet.add(start);

        while (!openSet.isEmpty()) {
            Coordinate current = openSet.poll();

            if (current.equals(end)) {
                List<Coordinate> path = new LinkedList<>();

                Coordinate cur = end;
                path.add(current);
                while (cameFrom.containsKey(cur)) {
                    cur = cameFrom.get(cur);
                    path.add(cur);
                }

                Collections.reverse(path);

                return (Queue<Coordinate>) path;
            }

            Coordinate[] neighbors = CoordinateSystem.getNeighbors(current);
            Coordinate[] postProcess = neighbors;
            if (obstacleFilter != null) {
                postProcess = Arrays.stream(neighbors)
                        .filter(obstacleFilter)
                        .toArray(Coordinate[]::new);
            }

            for (Coordinate neighbor : postProcess) {
                int neighborValue = steps.get(current) + 1;

                if (!steps.containsKey(neighbor) || neighborValue < steps.get(neighbor)) {
                    steps.put(neighbor, neighborValue);
                    endDistance.put(neighbor, neighbor.getDistance(end));
                    cameFrom.put(neighbor, current);
                    openSet.add(neighbor);
                }
            }
        }
        return null;
    }

    /**
     * Gets all coordinates of the grid
     *
     * @return Array of coordinates
     */
    public static Coordinate[] getCoordinates() {
        List<Coordinate> coords = new ArrayList<>((int) Math.pow(GridController.mapSize, 2));
        for (int i = 0; i < GridController.mapSize; i++) {
            for (int j = 0; j < GridController.mapSize; j++) {
                coords.add(new Coordinate(i, j));
            }
        }
        return coords.toArray(Coordinate[]::new);
    }

    /**
     * Checks if a specified coordinate is within the map bounds
     *
     * @param coord Coordinate to check
     * @return If it is in bounds
     */
    private static boolean inBounds(Coordinate coord) {
        int size = GridController.mapSize;

        return coord.getX() < size && coord.getY() < size
                &&
                coord.getX() > -1 && coord.getY() > -1;
    }
}
