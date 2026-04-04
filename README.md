
## Demo project of migration from the legacy web application to Spring Boot
See [presentation slides](https://miro.com/app/board/uXjVG7ZzHdE=/)

See migrations:
* step-by-step https://github.com/seregamorph/petclinic-legacy/pull/2
* in one go with AI https://github.com/seregamorph/petclinic-legacy/pull/1

REST API

```
┌────────────────┬───────────────────────┬─────────────────────────────────────┐
│     Method     │         Path          │             Description             │
├────────────────┼───────────────────────┼─────────────────────────────────────┤
│ GET/POST       │ /api/owners           │ list / create                       │
├────────────────┼───────────────────────┼─────────────────────────────────────┤
│ GET/PUT/DELETE │ /api/owners/{id}      │ get / update / delete               │
├────────────────┼───────────────────────┼─────────────────────────────────────┤
│ GET/POST       │ /api/pets[?ownerId=N] │ list (optionally by owner) / create │
├────────────────┼───────────────────────┼─────────────────────────────────────┤
│ GET/PUT/DELETE │ /api/pets/{id}        │ get / update / delete               │
├────────────────┼───────────────────────┼─────────────────────────────────────┤
│ GET/POST       │ /api/vets             │ list / create                       │
├────────────────┼───────────────────────┼─────────────────────────────────────┤
│ GET/PUT/DELETE │ /api/vets/{id}        │ get / update / delete               │
├────────────────┼───────────────────────┼─────────────────────────────────────┤
│ GET/POST       │ /api/visits[?petId=N] │ list (optionally by pet) / create   │
├────────────────┼───────────────────────┼─────────────────────────────────────┤
│ GET/PUT/DELETE │ /api/visits/{id}      │ get / update / delete               │
└────────────────┴───────────────────────┴─────────────────────────────────────┘
```

## To run

### start postgres
```shell
docker compose up -d
```

### deploy to Tomcat 10+
```shell
cp target/petclinic.war $CATALINA_HOME/webapps/
```

### Run e2e tests
(requires started docker compose)
```shell
mvn clean verify
```

### Test
curl http://localhost:8080/petclinic/api/owners
curl http://localhost:8080/petclinic/api/vets

DB credentials live in `src/main/resources/db.properties` and can be overridden with DB_URL, DB_USERNAME, DB_PASSWORD environment variables.

