# learning-management
ELTE Web Engineering learning management system repo

In order to run the project, you must first install sbt
https://www.scala-sbt.org/download.html

Once installed and the PATH variable is set up

- Open up a Terminal
- Navigate to the project library (which contains build.sbt)
- Enter `sbt run` in the terminal
- Open up a browser and navigate to `http://localhost:3000`

The application will initialize a H2 file database in the current user's own folder with the following path
~/zoltan-tudlik/learning-management/h2-db

- **Implemented** Main page The main page is a greeting page, no other functionality can be accessed without authentication.
- **Implemented** Login A student can log in with its username and password. (The users are already registered in the database. Please provide some username-password pairs on the page, to accomplish the login.)
- **Implemented** Listing page After authentication list the tasks belonging to the authenticated student.
- **Only backend is implemented** Solving a task Clicking on a task, a new page appears, where the student can see a detailed description of the task, and can give his/her answer in a multi-line text-box. Clicking on a “Submit” button, the student sends the answer to the system.
- **Implemented** Revoke submission If a task is already submitted it is indicated on the listing page in the task list beside the task name. The student can “Revoke submission” by clicking on a button.
- **Implemented** Date limit The submit and revoke functionality can be accessed in a date interval which is set for each subject independently. Please prefill the database with some available and outdated task.

