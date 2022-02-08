package cs5004.animator.view;

import java.io.FileWriter;
import java.util.List;

import cs5004.animator.model.AnimatorEvent;
import cs5004.animator.model.animatorshapes.AnimatorShape;
import cs5004.animator.model.ReadOnlyEasyAnimatorModel;

/**
 * This is the textual view for the EasyAnimator. It takes a model and will write the description it
 * gets from the model to the appendable that is passed to it. It will then attempt to output to a
 * file or System.out depending on what is specified in the constructor.
 */
public class EasyAnimatorTextView implements EasyAnimatorView {
  private ReadOnlyEasyAnimatorModel model;
  private Appendable append;
  private String output;

  /**
   * Constructs the text view with a model and a given output path.
   *
   * @param model      is the Read-Only EasyAnimatorModel that the view will get the text data
   *                   from.
   * @param appendable is an Appendable object that this view will pass the text to.
   * @param output     is the desired output of the view (a file or System.out).
   */
  public EasyAnimatorTextView(ReadOnlyEasyAnimatorModel model, Appendable appendable,
                              String output) {
    this.model = model;
    this.append = appendable;
    this.output = output;
  }

  /**
   * This gets the full description of of entire animation sequence that our model currently
   * represents. It provides information on what shapes were created when, their position, their
   * color, and the time each animation takes.
   *
   * @return a String description of the entire animation sequence and each of its important
   *     components (i.e. shapes, colors, and time).
   */
  private String buildText() {
    List<AnimatorEvent> eventList = model.getEventList();
    StringBuilder eventLog = new StringBuilder();
    for (AnimatorEvent event : eventList) {
      eventLog.append(event.toString());
      eventLog.append("\n\n");
    }
    return eventLog.toString().stripTrailing();
  }

  /**
   * This renders our text view by passing the text to the appendable that this view holds. It will
   * also output the text to System.out if that was specified or will attempt to write to a file
   * otherwise.
   */
  @Override
  public void renderView() {
    String text = buildText();
    try {
      append.append(text);
      if (output.equals("out")) {
        System.out.println(append.toString());
      } else {
        FileWriter writer = new FileWriter(output);
        writer.write(append.toString());
        writer.close();
      }
    } catch (Exception e) {
      System.out.println("Failed to output text view");
    }
  }

  /**
   * This does nothing in our text view.
   *
   * @param shapeList is the list of Shapes it must show.
   * @throws UnsupportedOperationException as this does nothing in the text views.
   */
  @Override
  public void setFrame(List<AnimatorShape> shapeList) throws UnsupportedOperationException {
    // This method does nothing in our textual views.
    throw new UnsupportedOperationException("This method is unsupported for text views");
  }

  /**
   * This does nothing in our text view.
   *
   * @throws UnsupportedOperationException as this does nothing in the text views
   */
  @Override
  public void refresh() throws UnsupportedOperationException {
    // This method does nothing in our textual views.
    throw new UnsupportedOperationException("This method is unsupported for text views");
  }
}