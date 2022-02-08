package cs5004.animator.model.animatorshapes;

/**
 * These are the various types of Shapes that we can animate in our EasyAnimator. They represent a
 * 2D shape of the same name.
 */
public enum ShapeTypes {
  Rectangle("Rectangle"),
  Oval("Oval"),
  Ellipse("Ellipse");

  private final String type;

  /**
   * Constructs our ShapeType in the event we want to add another Shape.
   * @param type is the String version of what we would call this shape.
   */
  ShapeTypes(String type) {
    this.type = type;
  }

  /**
   * Simply gets the word that describes this Shape (i.e. Rectangle, oval, etc.).
   * @return the one word, String description of this Shape.
   */
  public String getTypeTxt() {
    return this.type;
  }
}
