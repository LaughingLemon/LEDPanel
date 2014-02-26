package laughing.lemon.components;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.Arrays;

public class LEDPanel extends Panel {

    private int displayNumber = 8;

    //getter (bean compatibility)
    public int getDisplayNumber() {
        return displayNumber;
    }

    //setter (bean compatibility)
    public void setDisplayNumber(int displayNumber) {
        //only update is the value has actually changed
        if(this.displayNumber != displayNumber % 10) {
            //modulo 10 to create single digit
            this.displayNumber = displayNumber % 10;
            //repaint the component
            repaint();
        }
    }

    //points array is a constant
    private static final int[][] points = new int[6][2];

    //simple utility function to set the point co-ordinates
    private void setPoint(int[] inputPoint, int xCoOrd, int yCoOrd) {
        inputPoint[0] = xCoOrd;
        inputPoint[1] = yCoOrd;
    }

    //create an array of points representing a hexagonal segment
    private void populateSegmentPoints(int originX,   int originY,
                                       int thickness, int length) {
        //test to see if it's vertical segment
        if(thickness >= length) {
            //see documentation for details of how plots are calculated
            setPoint(points[0], length / 2,             0);
            setPoint(points[1], thickness - length / 2, 0);
            setPoint(points[2], thickness,              length / 2);
            setPoint(points[3], thickness - length / 2, length);
            setPoint(points[4], length / 2,             length);
            setPoint(points[5], 0,                      length / 2);
        } else {
            // or horizontal
            setPoint(points[0], thickness / 2,          0);
            setPoint(points[1], thickness,              thickness / 2);
            setPoint(points[2], thickness,              length - thickness / 2);
            setPoint(points[3], thickness / 2,          length);
            setPoint(points[4], 0,                      length - thickness / 2);
            setPoint(points[5], 0,                      thickness / 2);
        }

        //shift everything relative to the
        //origin point
        for(int[] point : points){
            point[0] += originX;
            point[1] += originY;
        }
    }

    //creates a segment polygon from array of points
    private GeneralPath createSegment() {
        //create a polygon using the GeneralPath class
        GeneralPath segment = new GeneralPath(GeneralPath.WIND_EVEN_ODD, points.length);
        //start at the beginning...
        segment.moveTo(points[0][0], points[0][1]);
        //draw a line from one point to the next
        for(int index = 1; index < points.length; index++) {
            segment.lineTo(points[index][0], points[index][1]);
        }
        //close off the line to create a shape
        segment.closePath();
        //return the shape
        return segment;
    }

    private void displaySegment(Graphics2D g2,
                                int[] filter) {
        //only create the segment if it's used in the display number
        //uses binarySearch function to see if the number
        //is in the filter
        if(Arrays.binarySearch(filter, displayNumber) > -1) {
            //create the polygon on the panel surface
            g2.fill(createSegment());
        }
    }

    //make number filters constants
    private static final int[] topSegmentFilter         = {0, 2, 3, 5, 6, 7, 8, 9};
    private static final int[] topLeftSegmentFilter     = {0, 4, 5, 6, 8, 9};
    private static final int[] topRightSegmentFilter    = {0, 1, 2, 3, 4, 7, 8, 9};
    private static final int[] middleSegmentFilter      = {2, 3, 4, 5, 6, 8, 9};
    private static final int[] bottomLeftSegmentFilter  = {0, 2, 6, 8};
    private static final int[] bottomRightSegmentFilter = {0, 1, 3, 4, 5, 6, 7, 8, 9};
    private static final int[] bottomSegmentFilter      = {0, 2, 3, 5, 6, 8};

    public void paint(Graphics g) {
        //convert (up-cast) the Graphics object to
        //a Graphics2D object
        Graphics2D g2 = (Graphics2D) g;

        //fix the width of the component
        //to be 55% of height
        int height = getHeight();
        int width = (int) (height * 0.55);

        setSize(width, height);

        //calculate the segment thickness and length
        int segmentThickness  = 2 * width - height;
        int segmentLength     = 2 * height - 3 * width;

        //set the segment colours to the
        //panel's foreground
        g2.setPaint(getForeground());

        //create the segments.
        //See documentation for calculations
        //of origin points

        //top segment
        populateSegmentPoints(segmentThickness,
                              0,
                              segmentLength,
                              segmentThickness);
        displaySegment(g2, topSegmentFilter);
        //top left segment
        populateSegmentPoints(0,
                              segmentThickness,
                              segmentThickness,
                              segmentLength);
        displaySegment(g2, topLeftSegmentFilter);
        //top right segment
        populateSegmentPoints(segmentLength + segmentThickness,
                              segmentThickness,
                              segmentThickness,
                              segmentLength);
        displaySegment(g2, topRightSegmentFilter);
        //middle segment
        populateSegmentPoints(segmentThickness,
                              segmentLength + segmentThickness,
                              segmentLength,
                              segmentThickness);
        displaySegment(g2, middleSegmentFilter);
        //bottom left segment
        populateSegmentPoints(0,
                              segmentThickness * 2 + segmentLength,
                              segmentThickness,
                              segmentLength);
        displaySegment(g2, bottomLeftSegmentFilter);
        //bottom right segment
        populateSegmentPoints(segmentLength + segmentThickness,
                              segmentThickness * 2 + segmentLength,
                              segmentThickness,
                              segmentLength);
        displaySegment(g2, bottomRightSegmentFilter);
        //bottom segment
        populateSegmentPoints(segmentThickness,
                              2 * (segmentLength + segmentThickness),
                              segmentLength,
                              segmentThickness);
        displaySegment(g2, bottomSegmentFilter);
    }
}
