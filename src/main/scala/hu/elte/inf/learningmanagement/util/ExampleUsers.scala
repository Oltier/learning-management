package hu.elte.inf.learningmanagement.util

import hu.elte.inf.learningmanagement.model.User
import com.github.t3hnar.bcrypt.Password

object ExampleUsers {

  val user1 = User(
    userName = "ce0ta1",
    password = "12345".bcrypt
  )
  val user2 = User(
    userName = "ce0ta2",
    password = "123456".bcrypt
  )
  val user3 = User(
    userName = "ce0ta3",
    password = "1234567".bcrypt
  )

  val users = Seq(user1, user2, user3)

}
