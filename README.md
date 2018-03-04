# Pipeline Pattern
In software engineering, a pipeline consists of a chain of processing elements (processes, threads, coroutines, functions, etc.), arranged so that the output of each element is the input of the next; the name is by analogy to a physical pipeline.

![alt text](https://github.com/cosminseceleanu/scala-pipeline/blob/master/pipeline-model.png)

<strong>The filter</strong> transforms or filters the data it receives via the pipes with which it is connected. A filter can have any number of input pipes and any number of output pipes.

<strong>The pipe</strong> is the connector that passes data from one filter to the next. It is a directional stream of data, that is usually implemented by a data buffer to store all data, until the next filter has time to process it.

## Introduction
This project allows you to implement the pipeline pattern while creating reusable pipelines in your Scala applications. You can create pipelines consisting of one or more filters and then process them synchronous or asynchronous. Pipeline processing is initiated by some payload(input) and this payload will be passed and transformed from filter to filter in order to complete the required process.

## Usage
In order to create a pipeline you must specify type of initial payload.
```scala
import com.cosmin.pipeline.Pipeline

val pipeline = Pipeline[Int, Int]()

```

Operations in a pipeline i.e. filters can be classes that extends Filter trait or pure functions that receive a input and returns processed input. 
In the below example we add Sqrt filter and an anonymus filter who creates a string message with the sqrt value of the input.

```scala
import com.cosmin.pipeline.{Filter, Pipeline}

class Sqrt extends Filter[Int, Double] {
  override def execute: Int => Double = input => Math.sqrt(input)
}

val pipeline = Pipeline[Int, Int]() | new Sqrt | (sqrt => s"Sqrt: $sqrt!")

```
 After pipeline filters was added we can process them using execute method of pipeline who take as parameter initial payload and a callback(onComplete) as curried parameter to be called when pipeline was processed. Callback receive as input parameter a Try object with the value of the last filter output in case of success.
 
 ```scala
 pipeline.execute(4) {
   case Success(output) => println(output)// print to console: Sqrt: 2.0!
 }
```

### Word Count Example

##### Objective: count appearances of a word in text file
```UNIX
    cat "myText.txt" | grep "hello" | wc -l
```

##### Define filters
```scala
import com.cosmin.pipeline.{Filter, Pipeline}
import scala.io.Source

class Cat() extends Filter[String, Seq[String]]{
  override def execute: String => Seq[String] = file => Source.fromFile(file).getLines.toSeq
}

object Cat {
  def apply(): Cat = new Cat()
}

class Grep(word: String) extends Filter[Seq[String], Seq[String]] {
  override def execute: Seq[String] => Seq[String] = lines => lines.filter(_.contains(word))
}

object Grep {
  def apply(word: String): Grep = new Grep(word)
}

class Count extends Filter[Seq[String], Int] {
  override def execute: Seq[String] => Int = lines => lines.count(_ => true)
}

object Count {
  def apply(): Count = new Count()
}
```

##### Building the pipeline
```scala
import com.cosmin.pipeline.{Filter, Pipeline}
val pipeline = Pipeline[String, String]() | Cat() | Grep("hello") | Count()

```

##### Execute the pipeline
```scala
 pipeline.execute("myText.txt") {
    case Success(output) => println(s"word 'hello' was found on $output lines")
  }
```
Code above print to console 'word 'hello' was found on 3 lines' for the file https://github.com/cosminseceleanu/scala-pipeline/blob/master/myText.txt

