CREATE TABLE IF NOT EXISTS cards (
    card_id INTEGER PRIMARY KEY,
    pokemon_name VARCHAR(250) UNIQUE NOT NULL,
    pokemon_type VARCHAR(20) NOT NULL,
    pokemon_hp INTEGER NOT NULL,
    evolved_from VARCHAR(250),
    image VARCHAR(250)
);