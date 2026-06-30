# Comunicador

A Java EE web application that helps people with motor disabilities communicate by selecting images representing everyday objects.

## Demo

https://github.com/user-attachments/assets/bb31e303-5983-4836-af99-19ac14298a6e


## Technology Stack

- **Java EE 7** (EJB 3, JPA 2.1, JSF 2.2)
- **PrimeFaces** — UI component library
- **EclipseLink** — JPA provider
- **MySQL 5.x** — relational database
- **GlassFish 4.x** — application server

---

## Prerequisites

| Tool | Version |
|------|---------|
| JDK | 7 or later |
| GlassFish | 4.x |
| MySQL | 5.x or later |
| MySQL Connector/J | 5.x (for GlassFish) |

---

## Setup

### 1. Create the database

```sql
CREATE DATABASE comunicador CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON comunicador.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

> The app uses EclipseLink's `create-tables` DDL mode, so tables are created automatically on first deploy.

Insert an initial admin user (the app has no registration UI):

```sql
USE comunicador;
INSERT INTO usuarios (username, password) VALUES ('admin', 'admin');
```

### 2. Install the MySQL JDBC driver in GlassFish

Download **MySQL Connector/J 8.0.33** from Maven Central (8.x is required — 5.x is incompatible with MySQL 8/9):

```bash
curl -L "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar" -o mysql-connector-j-8.0.33.jar
cp mysql-connector-j-8.0.33.jar $GLASSFISH_HOME/lib/
cp mysql-connector-j-8.0.33.jar $GLASSFISH_HOME/domains/domain1/lib/
asadmin restart-domain domain1
```

### 3. Register the JDBC datasource

Connector/J 8.x uses a different datasource class than the one in `glassfish-resources.xml`, so create the pool via `asadmin` directly:

```bash
asadmin create-jdbc-connection-pool \
  --datasourceclassname com.mysql.cj.jdbc.MysqlDataSource \
  --restype javax.sql.DataSource \
  --property "serverName=localhost:portNumber=3306:databaseName=comunicador:user=root:password=admin:useSSL=false:allowPublicKeyRetrieval=true" \
  mysql_comunicador_rootPool

asadmin create-jdbc-resource \
  --connectionpoolid mysql_comunicador_rootPool \
  comunicadorDS

# Verify the connection
asadmin ping-connection-pool mysql_comunicador_rootPool
```

### 4. Build the WAR

The Ant build requires NetBeans libraries. Build the WAR manually instead:

```bash
GF=$GLASSFISH_HOME
GF_CP=$(find $GF/modules -name "*.jar" | tr '\n' ':')
PF=/path/to/primefaces-5.3.jar   # download from Maven Central

mkdir -p /tmp/build/WEB-INF/classes/META-INF /tmp/build/WEB-INF/classes/bundle /tmp/build/WEB-INF/lib

# Compile
find src/java -name "*.java" | xargs javac -source 1.7 -target 1.7 \
  -cp "$PF:$GF_CP" -d /tmp/build/WEB-INF/classes

# Copy resources
cp -r web/. /tmp/build/
cp src/conf/persistence.xml /tmp/build/WEB-INF/classes/META-INF/
cp src/java/bundle/bundle.properties /tmp/build/WEB-INF/classes/bundle/
cp $PF /tmp/build/WEB-INF/lib/

jar -cf comunicador2.war -C /tmp/build .
```

### 5. Deploy

Via the `asadmin` CLI:

```bash
asadmin deploy dist/comunicador2.war
```

Or drag the WAR into the GlassFish Admin Console (`http://localhost:4848`) under **Applications → Deploy**.

---

## Running the Application

Open a browser and navigate to:

```
http://localhost:9090/comunicador2/
```

You will be redirected to the login page. Use the credentials you inserted into the `usuarios` table (e.g. `admin` / `admin`).

### Application pages

| URL | Description |
|-----|-------------|
| `/comunicador2/login.xhtml` | Login page |
| `/comunicador2/welcome.xhtml` | Home (requires login) |
| `/comunicador2/admin/tematicas/index.xhtml` | Manage topics |
| `/comunicador2/admin/elementositeractivos/index.xhtml` | Manage interactive elements |

---

## Project Structure

```
comunicadorMartin/
├── src/
│   ├── java/
│   │   ├── ar/edu/unnoba/
│   │   │   ├── admin/backing/   # JSF Managed Beans
│   │   │   └── model/           # JPA entities
│   │   ├── DAO/                 # EJB Data Access Objects
│   │   └── Converter/           # JSF converters
│   └── conf/
│       └── persistence.xml
├── web/
│   ├── WEB-INF/
│   │   ├── web.xml
│   │   ├── faces-config.xml
│   │   └── beans.xml
│   ├── template.xhtml           # Page layout template
│   ├── login.xhtml
│   ├── welcome.xhtml
│   └── admin/
│       ├── tematicas/
│       └── elementositeractivos/
└── setup/
    └── glassfish-resources.xml  # JDBC datasource definition
```


---

## Notes

- The context root is `/comunicador2` (set in `web/WEB-INF/glassfish-web.xml`).
- Table auto-creation is enabled (`eclipselink.ddl-generation = create-tables`). Switch to `none` in `src/conf/persistence.xml` after the first deploy to prevent accidental table recreation.
- There is no password hashing — passwords are stored and compared in plain text. This is suitable for a university project but not for production use.
