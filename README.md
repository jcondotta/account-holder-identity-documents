# Recipients Project v.1.0

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=jcondotta_account-holder-identity-documents&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jcondotta_account-holder-identity-documents)    
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=jcondotta_account-holder-identity-documents&metric=coverage)](https://sonarcloud.io/summary/new_code?id=jcondotta_account-holder-identity-documents)

This project is part of a microservice architecture responsible for managing account holder identity documents. The service provides RESTful APIs for uploading, validating, and retrieving identity documents for secure, streamlined account holder verification. The system leverages AWS services and modern software practices to ensure security, scalability, and maintainability.

## Tech Stack

### Languages & Frameworks:

- **Java 17:** Core programming language.
- **Micronaut 4.5.0+:** Framework used to build the microservice with lightweight, fast startup times and cloud-native capabilities.

### Infrastructure:

- **Amazon S3:** Storage for uploaded identity documents.
- **AWS Lambda:** Serverless compute platform for running the microservice.
- **AWS API Gateway:** Gateway for exposing and managing the API endpoints.
- **Terraform:** Infrastructure as Code (IaC) tool used for managing AWS resources like DynamoDB, Lambda, and API Gateway.
- **LocalStack:** A fully functional local AWS cloud stack used for local testing of AWS services like S3 and Lambda.

### CI/CD & Containerization:

- **GitHub Actions:** Automated pipeline for building, testing, and deploying the microservice.

### Testing:

- **JUnit 5:** Framework for unit and integration testing.
- **Mockito:** Framework for mocking dependencies in tests.
- **AssertJ:** Library for fluent assertion statements.
- **TestContainers:** Library used to spin up containers for integration testing with services like DynamoDB, SQS, and LocalStack.

### Documentation:

- **Swagger API:** API documentation and testing interface to explore the RESTful endpoints.

## Features

- **Identity Document Management:** Upload, validate, and retrieve account holder identity documents.
- **Infrastructure as Code:** AWS infrastructure is managed and deployed using Terraform.
- **Local Testing:** Fully local development setup using JUnit 5, Mockito, AssertJ, LocalStack and TestContainers.
- **CI/CD Pipeline:** GitHub Actions for continuous integration and deployment.