package com.cosmin.examples.wordcount

import com.cosmin.pipeline.Pipeline
import com.cosmin.pipeline.executor.{AkkaExecutor, AsyncExecutor}

import scala.util.Success

/**
  * Implementation of the following UNIX command 'cat "myText.txt" | grep "hello" | wc -l'
  */
object WordCount {
  def main(args: Array[String]): Unit = {
    val wordToFind = "hello"
    val pipeline = Pipeline[String, String]() | Cat() | Grep(wordToFind) | Count()

    pipeline.execute("myText.txt") {
      case Success(output) => println(s"word '$wordToFind' was found on $output lines")
    }

    executeAsync(pipeline, wordToFind)
    executeAsyncUsingAkka(pipeline, wordToFind)
  }

  private def executeAsync(pipeline: Pipeline[String, Int], wordToFind: String): Unit = {
    implicit val asyncExecutor: AsyncExecutor[String, Int] = AsyncExecutor[String, Int]
    pipeline.execute("myText.txt") {
      case Success(output) => println(s"Async ---> word '$wordToFind' was found on $output lines")
    }
    Thread.sleep(2000)
  }

  private def executeAsyncUsingAkka(pipeline: Pipeline[String, Int], wordToFind: String): Unit = {
    implicit val akkaExecutor: AkkaExecutor[String, Int] = AkkaExecutor[String, Int]
    pipeline.execute("myText.txt") {
      case Success(output) => println(s"Akka Async ---> word '$wordToFind' was found on $output lines")
    }
  }
}
