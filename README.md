# J2E-TP6 — Spring Boot et Swagger : documentation d'API

Implémentation du **TP 6** du cours *Architecture Microservices : Conception, Déploiement et Orchestration* (MLIAEdu).

API REST de gestion d'étudiants construite avec **Spring Boot 3**, **Spring Data JPA**,
**JUnit 5 + Mockito** et documentée automatiquement avec **springdoc-openapi (Swagger UI)**.

> Le code, les commentaires et les noms enregistrés en base (table `students`, colonnes
> `last_name` / `first_name` / `birth_date`) sont en **anglais**. Seule cette documentation est en français.

## Objectifs couverts

| Étape du TP | Où |
|---|---|
| 1. Projet Spring Initializr | `pom.xml` |
| 2. Analyse du `pom.xml` | `pom.xml` (starters web, data-jpa, validation, test) |
| 3. Configuration MySQL | `src/main/resources/application.properties` |
| 4. Entité et Repository | `entity/Student.java`, `repository/StudentRepository.java` |
| 5. Services et contrôleurs REST | `service/StudentService.java`, `controller/StudentController.java` |
| 6. Lancer l'application | `StudentManagementApplication.java` |
| 7. Tester les endpoints | section [Endpoints](#endpoints) |
| 8. Tests JUnit 5 + Mockito | `src/test/java` |
| 9. Intégration Swagger | dépendance springdoc + `config/OpenApiConfig.java` |

## Structure

```
J2E-TP6-Swagger/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/example/studentmanagement/
    │   │   ├── StudentManagementApplication.java
    │   │   ├── controller/StudentController.java   # @RestController
    │   │   ├── entity/Student.java                 # @Entity (table: students)
    │   │   ├── repository/StudentRepository.java   # JpaRepository + @Query
    │   │   ├── service/StudentService.java         # @Service, transactionnel
    │   │   └── config/
    │   │       ├── OpenApiConfig.java              # métadonnées Swagger
    │   │       ├── CorsConfig.java                 # accès depuis le client React (TP9)
    │   │       └── DataInitializer.java            # jeu de données de démo
    │   └── resources/application.properties        # MySQL + springdoc
    └── test/
        ├── java/com/example/studentmanagement/
        │   ├── StudentControllerTest.java          # Mockito, aucun accès BDD
        │   └── StudentRepositoryTest.java          # H2 en mémoire
        └── resources/application-test.properties
```

## Modèle de données

```
students
--------
id          INT (PK, auto-increment)
last_name   VARCHAR(60) NOT NULL
first_name  VARCHAR(60) NOT NULL
birth_date  DATE
```

## Prérequis

- JDK 17+
- Maven 3.8+
- MySQL 8 (ou MariaDB) pour l'exécution « réelle »
- Aucune base requise pour les tests : H2 en mémoire

## Configuration

```sql
CREATE DATABASE studentdb CHARACTER SET utf8mb4;
```

`src/main/resources/application.properties` :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/studentdb?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
server.port=8080
```

> `ddl-auto=update` laisse Hibernate créer la table `students` à partir de l'entité.

## Exécution

```bash
mvn spring-boot:run      # démarre sur http://localhost:8080
mvn test                 # tests unitaires + tests H2
```

Au démarrage, `DataInitializer` insère 3 étudiants si la table est vide.

## Endpoints

| Méthode | URL | Code | Description |
|---|---|---|---|
| POST | `/students/save` | 201 | Créer un étudiant |
| GET | `/students/all` | 200 | Lister tous les étudiants |
| GET | `/students/{id}` | 200 / 404 | Trouver un étudiant |
| PUT | `/students/{id}` | 200 / 404 | Mettre à jour un étudiant |
| DELETE | `/students/delete/{id}` | 204 / 404 | Supprimer un étudiant |
| GET | `/students/count` | 200 | Nombre total d'étudiants |
| GET | `/students/byYear` | 200 | Répartition par année de naissance |
| GET | `/students/countByYear?year=1985` | 200 | Étudiants nés une année donnée |
| GET | `/students/search?lastName=ben` | 200 | Recherche par nom (insensible à la casse) |

### Exemples

```bash
# CREATE
curl -X POST http://localhost:8080/students/save \
  -H "Content-Type: application/json" \
  -d '{"lastName":"LACHGAR","firstName":"Mohamed","birthDate":"1985-09-01"}'
# -> 201 Created

# READ ALL
curl http://localhost:8080/students/all

# COUNT
curl http://localhost:8080/students/count
# -> 3

# BY YEAR  -> [[1985,2],[1990,1]]
curl http://localhost:8080/students/byYear

# DELETE
curl -X DELETE http://localhost:8080/students/delete/1
# -> 204 No Content
```

## Swagger

- Swagger UI : <http://localhost:8080/swagger-ui.html>
- OpenAPI JSON : <http://localhost:8080/v3/api-docs>

Chaque endpoint est annoté (`@Operation`, `@ApiResponse`, `@Schema`) : la documentation est
donc toujours synchronisée avec le code.

## Tests

| Classe | Type | Contenu |
|---|---|---|
| `StudentControllerTest` | Unitaire (Mockito) | 201, 204/404, listes, comptage, mise à jour, « service jamais appelé » sur 404 |
| `StudentRepositoryTest` | Intégration (`@DataJpaTest` + H2) | persistance, agrégation `group by`, recherche par nom |

```
Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
```

## Notes d'implémentation

- **Injection par constructeur** partout (aucun `@Autowired` sur les champs)
- `StudentService` est `@Transactional(readOnly = true)` au niveau classe,
  avec `@Transactional` sur les méthodes d'écriture
- Le `@Profile("!test")` du `DataInitializer` évite de polluer les tests
- `CorsConfig` autorise `http://localhost:*`, ce qui permet au client React du TP 9 de consommer l'API
