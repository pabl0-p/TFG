CREATE DATABASE JLINK_APP;

CREATE TABLE IMAGE(
    id SERIAL PRIMARY KEY, 
    FILENAME varchar(100), 
    TITLE varchar(50), 
    DESCRIPTION varchar(300),
    STORAGE_DATE varchar(100)
);