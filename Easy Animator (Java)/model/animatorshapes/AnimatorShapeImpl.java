package cs5004.animator.model.animatorshapes;

import java.awt.Color;

/**
 * This is the implementation of the AnimatorShape interface. Since there are not any particular
 * things a specific type of AnimatorShape that must perform a unique task at this time, I have
 * changed from individual shape implementations to an Enum that describes the type of Shape.
 */
public class AnimatorShapeImpl implements AnimatorShape {
  private Color color;
  private XYCoordinate referencePoint;
  private String name;
  private double xSpace;
  private double ySpace;
  private ShapeTypes type;

  /**
   * This constructor is the base requirements for creating any Shape which is a name, its type, a
   * reference point, a color, and largest horizontal and vertical space.
   *
   * @param name        is the String that this object is identified as.
   * @param type        is the type of Shape it is (i.e. Rectangle, Oval, etc.);
   * @param xCoordinate is the x-coordinate of the (x,y) point that is this Shape's reference
   *                    point.
   * @param yCoordinate is the y-coordinate of the (x,y) point that is this Shape's reference
   *                    point.
   * @param color       is the desired Color of the Shape.
   * @param xSpace      is the maximum horizontal space the shape has (i.e. width in a Rectangle).
   * @param ySpace      is the maximum vertical space the shape has (i.e. length in a Rectangle),
   * @throws IllegalArgumentException if xSpace or ySpace is negative as a Shape cannot have take up
   *                                  negative space.
   */
  public AnimatorShapeImpl(String name, ShapeTypes type, double xCoordinate, double yCoordinate,
                           Color color, double xSpace, double ySpace)
          throws IllegalArgumentException {
    if (xSpace < 0 || ySpace < 0) {
      throw new IllegalArgumentException("A shape cannot take up negative space");
    } else if (name == null || name.length() == 0) {
      throw new IllegalArgumentException("A shape's name cannot be empty or null");
    }
    this.name = name;
    this.type = type;
    this.referencePoint = new XYCoordinate(xCoordinate, yCoordinate);
    this.color = color;
    this.xSpace = xSpace;
    this.ySpace = ySpace;
  }

  @Override
  public ShapeTypes getType() {
    return this.type;
  }

  /**
   * Returns the maximum vertical space (y-axis) of this Shape. For a Rectangle or a Triangle this
   * would be the height or for an Oval it is the Y radius.
   *
   * @return the maximum vertical space (a double) of the the Shape.
   */
  @Override
  public double getYSpace() {
    return this.ySpace;
  }

  /**
   * Returns the maximum horizontal space (x-axis) of this Shape. For a Rectangle or a Triangle this
   * would be the width/base or for an Oval it is the X radius.
   *
   * @return the the maximum horizontal space (a double) of the Shape.
   */
  @Override
  public double getXSpace() {
    return this.xSpace;
  }

  @Override
  public XYCoordinate getReferencePoint() {
    return this.referencePoint;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public Color getColor() {
    return this.color;
  }

  /**
   * This is a helper method for the toString methods of our AnimatorShapes. It takes the Color that
   * this object is and converts it to a String of the RGB values on the scale of 0.0-1.0.
   *
   * @return an RGB String representation of the color of the Shape in the format of (1.0,1.0,1.0).
   */
  private String colorString() {
    String colorStr = "(";
    // passing in null  will return an array with the decimal RGB values
    float[] rgb = color.getColorComponents(null);
    for (int i = 0; i < 3; i++) {
      if (i == 2) {
        colorStr += String.format("%.1f", rgb[i]) + ")";
      } else {
        colorStr += String.format("%.1f", rgb[i]) + ",";
      }
    }
    return colorStr;
  }

  @Override
  public AnimatorShapeImpl move(double xCoordinate, double yCoordinate) {
    return new AnimatorShapeImpl(name, type, xCoordinate, yCoordinate, color, xSpace, ySpace);
  }

  /**
   * Scales a shape by changing its xValue and yValue to the values passed in.
   *
   * @param xValue is the largest horizontal value (i.e. width or radius) to change the Shape to.
   * @param yValue is the largest vertical value (i.e. height) to change the Shape to.
   * @return a new AnimatorShape with the given xValue and yValue.
   * @throws IllegalArgumentException if xValue or yValue passed in is 0.
   */
  @Override
  public AnimatorShapeImpl scaleSize(double xValue, double yValue) throws IllegalArgumentException {
    if (xValue <= 0 || yValue <= 0) {
      throw new IllegalArgumentException("Cannot scale a shape to 0");
    }
    return new AnimatorShapeImpl(name, type, getReferencePoint().getXCoordinate(),
            getReferencePoint().getYCoordinate(), getColor(), xValue, yValue);
  }

  /**
   * Changes the color of a Shape to the Color passed in (returns a new AnimatorShape).
   *
   * @param toColor is the Color it should be changed to.
   * @return a new AnimatorShape with the given color.
   * @throws IllegalArgumentException if the toColor is the same as the AnimatorShape's current
   *                                  color
   */
  @Override
  public AnimatorShapeImpl changeColor(Color toColor) throws IllegalArgumentException {
    if (toColor.equals(getColor())) {
      throw new IllegalArgumentException("Color cannot be the same as before");
    }
    return new AnimatorShapeImpl(name, type, getReferencePoint().getXCoordinate(),
            getReferencePoint().getYCoordinate(), toColor, xSpace, ySpace);
  }

  /**
   * This is a helper method for description which simply gets the dimensions of the Shape if that
   * is all the user wants.
   *
   * @return a String showing just the dimensions (i.e. Height & Width) of the Shape.
   */
  private String specifyDimensions() {
    if (type.getTypeTxt().equals("Rectangle")) {
      return "Width: " + String.format("%.1f", xSpace) + ", Height: "
              + String.format("%.1f", ySpace);
    } else {
      return "X radius: " + String.format("%.1f", xSpace) + ", Y radius: "
              + String.format("%.1f", ySpace);
    }
  }

  @Override
  public String description(boolean dimensionsOnly) {
    if (dimensionsOnly) {
      return specifyDimensions();
    } else if (type.getTypeTxt().equals("Rectangle")) {
      return "Name: " + getName() + "\nType: " + type.getTypeTxt() + "\nMin corner: "
              + getReferencePoint().toString() + ", " + specifyDimensions()
              + ", Color: " + colorString();
    } else { // if not rectangle, is Oval or Ellipse
      return "Name: " + getName() + "\nType: " + type.getTypeTxt() + "\nCenter: "
              + getReferencePoint().toString() + ", " + specifyDimensions()
              + ", Color: " + colorString();
    }
  }

  @Override
  public AnimatorShapeImpl deepCopy() {
    return new AnimatorShapeImpl(getName(), type, getReferencePoint().getXCoordinate(),
            getReferencePoint().getYCoordinate(), getColor(), getXSpace(), getYSpace());
  }
}
