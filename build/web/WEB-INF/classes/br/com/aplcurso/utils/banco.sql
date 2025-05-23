create table usuario (
    id serial primary key,
    nome varchar(100) not null,
    datanascimento date not null,
    cpf varchar(11) not null,
    email varchar(100) not null,
    senha varchar(20) not null,
    salario decimal(15,2) not null,
    idestado integer,
    CONSTRAINT fk_usuario_estado FOREIGN KEY (idestado) REFERENCES estado (id)
);

create table estado (
    id serial primary key,
    nomeEstado varchar(100) not null,
    siglaEstado varchar(2) not null
);

insert into usuario (nome, datanascimento, cpf, email, senha, salario)
values ('João José Gomes da Silva', '1990-08-10', '08243060073', 'joaojosegomes@gmail.com', 'senha123', 5200.00);

insert into estado (nomeEstado, siglaEstado)
values ('São Paulo', 'SP');
