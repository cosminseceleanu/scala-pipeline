package com.cosmin.examples

import com.cosmin.pipeline.Pipeline
import com.cosmin.examples.filters.{HelloWorld, PlusOne, ToString}

object Main {
  def main(args: Array[String]): Unit = {
    val pipeline = new Pipeline[Int, Int](List()) | PlusOne() | ToString() | HelloWorld()
    val res = pipeline.execute(3)
    println(res)
    println("--- end ---")
  }

  /*
      val pipeline = Pipeline()
      pipeline pipe Stage1 pipe Stage2 pipe Stage3
      pipeline | Stage1 | Stage2 | Stage3
      val res = pipeline.execute(input)
     */
}