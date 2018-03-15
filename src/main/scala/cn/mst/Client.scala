package cn.mst

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory


class Client(val masterHost: String, val masterPort: Int) extends Actor{
  var master : ActorSelection = _

  override def preStart(): Unit = {

    master = context.actorSelection(s"akka.tcp://ServerSystem@$masterHost:$masterPort/user/Server") //需要有/user, Master要和master那边创建的名字保持一致

  }

  override def receive: Receive = {
    case "reply" => {
      println("a reply form master")
    }
    case "begin" => {
      while (true){
        val line = Console.readLine
        val arr = line.toCharArray
        val res = arr.map(_ + 10)
        master ! msgbox(res)
      }
    }
    case msgbox(msg) => {
      println(msg)
    }
  }

  def messMthod(msg: Array[Int]) = {
     master ! msgbox(msg)
  }
}

object Client {
  def main(args: Array[String]) {
    val host = args(0)
    val port = args(1).toInt
    val masterHost = args(2)
    val masterPort = args(3).toInt

    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin
    val config = ConfigFactory.parseString(configStr)

    val actorSystem = ActorSystem("Client", config)

    val client = actorSystem.actorOf(Props(new Client(masterHost, masterPort)), "Client")
    client ! "begin"
    actorSystem.awaitTermination()
  }
}
