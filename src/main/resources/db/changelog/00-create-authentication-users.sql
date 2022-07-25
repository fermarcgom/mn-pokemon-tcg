CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(250) UNIQUE NOT NULL,
    password VARCHAR(200) NOT NULL
);

INSERT INTO users (email, password)
VALUES
    ('fermarcgom@example.com', 'password')