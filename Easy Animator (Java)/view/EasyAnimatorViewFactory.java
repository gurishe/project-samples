package cs5004.animator.view;

import java.util.NoSuchElementException;

import cs5004.animator.model.ReadOnlyEasyAnimatorModel;

/**
 * This is our View Factory which takes in a model, the requested type of View, an appendable
 * (necessary for SVG and text views), and a speed (necessary for SVG and visual views).
 */
public class EasyAnimatorViewFactory {

  /**
   * This constructs our ViewFactory with an EasyAnimatorModel, a String that represents the type of
   * view to return, an Appendable for a the text views, and a speed for the SVG and visual views.
   *
   * @param model    is a populated EasyAnimatorModel for the view to read from.
   * @param viewType is either svg, text, or visual to get the appropriate type of view.
   * @param append   is an appendable for the text and SVG views to output to.
   * @param speed    is the speed that the visual or SVG views should play at.
   * @param out      is the desired output of the view (for text-based views).
   * @return the appropriate instance of the EasyAnimatorView depending on which viewType was input.
   * @throws NoSuchElementException if the viewType does not match "svg", "text", "visual", or
   *                                "playback".
   */
  public static EasyAnimatorView getView(ReadOnlyEasyAnimatorModel model, String viewType,
                                         Appendable append, int speed, String out)
          throws NoSuchElementException {
    switch (viewType) {
      case "svg": // construct SVG View
        return new EasyAnimatorSVGView(model, append, speed, out);
      case "text": // construct purely text view
        return new EasyAnimatorTextView(model, append, out);
      case "visual": // prepare a visual view with Swing
        return new EasyAnimatorVisualView(model.getBounds()[0], model.getBounds()[1],
                model.getBounds()[2], model.getBounds()[3]);
      case "playback": // dynamic view with playback capabilities
        return new EasyAnimatorDynamicView(model.getBounds()[0], model.getBounds()[1],
                model.getBounds()[2], model.getBounds()[3]);
      default:
        throw new NoSuchElementException("View type does not exist");
    }
  }
}
