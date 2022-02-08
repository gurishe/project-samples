package cs5004.animator.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;

import javax.swing.JPanel;

import cs5004.animator.model.animatorshapes.AnimatorShape;
import cs5004.animator.model.animatorshapes.ShapeTypes;

/**
 * This is the AnimatorPanel which is the window that will paint our Shapes on. It is given a list
 * of Shapes and it will draw each Shape that it is given on the panel.
 */
public class AnimatorPanel extends JPanel {
  private List<AnimatorShape> shapeList;

  /**
   * This method gives our panel the list of Shapes that it needs to display at the current frame.
   *
   * @param shapeList is a list of AnimatorShapes that it will display.
   */
  void setCurrentFrame(List<AnimatorShape> shapeList) {
    this.shapeList = shapeList;
  }

  @Override
  protected void paintComponent(Graphics graphic) {
    super.paintComponent(graphic);
    Graphics2D castGraphic = (Graphics2D) graphic;

    for (AnimatorShape shape : shapeList) {
      // get reference point, width, and height
      int x = (int) shape.getReferencePoint().getXCoordinate();
      int y = (int) shape.getReferencePoint().getYCoordinate();
      int w = (int) shape.getXSpace();
      int h = (int) shape.getYSpace();
      castGraphic.setColor(shape.getColor()); // set the color of the shape
      if (shape.getType() == ShapeTypes.Oval || shape.getType() == ShapeTypes.Ellipse) {
        castGraphic.fillOval(x, y, w, h); // draw an oval/ellipse
      } else if (shape.getType() == ShapeTypes.Rectangle) {
        castGraphic.fillRect(x, y, w, h); // draw a rectangle
      }
    }
  }
}
