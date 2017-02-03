/**
 * Created by uc on 2017/2/3.
 */

import akka.actor._

class HelloActor extends Actor {
  def receive = {
    case "hello" => println("world")
    case _       => println("huh?")
  }


}

object Main extends App{

    val system = ActorSystem("HelloSystem")

    val helloActor = system.actorOf(Props[HelloActor], name = "helloactor")

    helloActor ! "hello"
    helloActor ! "buenos dias"

    system.shutdown

}
