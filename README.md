# Virtual Teacher

Virtual Teacher is an online platform for tutoring. Users will be able to register as either a teacher or a student.

**Students** must be able to enroll for video courses, watch the available video lectures. After finishing each video within
a course, students will be asked to submit an assignment, which will be grated by the teacher.
After a successful completion of a course, the students must be able to rate it.

**Teachers** should be able to create courses and upload video lectures with assignments.Every user can become teacher 
only after approval from an administrator.

## Team Members
* Dimitar Tsanev - GitHub [https://github.com/DimitarTsanev]
* Karina Ivanova - GitHub [https://github.com/KarinaIvanova01]
* Preslav Marinov - GitHub [https://github.com/Pmarinov344]


## Areas
* **[Public part](#public-part)** - accessible without authentication.
* **[Private part](#private-part)** - must be accessible only if the user is authenticated.
* *[Students](#students)*
* *[Courses](#courses)*
* *[Teachers](#teachers)*
* **[Administration part](#administration-part)** - administrators must not be registered thought the ordinary registration process. 

### Public part
* Anonymous users must be able to register as students or teachers.
* The anonymous users must also be able to browse the catalog of available courses. The users must be able to filter the
catalog by course name, course topic, teacher and rating, as well as to sort the catalog by name and/or rating.

- photo
### Private part
#### Students
* Student must be able to view and edit their profile and change the password.
* Student must be able to enroll for courses and see their completed courses. -photo
* Student can take notes in plain text for each lecture.

#### Courses
* The video in a course must be accessible only after enrollment.
* Students must submit their work as a text file (txt, doc, docx, etc.).
* Students must be able to see the grade they've received for the assignment and their average grade for the course.
* Courses must be either drafts ot published. Draft courses are visible to the teachers.
* Courses could have a comments page, where students can comment on the lectures and ask questions.

#### Teachers
* Teachers have access to all the functionality that students do.
* Teachers must be able to modify courses and search for students. 

### Administration part
* They must be able to modify/delete courses and users.
* They can approve administrators and teachers.

## Optional features
* #### Email verification #### 
In order for the registration to be completed, the user must verify their email by clicking on a link sent to their email
by application. Before verify their email, users cannot make transactions.
* #### Refer a friend ####
A user can enter the email of people, not yet registered for the application, and invite them to register. The application
sends to that email a registration link.
* #### Course rating ####
Each course can be rated from 1 to 5 from the students.

## Technologies
* REST API
* Spring Web
* Thymeleaf
* HTML / CSS
* Javascript
* DataBase and SQL
* Hibernate
* Mockito 
![schema.png](src%2Fmain%2Fresources%2Fstatic%2Fimages%2Freadme%2Fschema.png)

## Notes
* MediaWiki Action API
* Unit test code coverage of the business logic
* Swagger documentation -
* To set up and run the project locally : 
1. create a database and fill the table;
2. run the application
3. go to localhost:8080