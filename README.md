# Coding Challenge
## What's Provided
A simple [Spring Boot](https://projects.spring.io/spring-boot/) web application has been created and bootstrapped 
with data. The application contains information about all employees at a company. On application start-up, an in-memory 
Mongo database is bootstrapped with a serialized snapshot of the database. While the application runs, the data may be
accessed and mutated in the database without impacting the snapshot.

### How to Run
The application may be executed by running `gradlew bootRun`.

### How to Use
The following endpoints are available to use:
```
* CREATE
    * HTTP Method: POST 
    * URL: localhost:8080/employee
    * PAYLOAD: Employee
    * RESPONSE: Employee
* READ
    * HTTP Method: GET 
    * URL: localhost:8080/employee/{id}
    * RESPONSE: Employee
* UPDATE
    * HTTP Method: PUT 
    * URL: localhost:8080/employee/{id}
    * PAYLOAD: Employee
    * RESPONSE: Employee
```
The Employee has a JSON schema of:
```json
{
  "type":"Employee",
  "properties": {
    "employeeId": {
      "type": "string"
    },
    "firstName": {
      "type": "string"
    },
    "lastName": {
          "type": "string"
    },
    "position": {
          "type": "string"
    },
    "department": {
          "type": "string"
    },
    "directReports": {
      "type": "array",
      "items" : "string"
    }
  }
}
```
For all endpoints that require an "id" in the URL, this is the "employeeId" field.

## What to Implement
Clone or download the repository, do not fork it.

### Task 1
Create a new type, ReportingStructure, that has two properties: employee and numberOfReports.

For the field "numberOfReports", this should equal the total number of reports under a given employee. The number of 
reports is determined to be the number of directReports for an employee and all of their distinct reports. For example, 
given the following employee structure:
```
                    John Lennon
                /               \
         Paul McCartney         Ringo Starr
                               /        \
                          Pete Best     George Harrison
```
The numberOfReports for employee John Lennon (employeeId: 16a596ae-edd3-4847-99fe-c4518e82c86f) would be equal to 4. 

This new type should have a new REST endpoint created for it. This new endpoint should accept an employeeId and return 
the fully filled out ReportingStructure for the specified employeeId. The values should be computed on the fly and will 
not be persisted.

### Task 2
Create a new type, Compensation. A Compensation has the following fields: employee, salary, and effectiveDate. Create 
two new Compensation REST endpoints. One to create and one to read by employeeId. These should persist and query the 
Compensation from the persistence layer.

## Delivery
Please upload your results to a publicly accessible Git repo. Free ones are provided by Github and Bitbucket.

---

## Some notes on the challenge (by Jason):
Upon completing the listed tasks, defined within the README.md file, I ran into some possible issues with Task 2 
specifically that I would like to address.

Problem: If an Employee is passed as a query param into the new Compensation REST endpoints, thereby persisting 
the Compensation entity in which the Employee is composed by, we will be redundantly persisting this data into 
the new Collection (Compensation) by default, which could pose future data synchronization issues.

Problem Example: An employee is promoted and their compensation is updated within the Compensation entity along 
with their new position, but the position is not updated within the Employee entity (or Collection), causing inconsistent data 
that could negatively affect a U.I. or external system that the HR department utilizes.

Solution Proposal: I would suggest the best course of action would be to persist the Compensation data, 
embedded within the Employee documents, so the data is represented as one aggregate unit. The reason I say 
this is that if we persist the Compensation data in a separate collection 
(linked to an Employee via the employeeId property to prevent redundancy), 
we cannot simply write JOIN statements to later query the data in a relational sense. Aggregations could 
be an option or Map-Reduce functionality, both storing into a materialized view, but they tend to be resource 
intensive and not very performant. Embedding the data currently
doesn’t seem problematic since the relationship of an Employee to Compensation should be one to one.

All of this being said, I went along with just persisting the Employee data redundantly because I felt 
I may be looking too deep into the assignment. If this problem was something I was intended to address, 
if you allow, I will go back and refactor my solution.