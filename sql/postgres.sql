CREATE SEQUENCE aluno_seq;

CREATE TABLE aluno (
    aluno_id INT NOT NULL DEFAULT NEXTVAL ('aluno_seq'),
    telegram_code VARCHAR(120) UNIQUE NOT NULL,
    nome VARCHAR(120),
    usuario_siga VARCHAR(120),
    senha_siga VARCHAR(120),
    ultima_atualizacao DATE DEFAULT CURRENT_DATE,
    ativo SMALLINT DEFAULT 1,
    PRIMARY KEY (aluno_id)
);

CREATE SEQUENCE materia_seq;

CREATE TABLE materia (
	materia_id INT NOT NULL DEFAULT NEXTVAL ('materia_seq'),
	nome VARCHAR(120),
    PRIMARY KEY (materia_id)
);

CREATE SEQUENCE aula_seq;

CREATE TABLE aula (
    aula_id INT NOT NULL DEFAULT NEXTVAL ('aula_seq'),
    materia_id INT NOT NULL,
    dia_semana INT NOT NULL,
    quantidade INT NOT NULL,
    FOREIGN KEY (materia_id) REFERENCES materia (materia_id),
    PRIMARY KEY (aula_id, materia_id)
);

CREATE SEQUENCE matricula_seq;

CREATE TABLE matricula (
    matricula_id INT NOT NULL DEFAULT NEXTVAL ('matricula_seq'),
    aluno_id INT NOT NULL,
    materia_id INT NOT NULL,
    FOREIGN KEY (aluno_id) REFERENCES aluno (aluno_id),
    FOREIGN KEY (materia_id) REFERENCES materia (materia_id),
    PRIMARY KEY (matricula_id)
);

CREATE SEQUENCE falta_seq;

CREATE TABLE falta (
    falta_id INT NOT NULL DEFAULT NEXTVAL ('falta_seq'),
    matricula_id INT NOT NULL,
    data_falta DATE NOT NULL,
    quantidade INT NOT NULL, 
    FOREIGN KEY (matricula_id) REFERENCES matricula (matricula_id),
    PRIMARY KEY (falta_id, matricula_id)
); 

CREATE SEQUENCE nota_seq;

CREATE TABLE nota (
    nota_id INT NOT NULL DEFAULT NEXTVAL ('nota_seq'),
    matricula_id INT NOT NULL,
    nota FLOAT NOT NULL, 
    FOREIGN KEY (matricula_id) REFERENCES matricula (matricula_id),
    PRIMARY KEY (nota_id, matricula_id)
);
