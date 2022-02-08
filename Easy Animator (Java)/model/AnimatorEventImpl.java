package cs5004.animator.model;

import java.awt.Color;

import cs5004.animator.model.animatorshapes.AnimatorShape;

/**
 * This is the implementation of the AnimatorEvent interface. This represents a specific animatable
 * transformation applied to a Shape. This may be the creation of a Shape, the scaling of a shape,
 * the color change of a shape, or the moving of a shape from one point to another.
 */
public class AnimatorEventImpl implements AnimatorEvent {
  private int startTime;
  private int endTime;
  private AnimatorShape originalShape;
  private AnimatorShape endShape;

  /**
   * This is the default constructor for all of our events. It represents our Animator taking a
   * Shape and perform some transformation to it over time.
   *
   * @param startShape is the original shape we are transforming or creating.
   * @param endShape is the new shape after the event has occurred.
   * @param start is the start time of the animation event.
   * @param end   is the end time of the animation event.
   */
  public AnimatorEventImpl(AnimatorShape startShape, AnimatorShape endShape, int start, int end) {
    this.originalShape = startShape;
    this.endShape = endShape;
    this.startTime = start;
    this.endTime = end;
  }

  /**
   * This is the constructor for any events where we are creating a new Shape in our animator, thus
   * our startShape and endShape are considered the same object for the duration of this event
   * (transformations may still occur to the object over the course of the animation).
   *
   * @param shape is the shape we are creating in our animation.
   * @param start is the time the shape is created.
   * @param end is the time the shape disappears.
   */
  public AnimatorEventImpl(AnimatorShape shape, int start, int end) {
    this.originalShape = shape;
    this.endShape = shape;
    this.startTime = start;
    this.endTime = end;
  }

  @Override
  public int getStartTime() {
    return this.startTime;
  }

  @Override
  public int getEndTime() {
    return this.endTime;
  }

  @Override
  public void updateEndTime(int time) {
    this.endTime = time;
  }

  @Override
  public AnimatorShape getStartShape() {
    return this.originalShape;
  }

  @Override
  public AnimatorShape getEndShape() {
    return this.endShape;
  }

  /**
   * This is duplicate code from AnimatorShapeImpl to change color into the desired string template
   * of (1.0,1.0,1.0).
   *
   * @param color is the Color we are converting to a String.
   * @return the String version of the color in RGB scale from 0.0-1.0. For example, Black is
   *     (0.0,0.0,0.0).
   */
  private String colorString(Color color) {
    String colorStr = "(";
    float[] rgb = color.getColorComponents(null);
    for (int i = 0; i < 3; i++) {
      if (i == 2) {
        colorStr += String.format("%.1f", rgb[i]) + ")";
      } else {
        colorStr += String.format("%.1f", rgb[i]) + ",";
      }
    }
    return colorStr;
  }

  @Override
  public String toString() {
    if (originalShape.equals(endShape)) {
      return "Shape created:\n" + getEndShape().description(false) + "\n"
              + "Appears at t=" + getStartTime() + "\nDisappears at t=" + getEndTime();
    } else if (originalShape.getColor() != endShape.getColor()) {
      String start = colorString(getStartShape().getColor());
      String end = colorString(getEndShape().getColor());
      return "Shape " + getStartShape().getName() + " changes color from "
              + start + " to " + end + " from t=" + getStartTime() + " to t=" + getEndTime();
    } else if (originalShape.getYSpace() != endShape.getYSpace()
            || originalShape.getXSpace() != endShape.getXSpace()) {
      return "Shape " + getStartShape().getName() + " scales from "
              + getStartShape().description(true) + " to "
              + getEndShape().description(true) + " from t=" + getStartTime()
              + " to t=" + getEndTime();
    } else {
      return "Shape " + getStartShape().getName() + " moves from "
              + getStartShape().getReferencePoint().toString() + " to "
              + getEndShape().getReferencePoint().toString() + " from t=" + getStartTime()
              + " to t=" + getEndTime();
    }
  }
}
