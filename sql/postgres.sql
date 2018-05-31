-- Modelo físico
-- Banco de dados: Postgresql

CREATE SEQUENCE alu_seq;

CREATE TABLE ALUNO (
    alu_id INT NOT NULL DEFAULT NEXTVAL ('alu_seq'),
    alu_telegram_code VARCHAR(120) UNIQUE NOT NULL,
    alu_nome VARCHAR(120),
    alu_usuario_siga VARCHAR(120),
    alu_senha_siga VARCHAR(120),
    alu_ultima_atualizacao DATE DEFAULT CURRENT_DATE,
    alu_ativo SMALLINT DEFAULT 1,

    PRIMARY KEY (alu_id)
);

CREATE SEQUENCE mat_seq;

CREATE TABLE MATERIA (
    mat_id INT NOT NULL DEFAULT NEXTVAL ('mat_seq'),
    mat_nome VARCHAR(120),

    PRIMARY KEY (mat_id)
);

CREATE SEQUENCE aul_seq;

CREATE TABLE AULA (
    aul_id INT NOT NULL DEFAULT NEXTVAL ('aul_seq'),
    mat_id INT NOT NULL,
    aul_dia_semana INT NOT NULL,
    aul_quantidade INT NOT NULL,

    FOREIGN KEY (mat_id) REFERENCES materia (mat_id),
    PRIMARY KEY (aul_id, mat_id)
);

CREATE SEQUENCE matri_seq;

CREATE TABLE MATRICULA (
    matri_id INT NOT NULL DEFAULT NEXTVAL ('matri_seq'),
    alu_id INT NOT NULL,
    mat_id INT NOT NULL,

    FOREIGN KEY (alu_id) REFERENCES aluno (alu_id),
    FOREIGN KEY (mat_id) REFERENCES materia (mat_id),
    PRIMARY KEY (matri_id)
);

CREATE SEQUENCE fal_seq;

CREATE TABLE FALTA (
    fal_id INT NOT NULL DEFAULT NEXTVAL ('fal_seq'),
    matri_id INT NOT NULL,
    fal_data DATE NOT NULL,    
    fal_quantidade INT NOT NULL,

    FOREIGN KEY (matri_id) REFERENCES matricula (matri_id),
    PRIMARY KEY (fal_id, matri_id)
); 

CREATE SEQUENCE not_seq;

CREATE TABLE NOTA (
    not_id INT NOT NULL DEFAULT NEXTVAL ('not_seq'),
    matri_id INT NOT NULL,
    not_valor FLOAT NOT NULL,

    FOREIGN KEY (matri_id) REFERENCES matricula (matri_id),
    PRIMARY KEY (not_id, matri_id)
);

COMMIT;

-- Triggers

-- Criando Trigger para atualizar a data da última atualização sempre que o 'alu_ativo' mudar
-- Definindo a função que será executada pela Trigger
CREATE FUNCTION update_date() RETURNS TRIGGER AS $$
BEGIN
	UPDATE aluno SET alu_ultima_atualizacao=now() WHERE alu_id = NEW.alu_id;
	RETURN NEW;
END; $$
LANGUAGE plpgsql;

-- Criando a Trigger que fará a execução da função definida anteriormente.
-- Este é executada sempre depois de uma atualização no alu_ativo.
CREATE TRIGGER after_update_date
AFTER UPDATE OF alu_ativo
	ON aluno
	FOR EACH ROW 
	EXECUTE PROCEDURE update_date();
