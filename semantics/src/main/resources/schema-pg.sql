DROP TABLE IF EXISTS billionaires;

CREATE TABLE billionaires (
  id SERIAL  PRIMARY KEY,
  first_name VARCHAR(250) NOT NULL,
  last_name VARCHAR(250) NOT NULL,
  career VARCHAR(250) DEFAULT NULL
);
