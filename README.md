# Product Manager

A Spring Boot 3 + JPA web application for managing products with Thymeleaf frontend.

## Features

- **CRUD** — Create, read, update, delete products
- **Search** — by product name (case-insensitive)
- **Filter** — by category or price range
- **Sort** — by price descending
- **Validation** — server-side form validation

## Tech Stack

| Layer      | Technology                        |
|------------|-----------------------------------|
| Backend    | Java 17+, Spring Boot 3.3.5       |
| Build      | Gradle 9.5.1 (Groovy DSL)         |
| Database   | PostgreSQL (production), H2 (dev) |
| ORM        | Spring Data JPA / Hibernate       |
| Frontend   | Thymeleaf, HTML, CSS              |
| Container  | Docker                            |

## Entity

```
Product
├── id          (Long, auto-generated)
├── name        (String, required)
├── category    (String, required)
├── price       (Integer, required)
├── description (String, max 2000 chars)
└── imageUrl    (String)
```

## API Routes

| Method | Path                         | Description            |
|--------|------------------------------|------------------------|
| GET    | `/`                          | Redirect to `/products`|
| GET    | `/products`                  | List all products      |
| GET    | `/products/new`              | Show add form          |
| POST   | `/products/save`             | Create / update product|
| GET    | `/products/{id}`             | View product detail    |
| GET    | `/products/edit/{id}`        | Show edit form         |
| GET    | `/products/delete/{id}`      | Delete product         |
| GET    | `/products/search?keyword=`  | Search by name         |
| GET    | `/products/category?category=`| Filter by category    |
| GET    | `/products/sort/price-desc`  | Sort by price ↓        |
| GET    | `/products/price-range?min=&max=` | Filter by price range|

## Local Development (H2)

```powershell
gradlew.bat bootJar -x test
$env:SPRING_PROFILES_ACTIVE="dev"
java -jar build\libs\product-manager-0.0.1-SNAPSHOT.jar
```

Or use `run.bat` (handles Unicode paths):

```powershell
run.bat
```

App: http://localhost:8080  
H2 Console: http://localhost:8080/h2-console (db: `jdbc:h2:mem:productdb`, user: `sa`)

## Railway Deployment (PostgreSQL)

The app auto-detects Railway environment variables (`PGHOST`, `PGPORT`, `PGDATABASE`, `PGUSER`, `PGPASSWORD`). Default profile (`application.properties`) is PostgreSQL-ready.

```json
// railway.json
{ "buildCommand": "./gradlew clean bootJar", "startCommand": "java -jar build/libs/*.jar" }
```

## Docker

```dockerfile
docker build -t product-manager .
docker run -p 8080:8080 -e SPRING_PROFILES_ACTIVE=dev product-manager
```

## Project Structure

```
product-manager/
├── build.gradle
├── settings.gradle
├── Dockerfile
├── railway.json
├── src/
│   ├── main/
│   │   ├── java/com/example/productmanager/
│   │   │   ├── ProductManagerApplication.java
│   │   │   ├── controller/ProductController.java
│   │   │   ├── service/ProductService.java
│   │   │   ├── repository/ProductRepository.java
│   │   │   └── entity/Product.java
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   ├── application-dev.properties
│   │   │   ├── static/css/style.css
│   │   │   └── templates/product/
│   │   │       ├── list.html
│   │   │       ├── form.html
│   │   │       └── detail.html
│   │   └── ...
│   └── test/
└── ...
```
