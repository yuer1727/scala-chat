package main.scala

/**
 * Created by uc on 2017/2/3.
 */
sealed trait ChatEvent
case class LoginEvent(userName: String) extends ChatEvent
case class LogoutEvent(userName: String) extends ChatEvent
case class MessageEvent(userName: String, message: String) extends ChatEvent

