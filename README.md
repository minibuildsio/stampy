# Stampy

Stampy is a simple stamp collection tracker app written to demo:
- Gradle integration tests and test containers: https://howardpaget.co.uk/posts/gradle-integration-tests/
- Flyway database migrations: https://www.minibuilds.io/posts/spring-boot-flyway-database-migration/
- Auth using Spring Security and JWTs: https://www.minibuilds.io/posts/spring-boot-jwt-auth/

The integration tests are in a separate folder to the unit tests called `integrationTest`. The Gradle related set up can be found in `build.gradle` under `testing`. The integration tests are set up to automatically run as part of the build (after the regular tests) but can separately by running the `ingtegrationTest` task.

## Run the project

### Start Postgres in a Docker container

```
docker-compose -f localenv/docker-compose.yaml up -d
```

### Run the bootRun task

```
./gradlew bootRun
```

## Database Migration via Flyway

Database migrations are managed by flyway which is run automatically on startup. Flyway will apply migrations (SQL scripts) in `src/main/resources/db/migration`, applied migrations are tracked in the table `flyway_schema_history`.

> Note on security: The migrations are run as a user that has owner privileges because the migrations need to create tables, drop tables, etc. The application itself runs as a less privileged user.