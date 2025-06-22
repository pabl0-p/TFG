# JPEG-Linked-Media-Format-JLINK-Applications

1. Run psql

```
docker run -d \  --name psql-server \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=JLINK_APP \
  -p 5432:5432 \
  postgres:13.1
```

2. Create table

```
docker exec -it psql-server sh

psql -U postgres -d JLINK_APP

CREATE TABLE IMAGE(
    id SERIAL PRIMARY KEY, 
    FILENAME varchar(100), 
    TITLE varchar(50), 
    DESCRIPTION varchar(300),
    STORAGE_DATE varchar(100)
);
```

3. Build app

```
mvn clean package

docker build -t mipams/jlink-web-app:2.0 .

docker run -d -p 8080:8080 --link psql-server:postgres  --name app mipams/jlink-web-app:2.0
```