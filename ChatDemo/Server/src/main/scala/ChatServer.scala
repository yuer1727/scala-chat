package main.scala

import java.io.File

import akka.actor.{Props, ActorSystem, Actor, ActorRef}
import com.typesafe.config.ConfigFactory

import scala.collection.mutable

/**
 * Created by uc on 2017/2/3.
 */
class ChatServer extends Actor{
      val sessions = new mutable.HashMap[String, ActorRef]

      def receive = {
        case LoginEvent(userName: String) => {
          sessions.put(userName, sender)
          sendmessageFrom(userName, "I just logged in!")
          println(userName + " just logged in")
        }
        case LogoutEvent(userName) => {
          sessions.remove(userName).get ! LogoutEvent(userName)
          sendmessageFrom(userName, "I just logged out !")
          println(userName + " just logged out !")
        }
        case MessageEvent(userName, message) => {
          sendmessageFrom(userName, message)
          println(userName + "  sent " + message)
        }
      }

      def sendmessageFrom(userName: String , message: String) = {
        sessions.values.foreach(_ ! MessageEvent(userName, message))
      }

}


object ChatServerMain extends App{

      //get config file
      val configFile = getClass.getClassLoader.getResource("server_application.conf").getFile
      //read config
      val config = ConfigFactory.parseFile(new File(configFile))
      //create actorsystem with config
      val  serverSystem = ActorSystem("SimpleChatServer", config)
      //start a server actor
      val serverActor = serverSystem.actorOf(Props[ChatServer] , name = "ChatServer")

      println(" server is started")


}
