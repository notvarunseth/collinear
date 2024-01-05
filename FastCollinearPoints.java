import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FastCollinearPoints {
    private LineSegment[] segments;
    private List<Pair> pairs;


    private class Pair implements Comparable<Pair> {


        private final Point p;
        private final Point q;

        Pair(Point p, Point q) {
            this.p = p;
            this.q = q;
        }

        public int compareTo(Pair that) {
            /* YOUR CODE HERE */
            int pCompared = this.p.compareTo(that.p);
            int qCompared = this.q.compareTo(that.q);
            if (pCompared == 0 && qCompared == 0) {
                return 0;
            }
            if (pCompared < 0 || (pCompared == 0 && qCompared < 0)) {
                return -1;
            }
            return 1;
        }


    }

    public FastCollinearPoints(Point[] points) {
        pairs = new ArrayList<>();
        segments = new LineSegment[0];

        if (points == null || points.length == 0) {
            throw new IllegalArgumentException();
        }
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }


        Point[] points2 = Arrays.stream(points).sorted(Point::compareTo).toArray(Point[]::new);
        for (int i = 1; i < points2.length; i++) {
            if (points2[i].compareTo(points2[i - 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }


        if (points.length < 4) {
            return;
        }
        // Object[] foo = Arrays.stream(myPoints).sorted(comparator).toArray();

        int left;
        int right;
        int mid;
        int midValue;


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

            Point[] foo = Arrays.stream(myPoints).sorted(comparator).toArray(Point[]::new);

            // for (int m = 0; m < foo.length; m++) {
            // StdOut.println("i: " + i + " m: " + m + " point: " + foo[m]);
            // }

            int streak = 1;
            Point firstPoint = foo[0];
            double lastSlope = p.slopeTo(firstPoint);

            Point minPoint = p.compareTo(firstPoint) < 0 ? p : firstPoint;
            Point maxPoint = p.compareTo(firstPoint) > 0 ? p : firstPoint;
            for (int k = 1; k < foo.length; k++) {
                Point myPoint = foo[k];
                double slope = p.slopeTo(myPoint);

                if (lastSlope != slope) {
                    if (streak >= 3) {
                        // StdOut.println("Streak loop" + streak);
                        // segments.add(new LineSegment(minPoint, maxPoint));
                        boolean good = true;
                        Pair newPair = new Pair(minPoint, maxPoint);
                        // binary search
                        left = 0;
                        right = pairs.size();

                        while (left < right) {
                            mid = (left + right) / 2;

                            midValue = pairs.get(mid).compareTo(newPair);
                            if (midValue == 0) {
                                good = false;
                                break;
                            }
                            if (midValue < 0) {
                                left = mid + 1;
                            }
                            else {
                                right = mid;
                            }
                        }


                        if (good) {
                            pairs.add(left, newPair);
                        }
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
                // segments.add(new LineSegment(minPoint, maxPoint));
                boolean good = true;
                Pair newPair = new Pair(minPoint, maxPoint);

                left = 0;
                right = pairs.size();

                while (left < right) {
                    mid = (left + right) / 2;

                    midValue = pairs.get(mid).compareTo(newPair);
                    if (midValue == 0) {
                        good = false;
                        break;
                    }
                    if (midValue < 0) {
                        left = mid + 1;
                    }
                    else {
                        right = mid;
                    }
                }
                
                if (good) {
                    pairs.add(newPair);
                }
            }

        }

        pairs = pairs.stream().sorted(Pair::compareTo).collect(Collectors.toList());
        List<Pair> pairs2 = new ArrayList<>();
        Pair lastElement = null;
        for (Pair pair : pairs) {
            if (lastElement == null || lastElement.compareTo(pair) != 0) {
                pairs2.add(pair);
                lastElement = pair;
            }
        }
        pairs = pairs2;
        segments = pairs.stream().map(pair1 -> new LineSegment(pair1.p, pair1.q))
                        .toArray(LineSegment[]::new);


    }     // finds all line segments containing 4 or more points

    public int numberOfSegments() {        // the number of line segments
        return pairs.size();
    }

    public LineSegment[] segments() {                // the line segments
        // return segments;
        return Arrays.copyOf(segments, segments.length);
        // return segments.toArray(new LineSegment[0]);
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
