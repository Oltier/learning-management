package hu.elte.inf.learningmanagement.util

import hu.elte.inf.learningmanagement.model.Task
import org.joda.time.DateTime

object ExampleTasks {

  val taskOngoing1 = Task(
    name = "Learning management system",
    description = "Create an amazing learning management system!",
    submissionStart = DateTime.now().minusHours(1),
    submissionEnd = DateTime.now().plusDays(1)
  )

  val taskOngoing2 = Task(
    name = "Learning management Neptun",
    description = "Create a scalable learning management system called Neptun!",
    submissionStart = DateTime.now().minusHours(1),
    submissionEnd = DateTime.now().plusDays(1)
  )

  val taskNotStartedYet = Task(
    name = "Flying cars",
    description = "Create some flying cars",
    submissionStart = DateTime.now().plusDays(5),
    submissionEnd = DateTime.now().plusDays(10)
  )

  val taskExpired = Task(
    name = "Wheels",
    description = "Create something which makes our cars go easier",
    submissionStart = DateTime.now().minusDays(5),
    submissionEnd = DateTime.now().minusDays(10)
  )

  val tasks = Seq(taskOngoing1, taskOngoing2, taskNotStartedYet, taskExpired)

}
