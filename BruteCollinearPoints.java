import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> segments;

    // private float getSquaredDistance(Point point1, Point point2) {
    //     return ((point1.x - point2.x) ^ 2 + (point1.y - point2.y) ^ 2);
    // }


    public BruteCollinearPoints(Point[] points) {    // finds all line segments containing 4 points

        segments = new ArrayList<LineSegment>();


        if (points == null || points.length == 0) {
            throw new IllegalArgumentException();
        }

        List<Point> existingPoints = new ArrayList<Point>();
        int left;
        int right;
        int mid;
        int midValue;
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
            left = 0;
            right = existingPoints.size();
            while (left < right) {
                mid = (left + right) / 2;
                midValue = existingPoints.get(mid).compareTo(point);
                if (midValue == 0) {
                    throw new IllegalArgumentException();
                }
                if (midValue < 0) {
                    left = mid + 1;
                }
                else {
                    right = mid;
                }
            }
            existingPoints.add(point);
        }


        for (int i = 0; i < points.length; i++) {

            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int m = k + 1; m < points.length; m++) {
                        Point point1 = points[i];
                        Point point2 = points[j];
                        Point point3 = points[k];
                        Point point4 = points[m];
                        Comparator<Point> comparator = point1.slopeOrder();
                        if (comparator.compare(point2, point3) != 0) {
                            continue;
                        }
                        if (comparator.compare(point2, point4) != 0) continue;

                        Point[] array = { point1, point2, point3, point4 };
                        Point minPoint = Arrays.stream(array).min(Point::compareTo).orElse(null);
                        Point maxPoint = Arrays.stream(array).max(Point::compareTo).orElse(null);
                        // StdOut.println("i: " + i + "j: " + j + "k: " + k + "m: " + m);
                        // StdOut.println("min: " + minPoint + "max: " + maxPoint);
                        segments.add(new LineSegment(minPoint, maxPoint));
                    }
                }
            }
        }
    }

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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
