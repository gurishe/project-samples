package cs5004.animator.model;

import cs5004.animator.model.animatorshapes.AnimatorShape;

/**
 * This interface represents all of the actual animated events that take place over the course of
 * the animation. These include when shapes are added to the animation (created) and when shapes are
 * changed in any way (color or size). All events take place over a time interval that is unit-less
 * and is just represented by integers here.
 */
public interface AnimatorEvent {

  /**
   * This gets the integer value that represents the time the animation event will begin to occur.
   *
   * @return the integer of time when the animation (i.e. change) will occur.
   */
  int getStartTime();

  /**
   * This gets the integer value that represents the time the animation event will end.
   *
   * @return the integer of time when the animation (i.e. change) has finished.
   */
  int getEndTime();

  /**
   * This allows a user to update the end time, especially if they wanted to extend when a Shape
   * disappears.
   */
  void updateEndTime(int time);

  /**
   * This is the original shape that we are going to modify in some way (unless we are creating it
   * for the first time) over the course of the animation.
   *
   * @return the AnimatorShape object that exists at the beginning of the animation event.
   */
  AnimatorShape getStartShape();

  /**
   * This is the final form of the shape after it underwent some transformation during the animation
   * event.
   *
   * @return the new AnimatorShape object after it has undergone a transformation.
   */
  AnimatorShape getEndShape();

  /**
   * Produces a verbose description of the actual event that has taken place. For example, if a new
   * shape was created, it provides the name, type, and dimensions of the new Shape. If a shape has
   * changed it provides a description of what exactly changed and over what time period the change
   * has occurred.
   *
   * @return a String description of the animation event to give an understanding of what happened
   *     over the course of this event.
   */
  String toString();
}
