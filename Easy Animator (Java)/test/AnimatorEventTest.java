import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

import cs5004.animator.model.animatorshapes.AnimatorShapeImpl;
import cs5004.animator.model.animatorshapes.ShapeTypes;
import cs5004.animator.model.AnimatorEvent;
import cs5004.animator.model.AnimatorEventImpl;

import static org.junit.Assert.assertEquals;

/**
 * This is the test class for all of the methods for the AnimatorEvent interface and its
 * implementing class.
 */
public class AnimatorEventTest {
  private AnimatorShapeImpl rect1;
  private AnimatorShapeImpl oval1;

  @Before
  public void setUp() {
    rect1 = new AnimatorShapeImpl("rect01", ShapeTypes.Rectangle,
            0.00, 0.00, Color.BLACK, 10, 5);
    oval1 = new AnimatorShapeImpl("222", ShapeTypes.Oval,
            250.203, 750.08, Color.GREEN, 25, 50);
  }

  @Test
  public void testGetStartTime() {
    AnimatorEvent event = new AnimatorEventImpl(rect1, 0, 5);
    assertEquals(0, event.getStartTime());
    AnimatorShapeImpl oval2 = oval1.changeColor(Color.GRAY);
    event = new AnimatorEventImpl(oval1, oval2, 25, 75);
    assertEquals(25, event.getStartTime());
  }

  @Test
  public void testGetEndTime() {
    AnimatorEvent event = new AnimatorEventImpl(rect1, 0, 5);
    assertEquals(5, event.getEndTime());
    AnimatorShapeImpl oval2 = oval1.scaleSize(25, 30);
    event = new AnimatorEventImpl(oval1, oval2, 25, 67);
    assertEquals(67, event.getEndTime());
  }

  @Test
  public void testGetStartShape() {
    AnimatorEvent event = new AnimatorEventImpl(rect1, 0, 5);
    assertEquals(rect1.getName(), event.getStartShape().getName());
    AnimatorShapeImpl oval2 = oval1.move(2, 3);
    event = new AnimatorEventImpl(oval1, oval2, 15, 16);
    assertEquals(oval1.getName(), event.getStartShape().getName());
  }

  @Test
  public void testGetEndShape() {
    AnimatorEvent event = new AnimatorEventImpl(rect1, 0, 5);
    assertEquals(rect1.getName(), event.getEndShape().getName());
    AnimatorShapeImpl oval2 = oval1.changeColor(Color.GRAY);
    event = new AnimatorEventImpl(oval1, oval2, 25, 75);
    assertEquals(oval1.getName(), event.getEndShape().getName());
  }

  @Test
  public void testToString() {
    String expected = "Shape created:\nName: rect01\nType: Rectangle\nMin corner: (0.0,0.0), "
            + "Width: 10.0, Height: 5.0, Color: (0.0,0.0,0.0)\nAppears at t=0\nDisappears at t=5";
    AnimatorEvent event = new AnimatorEventImpl(rect1, 0, 5);
    assertEquals(expected, event.toString());

    expected = "Shape 222 changes color from (0.0,1.0,0.0) to (1.0,0.0,0.0) from t=25 to t=75";
    AnimatorShapeImpl oval2 = oval1.changeColor(Color.RED);
    event = new AnimatorEventImpl(oval1, oval2, 25, 75);
    assertEquals(expected, event.toString());

    expected = "Shape 222 moves from (250.2,750.1) to (2.0,3.0) from t=15 to t=16";
    oval2 = oval1.move(2, 3);
    event = new AnimatorEventImpl(oval1, oval2, 15, 16);
    assertEquals(expected, event.toString());

    expected = "Shape 222 scales from X radius: 25.0, Y radius: 50.0 to X radius: 60.0, "
            + "Y radius: 55.0 from t=25 to t=67";
    oval2 = oval1.scaleSize(60, 55);
    event = new AnimatorEventImpl(oval1, oval2, 25, 67);
    assertEquals(expected, event.toString());

    expected = "Shape rect01 scales from Width: 10.0, Height: 5.0 to Width: 5.0, Height: 2.5 "
            + "from t=2 to t=3";
    AnimatorShapeImpl rect2 = rect1.scaleSize(5, 2.5);
    event = new AnimatorEventImpl(rect1, rect2, 2, 3);
    assertEquals(expected, event.toString());
  }
}