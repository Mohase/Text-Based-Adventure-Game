package o1.Clues

import o1.Clues.{Adventure, Area, Item}

import scala.collection.mutable.Map

/** A `Player` object represents a player character controlled by the real-life user
  * of the program.
  *
  * A player object’s state is mutable: the player’s location and possessions can change,
  * for instance.
  *
  * @param startingArea  the player’s initial location */
class Player(startingArea: Area, adventure: Adventure):

  private var currentLocation = startingArea        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false              // one-way flag
  private val items = Map[String, Item]()
  var password = false
  var hasLost = false

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven

  /** Returns the player’s current location. */
  def location = this.currentLocation


  /** Attempts to move the player in the given direction. This is successful if there
    * is an exit from the player’s current location towards the direction name. Returns
    * a description of the result: "You go DIRECTION." or "You can't go DIRECTION." */
  def go(direction: String) =
    val destination = this.location.neighbor(direction)
    this.currentLocation = destination.getOrElse(this.currentLocation)
    if destination.isDefined then "You go " + direction + "." else "You can't go " + direction + "."


  /** Causes the player to rest for a short while (this has no substantial effect in game terms).
    * Returns a description of what happened. */
  def rest() =
    "You rest for a while. Better get a move on, though."


  /** Signals that the player wants to quit the game. Returns a description of what happened within
    * the game as a result (which is the empty string, in this case). */
  def quit() =
    this.quitCommandGiven = true
    ""

  /** Returns a brief description of the player’s state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name

  def drop(itemName: String): String =
    if items.contains(itemName) then
      location.addItem(items(itemName))
      items.remove(itemName)
      s"You drop the $itemName."
    else
      "You don't have that!"

  def examine(itemName: String): String =
    if itemName == "oven" || itemName == "light" || itemName == "picture" || itemName == "a punctured gas tank" then
      if location.items.contains(itemName) then
        s"${location.items(itemName).description}"
      else
        s"${location.name} does not contain $itemName"
    else
      if items.contains(itemName) then
        s"You look closely at the $itemName.\n ${items(itemName).description}"
      else
        "If you want to examine an item, it has to be in your inventory."


  def use(itemName: String): String =
    if items.contains(itemName) then
      if itemName == "lighter" then
        hasLost = true
        ""
      else if itemName == "grandpa's gas mask" then
        this.adventure.turnCount = this.adventure.turnCount - 3
        "Ew, disgusting, but atleast I have a few more minutes (+3 travels)."
      else if itemName == "morse code chart" then
        "A   . _       B   _ . . .   C   _ . _ .   \nD   _ . .   E   .   F   . . _ .   \nG   _ _ .   H   . . . .   I   . .   \nJ   . _ _ _   K   _ . _   L   . _ . .   \nM   _ _   N   _ .   O   _ _ _   \nP   . _ _ .   Q   _ _ . _   R   . _ .   \nS   . . .   T   _   U   . . _   \nV   . . . _   W   . _ _   X   _ . . _   \nY   _ . _ _   Z   _ _ . ."
      else
        ""
    else
      "If you want to use an item, it has to be in your inventory."

  def unlock(passwords: String): String =
    if this.location.name == "Door" then
      if passwords.toLowerCase == "3.14 in oven" then
        this.password = true
        ""
      else
        "Incorrect, try again."
    else
      "You are not at the door."


  def help(): String =
    "Help: \nExplore the house \nFind the Gas Mask for more time \nHint (You are allowed to use your phone): https://www.youtube.com/watch?v=Wz81ybJ2L2E (22:56)" +
      "\nCommands availible: \ngo \nrest \nquit \ndrop \nexamine \nuse \nunlock \nhelp (duh)"

  def get(itemName: String): String =
    if itemName == "oven" || itemName == "light" || itemName == "a punctured gas tank" || itemName == "picture"  then
      s"Can't pick up $itemName."
    else
      val removedItem = location.removeItem(itemName)
      if removedItem.isDefined then
        items += (itemName -> removedItem.get )
        s"You picked up the $itemName."
      else
        s"There is no $itemName here to pick up."

  
  def has(itemName: String): Boolean = items.contains(itemName)

  def inventory: String =
    if items.isEmpty then
      "You are empty-handed."
    else
      "You are carrying:\n " + items.keys.mkString("\n")




end Player

