import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BVH implements Iterable<Circle>{
    Box boundingBox;

    BVH child1;
    BVH child2;

    Circle containedCircle;

    // todo for students
    public BVH(ArrayList<Circle> circles) {
        this.boundingBox = buildTightBoundingBox(circles);

        if (circles.size() > 1){
            ArrayList[] combinedArray = split(circles, boundingBox);
            this.child1 = new BVH(combinedArray[0]);
            this.child2 = new BVH(combinedArray[1]);
        } else if (circles.size() == 1){
            this.containedCircle = circles.get(0);
        }

    }

    public void draw(Graphics2D g2) {
        this.boundingBox.draw(g2);
        if (this.child1 != null) {
            this.child1.draw(g2);
        }
        if (this.child2 != null) {
            this.child2.draw(g2);
        }
    }

    // todo for students
    public static ArrayList<Circle>[] split(ArrayList<Circle> circles, Box boundingBox) {

        ArrayList<Circle> arraylist1 = new ArrayList<Circle>();
        ArrayList<Circle> arraylist2 = new ArrayList<Circle>();

        if (boundingBox.getWidth() >= boundingBox.getHeight()){
            for (Circle thisCircle : circles ){
                if (thisCircle.position.x == boundingBox.getMidX()){
                    arraylist1.add(thisCircle);
                    //arraylist2.add(thisCircle);
                } else if (thisCircle.position.x < boundingBox.getMidX()){
                    arraylist1.add(thisCircle);
                } else {
                    arraylist2.add(thisCircle);
                }
            }

        } else {
            for (Circle thisCircle : circles ){
                if (thisCircle.position.y < boundingBox.getMidY()){
                    arraylist1.add(thisCircle);
                } else {
                    arraylist2.add(thisCircle);
                }
            }

        }
        return new ArrayList[]{arraylist1,arraylist2};
    }

    // returns the smallest possible box which fully encloses every circle in circles
    public static Box buildTightBoundingBox(ArrayList<Circle> circles) {
        Vector2 bottomLeft = new Vector2(Float.POSITIVE_INFINITY);
        Vector2 topRight = new Vector2(Float.NEGATIVE_INFINITY);

        for (Circle c : circles) {
            bottomLeft = Vector2.min(bottomLeft, c.getBoundingBox().bottomLeft);
            topRight = Vector2.max(topRight, c.getBoundingBox().topRight);
        }

        return new Box(bottomLeft, topRight);
    }

    // METHODS BELOW RELATED TO ITERATOR

    // todo for students
    @Override
    public Iterator<Circle> iterator() {
        return new BVHIterator(this);
    }

    public class BVHIterator implements Iterator<Circle> {
        ArrayList<Circle> iteratorCircles = new ArrayList<Circle>();
        int currentIndex;

        // todo for students
        public BVHIterator(BVH bvh) {
            //ArrayList<Circle> iteratorCircles = new ArrayList<Circle>();
            iteratorCircles.addAll(addrecursive(bvh));
        }

        // todo for students
        @Override
        public boolean hasNext() {
            if (iteratorCircles.size() >= currentIndex + 1){
                return true;
            } else return false;
        }

        // todo for students
        @Override
        public Circle next() {
            currentIndex++;
            return iteratorCircles.get(currentIndex -1);

        }

        public ArrayList<Circle> addrecursive(BVH bvh){
            Circle currentCircle;
            ArrayList<Circle> iteratorCircles2 = new ArrayList<Circle>();
            //this.addAll(iteratorCircles);

            if (bvh.containedCircle != null){
                iteratorCircles2.add(bvh.containedCircle);
            }

            if (bvh.child1 != null) {
                ArrayList<Circle> temp1 = addrecursive(bvh.child1);
                iteratorCircles2.addAll(temp1);
            }

            if (bvh.child2 != null){
                ArrayList<Circle> temp2 = addrecursive(bvh.child2);
                iteratorCircles2.addAll(temp2);
            }
            return iteratorCircles2;

        }

    }


}