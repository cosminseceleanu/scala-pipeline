package com.cosmin.examples

import com.cosmin.pipeline.executor.AkkaExecutor

import scala.util.Success

object AkkaPipeline {
  def main(args: Array[String]): Unit = {
    val exec = AkkaExecutor[Int, Int]
//    exec.execute(1, List()) {
//      case Success(nr) => println("aaaa")
//    }

    val l = List(1, 2, 3)
    println(l.head)
    println(l.tail)
  }
}
