package cs5004.animator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import cs5004.animator.controller.EasyAnimatorController;
import cs5004.animator.controller.EasyAnimatorControllerImpl;
import cs5004.animator.util.AnimationReader;
import cs5004.animator.view.EasyAnimatorView;
import cs5004.animator.view.EasyAnimatorViewFactory;
import cs5004.animator.model.EasyAnimatorModel;
import cs5004.animator.model.EasyAnimatorModelImpl;

/**
 * Our entry point into our animation which will construct the model and the appropriate view. Then
 * it constructs a controller and calls the begin() method on it to start the animation. It expects
 * input from the command line to follow the format of -in followed by the input file, -view
 * followed by the type of view, -out followed by the file to output to, and -speed followed by the
 * speed in ticks per second. If -out is not specified, it is defaulted to System.out. If speed is
 * not specified, it is defaulted to 1 tick per second.
 */
public final class EasyAnimator {
  /**
   * The actual main function that acts as controller.
   *
   * @param args is a String inputted by the user at the command line.
   * @throws FileNotFoundException if the -in file specified does not exist.
   */
  public static void main(String[] args) throws FileNotFoundException {
    String in = ""; // File to read from
    String view = ""; // Type of view
    String out = "out"; // Defaults to System.out
    int speed = 1; // Defaults to 1 tick per second
    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "-in":
          in = args[i + 1];
          break;
        case "-view":
          view = args[i + 1];
          break;
        case "-out":
          out = args[i + 1];
          break;
        case "-speed":
          speed = Integer.parseInt(args[i + 1]);
          break;
        default: // defaults to doing nothing
          break;
      }
    }
    File inFile = new File(in); // open our in file
    EasyAnimatorModel model = AnimationReader
            .parseFile(new FileReader(inFile), new EasyAnimatorModelImpl.AnimatorBuilder());
    StringBuilder build = new StringBuilder();
    // get the appropriate view
    EasyAnimatorView animatorView = EasyAnimatorViewFactory.getView(model, view, build, speed, out);
    // pass to our controller
    EasyAnimatorController controller = new EasyAnimatorControllerImpl(animatorView, model, speed);
    controller.begin();
  }
}