Interview question
==================

# Course Registration - A Spring Boot App

This is a very basic spring-boot app to create courses and register participants in courses.

# REST APIs
A course entity has a `title`, `startDate`, `endDate`, `capacity`, and `remainingPlaces`.

### Create course (POST http://localhost:5000/courses)
with body
```json
{
  "title": "Course title",
  "startDate": "2021-05-01",
  "endDate": "2021-05-05",
  "capacity": 10
}
```
Response will be 201:
```json
{
  "id": 1,
  "title": "Course title",
  "startDate": "2021-05-01",
  "endDate": "2021-05-05",
  "capacity": 10,
  "remaining": 10
}
```

### Search course by title (GET http://localhost:5000/courses?q=title)
Response will be 200 with body :
```json
[
  {
  "id": 1,
  "title": "Course title",
  "startDate": "2021-05-01",
  "endDate": "2021-05-05",
  "capacity": 10,
  "remaining": 10
  },
  ...
]
```
### Get course details (GET http://localhost:5000/courses/1)
Response will be 200 with body :
```json
  {
  "id": 1,
  "title": "Course title",
  "startDate": "2021-05-01",
  "endDate": "2021-05-05",
  "capacity": 10,
  "remaining": 5,
  "participants":[
    {"name":"Daniel", "registrationDate":"2021-05-01"},
    ...
  ]
  },
```

### Sign up user for course (POST http://localhost:5000/courses/1/add)
Body should be user details:
```json
{
  "courseId": 1,
  "registrationDate": "2021-04-01",
  "name": "Daniel"
}
```
Response will be: 
* 200 if registration was successful, and a response body similar to get course details request.
* 400 if `name` already enrolled to the course.
* 400 if `registrationDate` is 3 days before course `startDate` or after.
* 400 if course is full.
* 404 if course does not exist.

### Cancel user enrollment (POST http://localhost:5000/courses/1/remove)
Body should be user details:
```json
{
  "courseId": 1,
  "cancelDate": "2021-05-01",
  "name": "Daniel"
}
```
Response will be: 
* 200 if cancellation was successful, and a response body similar to get course details request.
* 404 if course does not exist or user is not enrolled to course.
* 400 if `cancelDate` is 3 days before course `startDate` or after.

## Guidelines
* Run the application using the command `mvn spring-boot:run`
* Uncomment the lines within methods in com/example/demo/course/CourseControllerTests.java to run the tests separately.
