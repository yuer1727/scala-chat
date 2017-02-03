package main.scala

import java.io.File

import akka.actor.{Props, ActorSystem, Actor}
import com.typesafe.config.ConfigFactory

import scala.util.Random

/**
 * Created by uc on 2017/2/3.
 */
class SimpleChatClient(userName: String) extends Actor{

    val remoteActor = context.actorSelection("akka.tcp://SimpleChatServer@127.0.0.1:9999/user/ChatServer")

    override def preStart(): Unit = {
        /*
          Connect to remote actor. The following are the different parts of actor path
          akka.tcp : enabled-transports  of remote_application.conf
          RemoteSystem : name of the actor system used to create remote actor
          127.0.0.1:5150 : host and port
          user : The actor is user defined
          remote : name of the actor, passed as parameter to system.actorOf call
         */

        println("That 's remote:" + remoteActor)
        remoteActor ! LoginEvent(userName)
    }

    def receive = {
      case MessageEvent(userName, message) => println(userName + ": " + message)
      case "bye" => { sender ! LogoutEvent(userName);  }
      case "say" => chat
      case message: String => sender ! MessageEvent(userName, message)
    }

    def chat(): Unit = {
      var finished = false
      while (!finished) {
        var message = Console.readLine
        finished = message.trim == "bye"
        remoteActor ! MessageEvent(userName, message)
      }
    }

}

class ReadLineClient(userName: String) extends Actor{

  val remoteActor = context.actorSelection("akka.tcp://SimpleChatServer@127.0.0.1:9999/user/ChatServer")

  def receive = {
    case "say" => chat
    case _ => "nothing"
  }

  def chat(): Unit = {
    var finished = false
    while (!finished) {
      var message = Console.readLine
      finished = message.trim == "bye"
      remoteActor ! MessageEvent(userName, message)
    }
  }

}




object SimpleChatClientMain extends App{

  print("Username: ")
  val username = Console.readLine

  val configFile = getClass.getClassLoader.getResource("client_application.conf").getFile
  val config = ConfigFactory.parseFile(new File(configFile))
  val clientActorSystem = ActorSystem("ClientSystem",config)
  val clientActor = clientActorSystem.actorOf(Props(new SimpleChatClient(username)), name="SimpleChatClient")

  val readLineActor = clientActorSystem.actorOf(Props(new ReadLineClient(username)), name="ReadLine")
  readLineActor ! "say"
}