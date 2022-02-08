package cs5004.animator.model.animatorshapes;

/**
 * This class represents a 2D point on a graph or a plane which has an x-coordinate and a
 * y-coordinate. Every Shape in our Animator will have at least one defining XYCoordinate.
 */
public class XYCoordinate {
  private double xCoordinate;
  private double yCoordinate;

  /**
   * This constructs our XYCoordinate by taking two doubles. The first represents our desired
   * x-coordinate and the second is our desired y-coordinate. Together the represent a point on a
   * graph.
   * @param x is the x-coordinate of the point.
   * @param y is the y-coordinate of the point.
   */
  public XYCoordinate(double x, double y) {
    this.xCoordinate = x;
    this.yCoordinate = y;
  }

  /**
   * A getter function that retrieves the x-coordinate of our point.
   * @return the double value of our x-coordinate.
   */
  public double getXCoordinate() {
    return xCoordinate;
  }

  /**
   * A getter function that retrieves the y-coordinate of our point.
   * @return the double value of our y-coordinate.
   */
  public double getYCoordinate() {
    return yCoordinate;
  }

  /**
   * Produces a String version of XYCoordinate point enclosed in parentheses, each coordinate is
   * separated by a comma, and the double values are to displayed to only 1 decimal place.
   *
   * @return a String representation of our XYCoordinate where the x-coordinate and y-coordinate
   *     only show to 1 decimal place.
   */
  @Override
  public String toString() {
    return "(" + String.format("%.1f", xCoordinate) + ","
            + String.format("%.1f", yCoordinate) + ")";
  }
}
