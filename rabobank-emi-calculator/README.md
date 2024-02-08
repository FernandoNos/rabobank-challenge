# Rabobank FullStack Technical Assignment

# Project

Developed using:

- Java;
- Spring Boot 3.0.6;
- SQLite;

## Product Decisions

- From the description, the understanding was that `loan value`, `yearly interest rate`, and `loan term in years`, could
  not be equal to 0;
- It was assumed that the product had the potential to support different kinds of loan estimate calculations;

# Technicalities

## Details

- The endpoint `/loans/emi/estimates` was used to support the functionality described, given that it not only calculates
  but also modifies the state of the system by storing a new entry for the user;
- With the future of the system in mind, it was decided to centralize the logic for the actual calculation in strategies
  under `com.rabobank.loans.domain.strategies`, making it easy to expand the number of calculations supported;
- A new user is created when a request for a new estimate is received. Ideally, the user would be pre-existing, and this
  wouldn't be a necessary step;
- The value object `CalculateLoanEstimate` as a way to create an internal abstraction layer that can be re-utilized by
  other flows within this microservice;

## Database Schema

The tables `User` and `Calculation` have a `nx1` relationship, meaning that one user can have multiple estimates;

### Table Loan Estimate

Represents the loan estimate entries.

- **id**: Long that uniquely identifies this entry;
- **value**: result of the calculation;
- **type**: type of the loan that was calculated (so far, only EMI);
- **user_id**: id of the user for whom the loan was estimated;
- **created_at**: date when the estimate was created;

----

### Table User

Represents the user.

- **id:**: Long that uniquely identifies this user;
- **email**: unique email associated with the user;
- **created_at**: date when the user was first created;

## REST endpoints

### Create Estimate

#### Request

```
POST /loans/emi/estimates HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{
    "loan_value": 1000,
    "yearly_interest_rate": 5,
    "loan_term_in_years": 5,
    "email":"test@test.com"
}
```

#### Response

```
{
    "value": 16.69
}
```

## How to run

Ensure you have Docker installed.

Run the file name `run.sh` at the root level of the project.

Or, if you'd like to run it without Docker, make sure you update `src/main/resources/application.yml` so that instead
of `url: jdbc:sqlite:/usr/app/data/mydb.sqlite` it contain `url: jdbc:sqlite:mydb.sqlite`, then run the project.

## Next steps

- Review naming within project to ensure it's standardized and clear;
- Understand whether big values for `loan value` can pose an issue from a product perspective, or if the current setup
  suffices;
- Audit logs - which can then be used to keep track of all the changes done, and ensure illegal behaviors are
  identified;
- Events - given the information in this service is important, and can be used to drive multiple other internal flows,
  it would be interesting to have async events published into the environment, allowing other domains to react to
  loan estimates related updates;
- Replace the current solution, which relies on exceptions to inform the user of business validations
  with [Either](https://apidocs.arrow-kt.io/arrow-core/arrow.core/-either/index.html), so that exceptions can be used
  for exceptional scenarios only;
- As part of this project, it was decided to use SQLite as database. Given its rather limited features, it'd be
  interesting to move into a more robust database;
- Expand our test suite to include proper integration tests;
- Authentication - it was not included as part of this MVP1, which poses a great threat to the reliability of our
  system. For this version, it is advisable to make it only accessible internally, as a first step;
- As stated before, entries in the `User` table are created along with `Loan Calculations`. It would be interesting that
  a
  separate set of endpoints is created, so that the `create estimate` endpoint is not responsible for anything except
  creating estimates;
- Include logs;
- Depending on the amount of users, it would be interesting to expand the stack so that it can better handle back
  pressure, for example, by using [WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html);
- Make the loan type part of the request, so that the user can choose what algorithm to use;