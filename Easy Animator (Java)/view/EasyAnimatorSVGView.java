package cs5004.animator.view;

import java.awt.Color;
import java.io.FileWriter;
import java.util.List;

import cs5004.animator.model.animatorshapes.AnimatorShape;
import cs5004.animator.model.AnimatorEvent;
import cs5004.animator.model.EasyAnimatorModelImpl;
import cs5004.animator.model.ReadOnlyEasyAnimatorModel;

/**
 * This is the SVG view of our EasyAnimator which takes all the shapes and events in our model and
 * converts each of them to an SVG displayable format.
 */
public class EasyAnimatorSVGView implements EasyAnimatorView {
  private ReadOnlyEasyAnimatorModel model;
  private Appendable append;
  private int speed;
  private String output;

  /**
   * Constructs the SVG view with a model, and appendable to add the SVG formatted text to, and
   * a speed so the SVG knows how fast to play the animation.
   * @param model is a Read-Only EasyAnimatorModel from which to read the Shapes and Events.
   * @param out is an Appendable that the formatted SVG text will be added to.
   * @param speed is the speed at which the SVG should play.
   * @param output is the desired output of the view (a file or System.out).
   */
  public EasyAnimatorSVGView(ReadOnlyEasyAnimatorModel model, Appendable out, int speed,
                             String output) {
    this.model = model;
    this.append = out;
    this.speed = speed;
    this.output = output;
  }

  /**
   * A simple calculation turning a time into milliseconds for SVG formatting.
   *
   * @param time is the start time of an event.
   * @return the time in milliseconds.
   */
  private int calcSVGTime(int time) {
    return time * (1000 / speed);
  }

  /**
   * A simple calculation turning two times into a duration in milliseconds for SVG formatting.
   *
   * @param start is the start time of an event.
   * @param end   is the end time of an event.
   * @return the duration of the event in milliseconds.
   */
  private int calcSVGTime(int start, int end) {
    int begin = start * (1000 / speed);
    int finish = end * (1000 / speed);
    return finish - begin;
  }

  /**
   * This formats an AnimatorEvent where a shape is created into the appropriate SVG string that
   * declares a new shape.
   *
   * @param event is the event where the Shape is created.
   * @return the AnimatorEvent as a string, but formatted for an SVG output.
   */
  private String declareShape(AnimatorEvent event) {
    String declaration = "";
    int x = (int) event.getStartShape().getReferencePoint().getXCoordinate();
    int y = (int) event.getStartShape().getReferencePoint().getYCoordinate();
    int width = (int) event.getStartShape().getXSpace();
    int height = (int) event.getStartShape().getYSpace();
    Color color = event.getStartShape().getColor();
    String colorStr = "rgb(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue()
            + ")";
    if (event.getStartShape().getType().getTypeTxt().equals("Rectangle")) {
      declaration += "<rect id=\"" + event.getStartShape().getName() + "\" x=\"" + x + "\" y=\""
              + y + "\" width=\"" + width + "\" height=\"" + height + "\" fill=\"" + colorStr
              + "\" visibility=\"visible\" >\n";
    } else if (event.getStartShape().getType().getTypeTxt().equals("Ellipse")) {
      declaration += "<ellipse id=\"" + event.getStartShape().getName() + "\" cx=\"" + x
              + "\" cy=\"" + y + "\" rx=\"" + width + "\" ry=\"" + height + "\" fill=\"" + colorStr
              + "\" visibility=\"visible\" >\n";
    }
    return declaration;
  }

  /**
   * This formats all the AnimatorEvents for a rectangle into the matching SVG output.
   *
   * @param name is the name of the Shape whose events we want to format to an SVG.
   * @return all of the AnimatorEvents that occurred for a Rectangle formatted as an SVG.
   */
  private String formatRect(String name) {
    EasyAnimatorModelImpl castModel = (EasyAnimatorModelImpl) model;
    List<AnimatorEvent> listOfEvents = castModel.getEventsForShape(name);
    String shapeDeclared = "";
    String formattedShape = "";
    for (AnimatorEvent event : listOfEvents) {
      String formatStart = calcSVGTime(event.getStartTime()) + "ms";
      String formatDur = calcSVGTime(event.getStartTime(), event.getEndTime()) + "ms";
      if (event.getStartShape().equals(event.getEndShape())) {
        shapeDeclared = declareShape(event);
      } else if (event.getStartShape().getXSpace() != event.getEndShape().getXSpace()
              || event.getStartShape().getYSpace() != event.getEndShape().getYSpace()) {
        int startX = (int) event.getStartShape().getXSpace();
        int startY = (int) event.getStartShape().getYSpace();
        int endX = (int) event.getEndShape().getXSpace();
        int endY = (int) event.getEndShape().getYSpace();
        if (startX != endX) {
          formattedShape += "<animate attributeType=\"xml\" begin=\"" + formatStart + "\" dur=\""
                  + formatDur + "\" attributeName=\"width\" from=\""
                  + event.getStartShape().getXSpace() + "\" to=\"" + event.getEndShape().getXSpace()
                  + "\" fill=\"freeze\" />\n";
        }
        if (startY != endY) {
          formattedShape += "<animate attributeType=\"xml\" begin=\"" + formatStart + "\" dur=\""
                  + formatDur + "\" attributeName=\"height\" from=\""
                  + event.getStartShape().getXSpace() + "\" to=\"" + event.getEndShape().getXSpace()
                  + "\" fill=\"freeze\" />\n";
        }
      } else if (event.getStartShape().getColor() != event.getEndShape().getColor()) {
        Color startColor = event.getStartShape().getColor();
        Color endColor = event.getEndShape().getColor();
        String colorStr1 = "rgb(" + startColor.getRed() + "," + startColor.getGreen() + ","
                + startColor.getBlue() + ")";
        String colorStr2 = "rgb(" + endColor.getRed() + "," + endColor.getGreen() + ","
                + endColor.getBlue() + ")";
        formattedShape += "<animate attributeType=\"xml\" begin=\"" + formatStart + "\" dur=\""
                + formatDur + "\" attributeName=\"fill\" from=\""
                + colorStr1 + "\" to=\"" + colorStr2 + "\" />\n";
      } else if (event.getStartShape().getReferencePoint().getXCoordinate()
              != event.getEndShape().getReferencePoint().getXCoordinate()
              || event.getStartShape().getReferencePoint().getYCoordinate()
              != event.getEndShape().getReferencePoint().getYCoordinate()) {
        int startX = (int) event.getStartShape().getReferencePoint().getXCoordinate();
        int startY = (int) event.getStartShape().getReferencePoint().getYCoordinate();
        int endX = (int) event.getEndShape().getReferencePoint().getXCoordinate();
        int endY = (int) event.getEndShape().getReferencePoint().getYCoordinate();
        if (startX != endX) {
          formattedShape += "<animate attributeType=\"xml\" begin=\"" + formatStart + "\" dur=\""
                  + formatDur + "\" attributeName=\"x\" from=\""
                  + startX + "\" to=\"" + endX + "\" fill=\"freeze\" />\n";
        }
        if (startY != endY) {
          formattedShape += "<animate attributeType=\"xml\" begin=\"" + formatStart + "\" dur=\""
                  + formatDur + "\" attributeName=\"y\" from=\""
                  + startY + "\" to=\"" + endY + "\" fill=\"freeze\" />\n";
        }
      }
    }
    return shapeDeclared + formattedShape + "</rect>\n\n";
  }

  /**
   * This formats all the AnimatorEvents for an Ellipse into the matching SVG output.
   *
   * @param name is the name of the Shape whose events we want to format to an SVG.
   * @return all of the AnimatorEvents that occurred for an Ellipse formatted as an SVG.
   */
  private String formatEllipse(String name) {
    EasyAnimatorModelImpl castModel = (EasyAnimatorModelImpl) model;
    List<AnimatorEvent> listOfEvents = castModel.getEventsForShape(name);
    String shapeDeclared = "";
    String formattedShape = "";
    for (AnimatorEvent event : listOfEvents) {
      String formatStart = calcSVGTime(event.getStartTime()) + "ms";
      String formatDur = calcSVGTime(event.getStartTime(), event.getEndTime()) + "ms";
      // Creation event always holds the same shape for start and end
      if (event.getStartShape().equals(event.getEndShape())) {
        shapeDeclared = declareShape(event);
      } else if (event.getStartShape().getXSpace() != event.getEndShape().getXSpace()
              || event.getStartShape().getYSpace() != event.getEndShape().getYSpace()) {
        int startX = (int) event.getStartShape().getXSpace();
        int startY = (int) event.getStartShape().getYSpace();
        int endX = (int) event.getEndShape().getXSpace();
        int endY = (int) event.getEndShape().getYSpace();
        if (startX != endX) {
          formattedShape += "<animate attributeType=\"xml\" begin=\"" + formatStart + "\" dur=\""
                  + formatDur + "\" attributeName=\"rx\" from=\""
                  + event.getStartShape().getXSpace() + "\" to=\"" + event.getEndShape().getXSpace()
                  + "\" fill=\"freeze\" />\n";
        }
        if (startY != endY) {
          formattedShape += "<animate attributeType=\"xml\" begin=\"" + formatStart + "\" dur=\""
                  + formatDur + "\" attributeName=\"ry\" from=\""
                  + event.getStartShape().getXSpace() + "\" to=\"" + event.getEndShape().getXSpace()
                  + "\" fill=\"freeze\" />\n";
        }
      } else if (event.getStartShape().getColor() != event.getEndShape().getColor()) {
        Color startColor = event.getStartShape().getColor();
        Color endColor = event.getEndShape().getColor();
        String colorStr1 = "rgb(" + startColor.getRed() + "," + startColor.getGreen() + ","
                + startColor.getBlue() + ")";
        String colorStr2 = "rgb(" + endColor.getRed() + "," + endColor.getGreen() + ","
                + endColor.getBlue() + ")";
        formattedShape += "<animate attributeType=\"xml\" begin=\"" + formatStart + "\" dur=\""
                + formatDur + "\" attributeName=\"fill\" from=\""
                + colorStr1 + "\" to=\"" + colorStr2 + "\" />\n";
      } else if (event.getStartShape().getReferencePoint().getXCoordinate()
              != event.getEndShape().getReferencePoint().getXCoordinate()
              || event.getStartShape().getReferencePoint().getYCoordinate()
              != event.getEndShape().getReferencePoint().getYCoordinate()) {
        int startX = (int) event.getStartShape().getReferencePoint().getXCoordinate();
        int startY = (int) event.getStartShape().getReferencePoint().getYCoordinate();
        int endX = (int) event.getEndShape().getReferencePoint().getXCoordinate();
        int endY = (int) event.getEndShape().getReferencePoint().getYCoordinate();
        if (startX != endX) {
          formattedShape += "<animate attributeType=\"xml\" begin=\"" + formatStart + "\" dur=\""
                  + formatDur + "\" attributeName=\"cx\" from=\""
                  + startX + "\" to=\"" + endX + "\" fill=\"freeze\" />\n";
        }
        if (startY != endY) {
          formattedShape += "<animate attributeType=\"xml\" begin=\"" + formatStart + "\" dur=\""
                  + formatDur + "\" attributeName=\"cy\" from=\""
                  + startY + "\" to=\"" + endY + "\" fill=\"freeze\" />\n";
        }
      }
    }
    return shapeDeclared + formattedShape + "</ellipse>\n\n";
  }

  /**
   * This takes our model and builds it out into proper SVG format for by setting its header and
   * formatting each shape into an SVG output.
   *
   * @return a String which represents a fully formatted SVG build.
   */
  private String buildSVG() {
    StringBuilder svgBuilder = new StringBuilder();
    int width = model.getBounds()[2] - model.getBounds()[0];
    int height = model.getBounds()[3] - model.getBounds()[1];
    String header = "<svg width=\"" + width + "\" height=\"" + height + "\" viewBox=\""
            + model.getBounds()[0] + " " + model.getBounds()[1] + " "
            + model.getBounds()[2] + " " + model.getBounds()[3]
            + "\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\n\n";
    svgBuilder.append(header);

    // format each Shape correctly in SVG and add it
    List<AnimatorShape> shapeList = model.getShapeList();
    for (AnimatorShape shape : shapeList) {
      if (shape.getType().getTypeTxt().equals("Rectangle")) {
        String svgRect = formatRect(shape.getName());
        svgBuilder.append(svgRect);
      } else {
        String svgEllipse = formatEllipse(shape.getName());
        svgBuilder.append(svgEllipse);
      }
    }
    // end our SVG doc
    svgBuilder.append("</svg>");
    return svgBuilder.toString();
  }

  @Override
  public void renderView() {
    String svg = buildSVG();
    try {
      append.append(svg);
      if (output.equals("out")) {
        System.out.println(append.toString());
      } else {
        FileWriter writer = new FileWriter(output);
        writer.write(append.toString());
        writer.close();
      }
    } catch (Exception e) {
      System.out.println("Failed to output SVG view");
    }
  }

  /**
   * This does nothing in the SVG format.
   *
   * @param shapeList is the list of Shapes it must show.
   * @throws UnsupportedOperationException always as this method does nothing in text-based views.
   */
  @Override
  public void setFrame(List<AnimatorShape> shapeList) throws UnsupportedOperationException {
    // This method does nothing in our textual views.
    throw new UnsupportedOperationException("This method is unsupported for text views");
  }

  /**
   * This method does nothing in the SVG format.
   * @throws UnsupportedOperationException always as this method does nothing in text-based views.
   */
  @Override
  public void refresh() throws UnsupportedOperationException {
    // This method does nothing in our textual views.
    throw new UnsupportedOperationException("This method is unsupported for text views");
  }
}
