package com.cosmin.examples.wordcount

import com.cosmin.pipeline.Pipeline
import com.cosmin.pipeline.executor.{AkkaExecutor, AsyncExecutor, PipelineExecutor, SynchronouslyExecutor}

import scala.util.{Failure, Success}

/**
  * Implementation of the following UNIX command 'cat "myText.txt" | grep "hello" | wc -l'
  */
object WordCount {
  def main(args: Array[String]): Unit = {
    val wordToFind = "hello"
    val pipeline = Pipeline[String, String]() | Cat() | Grep(wordToFind) | Count()

    doExecute(pipeline, SynchronouslyExecutor(), "Sync", wordToFind)
    doExecute(pipeline, SynchronouslyExecutor(), "Sync Failed", wordToFind) ("notFound.txt")

    executeAsync(pipeline, wordToFind)
    executeAsyncUsingAkka(pipeline, wordToFind)
  }

  private def executeAsync(pipeline: Pipeline[String, Int], wordToFind: String): Unit = {
    val asyncExecutor: AsyncExecutor[String, Int] = AsyncExecutor[String, Int]
    doExecute(pipeline, asyncExecutor, "Async", wordToFind)
    doExecute(pipeline, asyncExecutor, "Async Failed", wordToFind) ("notFound.txt")
  }

  private def executeAsyncUsingAkka(pipeline: Pipeline[String, Int], wordToFind: String): Unit = {
    val akkaExecutor: AkkaExecutor[String, Int] = AkkaExecutor[String, Int]
    doExecute(pipeline, akkaExecutor, "Akka Async", wordToFind)
    doExecute(pipeline, akkaExecutor, "Akka Async Failed", wordToFind) ("notFound.txt")
  }

  private def doExecute(pipe: Pipeline[String, Int], exec: PipelineExecutor[String, Int], prefix: String, word: String) (implicit file: String = "myText.txt") : Unit = {
    pipe.execute(file) {
      case Success(output) => println(s"$prefix ---> word '$word' was found on $output lines")
      case Failure(e) => println(s"$prefix ---> error: $e")
    } (exec)
  }
}
