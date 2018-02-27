CREATE DATABASE clients_db
DEFAULT CHARACTER SET utf8
DEFAULT COLLATE utf8_general_ci;

USE clients_db;

CREATE TABLE clients(
	id VARCHAR(120) NOT NULL,
    name_client VARCHAR(120) NOT NULL,
    siga_hash VARCHAR(120) NOT NULL,
    PRIMARY KEY (id)
)DEFAULT CHARSET = utf8;
