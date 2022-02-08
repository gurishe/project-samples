import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.Before;

import java.awt.Color;
import java.util.List;

import cs5004.animator.controller.EasyAnimatorController;
import cs5004.animator.controller.EasyAnimatorControllerImpl;
import cs5004.animator.model.EasyAnimatorModel;
import cs5004.animator.model.EasyAnimatorModelImpl;
import cs5004.animator.model.animatorshapes.AnimatorShape;
import cs5004.animator.model.animatorshapes.AnimatorShapeImpl;
import cs5004.animator.model.animatorshapes.ShapeTypes;
import cs5004.animator.view.EasyAnimatorSVGView;
import cs5004.animator.view.EasyAnimatorTextView;
import cs5004.animator.view.EasyAnimatorView;

/**
 * This is the test class for our EasyAnimatorController and its associated methods. It also tests
 * the AnimationFrameHandler which is an action listener used by the controller for both visual
 * views.
 */
public class EasyAnimatorControllerTest {
  private EasyAnimatorModel model;

  @Before
  public void setUp() {
    model = new EasyAnimatorModelImpl();
    model.setBounds(200, 70, 360, 360);
    AnimatorShape r1 = new AnimatorShapeImpl("r1", ShapeTypes.Rectangle, 0.0,
            0.0, Color.BLACK, 25, 30);
    AnimatorShape o1 = new AnimatorShapeImpl("o1", ShapeTypes.Ellipse, 25.00,
            25.00, Color.YELLOW, 5, 10);
    model.createShape(r1, 0, 100);
    model.createShape(o1, 0, 25);
    model.changeColor("o1", Color.GREEN, 15, 25);
    model.moveShape("r1", 2, 0, 10, 75);
    model.scaleShape("o1", 5, 15, 24, 25);
  }

  @Test
  public void testBeginText() {
    StringBuilder builder = new StringBuilder();
    EasyAnimatorView text = new EasyAnimatorTextView(model, builder, "out");
    EasyAnimatorController controller = new EasyAnimatorControllerImpl(text, model, 1);
    controller.begin(); // controller should call renderView

    StringBuilder builder2 = new StringBuilder();
    EasyAnimatorView text2 = new EasyAnimatorTextView(model, builder2, "out");
    text2.renderView(); // should match controller output
    assertEquals(builder2.toString(), builder.toString().stripTrailing());
  }

  @Test
  public void testBeginSVG() {
    EasyAnimatorModel model2 = new EasyAnimatorModelImpl();
    StringBuilder builder = new StringBuilder();
    EasyAnimatorSVGView view2 = new EasyAnimatorSVGView(model2, builder, 50, "out");
    EasyAnimatorController controller = new EasyAnimatorControllerImpl(view2, model2, 50);
    controller.begin();
    String expected = "<svg width=\"0\" height=\"0\" viewBox=\"0 0 0 0\" version=\"1.1\" "
            + "xmlns=\"http://www.w3.org/2000/svg\">\n\n</svg>";
    assertEquals(expected, builder.toString());
  }

  @Test
  public void testHandlePlayButtons() {
    StringBuilder builder = new StringBuilder();
    EasyAnimatorView mockView = new MockView(builder);
    EasyAnimatorController controller = new EasyAnimatorControllerImpl(mockView, model, 1);
    controller.handleButton("start"); // should start our timer
    try {
      Thread.sleep(100); // wait for timer
    } catch (Exception e) {
      System.out.println("Failed sleep");
    }
    controller.handleButton("pause"); // should stop our timer
    String expected = "Frame set\nRender\nRefresh\n";
    assertEquals(expected, builder.toString());

    controller.handleButton("restart"); // should begin our timer again
    try {
      Thread.sleep(1000); // wait for timer
    } catch (Exception e) {
      System.out.println("Failed sleep");
    }
    controller.handleButton("pause");
    assertEquals(expected + expected, builder.toString());
  }

  @Test
  public void testHandleButtonsLoopOff() {
    EasyAnimatorModel model2 = new EasyAnimatorModelImpl();
    model2.setBounds(200, 70, 360, 360);
    AnimatorShape r1 = new AnimatorShapeImpl("r1", ShapeTypes.Rectangle, 0.0,
            0.0, Color.BLACK, 25, 30);
    model2.createShape(r1, 0, 5);
    StringBuilder builder = new StringBuilder();
    EasyAnimatorView mockView = new MockView(builder);
    EasyAnimatorController controller = new EasyAnimatorControllerImpl(mockView, model2, 1);

    controller.handleButton("start");
    try {
      Thread.sleep(7000); // wait for timer
    } catch (Exception e) {
      System.out.println("Failed sleep");
    }
    controller.handleButton("pause"); // should stop our timer
    String expected = "Frame set\nRender\nRefresh\n";
    String noFrame = "No frame\nRender\nRefresh\n"; // indicates no looping
    assertEquals(expected + expected + expected + expected + expected + noFrame + noFrame,
            builder.toString());
  }

  @Test
  public void testHandleButtonsLoopOn() {
    EasyAnimatorModel model2 = new EasyAnimatorModelImpl();
    model2.setBounds(200, 70, 360, 360);
    AnimatorShape r1 = new AnimatorShapeImpl("r1", ShapeTypes.Rectangle, 0.0,
            0.0, Color.BLACK, 25, 30);
    model2.createShape(r1, 0, 5);
    StringBuilder builder = new StringBuilder();
    EasyAnimatorView mockView = new MockView(builder);
    EasyAnimatorController controller = new EasyAnimatorControllerImpl(mockView, model2, 1);

    controller.handleButton("loop"); // animation should restart at end
    controller.handleButton("start");
    try {
      Thread.sleep(7000); // wait for timer
    } catch (Exception e) {
      System.out.println("Failed sleep");
    }
    controller.handleButton("pause"); // should stop our timer
    String expected = "Frame set\nRender\nRefresh\n";
    String noFrame = "No frame\nRender\nRefresh\n";
    assertEquals(expected + expected + expected + expected + expected + noFrame + expected,
            builder.toString());
  }

  @Test
  public void testHandleButtonsIncrease() {
    StringBuilder builder = new StringBuilder();
    EasyAnimatorView mockView = new MockView(builder);
    EasyAnimatorController controller = new EasyAnimatorControllerImpl(mockView, model, 1);

    // total increase by 20 ms, should be noticeable increase of frames from original test
    controller.handleButton("increase");
    controller.handleButton("increase");
    controller.handleButton("increase");
    controller.handleButton("increase");
    controller.handleButton("increase");
    controller.handleButton("increase");
    controller.handleButton("increase");
    controller.handleButton("increase");
    controller.handleButton("increase");
    controller.handleButton("increase");

    try {
      Thread.sleep(2000); // wait for timer
    } catch (Exception e) {
      System.out.println("Failed sleep");
    }
    controller.handleButton("pause"); // should stop our timer
    String expected = "Frame set\nRender\nRefresh\n";
    assertEquals(expected + expected + expected, builder.toString());
  }

  @Test
  public void testHandleButtonsDecrease() {
    StringBuilder builder = new StringBuilder();
    EasyAnimatorView mockView = new MockView(builder);
    EasyAnimatorController controller = new EasyAnimatorControllerImpl(mockView, model, 1);

    // total decrease by 20 ms, should be noticeable loss of 1 frame from previous test
    controller.handleButton("decrease");
    controller.handleButton("decrease");
    controller.handleButton("decrease");
    controller.handleButton("decrease");
    controller.handleButton("decrease");
    controller.handleButton("decrease");
    controller.handleButton("decrease");
    controller.handleButton("decrease");
    controller.handleButton("decrease");
    controller.handleButton("decrease");

    try {
      Thread.sleep(2000); // wait for timer
    } catch (Exception e) {
      System.out.println("Failed sleep");
    }
    controller.handleButton("pause"); // should stop our timer
    String expected = "Frame set\nRender\nRefresh\n";
    assertEquals(expected + expected, builder.toString());
  }

  /**
   * This is a mock view for testing that simply prints appends to the builder used to create it
   * whenever the given method is invoked.
   */
  public static class MockView implements EasyAnimatorView {
    private StringBuilder builder;

    public MockView(StringBuilder builder) {
      this.builder = builder;
    }

    @Override
    public void renderView() {
      builder.append("Render\n");
    }

    @Override
    public void setFrame(List<AnimatorShape> shapeList) {
      if (shapeList.size() != 0) {
        builder.append("Frame set\n");
      } else {
        builder.append("No frame\n");
      }
    }

    @Override
    public void refresh() {
      builder.append("Refresh\n");
    }
  }
}