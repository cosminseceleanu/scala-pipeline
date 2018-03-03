package com.cosmin.examples

import com.cosmin.pipeline.Pipeline

/**
  * Pipeline who build 'Hello World!' message using anonymous filters for 'World' and '!'
  */
object HelloWorld {
  def main(args: Array[String]): Unit = {
    //build pipeline
    val pipeline = Pipeline[String, String]() | (i => s"$i World") | (i => s"$i!")

    //execute pipeline filters
    println(pipeline.execute("Hello"))
  }
}
