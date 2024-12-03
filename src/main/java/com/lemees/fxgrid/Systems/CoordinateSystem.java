package com.lemees.fxgrid.Systems;

import com.lemees.fxgrid.Coordinate;
import com.lemees.fxgrid.GridController;

import java.util.*;
import java.util.function.Predicate;

public class CoordinateSystem {
    public static Coordinate[] getNighbors(Coordinate org) {
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

    public static Queue<Coordinate> getPath(Coordinate start, Coordinate end, Predicate<Coordinate> obstacleFilter){
        Map<Coordinate, Integer> steps = new HashMap<>();
        Map<Coordinate, Double> endDistance = new HashMap<>();
        Map<Coordinate, Coordinate> cameFrom = new HashMap<>();

        PriorityQueue<Coordinate> openSet =
                new PriorityQueue<>(Comparator.comparingDouble(c->steps.get(c)+endDistance.get(c)));

        steps.put(start,0);
        endDistance.put(start,start.getDistance(end));
        openSet.add(start);

        while(!openSet.isEmpty()){
            Coordinate current = openSet.poll();

            if(current.equals(end)){
                List<Coordinate> path = new LinkedList<>();

                Coordinate cur = end;
                path.add(current);
                while(cameFrom.containsKey(cur)){
                    cur = cameFrom.get(cur);
                    path.add(cur);
                }

                Collections.reverse(path);

                return (Queue<Coordinate>) path;
            }

            Coordinate[] neighbors = CoordinateSystem.getNighbors(current);
            Coordinate[] postProcess = neighbors;
            if(obstacleFilter!=null){
                postProcess = Arrays.stream(neighbors)
                        .filter(obstacleFilter)
                        .toArray(Coordinate[]::new);
            }

            for(Coordinate neighbor:postProcess){
                int neighborValue = steps.get(current) + 1;

                if(!steps.containsKey(neighbor) || neighborValue < steps.get(neighbor)){
                    steps.put(neighbor,neighborValue);
                    endDistance.put(neighbor,neighbor.getDistance(end));
                    cameFrom.put(neighbor,current);
                    openSet.add(neighbor);
                }
            }
        }
        return null;
    }

    public static Coordinate[] getCoordinates(){
        List<Coordinate> coords = new ArrayList<>((int) Math.pow(GridController.mapSize,2));
        for(int i = 0; i< GridController.mapSize; i++){
            for(int j = 0; j< GridController.mapSize; j++){
                coords.add(new Coordinate(i,j));
            }
        }
        return coords.toArray(Coordinate[]::new);
    }

    private static boolean inBounds(Coordinate coord){
        int size = GridController.mapSize;

        return coord.getX() < size && coord.getY() < size
                &&
                coord.getX() > -1 && coord.getY() > -1;
    }
}
