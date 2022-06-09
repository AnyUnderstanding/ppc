package control

import data.BoundingBox
import data.Stroke
import util.PointList
import util.ScalarList
import util.Point
import java.util.stream.IntStream
import kotlin.math.round
import kotlin.streams.toList


class StrokeController {
    // https://en.wikipedia.org/wiki/Centripetal_Catmull%E2%80%93Rom_spline

    var stroke = Stroke(0xFF000000U)
    val points = ArrayList<Point>()
    private var sampleCount = 0
    private val sampleAfter = 5
    private val amt = 5

    // used to keep track of the bounding box containing to whole stroke
    var left = Double.POSITIVE_INFINITY
    var right = Double.NEGATIVE_INFINITY
    var top =  Double.NEGATIVE_INFINITY
    var bottom = Double.POSITIVE_INFINITY


    fun catmullRomSpline(p0: Point, p1: Point, p2: Point, p3: Point): PointList {
        val alpha: Double = 0.25; // 0.5^2 = 0.25
        val t0: Double = 0.0;

        val t1: Double = t1(t0, alpha, p0, p1);
        val t2: Double = t1(t1, alpha, p1, p2);
        val t3: Double = t1(t2, alpha, p2, p3);

        val t: ScalarList = linespace(t1, t2, 200.0);


        val a0: PointList = (-t + t1) / (t1 - t0) * p0 + (((t - t0) / (t1 - t0)) * p1);
        val a1: PointList = (-t + t2) / (t2 - t1) * p1 + (((t - t1) / (t2 - t1)) * p2);
        val a2: PointList = (-t + t3) / (t3 - t2) * p2 + (((t - t2) / (t3 - t2)) * p3);


        val b0: PointList = a0 * ((-t + t2) / (t2 - t0)) + a1 * (((t - t0) / (t2 - t0)));
        val b1: PointList = a1 * ((-t + t3) / (t3 - t1)) + a2 * (((t - t1) / (t3 - t1)));

        val c: PointList = b0 * ((-t + t2) / (t2 - t1)) + b1 * (((t - t1) / (t2 - t1)));

        return c

    }


    fun linespace(start: Double, end: Double, precision: Double): ScalarList {

        // val amt: Int = max(kotlin.math.abs(kotlin.math.floor(end - start) * 3.0), 2.0).toInt();
        val stepSize = (end - start) / (amt - 1);



        return ScalarList(
            IntStream.range(0, amt).asDoubleStream().map {
                start + stepSize * it
            }.toList()
        )


    }

    fun t1(ti_prev: Double, alpha: Double, p0: Point, p1: Point): Double {
        return p0.getDistance(p1, alpha) + ti_prev
    }

    fun catmullRomChain(points: List<Point>): List<Point> {
        val startTime = System.nanoTime()
        val c = ArrayList<Point>();

        if (points.size < 4) {
            return c;
        }

        for (i in IntStream.range(0, points.size - 3)) {
            c.addAll(catmullRomSpline(points[i], points[i + 1], points[i + 2], points[i + 3]).values)
        }

        val endTime = System.nanoTime()
        val duration = endTime - startTime //divide by 1000000 to get milliseconds.

        println("Calculated Spline with " + points.size + " Points in " + duration + "ns")
        return c
    }


    fun newStroke(s: Stroke) {

        stroke.pendingPoints.forEach{
            calcBounds(it)
        }

        // calculate artificial bounding box to handle single point strokes
        if (stroke.spline.size == 0 && stroke.pendingPoints.size == 1) {
            left = stroke.pendingPoints[0].x -1
            right = stroke.pendingPoints[0].x + 1
            top = stroke.pendingPoints[0].y - 1
            bottom = stroke.pendingPoints[0].y + 1
            stroke.boundingBoxes.add(BoundingBox(Point(left, top), Point(right, bottom)))
        }
        stroke.mainBoundingBox = BoundingBox(Point(left, top), Point(right, bottom))


        left = Double.POSITIVE_INFINITY
        right = Double.NEGATIVE_INFINITY
        top =  Double.NEGATIVE_INFINITY
        bottom = Double.POSITIVE_INFINITY

        stroke = s
        sampleCount = 0
        points.clear()
    }

    fun addPoint(point: Point) {
        stroke.pendingPoints.add(point)
        if (sampleCount % sampleAfter == 0) {
            points.add(point)
            if (points.size >= 2) {
                stroke.boundingBoxes.add(BoundingBox(points[points.size - 2], point))
            }

            if (points.size == 2) {
                points.add(0, (points[1] - points[0]) + points[1])
            }
            if (points.size >= 3) {
                if (stroke.spline.size != 0) {
                    val i = points.size - 4
                    stroke.spline.removeRange(stroke.spline.size - amt, stroke.spline.size)


                    stroke.spline.addAll(
                        catmullRomSpline(points[i],
                            points[i + 1],
                            points[i + 2],
                            points[i + 3]).values.map {
                            calcBounds(it)
                            it}
                    )
                }
                val i = points.size - 3
                val endPoint = (points[i + 2] - points[i + 1]) + points[i + 2]

                stroke.spline.addAll(catmullRomSpline(points[i], points[i + 1], points[i + 2], endPoint).values.map {
                    calcBounds(it)
                it})
                stroke.pendingPoints.clear()
            }
        }
        sampleCount %= sampleAfter
        sampleCount++


    }

    fun calcBounds(point: Point){
        if (point.x < left) left = point.x
        if (point.x > right) right = point.x
        if (point.y > top) top = point.y
        if (point.y < bottom) bottom = point.y
    }
}