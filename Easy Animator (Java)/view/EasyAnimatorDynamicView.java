package cs5004.animator.view;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import cs5004.animator.controller.EasyAnimatorController;
import cs5004.animator.model.animatorshapes.AnimatorShape;

/**
 * This class represents an expansion of the EasyAnimatorVisualView. This view includes buttons that
 * allow the user to interact with the Animation by pausing it, restarting it from the beginning,
 * changing whether it loops back to the beginning, and increasing or decreasing the speed on
 * command.
 */
public class EasyAnimatorDynamicView extends JFrame implements EasyAnimatorView {
  private AnimatorPanel panel;
  private final JButton start = new JButton("Start/Resume");
  private final JButton pause = new JButton("Pause");
  private final JButton restart = new JButton("Restart");
  private final JButton loop = new JButton("Enable/Disable Loop");
  private final JButton incSpeed = new JButton("Increase Speed");
  private final JButton decSpeed = new JButton("Decrease Speed");

  /**
   * This constructs our view with an x and y coordinate for the top left point of the viewing
   * rectangle as well as the width and height of the viewing rectangle.
   *
   * @param x      is the x-coordinate of the top, left corner of the view frame.
   * @param y      is the y-coordinate of the top, left corner of the view frame.
   * @param width  is the width (pixels) of the view frame.
   * @param height is the height (pixels) of the view frame.
   */
  public EasyAnimatorDynamicView(int x, int y, int width, int height) {
    super("Easy Animator Dynamic View");
    if (width < 0 || height < 0) {
      JOptionPane.showMessageDialog(panel, "Invalid view width and/or height");
    }
    setSize(width, height);
    setLocation(x, y);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout()); // so buttons can be put on the bottom of the view

    JPanel optionPanel = new JPanel(); // panel to hold our buttons
    this.panel = new AnimatorPanel();
    JScrollPane scrollPane = new JScrollPane(optionPanel); // make all buttons accessible
    optionPanel.add(start);
    optionPanel.add(pause);
    optionPanel.add(restart);
    optionPanel.add(loop);
    optionPanel.add(incSpeed);
    optionPanel.add(decSpeed);
    add(scrollPane, BorderLayout.SOUTH);
    add(panel);
  }

  /**
   * This adds the action listeners to our buttons so that the controller knows what to do when
   * the user presses the button.
   *
   * @param controller is the controller that will change the view based upon what the user clicks
   *                   on.
   */
  public void addButtonListeners(EasyAnimatorController controller) {
    start.addActionListener(actionEvent -> controller.handleButton("start"));
    pause.addActionListener(actionEvent -> controller.handleButton("pause"));
    restart.addActionListener(actionEvent -> controller.handleButton("restart"));
    loop.addActionListener(actionEvent -> controller.handleButton("loop"));
    incSpeed.addActionListener(actionEvent -> controller.handleButton("increase"));
    decSpeed.addActionListener(actionEvent -> controller.handleButton("decrease"));
  }

  /**
   * This sets the JFrame to be visible so the user can view the animation and interact with the
   * buttons.
   */
  @Override
  public void renderView() {
    this.setVisible(true);
  }

  /**
   * Provides a list of Animator Shapes for the Animator Panel to display at a given tick.
   *
   * @param shapeList is the list of Shapes it must show.
   */
  @Override
  public void setFrame(List<AnimatorShape> shapeList) {
    this.panel.setCurrentFrame(shapeList);
  }

  /**
   * Simply tells this JFrame to repaint itself to give the illusion of animation to still frames.
   */
  @Override
  public void refresh() {
    this.repaint();
  }
}
