package utils;

public class Position {
    double x;
    double y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Position(double x, double y){
        this.x = x;
        this.y= y;
    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof Position)) return false;

        Position p = (Position) obj;

        return (int)p.getX() == (int)x && (int)p.getY() == (int)y;
    }

    @Override
    public String toString() {
        return "(" + (int)x + "; " + (int)y + ")";
    }
}
