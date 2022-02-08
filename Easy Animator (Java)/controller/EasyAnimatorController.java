package cs5004.animator.controller;

/**
 * This is the interface for our controller portion of the Easy Animator. It connects the model and
 * the view appropriately and will call the view to render itself. If a view accepts user input, it
 * also handles how the input changes the view.
 */
public interface EasyAnimatorController {

  /**
   * The entry point into the controller that detects the type of view and then either calls the
   * view to render itself for text views or sets up a Swing timer that will call the animation to
   * display itself for the visual views.
   */
  void begin();

  /**
   * The Dynamic View of the EasyAnimator accepts user input via a number of buttons that can be
   * pressed. This wires the buttons to the controller so that it knows what to manipulate when the
   * user clicks one.
   *
   * @param button is the String representation of button that the user clicked on.
   */
  void handleButton(String button);
}
