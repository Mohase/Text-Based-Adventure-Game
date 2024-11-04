package o1.Clues

import o1.Clues.Action

/** The class `Adventure` represents text adventure games. An adventure consists of a player and
  * a number of areas that make up the game world. It provides methods for playing the game one
  * turn at a time and for checking the state of the game.
  *
  * N.B. This version of the class has a lot of “hard-coded” information that pertains to a very
  * specific adventure game that involves a small trip through a twisted forest. All newly created
  * instances of class `Adventure` are identical to each other. To create other kinds of adventure
  * games, you will need to modify or replace the source code of this class. */
class Adventure:

  /** the name of the game */
  val title = "Clues"

  private val hallway      = Area("Hallway", "You are in the hallway. You have acces to all rooms through this room.")
  private val bedRoom = Area("BedRoom", "You are in the bedroom. The bed is surprisingly tidy.")
  private val livingRoom = Area("LivingRoom", "You are in the livingroom. Maybe you'll find something useful.")
  private val kitchen    = Area("Kitchen", "You are in the kitchen. Smells like mustikkapiirakka!")
  private val door      = Area("Door", "You are at the door. Insert the correct password to unlock.")
  private val destination = door


  hallway     .setNeighbors(Vector( "north" -> bedRoom, "east" -> kitchen, "south" -> livingRoom, "west" -> door   ))
  bedRoom.setNeighbors(Vector( "south" -> hallway ))
  livingRoom.setNeighbors(Vector( "north" -> hallway ))
  kitchen   .setNeighbors(Vector( "west" -> hallway ))
  door     .setNeighbors(Vector( "east" -> hallway ))

  // TODO: Uncomment the two lines below. Improve the code so that it places the items in clearing and southForest, respectively.
  kitchen.addItem(Item("oven", "smells delicious! Is that a paper in the oven?"))
  kitchen.addItem(Item("paper in the oven", "It reads: 3.14"))
  kitchen.addItem(Item("punctured gas tank", "so this is where the smell is coming from."))
  kitchen.addItem(Item("lighter","uu, intresting."))
  livingRoom.addItem(Item("grandpa's gas mask", "YES! This will buy me a few more minutes."))
  livingRoom.addItem(Item("morse code chart","hmm.. might this be related to the light in the bedroom?"))
  bedRoom.addItem(Item("light","It's flickering...: .__. .. . .. _. ___ ..._ . _. "))
  door.addItem(Item("picture","It's a picture of a pie in an oven, and the back says: no articles,lowercase. It might be a clue."))


  /** The character that the player controls in the game. */
  val player = Player(hallway, this)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val timeLimit = 15

  /** Determines if the adventure is complete, that is, if the player has won. */
  def isComplete = this.player.password

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver = this.isComplete || this.player.hasQuit || this.player.hasLost

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage = "You have woken up in a house with a funny smell and a password protected front door. Hurry up and escape, the smell might be dangerous."

  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage =
    if this.isComplete then
      "They ain't believe in us, BUT GOD DID!"
    else if this.turnCount == this.timeLimit then
      "Oh no! Time's up. Oxygen ran out, you collapse and weep like a child.\nGame over!"
    else if this.player.hasLost then
      "BOOM! The house exploded, you lose!"
    else  // game over due to player quitting
      "Quitter quitter, poop in litter!"

  /** Plays a turn by executing the given in-game command, such as “go west”. Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */
  def playTurn(command: String):String =
    val action = Action(command)
    val outcomeReport = action.execute(this.player)
    if outcomeReport.isDefined then
      this.turnCount += 1
    outcomeReport.getOrElse(s"""Unknown command: "$command".""")

end Adventure

