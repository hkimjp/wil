CREATE TABLE notes (
  id         SERIAL PRIMARY KEY,
  login      VARCHAR(20),
  date       CHAR(10),
  note       TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP);
