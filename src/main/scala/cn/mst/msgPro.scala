package cn.mst

trait msgPro extends Serializable{

}
case class msgbox(msg: Array[Int]) extends msgPro
