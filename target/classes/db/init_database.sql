CREATE TABLE TODO (
  ID BIGINT,
  NAME VARCHAR(255),
  OWNER VARCHAR(255),
  PRIORITY VARCHAR(255)
);

INSERT INTO TODO (id, name, owner, priority)
VALUES (1, 'First Todo list', 'Adi', 'High');