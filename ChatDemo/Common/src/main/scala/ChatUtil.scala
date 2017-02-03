package main.scala

import scala.util.Random


/**
 * Created by uc on 2017/2/3.
 */
object ChatUtil {

    val chaServiceHost  = "127.0.0.1"
    val chatServicePort = 9999

    def getFreePort =  new Random().nextInt(10000) + 10000
}
