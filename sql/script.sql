# Criando banco de dados
CREATE DATABASE users;

use users;

# Criando tabelass
CREATE TABLE aluno(
	aluno_id INT NOT NULL AUTO_INCREMENT,
    telegram_code LONG NOT NULL,
    nome VARCHAR(120),
    usuario_siga VARCHAR(120),
    senha_siga VARCHAR(120),
    ultima_atualizacao DATE,
    ativo TINYINT DEFAULT 1,
    PRIMARY KEY (aluno_id)
);


CREATE TABLE materia(
	materia_id INT NOT NULL AUTO_INCREMENT,
	nome VARCHAR(120),
    PRIMARY KEY (materia_id)
);


# Criando tabela com chave primaria/estrangeira
CREATE TABLE aula(
	aula_id INT NOT NULL AUTO_INCREMENT,
    materia_id INT NOT NULL,
    dia_semana INT NOT NULL,
    quantidade INT NOT NULL,
    PRIMARY KEY (aula_id, materia_id), 
    FOREIGN KEY (materia_id) REFERENCES materia (materia_id)
);


CREATE TABLE matricula(
	matricula_id INT NOT NULL AUTO_INCREMENT,
    aluno_id INT NOT NULL,
    materia_id INT NOT NULL,
    FOREIGN KEY (aluno_id) REFERENCES aluno (aluno_id),
    FOREIGN KEY (materia_id) REFERENCES materia (materia_id),
    PRIMARY KEY (matricula_id)
);

CREATE TABLE falta (
	falta_id INT NOT NULL AUTO_INCREMENT,
    matricula_id INT NOT NULL,
    data_falta DATE NOT NULL,
    quantidade INT NOT NULL,
    PRIMARY KEY (falta_id, matricula_id), 
    FOREIGN KEY (matricula_id) REFERENCES matricula (matricula_id)
); 

CREATE TABLE nota(
	nota_id INT NOT NULL AUTO_INCREMENT,
    matricula_id INT NOT NULL,
	nota FLOAT NOT NULL,
    PRIMARY KEY (nota_id, matricula_id), 
    FOREIGN KEY (matricula_id) REFERENCES matricula (matricula_id)
);