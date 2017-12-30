package hu.elte.inf.learningmanagement.model

import hu.elte.inf.learningmanagement.model.enum.SubmissionStatus


final case class UserTask(userId: Long, taskId: Long, submissionStatus: SubmissionStatus.EnumType = SubmissionStatus.NOT_SUBMITTED, answer: String = "")