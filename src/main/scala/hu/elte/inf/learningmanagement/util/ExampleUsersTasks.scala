package hu.elte.inf.learningmanagement.util

import hu.elte.inf.learningmanagement.model.UserTask
import hu.elte.inf.learningmanagement.model.enum.SubmissionStatus

object ExampleUsersTasks {

  val notSubmittedTask1User1 = UserTask(
    userId = 1,
    taskId = 1
  )

  val notSubmittedTask1User3 = UserTask(
    userId = 3,
    taskId = 1
  )

  val notSubmittedTask1User2 = UserTask(
    userId = 2,
    taskId = 1
  )

  val submittedTask2User1 = UserTask(
    userId = 1,
    taskId = 2,
    submissionStatus = SubmissionStatus.SUBMITTED,
    answer = "Neptun is probably not the best ida for this, because it's totally not Scalable."
  )

  val notSubmittedTask3User1 = UserTask(
    userId = 1,
    taskId = 3
  )

  val notSubmittedTask2User2 = UserTask(
    userId = 2,
    taskId = 2
  )

  val SubmittedTask4User1 = UserTask(
    userId = 1,
    taskId = 4,
    submissionStatus = SubmissionStatus.SUBMITTED,
    answer = "I firmly believe that creating something round-ish stuff will be an amazing idea."
  )

  val usersTasks = Seq(
    notSubmittedTask1User1,
    notSubmittedTask1User3,
    notSubmittedTask1User2,
    submittedTask2User1,
    notSubmittedTask3User1,
    notSubmittedTask2User2,
    SubmittedTask4User1
  )
}
