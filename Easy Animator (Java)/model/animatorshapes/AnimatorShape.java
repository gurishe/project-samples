package cs5004.animator.model.animatorshapes;

import java.awt.Color;

/**
 * This is the interface that all Shapes designed for our Animator model must adhere to.
 * AnimatorShapes must be able to change their size over time and they must be able to change their
 * color over time.
 */
public interface AnimatorShape {
  /**
   * Gets the current color of the Shape.
   *
   * @return the Color that this Shape is currently.
   */
  Color getColor();

  /**
   * Gets the given name of this Shape.
   *
   * @return the String name given to this AnimatorShape when it was created.
   */
  String getName();

  /**
   * Gets the type of Shape that this is (i.e. Rectangle, Oval, etc.).
   *
   * @return the ShapeTypes enumerated value representing type of shape that this AnimatorShape is.
   */
  ShapeTypes getType();

  /**
   * All shapes have a reference point represented by an XYCoordinate object which is just an (x,y)
   * point on a 2D plane. The x and y are both doubles.
   *
   * @return the XYCoordinate reference point of the Shape.
   */
  XYCoordinate getReferencePoint();

  /**
   * Returns the maximum vertical space (y-axis) of this Shape. For a Rectangle or a Triangle this
   * would be the height or for an Oval it is the Y radius.
   *
   * @return the maximum vertical space (a double) of the the Shape.
   */
  double getYSpace();

  /**
   * Returns the maximum horizontal space (x-axis) of this Shape. For a Rectangle or a Triangle this
   * would be the width/base or for an Oval it is the X radius.
   *
   * @return the the maximum horizontal space (a double) of the Shape.
   */
  double getXSpace();

  /**
   * Moves this shape from its previous location defined by (x,y) coordinates to the new location
   * provided.
   *
   * @param xCoordinate is the x-coordinate of the point to move this Shape to.
   * @param yCoordinate is the y-axis coordinate of the point to move this Shape to.
   * @return a new AnimatorShape identical to this one but with moved to a different place.
   */
  AnimatorShape move(double xCoordinate, double yCoordinate);

  /**
   * Changes the size of the current shape to new a new xValue and/or yValue. It can change the
   * horizontal size (x-axis) of the Shape, the vertical size (y-axis) of the Shape, or both.
   *
   * @param xValue is the largest horizontal value (i.e. width or radius) to change the Shape to.
   * @param yValue is the largest vertical value (i.e. height) to change the Shape to.
   * @return a new AnimatorShape identical to this one but with new dimensions.
   */
  AnimatorShape scaleSize(double xValue, double yValue);

  /**
   * Changes the color of the current shape from one Color to another.
   *
   * @param toColor is the Color it should be changed to.
   * @return a new AnimatorShape identical to this one but with a different color.
   */
  AnimatorShape changeColor(Color toColor);

  /**
   * Produces a String description of this Shape that includes what type of shape it is, the name of
   * the Shape, the reference point of that shape (i.e. the lower corner of a Rectangle or the
   * center of an oval), the defining dimensions of that shape (i.e. width and height for a
   * Rectangle).
   *
   * @param dimensionsOnly is a boolean value that can be toggled if we only want the dimension
   *                       description of the shape.
   * @return the String description of this AnimatorShape unless dimensionsOnly is true in which
   *         case we just return the dimensions.
   */
  String description(boolean dimensionsOnly);

  /**
   * Returns an identical, but separate copy of this AnimatorShape.
   *
   * @return an identical copy of the AnimatorShape.
   */
  AnimatorShape deepCopy();
}
