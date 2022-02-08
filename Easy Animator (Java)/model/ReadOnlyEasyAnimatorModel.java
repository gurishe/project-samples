package cs5004.animator.model;

import java.util.List;

import cs5004.animator.model.animatorshapes.AnimatorShape;

/**
 * This is the Read-Only parts of our model so our View(s) can only access the information-
 * gathering methods of our model.
 */
public interface ReadOnlyEasyAnimatorModel {

  /**
   * Gets the bounds of the model (the top, left-most point and the height and width of the view
   * box) and returns an array of all four values.
   *
   * @return an int array where int[0] is the left-most x-value, int[1] is the top-most y-value,
   *     int[2] is the width, and int[3] is the height.
   */
  int[] getBounds();

  /**
   * This gets the list of AnimatorEvents that the model currently holds.
   *
   * @return a list of all the AnimatorEvents that the model holds.
   */
  List<AnimatorEvent> getEventList();

  /**
   * This gets all the Shapes of the animation that are exist at specific time frame.
   *
   * @param timeFrame is the integer frame of time.
   * @return a list of all the AnimatorShapes in the given frame of time.
   */
  List<AnimatorShape> getShapesAtFrame(int timeFrame);

  /**
   * This gets a copy of the list of current shapes that the animation has in it.
   *
   * @return a copy list of all the shapes we have created for our animation.
   */
  List<AnimatorShape> getShapeList();

  /**
   * This gets the last frame (i.e. largest AnimatorEvent end time) and returns it.
   *
   * @return an integer representing the final frame of the Animation.
   */
  int getFinalFrame();
}
