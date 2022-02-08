import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

import cs5004.animator.view.EasyAnimatorSVGView;
import cs5004.animator.view.EasyAnimatorTextView;
import cs5004.animator.view.EasyAnimatorView;
import cs5004.animator.model.animatorshapes.AnimatorShapeImpl;
import cs5004.animator.model.animatorshapes.ShapeTypes;
import cs5004.animator.model.EasyAnimatorModel;
import cs5004.animator.model.EasyAnimatorModelImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This is the test class for our EasyAnimatorView interface and each implementing class. We test
 * the output of the text and SVG views to see if they match what is expected.
 */
public class EasyAnimatorViewTest {
  private EasyAnimatorModel model;
  private AnimatorShapeImpl r1;
  private AnimatorShapeImpl o1;


  @Before
  public void setUp() {
    model = new EasyAnimatorModelImpl();
    model.setBounds(200, 70, 360, 360);
    r1 = new AnimatorShapeImpl("r1", ShapeTypes.Rectangle,0.0, 0.0,
            Color.BLACK, 25, 30);
    o1 = new AnimatorShapeImpl("o1", ShapeTypes.Ellipse, 25.00, 25.00,
            Color.YELLOW, 5, 10);
    model.createShape(r1, 5, 100);
    model.createShape(o1, 0, 25);
    model.changeColor("o1", Color.GREEN, 15, 25);
    model.moveShape("r1", 2, 0, 10, 75);
    model.scaleShape("o1", 5, 15,24, 25);
  }

  @Test
  public void testRenderTextSimple() {
    StringBuilder build = new StringBuilder();
    EasyAnimatorModel model1 = new EasyAnimatorModelImpl();
    r1 = new AnimatorShapeImpl("r2", ShapeTypes.Rectangle,100.0, 0.0,
            Color.GREEN, 25, 30);
    model1.createShape(r1, 0, 100);
    model1.moveShape("r2" , 100, 100, 0, 100);
    model1.createShape(o1, 10, 90);
    EasyAnimatorView text = new EasyAnimatorTextView(model1, build, "out");
    text.renderView();
    assertTrue(build.toString().contains("Shape created:\nName: r2\nType: Rectangle\n"));
    String test1 = "Shape r2 moves from (100.0,0.0) to (100.0,100.0) from t=0 to t=100";
    assertTrue(build.toString().contains(test1));
    assertTrue(build.toString().contains("Name: o1\nType: Ellipse\nCenter: (25.0,25.0)"));
    test1 = "Shape created:\nName: o1\nType: Ellipse\nCenter: (25.0,25.0), X radius: 5.0, Y radius:"
            + " 10.0, Color: (1.0,1.0,0.0)\nAppears at t=10\nDisappears at t=90";
    assertTrue(build.toString().contains(test1));
  }

  @Test
  public void testRenderViewText() {
    StringBuilder build = new StringBuilder();
    EasyAnimatorView text = new EasyAnimatorTextView(model, build, "out");
    text.renderView();
    String expected = "Shape created:\nName: o1\nType: Ellipse\nCenter: (25.0,25.0), X radius: 5.0,"
              + " Y radius: 10.0, Color: (1.0,1.0,0.0)\nAppears at t=0\nDisappears at t=25\n\n"
              + "Shape created:\nName: r1\nType: Rectangle\nMin corner: (0.0,0.0), Width: "
              + "25.0, Height: 30.0, Color: (0.0,0.0,0.0)\nAppears at t=5\nDisappears at t=100\n\n"
              + "Shape r1 moves from (0.0,0.0) to (2.0,0.0) from t=10 to t=75\n\n"
              + "Shape o1 changes color from (1.0,1.0,0.0) to (0.0,1.0,0.0) from t=15 to t=25"
              + "\n\nShape o1 scales from X radius: 5.0, Y radius: 10.0 to X radius: 5.0, Y radius:"
              + " 15.0 from t=24 to t=25";
    assertEquals(expected, build.toString());
  }

  @Test
  public void testRenderSVGSimple() {
    StringBuilder build = new StringBuilder();
    EasyAnimatorModel model1 = new EasyAnimatorModelImpl();
    model1.setBounds(145, 50, 410, 199);
    r1 = new AnimatorShapeImpl("r2", ShapeTypes.Rectangle, 100.0, 0.0,
            Color.GREEN, 25, 30);
    model1.createShape(r1, 0, 100);
    model1.moveShape("r2", 100, 100, 0, 100);
    model1.createShape(o1, 10, 90);
    EasyAnimatorView svg = new EasyAnimatorSVGView(model1, build, 5, "out");
    svg.renderView();
    // shape should have header
    assertTrue(build.toString().contains("<svg width=\"265\" height=\"149\" viewBox="
            + "\"145 50 410 199\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n\n"));
    String test1 = "<rect id=\"r2\" x=\"100\" y=\"0\" width=\"25\" height=\"30\" fill=\""
            + "rgb(0,255,0)\" visibility=\"visible\" >\n"; // rect declaration
    assertTrue(build.toString().contains(test1));
    test1 = "<ellipse id=\"o1\" cx=\"25\" cy=\"25\" rx=\"5\" ry=\"10\" fill=\"rgb(255,255,0)\" "
            + "visibility=\"visible\" >"; // ellipse declaration
    assertTrue(build.toString().contains(test1));
    assertTrue(build.toString().contains("</ellipse>"));
    assertTrue(build.toString().contains("</rect>"));
    assertTrue(build.toString().contains("</svg>"));

    test1 = "<animate attributeType=\"xml\" begin=\"0ms\" dur=\"20000ms\" attributeName=\"y\" "
            + "from=\"0\" to=\"100\" fill=\"freeze\" />"; // animation
    assertTrue(build.toString().contains(test1));
  }

  @Test
  public void testRenderViewSVG() {
    StringBuilder build = new StringBuilder();
    EasyAnimatorView svg = new EasyAnimatorSVGView(model, build, 1, "out");
    svg.renderView();
    String expected = "<svg width=\"160\" height=\"290\" viewBox=\"200 70 360 360\" version=\"1.1\""
            + " xmlns=\"http://www.w3.org/2000/svg\">\n\n"
            + "<rect id=\"r1\" x=\"0\" y=\"0\" width=\"25\" height=\"30\" fill=\"rgb(0,0,0)\" "
            + "visibility=\"visible\" >\n"
            + "<animate attributeType=\"xml\" begin=\"10000ms\" dur=\"65000ms\" attributeName=\"x\""
            + " from=\"0\" to=\"2\" fill=\"freeze\" />\n"
            + "</rect>\n\n"
            + "<ellipse id=\"o1\" cx=\"25\" cy=\"25\" rx=\"5\" ry=\"10\" fill=\"rgb(255,255,0)\" "
            + "visibility=\"visible\" >\n"
            + "<animate attributeType=\"xml\" begin=\"15000ms\" dur=\"10000ms\" "
            + "attributeName=\"fill\" from=\"rgb(255,255,0)\" to=\"rgb(0,255,0)\" />\n"
            + "<animate attributeType=\"xml\" begin=\"24000ms\" dur=\"1000ms\" attributeName=\"ry\""
            + " from=\"5.0\" to=\"5.0\" fill=\"freeze\" />\n"
            + "</ellipse>\n\n"
            + "</svg>";
    assertEquals(expected, build.toString());
  }
}