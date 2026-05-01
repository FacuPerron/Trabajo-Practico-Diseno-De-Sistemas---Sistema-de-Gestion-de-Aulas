--poblado solo de los enums
CREATE TABLE Ubicacion(
	nombreUbicacion varchar(50) primary key
);
INSERT INTO Ubicacion values ('PRIMER_PISO'),('SEGUNDO_PISO'),('TERCER_PISO');

CREATE TABLE TipoPizarron(
	nombreTipoPizarron varchar(50) primary key
);

INSERT INTO TipoPizarron values ('PIZARRON'),('PIZARRA');
--enums
CREATE TABLE DiaSemana(
	nombre varchar(20) primary key
);
INSERT INTO DiaSemana values ('Lunes'),('Martes'),('Miercoles'),('Jueves'),('Viernes');

CREATE TABLE TipoPeriodo(
	nombre varchar(30) primary key
);
INSERT INTO TipoPeriodo values ('PRIMER_CUATRIMESTRE'),('SEGUNDO_CUATRIMESTRE');
CREATE TABLE TipoAula(
	nombre varchar(20) primary key
);
INSERT INTO TipoAula values ('SINRECURSOS'),('INFORMATICA'),('MULTIMEDIOS');



--Usuarios
CREATE TABLE Turno(
	nombreTurno varchar(20) primary key
);

INSERT INTO Turno values ('Mañana'),('Tarde'),('Noche');

CREATE TABLE Usuario(
	id int GENERATED ALWAYS AS IDENTITY,
	idUsuario varchar(50),
	nombre varchar(50),
	apellido varchar(50),
	email varchar(80),
	contrasena varchar(170),
	primary key(id)
);

CREATE TABLE Bedel(
	id int primary key,
	nombreTurno varchar(20),
	activo boolean,
	foreign key (nombreTurno) references Turno(nombreTurno),
	foreign key (id) references Usuario(id)
);

CREATE TABLE Administrador(
	id int primary key,
	foreign key (id) references Usuario(id)
);

--Administrador (sin una cuenta previa, no se puede iniciar sesión en el sistema como admin)
insert into Usuario (idUsuario,nombre,apellido,email,contrasena) values 
('admin1','Juan','Perez','mailadmin@gmail.com','1000:b16baf90cf2a82a25d10c84b647a0c5b:741b8024f767af9262663b11121679a959071604d4ee53ba859cfc7a62f4f89394b0367b675b326df70b2571ace608d71436ba9ac79f5749c1f4c214dd78d50e');

insert into administrador values
(1);


--Bedel (dejo un par, usar cualquiera para poder iniciar sesión como bedel)
insert into Usuario (idUsuario,nombre,apellido,email,contrasena) values 
('bedelActivo1','Pedro','Alvarez','mailbedelActivo1@gmail.com','1000:b16baf90cf2a82a25d10c84b647a0c5b:741b8024f767af9262663b11121679a959071604d4ee53ba859cfc7a62f4f89394b0367b675b326df70b2571ace608d71436ba9ac79f5749c1f4c214dd78d50e'),
('bedelActivo2','Leandro','Nuñez','mailbedelActivo2@gmail.com','1000:b16baf90cf2a82a25d10c84b647a0c5b:741b8024f767af9262663b11121679a959071604d4ee53ba859cfc7a62f4f89394b0367b675b326df70b2571ace608d71436ba9ac79f5749c1f4c214dd78d50e');

insert into bedel (id,nombreturno,activo)  values 
(2,'Tarde',true),
(3,'Noche',true);

--Aulas

CREATE TABLE Aula(
	id int  GENERATED ALWAYS AS IDENTITY,
	capacidad int,
	ubicacion varchar(50) references Ubicacion(nombreUbicacion),
	tipoPizarron varchar(50) references TipoPizarron(nombreTipoPizarron),
	aireAcondicionado boolean,
	habilitado boolean,
	primary key(id)
);
CREATE TABLE AulaSinRecursos(
	id int primary key,
	ventiladores boolean,
	foreign key (id) references Aula(id)
);
CREATE TABLE AulaInformatica(
	id int primary key,
	canon boolean,
	cantidadPCs int,
	foreign key (id) references Aula(id)
);
CREATE TABLE AulaMultimedios(
	id int primary key,
	canon boolean,
	televisor boolean,
	computadora boolean,
	ventiladores boolean,
	foreign key (id) references Aula(id)
);

--algunas aulas de los 3 tipos
INSERT INTO Aula (capacidad, ubicacion, tipoPizarron, aireAcondicionado, habilitado)
VALUES 
(30, 'PRIMER_PISO', 'PIZARRON', TRUE, TRUE),
(25, 'SEGUNDO_PISO', 'PIZARRA', FALSE, TRUE),
(50, 'TERCER_PISO', 'PIZARRON', TRUE, FALSE),
(40, 'PRIMER_PISO', 'PIZARRA', FALSE, TRUE),
(20, 'SEGUNDO_PISO', 'PIZARRON', TRUE, TRUE),
(35, 'PRIMER_PISO', 'PIZARRON', TRUE, TRUE),
(45, 'TERCER_PISO', 'PIZARRA', TRUE, FALSE),
(15, 'SEGUNDO_PISO', 'PIZARRON', FALSE, TRUE),
(60, 'TERCER_PISO', 'PIZARRON', TRUE, TRUE);

INSERT INTO AulaSinRecursos (id, ventiladores)
VALUES
(1, TRUE),
(2, FALSE),
(3, TRUE);
INSERT INTO AulaInformatica (id, canon, cantidadPCs)
VALUES
(4, TRUE, 15),
(5, TRUE, 20),
(6, FALSE, 10);
INSERT INTO AulaMultimedios (id, canon, televisor, computadora, ventiladores)
VALUES
(7, TRUE, TRUE, TRUE, TRUE),
(8, TRUE, FALSE, TRUE, FALSE),
(9, FALSE, TRUE, FALSE, TRUE);

--Reserva y periodo
CREATE TABLE Periodo(
	id int PRIMARY KEY,
	fechaInicio date,
	fechaFin date,
	tipoPeriodo varchar(30) references TipoPeriodo(nombre)
);

INSERT INTO Periodo (id, fechaInicio, fechaFin, tipoPeriodo) 
VALUES 
(1, '2024-03-01', '2024-06-30', 'PRIMER_CUATRIMESTRE'),
(2, '2024-08-01', '2024-11-30', 'SEGUNDO_CUATRIMESTRE'),
(3, '2025-03-01', '2025-06-30', 'PRIMER_CUATRIMESTRE'),
(4, '2025-08-01', '2025-11-30', 'SEGUNDO_CUATRIMESTRE');

CREATE TABLE Reserva(
	id int GENERATED ALWAYS AS IDENTITY,
	--idBedel varchar(50),
	idTablaBedel int,
	idPeriodo int,
	cantidadAlumnos int,
	mail_contacto varchar(80),
	idCurso varchar(50),
	nom_apel_docente varchar(100),
	fechaReserva date,
	tipoAula VARCHAR(30) references tipoAula(nombre),
	primary key(id),
	CONSTRAINT fk_idBedel FOREIGN KEY(idTablaBedel) REFERENCES Bedel(id),
	
	CONSTRAINT fk_idPeriodo FOREIGN KEY(idPeriodo) REFERENCES Periodo(id)
);

--Items de Reservas.

CREATE TABLE EsporadicaItem(
	id int GENERATED ALWAYS AS IDENTITY,
	idReserva int,
	idAula int,
	fecha date,
	hora_inicio time,
	hora_fin time,
	primary key(id),
	CONSTRAINT fk_idReservaE FOREIGN KEY(idReserva) REFERENCES Reserva(id),
	CONSTRAINT fk_idAulaE FOREIGN KEY(idAula) REFERENCES Aula(id)
);

CREATE TABLE PeriodicaItem(
	id int GENERATED ALWAYS AS IDENTITY,
	idReserva int,
	idAula int,
	diaSemana varchar(20) references diaSemana(nombre),
	hora_inicio time,
	hora_fin time,
	primary key(id),
	CONSTRAINT fk_idReservaP FOREIGN KEY(idReserva) REFERENCES Reserva(id),
	CONSTRAINT fk_idAulaP FOREIGN KEY(idAula) REFERENCES Aula(id)
);


