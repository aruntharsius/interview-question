Course Registration System
==========================

This project is a collection of REST APIs that implement a Course Registration System. 

# Description

REST APIs available in this project can do the following - 

* Create courses with a start date, end date and total capacity. 
* Register users for courses within 3 days of course start date. 
* Enroll or cancel users from a course unless the date is within 3 days of starting date. 
* Enroll a user only if the total capacity for the course is not full.

# Usage

* This is a spring-boot application that makes use of ![Java](https://img.shields.io/badge/java-1.8-blue)  ![MIT License](https://img.shields.io/badge/maven-3.8.1-blue) Please ensure that both are installed. 
* This project also uses ![Lombok](https://img.shields.io/badge/lombok-1.18.10-blue) Please install Lombok plugin in your IDE, it helps reduce the amount of ‘boilerplate code’.
* Build the project using `mvn clean install`
* Run it (using `mvn spring-boot:run`) or your favorite IDE.
* Javadoc is available in directory `/doc`

# API Documentation

Details about the APIs are available in [API Docs](https://github.com/aruntharsius/interview-question/blob/master/api_doc.html)

# Test

* This project uses ![JUnit Jupiter](https://img.shields.io/badge/JUnit-Jupter-blue) for unit testing. 
* Test the project (using `mvn clean test`) or your favorite IDE.
