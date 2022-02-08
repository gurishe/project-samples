import org.junit.Before;
import org.junit.Test;

import java.awt.Color;
import java.util.List;
import java.util.NoSuchElementException;

import cs5004.animator.model.animatorshapes.AnimatorShapeImpl;
import cs5004.animator.model.animatorshapes.AnimatorShape;
import cs5004.animator.model.animatorshapes.ShapeTypes;
import cs5004.animator.model.EasyAnimatorModelImpl;

import static org.junit.Assert.assertEquals;

/**
 * This is the test class for the EasyAnimatorModel interface and the current implementation of the
 * EasyAnimator model that I have constructed.
 */
public class EasyAnimatorModelTest {
  private AnimatorShapeImpl r1;
  private AnimatorShapeImpl o1;
  private EasyAnimatorModelImpl model;

  @Before
  public void setUp() {
    r1 = new AnimatorShapeImpl("r1", ShapeTypes.Rectangle,0.0, 0.0,
            Color.BLACK, 25, 30);
    o1 = new AnimatorShapeImpl("o1", ShapeTypes.Oval, 25.00, 25.00,
            Color.YELLOW, 5, 10);
    model = new EasyAnimatorModelImpl();
  }

  @Test
  public void testConstructor() {
    assertEquals(0, model.getEventList().size());
    assertEquals(0, model.getShapeList().size());
    assertEquals(0, model.getEventsAtFrame(0).size());
  }

  @Test
  public void testGetFinalFrame() {
    assertEquals(0, model.getFinalFrame());
    model.createShape(r1, 0, 5);
    assertEquals(5, model.getFinalFrame());
    model.createShape(o1, 0, 10);
    assertEquals(10, model.getFinalFrame());
    model.moveShape("r1", 2, 0, 1, 4);
    assertEquals(10, model.getFinalFrame());
  }

  @Test
  public void testGetEventList() {
    model.createShape(r1, 0, 5);
    assertEquals(1, model.getEventList().size());
    model.createShape(o1, 0, 10);
    assertEquals(2, model.getEventList().size());
    model.moveShape("r1", 2, 0, 1, 4);
    assertEquals(3, model.getEventList().size());
    String expected = "Shape r1 moves from (0.0,0.0) to (2.0,0.0) from t=1 to t=4";
    assertEquals(expected, model.getEventList().get(2).toString());
  }

  @Test
  public void testSetAndGetBounds() {
    model.setBounds(20, 250, 260, 360);
    assertEquals(20, model.getBounds()[0]);
    assertEquals(250, model.getBounds()[1]);
    assertEquals(260, model.getBounds()[2]);
    assertEquals(360, model.getBounds()[3]);
  }

  @Test
  public void testGetShapeList() {
    model.createShape(r1, 0, 2);
    model.createShape(o1, 0, 2);
    assertEquals(2, model.getShapeList().size());
    assertEquals("Rectangle", model.getShapeList().get(0).getType().getTypeTxt());
    assertEquals("Oval", model.getShapeList().get(1).getType().getTypeTxt());
  }

  @Test
  public void testGetShapeListIsCopy() {
    model.createShape(r1, 0, 2);
    model.createShape(o1, 0, 2);
    List<AnimatorShape> modify = model.getShapeList();
    assertEquals(2, modify.size());
    modify.add(r1);
    assertEquals(3, modify.size());
    assertEquals(2, model.getShapeList().size());
  }

  @Test
  public void createShape() {
    model.createShape(r1, 0, 2);
    String expected = "Shape created:\nName: r1\nType: Rectangle\nMin corner: (0.0,0.0), "
            + "Width: 25.0, Height: 30.0, Color: (0.0,0.0,0.0)\nAppears at t=0\nDisappears at t=2";
    assertEquals(expected, model.getEventList().get(0).toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void createShapeNameException() {
    model.createShape(r1, 0, 2);
    AnimatorShape shape = new AnimatorShapeImpl("r1", ShapeTypes.Oval,
            25.00, 25.00, Color.YELLOW, 5, 10);
    model.createShape(shape, 0, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createShapeNullException() {
    model.createShape(null, 0, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void createShapeTimeException() {
    model.createShape(r1, -1, 2);
  }

  @Test
  public void moveShape() {
    model.createShape(r1, 0 , 3);
    model.moveShape("r1", 2, 0, 1, 2);
    double x = model.getShapeList().get(0).getReferencePoint().getXCoordinate();
    double y = model.getShapeList().get(0).getReferencePoint().getYCoordinate();
    assertEquals(2, x, .01);
    assertEquals(0, y, .01);
    assertEquals(2, model.getEventsAtFrame(1).size());
    model.moveShape("r1", 2, 2, 2, 3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void moveShapeTimeException() {
    model.createShape(r1, 0, 2);
    model.moveShape("r1", 2, 0, 2, 1);
  }

  @Test(expected = NoSuchElementException.class)
  public void moveShapeNoName() {
    model.createShape(r1, 0 , 2);
    model.moveShape("o1", 2, 0, 2, 3);
  }

  @Test(expected = IllegalStateException.class)
  public void moveShapeInvalid() {
    model.createShape(r1, 0 , 2);
    model.moveShape("r1", 2, 0, 1, 3);
    model.moveShape("r1", 5, 0, 2, 5);
  }

  @Test(expected = IllegalStateException.class)
  public void moveShapeDisappeared() {
    model.createShape(r1, 0 , 2);
    model.moveShape("r1", 2, 0, 2, 3);
  }

  @Test
  public void scaleShape() {
    model.createShape(r1, 2, 6);
    model.scaleShape("r1", 50, 30, 2, 5);
    double x = model.getShapeList().get(0).getXSpace();
    double y = model.getShapeList().get(0).getYSpace();
    assertEquals(50, x, .01);
    assertEquals(30, y, .01);
    x = model.getShapeList().get(0).getReferencePoint().getXCoordinate();
    assertEquals(0.00, x, .01); // reference should not have changed
    model.scaleShape("r1", 30, 30, 5, 6);
  }

  @Test(expected = IllegalStateException.class)
  public void scaleShapeAlreadyScaled() {
    model.createShape(r1, 0, 5);
    model.scaleShape("r1", 100, 250, 2, 5);
    model.scaleShape("r1", 200, 250, 0, 3);
  }

  @Test(expected = NoSuchElementException.class)
  public void scaleShapeNoName() {
    model.createShape(r1, 0 , 2);
    model.createShape(o1, 5, 100);
    model.scaleShape("rectangle", 25, 30, 0, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void scaleShapeNegativeTime() {
    model.createShape(o1, 5, 100);
    model.scaleShape("o1", 18, 30, 50, -12);
  }

  @Test(expected = IllegalArgumentException.class)
  public void scaleShapeInvalidEnd() {
    model.createShape(o1, 5, 100);
    model.scaleShape("o1", 18, 30, 50, 12);
  }

  @Test
  public void changeColor() {
    model.createShape(o1, 5, 100);
    model.createShape(r1, 5, 100);
    assertEquals(Color.YELLOW, model.getShapeList().get(0).getColor());
    assertEquals(Color.BLACK, model.getShapeList().get(1).getColor());
    model.changeColor("o1", Color.BLACK, 6, 7);
    assertEquals(Color.BLACK, model.getShapeList().get(0).getColor());
    assertEquals(Color.BLACK, model.getShapeList().get(1).getColor());
    model.changeColor("o1", Color.GREEN, 7, 10);
  }

  @Test(expected = NoSuchElementException.class)
  public void changeColorInvalidName() {
    model.createShape(o1, 5, 100);
    model.changeColor("o2", Color.BLACK, 6, 7);
  }

  @Test(expected = IllegalStateException.class)
  public void changeColorInvalidRecolorTime() {
    model.createShape(o1, 5, 100);
    model.changeColor("o1", Color.BLACK, 6, 8);
    model.changeColor("o1", Color.YELLOW, 7, 8);
  }

  @Test(expected = IllegalArgumentException.class)
  public void changeColorInvalidTime() {
    model.createShape(o1, 5, 100);
    model.changeColor("o1", Color.YELLOW, 6, -8);
  }

  @Test(expected = IllegalArgumentException.class)
  public void changeColorInvalidColor() {
    model.createShape(o1, 5, 100);
    model.changeColor("o1", Color.YELLOW, 6, 8);
  }

  @Test
  public void testGetEventsAtFrame() {
    model.createShape(o1, 5, 100);
    model.createShape(r1, 6, 25);
    assertEquals(2, model.getEventsAtFrame(6).size());
    model.changeColor("o1", Color.GREEN, 25, 80);
    // below should be the first two still
    assertEquals(2, model.getEventsAtFrame(6).size());
    // r1 has disappeared so it should not show up in list at this frame
    assertEquals(2, model.getEventsAtFrame(25).size());
    AnimatorShapeImpl r2 = new AnimatorShapeImpl("R", ShapeTypes.Rectangle,
            0.0, 2.0, Color.RED, 1, 2);
    model.createShape(r2, 24, 26);
    assertEquals(3, model.getEventsAtFrame(25).size());
  }

  @Test
  public void testGetShapeAtFrame() {
    model.createShape(o1, 5, 100);
    model.createShape(r1, 6, 25);
    assertEquals(1, model.getShapesAtFrame(5).size());
    assertEquals(2, model.getShapesAtFrame(6).size());
    assertEquals(o1.toString(), model.getShapesAtFrame(5).get(0).toString());
    assertEquals(r1.toString(), model.getShapesAtFrame(6).get(1).toString());

    model.moveShape("o1", 25, 35, 25, 27);
    assertEquals(1, model.getShapesAtFrame(25).size());
    assertEquals(1, model.getShapesAtFrame(26).size());
    assertEquals(30,
            model.getShapesAtFrame(26).get(0).getReferencePoint().getYCoordinate(),
            .01);
    assertEquals(35,
            model.getShapesAtFrame(27).get(0).getReferencePoint().getYCoordinate(),
            .01);

    model.scaleShape("o1", 10, 12, 25, 27);
    assertEquals(1, model.getShapesAtFrame(26).size());
    assertEquals(8,
            model.getShapesAtFrame(26).get(0).getXSpace(),
            .01);
    assertEquals(11,
            model.getShapesAtFrame(26).get(0).getYSpace(),
            .01);

    AnimatorShapeImpl r2 = new AnimatorShapeImpl("R", ShapeTypes.Rectangle,
            0.0, 2.0, Color.RED, 1, 2);
    model.createShape(r2, 20, 26);
    assertEquals(2, model.getShapesAtFrame(25).size());
    model.changeColor("R", Color.BLUE, 20, 25);
    model.moveShape("R", 2, 2, 20, 25);
    assertEquals(r2.getName(),
            model.getShapesAtFrame(23).get(2).getName());
    assertEquals(model.getShapesAtFrame(23).get(2).getColor().toString(),
            new Color(0.4F, 0F, 0.6F).toString());
    assertEquals(1,
            model.getShapesAtFrame(23).get(2).getReferencePoint().getXCoordinate(),
            .01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetEventsAtFrameException() {
    model.createShape(o1, 5, 100);
    model.createShape(r1, 6, 25);
    model.getEventsAtFrame(-1);
  }
}