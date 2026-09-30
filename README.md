# Transactions API

REST API desarrollada con Spring Boot para almacenar transacciones en memoria, consultar transacciones por tipo y calcular la suma total de una transacción junto con todas sus transacciones descendientes.

## Tecnologías

- Java 21
- Spring Boot
- Maven
- JUnit 5
- MockMvc
- Docker

No se utiliza ninguna base de datos. Las transacciones se almacenan en memoria mediante un `HashMap`.

---

## Requisitos

Para ejecutar el proyecto localmente se necesita:

- Java 21 o superior

No es necesario tener Maven instalado, ya que el proyecto incluye Maven Wrapper.

Para ejecutar utilizando Docker se necesita:

- Docker

---

## Ejecutar localmente

Clonar el repositorio:

```bash
git clone <repository-url>
```

Ingresar al directorio del proyecto:

```bash
cd transactions-app
```

### Linux / macOS

Dar permisos de ejecución al Maven Wrapper si fuera necesario:

```bash
chmod +x mvnw
```

Ejecutar la aplicación:

```bash
./mvnw spring-boot:run
```

### Windows

Ejecutar:

```powershell
.\mvnw.cmd spring-boot:run
```

La aplicación quedará disponible en:

```text
http://localhost:8080
```

---

## Ejecutar tests

### Linux / macOS

```bash
./mvnw test
```

### Windows

```powershell
.\mvnw.cmd test
```

El proyecto incluye tests de integración utilizando `MockMvc` para validar los principales endpoints de la API.

---

## Ejecutar con Docker

Construir la imagen desde la raíz del proyecto:

```bash
docker build -t transactions-app .
```

Ejecutar el contenedor:

```bash
docker run --name transactions-container -p 8080:8080 transactions-app
```

La API quedará disponible en:

```text
http://localhost:8080
```

Para verificar que el contenedor está ejecutándose:

```bash
docker ps
```

---

# API

## Crear una transacción

```http
PUT /transactions/{transaction_id}
```

Ejemplo:

```http
PUT /transactions/10
```

Body:

```json
{
  "amount": 5000,
  "type": "cars"
}
```

Respuesta:

```json
{
  "status": "ok"
}
```

Una transacción también puede tener una transacción padre:

```http
PUT /transactions/11
```

```json
{
  "amount": 10000,
  "type": "shopping",
  "parent_id": 10
}
```

---

## Obtener transacciones por tipo

```http
GET /transactions/types/{type}
```

Ejemplo:

```http
GET /transactions/types/shopping
```

Respuesta:

```json
[
  11,
  12
]
```

---

## Obtener suma de una transacción

```http
GET /transactions/sum/{transaction_id}
```

La suma incluye el monto de la transacción indicada y todas las transacciones descendientes conectadas mediante `parent_id`.

Por ejemplo, para:

```text
10 ($5000)
└── 11 ($10000)
    └── 12 ($5000)
```

La petición:

```http
GET /transactions/sum/10
```

devuelve:

```json
{
  "sum": 20000
}
```

Mientras que:

```http
GET /transactions/sum/11
```

devuelve:

```json
{
  "sum": 15000
}
```

---

## Almacenamiento

Las transacciones se almacenan únicamente en memoria.

Se utiliza:

```java
HashMap<Long, Transaction>
```

donde el identificador de la transacción funciona como clave.

Los datos se eliminan cuando la aplicación se detiene o reinicia.

---

## Estructura del proyecto

```text
src/
├── main/
│   └── java/
│       └── ...
│           ├── controller/
│           ├── dto/
│           ├── model/
│           ├── repository/
│           └── service/
│
└── test/
    └── java/
        └── TransactionIntegrationTest.java
```

La aplicación sigue una arquitectura en capas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
In-memory storage
```

El `Controller` se encarga de exponer los endpoints REST, el `Service` contiene la lógica de negocio y el `Repository` administra el almacenamiento en memoria.