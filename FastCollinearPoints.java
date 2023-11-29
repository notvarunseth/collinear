import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segments;
    private ArrayList<Pair> pairs;


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
        // segments = new ArrayList<LineSegment>();


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

        pairs = new ArrayList<>();

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

                        // for (Pair pair : pairs) {
                        //     if (pair.equals(newPair)) {
                        //         good = false;
                        //         break;
                        //     }
                        // }

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
                for (Pair pair : pairs) {
                    if (pair.equals(newPair)) {
                        good = false;
                        break;
                    }
                }
                if (good) {
                    pairs.add(newPair);
                }
            }

        }


    }     // finds all line segments containing 4 or more points

    public int numberOfSegments() {        // the number of line segments
        return pairs.size();
    }

    public LineSegment[] segments() {                // the line segments
        return pairs.stream().map(pair -> new LineSegment(pair.p, pair.q))
                    .toArray(LineSegment[]::new);
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
