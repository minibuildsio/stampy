# Run the project

### Start Postgres in a Docker container

```
docker run --name stampydb -d -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=stampydb postgres
```

### Run the bootRun task

```
./gradlew bootRun
```