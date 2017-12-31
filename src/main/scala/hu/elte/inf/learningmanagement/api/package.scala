package hu.elte.inf.learningmanagement

import hu.elte.inf.learningmanagement.model.enum.SubmissionStatus
import org.joda.time.DateTime

package object api {

  final case class TaskListDto(taskId: Long, name: String, submissionStart: DateTime, submissionEnd: DateTime, submissionStatus: SubmissionStatus.EnumType)

  final case class TaskListDtoSeq(taskList: Seq[TaskListDto])

  final case class TaskDetailsDto(
      taskId: Long,
      name: String,
      submissionStart: DateTime,
      submissionEnd: DateTime,
      submissionStatus: SubmissionStatus.EnumType,
      answer: String
  )

  final case class SubmitSolutionDto(answer: String)

//  final case class LoginDto(username: String, password: String)
  final case class LoginDto(username: String)

}
