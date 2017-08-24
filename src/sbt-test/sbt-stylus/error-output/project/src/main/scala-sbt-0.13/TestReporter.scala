import java.util.concurrent.atomic.AtomicInteger

import sbt._

class TestLogger(target: File) extends Logger {
  val unrecognisedInputCount = new AtomicInteger(0)
  def trace(t: => Throwable): Unit = {}
  def success(message: => String): Unit = {}
  def log(level: Level.Value, message: => String): Unit = {
    if (level == Level.Error) {
      if (message.contains("""ParseError: expected "indent", got "=="""")) {
        if (unrecognisedInputCount.addAndGet(1) == 1) {
          IO.touch(target / "unrecognised-input-error")
        }
      }
    }
  }
}

class TestReporter(target: File) extends LoggerReporter(-1, new TestLogger(target))
