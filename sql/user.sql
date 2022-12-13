--> PREPARE

CREATE TABLE IF NOT EXISTS USERS (
  name VARCHAR(50),
  email VARCHAR(50),
  cpf VARCHAR(14),
  password VARCHAR(20)
);

--> DROP

DROP TABLE USERS;

--> INSERT

INSERT INTO USERS (?, ?, ?, ?) VALUES (?, ?, ?, ?);

--> ALL

SELECT * FROM USERS;

--> USER

SELECT * FROM USERS WHERE ? = ?;

--> DELETE

DELETE FROM USERS WHERE ? = ?;