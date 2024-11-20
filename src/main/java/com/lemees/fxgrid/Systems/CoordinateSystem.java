package com.lemees.fxgrid.Systems;

import com.lemees.fxgrid.Coordinate;
import com.lemees.fxgrid.HelloController;

import java.util.*;
import java.util.stream.Collectors;

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

    public static Stack<Coordinate> getPath(Coordinate start, Coordinate end){
        Map<Coordinate,Double> gValues = new HashMap<>(); //Distance from the current node to the start
        Map<Coordinate,Double> hValues = new HashMap<>(); //Distance from the current node to the end
        Map<Coordinate,Coordinate> cameFrom = new HashMap<>(); //Previous nodes

        PriorityQueue<Coordinate> openSet =
                new PriorityQueue<>(Comparator.comparingDouble(e->gValues.get(e) + hValues.get(e)));
        //Sets a priority queue to sort by g and h value, essensially paths closer to the start and end get higher values than further paths

        gValues.put(start,0.0);
        hValues.put(start,start.getDistance(end));
        //Initalizes the g and h values

        openSet.add(start);

        while(!openSet.isEmpty()){
            Coordinate current = openSet.poll();

            if(current.equals(end)){
                Coordinate cur = end;

                Stack<Coordinate> path = new Stack<>();

                while(cameFrom.containsKey(cur)){
                    path.push(cameFrom.get(cur));
                    cur = cameFrom.get(cur);
                }

                path.add(cur);
                return path; //We have hit the end
            }

            Coordinate[] neighbors = getNighbors(current);
            for(Coordinate neighbor:neighbors){
                //Gets the neighbors gValue
                double neighborValue = gValues.get(current)+current.getDistance(neighbor);

                //If the neighbor has not yet been visited, or a cheaper path to the neighbor has been found then move to it
                if(!gValues.containsKey(neighbor) || neighborValue < gValues.get(neighbor)){
                    cameFrom.put(neighbor,current);
                    gValues.put(neighbor,neighborValue);
                    hValues.put(neighbor,neighbor.getDistance(end));

                    //Every neighbor which we have not yet visited or have a cheaper route are stored
                    openSet.add(neighbor);
                }
            }
        }
        return null;
    }

    public static Coordinate[] getCoordinates(){
        List<Coordinate> coords = new ArrayList<>((int) Math.pow(HelloController.mapSize,2));
        for(int i = 0;i<HelloController.mapSize;i++){
            for(int j = 0;j<HelloController.mapSize;j++){
                coords.add(new Coordinate(i,j));
            }
        }
        return coords.toArray(Coordinate[]::new);
    }

    private static boolean inBounds(Coordinate coord){
        int size = HelloController.mapSize;

        return coord.getX() < size && coord.getY() < size
                &&
                coord.getX() > -1 && coord.getY() > -1;
    }
}
