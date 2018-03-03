package com.cosmin.examples

import java.util.concurrent.TimeUnit

import com.cosmin.pipeline.Pipeline
import com.cosmin.pipeline.executor.AsyncExecutor

import scala.concurrent.{Await, Future}
import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Pipeline who build 'Hello World!' message using anonymous filters for 'World' and '!'
  */
object HelloWorld {
  def main(args: Array[String]): Unit = {
    //build pipeline
    val pipeline = Pipeline[String, String]() | (i => s"$i World") | (i => s"$i!")
    val executor = AsyncExecutor[String, String]
    //execute async pipeline filters
    Await.ready(pipeline.execute("Hello") (executor), Duration(10, TimeUnit.SECONDS)).onComplete({
      case Success(out) => println(s"Async --> $out")
    })

    //execute sync pipeline filters
    Await.ready(pipeline.execute("Hey"), Duration(10, TimeUnit.SECONDS)).onComplete({
      case Success(out) => println(s"Sync --> $out")
    })
    println("end")
  }
}
