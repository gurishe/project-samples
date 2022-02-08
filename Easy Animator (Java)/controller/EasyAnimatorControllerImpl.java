package cs5004.animator.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import cs5004.animator.view.EasyAnimatorDynamicView;
import cs5004.animator.view.EasyAnimatorView;
import cs5004.animator.model.EasyAnimatorModel;
import cs5004.animator.view.EasyAnimatorVisualView;

/**
 * This is the controller portion of the Easy Animator. It takes a view and a model and will
 * call the view to display itself. For the text based views, it simply calls the renderView()
 * method as those views simply produce a text readout either to a file (if specified) or to
 * System.out. For the visual views, the controller sets up a Timer and then calls the view to
 * render itself based upon the speed given to this object when constructed.
 */
public class EasyAnimatorControllerImpl implements EasyAnimatorController, ActionListener {
  private EasyAnimatorModel model;
  private EasyAnimatorView view;
  private int speed;
  private Timer timer;
  private boolean loop;
  private int currentFrame;

  /**
   * Constructs our controller with an already populated model, view, and a given speed in ticks
   * per second.
   *
   * @param view is the type of EasyAnimatorView the controller will call to render.
   * @param model is the EasyAnimatorModel that holds all the data for the view to display.
   * @param speed is the speed at which the animation should play it if it is visual/SVG. Speed is
   *              in ticks per second.
   * @throws IllegalArgumentException if the speed entered is non-positive or if the view or model
   *                                  is null.
   */
  public EasyAnimatorControllerImpl(EasyAnimatorView view, EasyAnimatorModel model, int speed)
          throws IllegalArgumentException {
    if (speed <= 0) {
      throw new IllegalArgumentException("Speed must be positive");
    } else if (view == null || model == null) {
      throw new IllegalArgumentException("Model and view cannot be null");
    }
    this.model = model;
    this.view = view;
    this.currentFrame = 0;
    this.loop = false;
    this.speed = 1000 / speed; // convert speed from ticks per sec to millisecond
    this.timer = new Timer(this.speed, this);
    this.timer.setInitialDelay(0);
  }

  /**
   * This calls the view to render itself if it is text-based or, if it is a visual view, it calls
   * the timer to start itself so the visual view is rendered.
   */
  @Override
  public void begin() {
    if (view instanceof EasyAnimatorVisualView) {
      timer.start();
    } else if (view instanceof EasyAnimatorDynamicView) {
      ((EasyAnimatorDynamicView) view).addButtonListeners(this);
      timer.setRepeats(false);
      timer.start();
    } else {
      view.renderView();
    }
  }

  /**
   * This method is invoked in the Dynamic view if a user clicks on a button that should prompt the
   * controller to perform an action like pausing, restarting, or looping the animation.
   *
   * @param button is the String representation of button that the user clicked on.
   */
  @Override
  public void handleButton(String button) {
    switch (button) {
      case "start": // this is the same as resume
        timer.setRepeats(true);
        timer.start();
        break;
      case "pause":
        timer.stop();
        break;
      case "restart":
        timer.stop();
        currentFrame = 0;
        timer.start();
        break;
      case "loop":
        loop = !loop;
        break;
      case "decrease":
        speed += 10;
        timer.stop();
        timer.setDelay(speed);
        timer.start();
        break;
      case "increase":
        if (speed - 10 <= 0) {
          speed = 1;
        } else {
          speed -= 10;
        }
        timer.stop();
        timer.setDelay(speed);
        timer.start();
        break;
      default: // default does nothing
        break;
    }
  }

  /**
   * This is the method that is called by this Listener every time the timer goes off. It will give
   * the view a list of Shapes that it needs to render at the current frame or tick of time.
   *
   * @param actionEvent is effectively ignored in this method.
   */
  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    view.setFrame(model.getShapesAtFrame(currentFrame));
    view.renderView();
    view.refresh();
    currentFrame++;
    if (loop && currentFrame > model.getFinalFrame()) {
      currentFrame = 0;
    }
  }
}