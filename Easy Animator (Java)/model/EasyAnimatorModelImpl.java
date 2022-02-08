package cs5004.animator.model;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import cs5004.animator.util.AnimationBuilder;
import cs5004.animator.model.animatorshapes.AnimatorShape;
import cs5004.animator.model.animatorshapes.AnimatorShapeImpl;
import cs5004.animator.model.animatorshapes.ShapeTypes;

/**
 * This is the implementation of our EasyAnimatorModel Interface. It keeps track of the shapes that
 * are added to the animation as well as all of the events that occur throughout the animation. It
 * utilizes the AnimatorEvent interface to hold the starting and ending shapes as they are modified
 * as well as to keep track of the time frames over which those events are modified.
 *
 * <p>It also holds the width, height, and the x and y coordinate of the left-most, top-most corner
 * of the viewing rectangle.
 */
public class EasyAnimatorModelImpl implements EasyAnimatorModel {
  private List<AnimatorShape> shapeList;
  private List<AnimatorEvent> eventList;
  private int xBound;
  private int yBound;
  private int viewWidth;
  private int viewHeight;
  private int endFrame;

  /**
   * This constructs our EasyAnimatorModel. It holds a list of current Shapes that it has created as
   * well as a list of all the AnimatorEvents that are occurring throughout the Animation.
   */
  public EasyAnimatorModelImpl() {
    this.shapeList = new ArrayList<>();
    this.eventList = new ArrayList<>();
    this.endFrame = 0;
  }

  @Override
  public void setBounds(int x, int y, int width, int height) {
    this.xBound = x;
    this.yBound = y;
    this.viewWidth = width;
    this.viewHeight = height;
  }

  @Override
  public int getFinalFrame() {
    return this.endFrame;
  }

  @Override
  public int[] getBounds() {
    return new int[]{xBound, yBound, viewWidth, viewHeight};
  }

  @Override
  public List<AnimatorShape> getShapeList() {
    List<AnimatorShape> listCopy = new ArrayList<>();
    for (AnimatorShape shape : shapeList) {
      listCopy.add(shape.deepCopy());
    }
    return listCopy;
  }

  @Override
  public List<AnimatorEvent> getEventList() {
    return this.eventList;
  }

  /**
   * Returns a list of AnimatorEvents that are happening at a given time frame.
   *
   * @param timeFrame is the integer frame of time where we want to see what events are happening.
   * @return a list of all the Events still occurring at that given frame of time.
   * @throws IllegalArgumentException if the timeFrame given is a negative number.
   */
  public List<AnimatorEvent> getEventsAtFrame(int timeFrame) throws IllegalArgumentException {
    if (timeFrame < 0) {
      throw new IllegalArgumentException("Time frame cannot be negative");
    }
    return eventList.stream()
            .filter(event -> event.getStartTime() == timeFrame
                    || (event.getStartTime() < timeFrame && event.getEndTime() > timeFrame))
            .collect(Collectors.toList());
  }

  /**
   * This is a helper method for getShapeAtFrame which will get all the events occurring for a Shape
   * at a specific frame.
   *
   * @param name      is the unique name of the Shape we are looking for.
   * @param timeFrame is the frame for which we want to see if events are happening.
   * @return a list of all the events occurring for the given Shape.
   */
  private List<AnimatorEvent> getShapeEventsAtFrame(String name, int timeFrame) {
    return eventList.stream()
            .filter(event -> (event.getStartShape().getName().equals(name)
                    && (event.getStartTime() == timeFrame
                    || (event.getStartTime() <= timeFrame && event.getEndTime() > timeFrame))))
            .collect(Collectors.toList());
  }

  /**
   * This is the formula used to calculate the in-between dimensions of Shapes as they transform.
   *
   * @param startVal  is the value the Shape starts at.
   * @param endVal    is the value the Shape ends at.
   * @param startTime is the start time of the transformation.
   * @param endTime   is the end time of the transformation.
   * @param frame     is the time for which we are calculating this value.
   * @return the value that of a specific dimension at the provided time.
   */
  private static double tweenCalc(double startVal, double endVal, int startTime, int endTime,
                                  int frame) {
    double time1 = (double) (endTime - frame) / (endTime - startTime);
    double time2 = (double) (frame - startTime) / (endTime - startTime);
    double intermediateVal = (startVal * time1) + (endVal * time2);
    return Math.round(intermediateVal);
  }

  /**
   * This is a helper method used to build our Shapes in their intermediate form so that they can be
   * visually displayed.
   *
   * @param shape       is the name of the Shape that we want an intermediate form for.
   * @param shapeEvents is a list of the transformations occurring to a particular shape.
   * @param timeFrame   is the frame of time we want to build the intermediate version of this shape
   *                    for.
   * @return a Shape of the same name but with its dimensions, color, and reference point at their
   *         intermediate values.
   */
  private AnimatorShape buildIntermediateShape(AnimatorShape shape, List<AnimatorEvent> shapeEvents,
                                               int timeFrame) {
    String name = shape.getName();
    ShapeTypes type = shape.getType();
    double x = shape.getReferencePoint().getXCoordinate();
    double y = shape.getReferencePoint().getYCoordinate();
    Color color = shape.getColor();
    double width = shape.getXSpace();
    double height = shape.getYSpace();
    // Check our events at this frame and build our intermediate Shape state from each change.
    for (AnimatorEvent event : shapeEvents) {
      if (event.getStartShape().getXSpace() != event.getEndShape().getXSpace()
              || event.getStartShape().getYSpace() != event.getEndShape().getYSpace()) {
        width = tweenCalc(event.getStartShape().getXSpace(), event.getEndShape().getXSpace(),
                event.getStartTime(), event.getEndTime(), timeFrame);
        height = tweenCalc(event.getStartShape().getYSpace(), event.getEndShape().getYSpace(),
                event.getStartTime(), event.getEndTime(), timeFrame);
      } else if (event.getStartShape().getColor() != event.getEndShape().getColor()) {
        int r = (int) tweenCalc(event.getStartShape().getColor().getRed(),
                event.getEndShape().getColor().getRed(),
                event.getStartTime(), event.getEndTime(), timeFrame);
        int g = (int) tweenCalc(event.getStartShape().getColor().getGreen(),
                event.getEndShape().getColor().getGreen(),
                event.getStartTime(), event.getEndTime(), timeFrame);
        int b = (int) tweenCalc(event.getStartShape().getColor().getBlue(),
                event.getEndShape().getColor().getBlue(),
                event.getStartTime(), event.getEndTime(),
                timeFrame);
        color = new Color(r, g, b);
      } else if (event.getStartShape().getReferencePoint().getXCoordinate()
              != event.getEndShape().getReferencePoint().getXCoordinate()
              || event.getStartShape().getReferencePoint().getYCoordinate()
              != event.getEndShape().getReferencePoint().getYCoordinate()) {
        double startX = event.getStartShape().getReferencePoint().getXCoordinate();
        double startY = event.getStartShape().getReferencePoint().getYCoordinate();
        double endX = event.getEndShape().getReferencePoint().getXCoordinate();
        double endY = event.getEndShape().getReferencePoint().getYCoordinate();
        x = tweenCalc(startX, endX, event.getStartTime(), event.getEndTime(), timeFrame);
        y = tweenCalc(startY, endY, event.getStartTime(), event.getEndTime(), timeFrame);
      }
    }
    return new AnimatorShapeImpl(name, type, x, y, color, width, height);
  }

  /**
   * This helper method ensures that we are returning the most recent version of the Shape we are
   * looking for within a given time frame.
   *
   * @param name      is the name of the Shape we are looking for.
   * @param timeFrame is the frame of time we are matching to a shape.
   * @return the version of the Shape at the specified time frame.
   */
  private AnimatorShape mostRecentVersion(String name, int timeFrame) {
    AnimatorShape tempShape = getShape(name);
    List<AnimatorEvent> allEvents = getEventsForShape(name);
    for (AnimatorEvent event : allEvents) {
      if (event.getEndTime() <= timeFrame) {
        tempShape = event.getEndShape();
      }
    }
    return tempShape;
  }

  /**
   * Returns a list of AnimatorShapes at a given time frame.
   *
   * @param timeFrame is the integer frame of time.
   * @return a list of all the Shapes that are in a given frame of time.
   * @throws IllegalArgumentException if the timeFrame given is a negative number.
   */
  @Override
  public List<AnimatorShape> getShapesAtFrame(int timeFrame) throws IllegalArgumentException {
    if (timeFrame < 0) {
      throw new IllegalArgumentException("Time frame cannot be negative");
    }
    List<AnimatorShape> frameShapes = new ArrayList<>();
    for (AnimatorShape shape : shapeList) {
      // get our events for a specific shape at this time
      List<AnimatorEvent> shapeEvents = getShapeEventsAtFrame(shape.getName(), timeFrame);
      // if this is true, shape is not changing currently
      if (shapeEvents.size() == 1 && shapeEvents.get(0).getEndTime() >= timeFrame) {
        frameShapes.add(mostRecentVersion(shape.getName(), timeFrame));
      } else if (shapeEvents.size() > 1) {
        AnimatorShape recentShape = mostRecentVersion(shape.getName(), timeFrame);
        frameShapes.add(buildIntermediateShape(recentShape, shapeEvents, timeFrame));
      }
    }
    return frameShapes;
  }

  /**
   * This is a helper function for each of our methods that add a new AnimatorEvent to our events
   * list. It ensures that our eventList attribute is sorted in time order (from earliest event
   * start time to latest event start time). It also updates the final frame time if necessary.
   *
   * @param newEvent is the new AnimatorEvent to be added to our event's list.
   */
  private void addEvent(AnimatorEvent newEvent) {
    if (newEvent.getEndTime() > endFrame) { // update the ending frame number if necessary
      endFrame = newEvent.getEndTime();
    }
    if (eventList.isEmpty()) {
      eventList.add(newEvent);
      return;
    } else if (eventList.size() == 1) {
      if (newEvent.getStartTime() < eventList.get(0).getStartTime()) {
        eventList.add(0, newEvent);
        return;
      }
      eventList.add(newEvent);
      return;
    }
    // below used to sort increasing by start time
    int index = 0;
    for (AnimatorEvent eachEvent : eventList) {
      if (newEvent.getStartTime() == eachEvent.getStartTime()) {
        index = eventList.indexOf(eachEvent);
      } else if (eventList.indexOf(eachEvent) == eventList.size() - 1) {
        if (newEvent.getStartTime() < eachEvent.getStartTime()) {
          eventList.add(eventList.size() - 1, newEvent);
          return;
        }
        eventList.add(newEvent);
        return;
      }
    }
    eventList.add(index, newEvent);
  }

  /**
   * This creates a shape to be animated. Shapes can only be created a single time.
   *
   * @param shape     is the Shape we want to add to the animation.
   * @param appear    is the time the shape should appear.
   * @param disappear is the time the shape should disappear.
   * @throws IllegalArgumentException if the appear or disappear time is negative, if a Shape of the
   *                                  same name has already been created, or if the Shape is null.
   */
  @Override
  public void createShape(AnimatorShape shape, int appear, int disappear)
          throws IllegalArgumentException {
    if (appear < 0 || disappear < 0) {
      throw new IllegalArgumentException("Time cannot be negative");
    } else if (appear > disappear) {
      throw new IllegalArgumentException("Start time must be smaller than end time");
    } else if (shape == null) {
      throw new IllegalArgumentException("Shape cannot be null");
    } else if (isValidName(shape.getName())) {
      throw new IllegalArgumentException("A shape with this name already exists");
    }
    shapeList.add(shape);
    AnimatorEvent event = new AnimatorEventImpl(shape, appear, disappear);
    addEvent(event);
  }

  /**
   * This is a helper method that determines if a Shape has not appear yet or has disappeared and
   * thus cannot be modified.
   *
   * @param name  is the name of the shape we are checking
   * @param start is the start time of the event that would modify this shape.
   * @param end   is the end time of the event that would modify this shape.
   * @return false if the Shape has disappeared or has not yet appeared yet, true otherwise.
   */
  private boolean shapeExists(String name, int start, int end) {
    List<AnimatorEvent> shapeEvents = getEventsForShape(name);
    for (AnimatorEvent event : shapeEvents) {
      // Only CreateEvents hold the same object as the start and end shape
      if (event.getStartShape().equals(event.getEndShape())) {
        if (end > event.getEndTime() || start < event.getStartTime()) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Helper method which checks to see if a name passed in as the parameter matches a name in our
   * list of Shapes.
   *
   * @param name is the unique name of the Shape we are looking for.
   * @return true if there is a Shape with a matching name, false otherwise.
   */
  private boolean isValidName(String name) {
    for (AnimatorShape eachShape : shapeList) {
      if (eachShape.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * This is a helper function which gets all the events related to a particular shape identified by
   * its unique name.
   *
   * @param name is the name of the Shape that we want all the events for.
   * @return a list of all of the AnimatorEvents for a particular Shape.
   */
  public List<AnimatorEvent> getEventsForShape(String name) {
    return eventList.stream().filter(event -> event.getStartShape().getName().equals(name))
            .collect(Collectors.toList());
  }

  /**
   * This finds a particular shape in our Shape list identified by its unique name and returns it.
   *
   * @param name is the unique name of the Shape we want to find.
   * @return the Shape with the matching name.
   */
  private AnimatorShape getShape(String name) {
    return shapeList.stream().filter(shape -> shape.getName().equals(name))
            .collect(Collectors.toList()).get(0);
  }

  /**
   * This is will replace the shape in our shape list with the "replacement" version passed into
   * this method. It uses the names of the shapes as the replacement policy as all names must be
   * unique. This keeps our list of Shapes updated with the most recent version of each Shape's
   * location, color, and dimensions
   *
   * @param replacement is the Shape we will insert into our list once the Shape of the same name is
   *                    found.
   */
  private void updateShape(AnimatorShape replacement) {
    int index = shapeList.size();
    for (AnimatorShape shape : shapeList) {
      if (shape.getName().equals(replacement.getName())) {
        index = shapeList.indexOf(shape);
        break;
      }
    }
    shapeList.remove(index);
    shapeList.add(replacement);
  }

  /**
   * Helper function for adding a move to our animation. This checks if the Shape is already moving
   * within the given time frame. Our current model does not allow simultaneous Move events for one
   * Shape.
   *
   * @param name  is the name of the Shape we want to move.
   * @param start is the desired start time of the move.
   * @return true if the Shape can move in the given time frame, false otherwise.
   */
  private boolean isValidMove(String name, int start) {
    AnimatorShape shape = getShape(name);
    List<AnimatorEvent> shapeEvents = getEventsForShape(shape.getName());
    for (AnimatorEvent event : shapeEvents) {
      if (start < event.getEndTime()) {
        if (event.getStartShape().getReferencePoint().getXCoordinate()
                != event.getEndShape().getReferencePoint().getXCoordinate()
                || event.getStartShape().getReferencePoint().getYCoordinate()
                != event.getEndShape().getReferencePoint().getYCoordinate()) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Attempts to move a particular AnimatorShape identified by name. Our current model does not
   * allow simultaneous Move events for one Shape.
   *
   * @param name        is the name of the Shape we want to move.
   * @param xCoordinate is the x-coordinate that Shape will move to.
   * @param yCoordinate is the y-coordinate that the Shape will move to.
   * @param start       is the desired start time of the move.
   * @param end         is the desired end time of the move.
   * @throws NoSuchElementException if there is no existing Shape by the same name passed in or if
   *                                the name passed in is null.
   * @throws IllegalStateException  if the shape is already moving at the requested time frame or if
   *                                the shape has disappeared.
   */
  @Override
  public void moveShape(String name, double xCoordinate, double yCoordinate, int start, int end)
          throws NoSuchElementException, IllegalStateException, IllegalArgumentException {
    if (!isValidName(name) || name == null) {
      throw new NoSuchElementException("A Shape of that name does not exist");
    } else if (start < 0 || end < 0) {
      throw new IllegalArgumentException("Time cannot be negative");
    } else if (start > end) {
      throw new IllegalArgumentException("Start time must be smaller than end time");
    } else if (!shapeExists(name, start, end)) {
      throw new IllegalStateException("Shape does not exist in this time frame");
    } else if (!isValidMove(name, start)) {
      throw new IllegalStateException("Shape is already moving in this time frame");
    }
    AnimatorShape shape = getShape(name);
    AnimatorShape updatedShape = shape.move(xCoordinate, yCoordinate);
    AnimatorEventImpl move = new AnimatorEventImpl(shape, updatedShape, start, end);
    addEvent(move);
    updateShape(updatedShape);
  }

  /**
   * Helper method for scaling our Shapes that checks if this shape is already being scaled at this
   * time frame. Our model currently does not allow multiple scale events for one Shape at the same
   * time.
   *
   * @param name  is the name of the Shape we want to scale.
   * @param start is the desired start time of the scale.
   * @return true if the Shape is being scaled already in the given time frame, false otherwise.
   */
  private boolean isScaleOccurring(String name, int start) {
    AnimatorShape shape = getShape(name);
    List<AnimatorEvent> shapeEvents = getEventsForShape(shape.getName());
    for (AnimatorEvent event : shapeEvents) {
      if (start < event.getEndTime()) {
        if (event.getStartShape().getXSpace() != event.getEndShape().getXSpace()
                || event.getStartShape().getYSpace() != event.getEndShape().getYSpace()) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * This scales the maximum horizontal and/or vertical space that the shape takes up, but does not
   * change the reference point of a shape. A shape cannot be scaled twice in the same time frame.
   *
   * @param name   is the name of the Shape we want to scale.
   * @param xValue is the new width or horizontal radius of the Shape.
   * @param yValue is the new height or vertical radius of the Shape.
   * @param start  is the desired start time of the scale.
   * @param end    is the desired end time of the scale.
   * @throws NoSuchElementException   if a Shape with that name is not found in our shape list.
   * @throws IllegalArgumentException if our start or end time is negative, if our end time is
   *                                  before our start time, or if either of the new radii (xValue
   *                                  and yValue) are negative.
   * @throws IllegalStateException    if the shape does not exist in the given time frame or if the
   *                                  shape is already being scaled.
   */
  @Override
  public void scaleShape(String name, double xValue, double yValue, int start, int end)
          throws NoSuchElementException, IllegalArgumentException, IllegalStateException {
    if (!isValidName(name) || name == null) {
      throw new NoSuchElementException("A Shape of that name does not exist ");
    } else if (start < 0 || end < 0) {
      throw new IllegalArgumentException("Time cannot be negative");
    } else if (start > end) {
      throw new IllegalArgumentException("Start time must be smaller than end time");
    } else if (xValue <= 0 || yValue < 0) {
      throw new IllegalArgumentException("Shapes cannot take up negative space");
    } else if (!shapeExists(name, start, end)) {
      throw new IllegalStateException("Shape does not exist in this time frame");
    } else if (isScaleOccurring(name, start)) {
      throw new IllegalStateException("Shape is already being scaled at this time");
    }
    AnimatorShape shape = getShape(name);
    AnimatorShape updatedShape = shape.scaleSize(xValue, yValue);
    AnimatorEventImpl scale = new AnimatorEventImpl(shape, updatedShape, start, end);
    addEvent(scale);
    updateShape(updatedShape);
  }

  /**
   * Helper method for changing the color of a Shape that checks if this shape is already having its
   * color changed in this time frame. Our model currently does not allow multiple color events for
   * one Shape at the same time.
   *
   * @param name  is the name of the Shape we are checking for a current color change.
   * @param start is the desired start time of the new color change event.
   * @return true if the Shape's color is being changed already in the given time frame, false
   *         otherwise.
   */
  private boolean isColorChanging(String name, int start) {
    AnimatorShape shape = getShape(name);
    List<AnimatorEvent> shapeEvents = getEventsForShape(shape.getName());
    for (AnimatorEvent event : shapeEvents) {
      if (start < event.getEndTime()) {
        if (event.getStartShape().getColor() != event.getEndShape().getColor()) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Changes the color of a specific Shape identified by its unique name.
   *
   * @param name  is the name of the Shape whose color is to change.
   * @param color is the new Color the Shape will be.
   * @param start is the desired start time of the color change.
   * @param end   is the desired end time of the color change.
   * @throws NoSuchElementException   if there does not exist a Shape of that name or if the String
   *                                  passed in is null.
   * @throws IllegalStateException    if the shape has not appeared yet, has already disappeared, or
   *                                  if its color is changing in the given time frame.
   * @throws IllegalArgumentException if the start or end time is negative, if the start time is
   *                                  larger than the end time, or if the Shape is already the given
   *                                  color.
   */
  @Override
  public void changeColor(String name, Color color, int start, int end)
          throws NoSuchElementException, IllegalStateException, IllegalArgumentException {
    if (!isValidName(name) || name == null) {
      throw new NoSuchElementException("A Shape of that name does not exist");
    } else if (start < 0 || end < 0) {
      throw new IllegalArgumentException("Time cannot be negative");
    } else if (start > end) {
      throw new IllegalArgumentException("Start time must be smaller than end time");
    } else if (getShape(name).getColor() == color) {
      throw new IllegalArgumentException("The Shape is already this color");
    } else if (!shapeExists(name, start, end)) {
      throw new IllegalStateException("Shape does not exist in this time frame");
    } else if (isColorChanging(name, start)) {
      throw new IllegalStateException("Shape is already changing color in this time frame");
    }
    AnimatorShape shape = getShape(name);
    AnimatorShape updatedShape = shape.changeColor(color);
    AnimatorEventImpl newColor = new AnimatorEventImpl(shape, updatedShape, start, end);
    addEvent(newColor);
    updateShape(updatedShape);
  }

  /**
   * Helper method for our AnimatorBuilder which allows the updating of the time a Shape disappears,
   * so that the model does not throw exceptions when adding a new AnimatorEvent.
   *
   * @param name is the unique name of the AnimatorShape.
   * @param time is the integer we would like to update the time to.
   */
  private void updateDisappearTime(String name, int time) {
    if (time > endFrame) { // update our ending frame if necessary
      endFrame = time;
    }
    List<AnimatorEvent> events = getEventsForShape(name);
    for (AnimatorEvent shapeEvent : events) {
      if (shapeEvent.getStartShape().equals(shapeEvent.getEndShape())) {
        shapeEvent.updateEndTime(time);
        return;
      }
    }
  }

  /**
   * This is the implementation of the AnimationBuilder which takes some input and populates an
   * EasyAnimatorModelImpl with the given shapes and motions. It also has a map of Strings which it
   * uses to keep track of which Shapes we have created since each Shape must have a unique name
   * as a String.
   */
  public static final class AnimatorBuilder implements AnimationBuilder<EasyAnimatorModel> {
    private EasyAnimatorModelImpl model = new EasyAnimatorModelImpl();
    private Map<String, String> shapeTracker = new HashMap<>(); // keeps track of shape names

    @Override
    public EasyAnimatorModel build() {
      return model;
    }

    @Override
    public AnimationBuilder<EasyAnimatorModel> setBounds(int x, int y, int width, int height) {
      model.setBounds(x, y, width, height);
      return this;
    }

    @Override
    public AnimationBuilder<EasyAnimatorModel> declareShape(String name, String type) {
      shapeTracker.put(name, type);
      return this;
    }

    /**
     * This is used to determine to which ShapeTypes enum we are creating based upon the String
     * passed in.
     *
     * @param type is a String that we are matching to one of our predefined ShapeTypes text.
     * @return the ShapeTypes object with the matching text (i.e. Rectangle, Ellipse, etc.)
     */
    private ShapeTypes castType(String type) {
      if (type.equalsIgnoreCase(ShapeTypes.Rectangle.getTypeTxt())) {
        return ShapeTypes.Rectangle;
      } else if (type.equalsIgnoreCase(ShapeTypes.Ellipse.getTypeTxt())) {
        return ShapeTypes.Ellipse;
      } else {
        return ShapeTypes.Oval;
      }
    }

    @Override
    public AnimationBuilder<EasyAnimatorModel> addMotion(String name,
                                                         int t1, int x1, int y1, int w1, int h1,
                                                         int r1, int g1, int b1,
                                                         int t2, int x2, int y2, int w2, int h2,
                                                         int r2, int g2, int b2) {
      // if they are in the list, the Shape isn't created in our model, so we must first create it
      if (shapeTracker.containsKey(name)) {
        String type = shapeTracker.get(name);
        ShapeTypes modelType = castType(type);
        Color color = new Color(r1, r2, b1);
        AnimatorShapeImpl shape = new AnimatorShapeImpl(name, modelType, x1, y1, color, w1, h1);
        model.createShape(shape, t1, t2);
        shapeTracker.remove(name); // remove from the list so we know it is created
      } else {
        model.updateDisappearTime(name, t2); // always update our disappear time for existing Shapes
      }

      // Each if statement below checks for a state change of the Shape (i.e. move, color, or scale)
      if (x1 != x2 || y1 != y2) {
        model.moveShape(name, x2, y2, t1, t2);
      }
      if (w1 != w2 || h1 != h2) {
        model.scaleShape(name, w2, h2, t1, t2);
      }
      if (r1 != r2 || g1 != g2 || b1 != b2) {
        Color newColor = new Color(r2, g2, b2);
        model.changeColor(name, newColor, t1, t2);
      }
      return this;
    }
  }
}
