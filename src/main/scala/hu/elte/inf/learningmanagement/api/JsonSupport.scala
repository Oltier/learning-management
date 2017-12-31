package hu.elte.inf.learningmanagement.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol with JodaTimeJsonSupport {
  implicit val taskListDtoFormat: RootJsonFormat[TaskListDto] = jsonFormat5(TaskListDto)
  implicit val taskListDtoSeqFormat: RootJsonFormat[TaskListDtoSeq] = jsonFormat1(TaskListDtoSeq)
  implicit val taskDetailsDtoFormat: RootJsonFormat[TaskDetailsDto] = jsonFormat6(TaskDetailsDto)
  implicit val submitSolutionDto: RootJsonFormat[SubmitSolutionDto] = jsonFormat1(SubmitSolutionDto)
  implicit val loginDto: RootJsonFormat[LoginDto] = jsonFormat1(LoginDto)
}
