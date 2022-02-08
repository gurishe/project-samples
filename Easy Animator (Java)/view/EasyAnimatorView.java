package cs5004.animator.view;

import java.util.List;

import cs5004.animator.model.animatorshapes.AnimatorShape;

/**
 * This interface supports all the possible views for the EasyAnimatorModel.
 */
public interface EasyAnimatorView {

  /**
   * This draws our view at the current time.
   */
  void renderView();

  /**
   * This provides our View with the list of shapes to render.
   *
   * @param shapeList is the list of Shapes it must show.
   */
  void setFrame(List<AnimatorShape> shapeList);

  void refresh();
}
