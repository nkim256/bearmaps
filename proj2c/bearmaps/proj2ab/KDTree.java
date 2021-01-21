package bearmaps.proj2ab;
import java.util.Collection;


public class KDTree implements PointSet{
    private Node root;
    private class Node{
        private Point coord;
        private boolean orientation; /*true = right/left and false = up/down*/
        private Node leftBottom;
        private Node rightTop;
        private Node(Point p, boolean orientation) {
            coord = p;
            this.orientation = orientation;
        }
    }

    public KDTree(Collection<Point> points){
        for(Point x: points){
            root = setup(x, root, true);
        }
    }

    private Node setup(Point p, Node root, boolean y){
        if(root == null) {
            return new Node(p, y);
        }
        if(p.equals(root.coord)){
            return root;
        }
        int cmp = comparePoints(p, root.coord, root);
        if(cmp< 0){
            root.leftBottom = setup(p, root.leftBottom, !y);
        }
        else{
            root.rightTop = setup(p, root.rightTop, !y);
        }
        return root;

    }
    @Override
    public Point nearest(double x, double y){
        return nearest(new Point(x,y), root, root.coord);

    }
    private int comparePoints(Point x, Point y, Node z){
        if(z.orientation){
            return Double.compare(x.getX(), y.getX());
        }
        return Double.compare(x.getY(), y.getY());
    }
    private Point nearest(Point f, Node z, Point best){
        if(z == null){
            return best;
        }
        if(Point.distance(f, z.coord) < Point.distance(f, best)) best = z.coord;
        int cmp = comparePoints(f, z.coord, z);
        Node good;
        Node bad;
        if(cmp < 0){
            good = z.leftBottom;
            bad = z.rightTop;
        }
        else{
            bad = z.leftBottom;
            good = z.rightTop;
        }
        best = nearest(f, good, best);
        if(finalCheck(f, z, best)){
            best= nearest(f, bad, best);
        }
        return best;

    }
    private boolean finalCheck(Point x, Node z, Point best){
        double goodDistance = Point.distance(x, best);
        double badDistance;
        if(z.orientation){
            badDistance = Point.distance(new Point(z.coord.getX(), x.getY()), x);
        }
        else{
            badDistance = Point.distance(new Point(x.getX(), z.coord.getY()), x);
        }
        return badDistance<goodDistance;

    }
    private double distance(double x, double y, Point k){
        return Math.sqrt(Math.pow(x-k.getX(), 2) + Math.pow(y-k.getY(), 2));
    }
}


