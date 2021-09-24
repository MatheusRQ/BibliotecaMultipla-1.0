CREATE DATABASE IF NOT EXISTS BIBLIOTECA_MULTIPLA;

USE BIBLIOTECA_MULTIPLA;

CREATE TABLE IF NOT EXISTS CATEGORIA(
ID INT AUTO_INCREMENT,
NOME VARCHAR(30) NOT NULL,
PRIMARY KEY(ID)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS ANIMACAO(
ID INT AUTO_INCREMENT,
NOME VARCHAR(100) NOT NULL,
ANO_DE_LANCAMENTO INT,
GENERO VARCHAR(300),
NUMERO_EPISODIOS INT UNSIGNED,
CATEGORIA_ID INT,
DATA_DE_REGISTRO TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ASSISTIDO BIT(1) DEFAULT 0,
PRIMARY KEY (ID),
CONSTRAINT CATEGORIA_FK FOREIGN KEY (CATEGORIA_ID) REFERENCES CATEGORIA(ID)
);

CREATE TABLE IF NOT EXISTS ANIMACAO_N_ASSISTIDO (
ANIMACAO_ID INT NOT NULL,
RELEVANCIA ENUM('S', 'A', 'B', 'C', 'D'),
PRIORIDADE ENUM('--', '-', '=', '+', '++'),
PRIMARY KEY (ANIMACAO_ID),
CONSTRAINT FK_ANIMACAO_0 FOREIGN KEY (ANIMACAO_ID) REFERENCES ANIMACAO (ID)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS ANIMACAO_ASSISTIDO (
ANIMACAO_ID INT NOT NULL,
DATA_DE_FINALIZACAO TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
NOTA DECIMAL (5,3),
PRIMARY KEY (ANIMACAO_ID),
CONSTRAINT FK_ANIMACAO_1 FOREIGN KEY (ANIMACAO_ID) REFERENCES ANIMACAO (ID)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS DUBLADOR (
ID INT AUTO_INCREMENT,
NOME VARCHAR(50),
NASCIMENTO DATE,
PRIMARY KEY (ID)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS PERSONAGEM (
ID INT AUTO_INCREMENT,
NOME VARCHAR(50),
PRIMARY KEY (ID)
) ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS DUBLAGENS (
DUBLADOR_ID INT,
PERSONAGEM_ID INT,
ANIMACAO_ID INT,
CONSTRAINT FK_DUBLADOR
FOREIGN KEY (DUBLADOR_ID) REFERENCES DUBLADOR (ID),
CONSTRAINT FK_PERSONAGEM FOREIGN KEY (PERSONAGEM_ID) REFERENCES PERSONAGEM (ID),
CONSTRAINT FK_ANIMACAO
FOREIGN KEY (ANIMACAO_ID) REFERENCES ANIMACAO (ID)
);

INSERT INTO `biblioteca_multipla`.`categoria`
(`NOME`)
VALUES
('ANIMACAO ORIENTAL');

DELIMITER //

CREATE PROCEDURE ANIME_N_ASSISTIDO_INSERCAO (cNOME VARCHAR (100), cANO_DE_LANCAMENTO INT, cGENERO VARCHAR(250), cNUMERO_EPISODIOS INT, cCATEGORIA_ID INT, cRELEVANCIA ENUM('S', 'A', 'B', 'C', 'D'), cPRIORIDADE ENUM('--', '-', '=', '+', '++'))
BEGIN
	INSERT INTO ANIMACAO (ID, NOME, ANO_DE_LANCAMENTO, GENERO, NUMERO_EPISODIOS, CATEGORIA_ID, DATA_DE_REGISTRO, ASSISTIDO) VALUES (DEFAULT, cNOME, cANO_DE_LANCAMENTO, cGENERO, cNUMERO_EPISODIOS, cCATEGORIA_ID, default, default);
	INSERT INTO ANIMACAO_N_ASSISTIDO (ANIMACAO_ID, RELEVANCIA, PRIORIDADE) VALUES ((SELECT MAX(ID) FROM ANIMACAO), cRELEVANCIA, cPRIORIDADE);
END//

CREATE PROCEDURE ANIMACAO_ASSISTIDO_INSERCAO (cNOME VARCHAR (100), cANO_DE_LANCAMENTO INT, cGENERO VARCHAR(250), cNUMERO_EPISODIOS INT, cCATEGORIA_ID INT, cNOTA DECIMAL (5,3))
BEGIN
	INSERT INTO ANIMACAO (ID, NOME, ANO_DE_LANCAMENTO, GENERO, NUMERO_EPISODIOS, CATEGORIA_ID, DATA_DE_REGISTRO, ASSISTIDO) VALUES (DEFAULT, cNOME, cANO_DE_LANCAMENTO, cGENERO, cNUMERO_EPISODIOS, cCATEGORIA_ID, default, 1);
    INSERT INTO ANIMACAO_ASSISTIDO (ANIMACAO_ID, DATA_DE_FINALIZACAO, NOTA) VALUES ((SELECT MAX(ID) FROM ANIMACAO), DEFAULT, cNOTA);
END//

CREATE PROCEDURE TERMINA_ANIMACAO (cID INT, cNOTA DECIMAL (5,3))
BEGIN
	DELETE FROM ANIMACAO_N_ASSISTIDO WHERE ANIMACAO_ID = cID;
    INSERT INTO ANIMACAO_ASSISTIDO (ANIMACAO_ID, DATA_DE_FINALIZACAO, NOTA) VALUES (cID, DEFAULT, cNOTA);
    UPDATE ANIMACAO SET ASSISTIDO = 1 WHERE ID = cID;
END//

CREATE PROCEDURE REGISTROPERSONAGEM(cNOME VARCHAR(100), cANIMACAO_ID INT, cDUBLADOR_ID INT)
BEGIN
	INSERT INTO PERSONAGEM (NOME) VALUES (cNOME);
    INSERT INTO DUBLAGENS (DUBLADOR_ID, PERSONAGEM_ID, ANIMACAO_ID) VALUES (cDUBLADOR_ID, (SELECT MAX(ID) FROM PERSONAGEM), cANIMACAO_ID);
END//

DELIMITER ;
