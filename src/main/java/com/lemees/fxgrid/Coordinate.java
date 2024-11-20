package com.lemees.fxgrid;

public class Coordinate {
    private final int x;
    private final int y;

    public Coordinate(int x,int y){
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getDistance(Coordinate coord){
        double xs = Math.pow(coord.x-this.x,2);
        double ys = Math.pow(coord.y-this.y,2);

        return Math.sqrt(xs+ys);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Coordinate)) return false;
        Coordinate coordinate = ((Coordinate) obj);
        return coordinate.x==x&&coordinate.y==y;
    }

    @Override
    public String toString() {
        return String.valueOf(hashCode());
    }

    @Override
    public int hashCode() {
        return (x*1000)+(y*10);
    }
}
