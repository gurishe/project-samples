import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

import cs5004.animator.model.EasyAnimatorModel;
import cs5004.animator.model.EasyAnimatorModelImpl;

import static org.junit.Assert.assertEquals;

/**
 * This is the test class used to test the AnimationBuilder correctly formats the model that it
 * returns when build is called.
 */
public class AnimatorBuilderTest {
  private EasyAnimatorModelImpl.AnimatorBuilder builder;

  @Before
  public void setUp() {
    builder = new EasyAnimatorModelImpl.AnimatorBuilder();
  }

  @Test
  public void testAnimatorBuilderBounds() {
    builder.setBounds(5, 10, 15, 20);
    EasyAnimatorModel model = builder.build();
    assertEquals(5, model.getBounds()[0]);
    assertEquals(10, model.getBounds()[1]);
    assertEquals(15, model.getBounds()[2]);
    assertEquals(20, model.getBounds()[3]);
  }

  @Test
  public void testAnimatorBuilder() {
    builder.declareShape("r1", "rectangle");
    builder.declareShape("o1", "ellipse");
    builder.declareShape("rect", "rectangle");
    builder.addMotion("r1", 0, 0, 0, 25, 30,  0, 0, 0,
            50, 25, 25, 25, 35, 0, 255, 0);
    builder.addMotion("o1", 25, 25, 25, 5, 10, 255, 255, 0,
            50, 25, 25, 10, 10, 255, 255, 0);
    EasyAnimatorModel model = builder.build();
    assertEquals(2, model.getShapeList().size());
    assertEquals("r1", model.getShapeList().get(0).getName());
    assertEquals("o1", model.getShapeList().get(1).getName());
    assertEquals(Color.GREEN, model.getShapeList().get(0).getColor());
    assertEquals(Color.YELLOW, model.getShapeList().get(1).getColor());
    assertEquals(35, model.getShapeList().get(0).getYSpace(), .01);
    assertEquals(10, model.getShapeList().get(1).getXSpace(), .01);
    assertEquals(25, model.getShapeList().get(0).getReferencePoint().getXCoordinate(),
            .01);
    assertEquals(25, model.getShapeList().get(0).getReferencePoint().getYCoordinate(),
            .01);
  }
}
