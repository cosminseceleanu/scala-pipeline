package com.cosmin.examples

import com.cosmin.pipeline.Pipeline
import com.cosmin.pipeline.executor.AsyncExecutor

import scala.util.Success

/**
  * Pipeline who build 'Hello World!' message using anonymous filters for 'World' and '!'
  */
object HelloWorld {
  def main(args: Array[String]): Unit = {
    //build pipeline
    val pipeline = Pipeline[String, String]() | (i => s"$i World") | (i => s"$i!")

    //execute filters synchronous
    pipeline.execute("Hello") {
        case Success(out) => println(s"Sync --> $out")
    }

    executeAsync(pipeline)

    println("end")
  }

  def executeAsync(pipeline: Pipeline[String, String]): Unit = {
    implicit val executor: AsyncExecutor[String, String] = AsyncExecutor[String, String]
    pipeline.execute("Hey Async") {
      case Success(out) => println(s"Async --> $out")
    }

    Thread.sleep(2000)
  }
}
