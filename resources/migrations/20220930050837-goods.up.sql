CREATE TABLE goods (
  id    SERIAL PRIMARY KEY,
  from_ VARCHAR(20),
  to_   VARCHAR(20),
  kind  int DEFAULT 1,
  note  TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);