package cn.mst

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory


class Server extends Actor {



  override def preStart(): Unit = {
    println("服务器已经启动")
  }

  // 用于接收消息
  override def receive: Receive = {
    case "connect" => {
      println("a client connected")
      sender ! "reply"
    }
    case msgbox(msg) => {
      println("解密前")
      println(msg.map(_.toChar).toBuffer)
      println("解密后")
      val resInt = msg.map(_-10)
      val res = resInt.map(_ .toChar).toBuffer
      println(res)
    }
  }
}

object Server {
  def main(args: Array[String]) {

    val host = args(0)
    val port = args(1).toInt
    // 准备配置
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin
    val config = ConfigFactory.parseString(configStr)

    val actorSystem = ActorSystem("ServerSystem", config)

    val master = actorSystem.actorOf(Props[Server], "Server")

    actorSystem.awaitTermination()
  }
}
