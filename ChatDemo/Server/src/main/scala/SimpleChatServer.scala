package main.scala

import java.net.ServerSocket

import akka.actor._

import scala.collection.mutable

/**
 * Created by uc on 2017/2/3.
 */
class SimpleChatServer extends Actor {

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
        sessions.filterKeys(userName.equals(_)).values.foreach(_ ! MessageEvent(userName, message))
    }

}

object SimpleChatServerMain extends App{

    val  serverSystem = ActorSystem("SimpleChatServer")
    val serverActor = serverSystem.actorOf(Props[SimpleChatServer] , name = "SimpleChatServer")

    println(" server is started")


}