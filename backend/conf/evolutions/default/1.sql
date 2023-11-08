-- Roles schema

-- !Ups

CREATE TABLE roles(
   role_id serial PRIMARY KEY,
   role_name VARCHAR (255) UNIQUE NOT NULL
);

-- !Downs

DROP TABLE roles;;