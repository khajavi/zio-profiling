package zio.profiling.examples

import zio.profiling.causal._
import zio.{URIO, _}

object CausalProfilerToyExample extends ZIOAppDefault {

  def run: URIO[Any, ExitCode] =
    CausalProfiler(iterations = 100, candidateSelector = Root / "program" / *).profile {
      val io = for {
        _    <- progressPoint("iteration start")
        short = ZIO.blocking(ZIO.succeed(Thread.sleep(40))) <# "short"
        long  = ZIO.blocking(ZIO.succeed(Thread.sleep(100))) <# "long"
        _    <- short.zipPar(long)
      } yield ()
      io.forever <# "program"
    }
      .flatMap(_.renderToFile("profile.coz"))
      .exitCode
}
