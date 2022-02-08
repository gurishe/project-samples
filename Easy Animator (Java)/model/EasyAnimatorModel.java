package cs5004.animator.model;

import java.awt.Color;

import cs5004.animator.model.animatorshapes.AnimatorShape;

/**
 * This is the interface for our EasyAnimatorModel. This model represents an application for
 * creating and displaying simple 2D animations with shapes and colors.
 */
public interface EasyAnimatorModel extends ReadOnlyEasyAnimatorModel {

  /**
   * This sets the viewing boundaries that our model has.
   *
   * @param x is the left-most x-coordinate.
   * @param y is the top-most y-coordinate.
   * @param width is the width of the viewing box.
   * @param height is the height of the viewing box.
   */
  void setBounds(int x, int y, int width, int height);

  /**
   * Creates a Shape to be used as part of the animation.
   *
   * @param shape     is the Shape we want to add to the animation.
   * @param appear    is the time the shape should appear.
   * @param disappear is the time the shape should disappear.
   */
  void createShape(AnimatorShape shape, int appear, int disappear);

  /**
   * This moves the Shape we want (we call the Shape by name) to another reference point.
   *
   * @param name        is the name of the Shape we want to move.
   * @param xCoordinate is the x-coordinate that Shape will move to.
   * @param yCoordinate is the y-coordinate that the Shape will move to.
   * @param start       is the desired start time of the move.
   * @param end         is the desired end time of the move.
   */
  void moveShape(String name, double xCoordinate, double yCoordinate, int start, int end);

  /**
   * This will scale a specific Shape in our model from a start time to an end time.
   *
   * @param name   is the name of the Shape we want to scale.
   * @param xValue is the new width or horizontal radius of the Shape.
   * @param yValue is the new height or vertical radius of the Shape.
   * @param start  is the desired start time of the scale.
   * @param end    is the desired end time of the scale.
   */
  void scaleShape(String name, double xValue, double yValue, int start, int end);

  /**
   * This will change the color of a specific Shape in our model from a start time to an end time.
   *
   * @param name  is the name of the Shape whose color is to change.
   * @param color is the new Color the Shape will be.
   * @param start is the desired start time of the color change.
   * @param end   is the desired end time of the color change.
   */
  void changeColor(String name, Color color, int start, int end);
}
