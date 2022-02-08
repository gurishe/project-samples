package cs5004.animator.view;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cs5004.animator.model.animatorshapes.AnimatorShape;

/**
 * This is our visual view which uses the Swing library to display the animations for the
 * EasyAnimator.
 */
public class EasyAnimatorVisualView extends JFrame implements EasyAnimatorView {
  private AnimatorPanel panel;

  /**
   * This constructs the view with an x and y representing the top, left corner of the viewing frame
   * and the width and height specifying the width and height of the viewing frame.
   *
   * @param x      is the x-coordinate of the top, left corner of the view frame.
   * @param y      is the y-coordinate of the top, left corner of the view frame.
   * @param width  is the width (pixels) of the view frame.
   * @param height is the height (pixels) of the view frame.
   */
  public EasyAnimatorVisualView(int x, int y, int width, int height) {
    super("Easy Animator Visual View");
    if (width < 0 || height < 0) {
      JOptionPane.showMessageDialog(panel, "Invalid view width and/or height");
    }
    setSize(width, height);
    setLocation(x, y);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    this.panel = new AnimatorPanel();
    add(panel);
  }

  /**
   * Simply displays the AnimatorPanel so we can see the shapes.
   */
  @Override
  public void renderView() {
    setVisible(true);
  }

  /**
   * Provides the panel a list of AnimatorShapes for it to draw.
   *
   * @param shapeList is the list of Shapes it must show.
   */
  @Override
  public void setFrame(List<AnimatorShape> shapeList) {
    panel.setCurrentFrame(shapeList);
  }

  /**
   * Simply tells this JFrame to repaint itself to give the illusion of animation to still frames.
   */
  @Override
  public void refresh() {
    repaint();
  }
}
