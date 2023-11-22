import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        segments = new ArrayList<LineSegment>();
        for (int i = 0; i < points.length; i++) {
            Point p = points[i];

            int length = points.length;
            Point[] myPoints = new Point[length - 1];


            int index = 0;
            for (int j = 0; j < points.length; j++) {
                if (i == j) {
                    continue;
                }

                Point q = points[j];
                myPoints[index] = q;

                // double slope = p.slopeTo(q);
                // Arrays.sort();
                index++;
            }
            Comparator<Point> comparator = p.slopeOrder();

            Object[] foo = Arrays.stream(myPoints).sorted(comparator).toArray();

            // for (int m = 0; m < foo.length; m++) {
            // StdOut.println("i: " + i + " m: " + m + " point: " + foo[m]);
            // }

            int streak = 1;
            Point firstPoint = (Point) foo[0];
            double lastSlope = p.slopeTo(firstPoint);

            Point minPoint = p.compareTo(firstPoint) < 0 ? p : firstPoint;
            Point maxPoint = p.compareTo(firstPoint) > 0 ? p : firstPoint;
            for (int k = 1; k < foo.length; k++) {
                Point myPoint = (Point) foo[k];
                double slope = p.slopeTo(myPoint);

                if (lastSlope != slope) {
                    if (streak >= 3) {
                        // StdOut.println("Streak loop" + streak);
                        segments.add(new LineSegment(minPoint, maxPoint));
                    }
                    streak = 1;
                    minPoint = p.compareTo(myPoint) < 0 ? p : myPoint;
                    maxPoint = p.compareTo(myPoint) > 0 ? p : myPoint;
                    lastSlope = slope;
                    continue;
                }

                minPoint = minPoint.compareTo(myPoint) < 0 ? minPoint : myPoint;
                maxPoint = maxPoint.compareTo(myPoint) > 0 ? maxPoint : myPoint;

                streak++;
                lastSlope = slope;
            }

            // StdOut.println("Ended loop" + streak);
            if (streak >= 3) {
                // StdOut.println("Ended loop");
                segments.add(new LineSegment(minPoint, maxPoint));
            }

        }


    }     // finds all line segments containing 4 or more points

    public int numberOfSegments() {        // the number of line segments
        return segments.size();
    }

    public LineSegment[] segments() {                // the line segments
        return segments.toArray(new LineSegment[0]);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
