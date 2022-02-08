import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

import cs5004.animator.model.animatorshapes.AnimatorShapeImpl;
import cs5004.animator.model.animatorshapes.ShapeTypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This is the test class for the AnimatorShape interface and its implementation.
 */
public class AnimatorShapeTest {
  private AnimatorShapeImpl rect1;
  private AnimatorShapeImpl oval1;

  @Before
  public void setUp() {
    rect1 = new AnimatorShapeImpl("rect01", ShapeTypes.Rectangle,
            0.00, 0.00, Color.BLACK, 10, 5);
    oval1 = new AnimatorShapeImpl("222", ShapeTypes.Oval,
            250.203, 750.08, Color.GREEN, 25, 50);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorException() {
    AnimatorShapeImpl oval2 = new AnimatorShapeImpl("", ShapeTypes.Oval,
            0, 0, Color.GREEN, -2, 4);
  }

  @Test
  public void testGetColor() {
    assertEquals(Color.BLACK, rect1.getColor());
    assertEquals(Color.GREEN, oval1.getColor());
  }

  @Test
  public void testGetName() {
    assertEquals("rect01", rect1.getName());
  }

  @Test
  public void testGetType() {
    assertEquals("Rectangle", rect1.getType().getTypeTxt());
    assertEquals("Oval", oval1.getType().getTypeTxt());
  }

  @Test
  public void testGetXValue() {
    assertEquals(10.00, rect1.getXSpace(), .01);
  }

  @Test
  public void testGetYValue() {
    assertEquals(50, oval1.getYSpace(), .01);
  }

  @Test
  public void testGetReferencePoint() {
    assertEquals("(0.0,0.0)", rect1.getReferencePoint().toString());
  }

  @Test
  public void testDescription() {
    String expected = "Name: rect01\nType: Rectangle\nMin corner: (0.0,0.0), Width: 10.0, "
            + "Height: 5.0, Color: (0.0,0.0,0.0)";
    assertEquals(expected, rect1.description(false));
    expected = "Name: 222\nType: Oval\nCenter: (250.2,750.1), X radius: 25.0, "
            + "Y radius: 50.0, Color: (0.0,1.0,0.0)";
    assertEquals(expected, oval1.description(false));
  }

  @Test
  public void testDescriptionDimensions() {
    String expected = "Width: 10.0, Height: 5.0";
    assertEquals(expected, rect1.description(true));
    expected = "X radius: 25.0, Y radius: 50.0";
    assertEquals(expected, oval1.description(true));
  }

  @Test
  public void testMove() {
    AnimatorShapeImpl rect2 = rect1.move(2.00, 0.5);
    assertEquals("(2.0,0.5)", rect2.getReferencePoint().toString());
    assertNotEquals(rect2.description(false),
            rect1.description(false));
  }

  @Test
  public void testScaleSize() {
    AnimatorShapeImpl rect2 = rect1.scaleSize(10, 5);
    assertEquals(10.0, rect2.getXSpace(), .01);
    AnimatorShapeImpl rect3 = rect2.scaleSize(20, 2.5);
    assertEquals(2.5, rect3.getYSpace(), .01);
    assertEquals(10, rect1.getXSpace(), .01); // make sure original is the same

    AnimatorShapeImpl oval2 = oval1.scaleSize(125, 50);
    assertEquals(125, oval2.getXSpace(), .01);
    assertEquals(50, oval2.getYSpace(), .01); // make sure vertical is unchanged
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScaleSizeExceptionZero() {
    AnimatorShapeImpl rect2 = rect1.scaleSize(0, 25);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testScaleSizeExceptionNegative() {
    AnimatorShapeImpl oval2 = oval1.scaleSize(-.5, 25);
  }

  @Test
  public void testChangeColor() {
    AnimatorShapeImpl rect2 = rect1.changeColor(Color.WHITE);
    assertEquals(rect2.getColor(), Color.WHITE);
    assertEquals(rect1.getColor(), Color.BLACK); // make sure original is the same
  }

  @Test(expected = IllegalArgumentException.class)
  public void testChangeColorException() {
    AnimatorShapeImpl rect2 = rect1.changeColor(Color.BLACK);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testChangeColorExceptionOval() {
    AnimatorShapeImpl oval2 = oval1.changeColor(Color.GREEN);
  }

  @Test
  public void testDeepCopy() {
    AnimatorShapeImpl rect2 = rect1.deepCopy();
    assertNotEquals(rect2, rect1);
    assertEquals(rect1, rect1);

    AnimatorShapeImpl oval2 = oval1.deepCopy();
    assertNotEquals(oval2, oval1);
  }
}