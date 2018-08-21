use MegaCode;

CREATE TABLE Usuario(
	id bigint identity(1,1) primary key,
	nombre varchar(200) not null,
	edad smallint not null,
	sexo char not null,
	variables smallint not null default 0,
	si smallint not null default 0,
	para smallint not null default 0,
	mientras smallint not null default 0
);

CREATE TABLE Nivel(
	id int identity primary key,
	nombre varchar(200) not null,
	ruta varchar(max) not null,
	variables int not null,
	si int not null,
	para int not null,
	mientras int not null,
	dificultad int not null
);


CREATE TABLE Conexion(
	id uniqueidentifier primary key default newsequentialid(),
	entrada datetime not null,
	salida datetime,
	duracion int
);

CREATE TABLE Sesion(
	id uniqueidentifier primary key default newsequentialid(),
	usuarioId bigint,
	conexionId uniqueidentifier,
	nivelId int,
	tiempo timestamp,
	intentos int,
	ayudas int,
	inactividad int
	constraint FK_Sesion_UsuarioId foreign key (usuarioId) references Usuario(id),
	constraint FK_Sesion_ConexionId foreign key (conexionId) references Conexion(id),
	constraint FK_Sesion_NivelId foreign key (nivelId) references Nivel(id)
);

CREATE TABLE Emociones_Sesion(
	id uniqueidentifier primary key default newsequentialid(),
	sesionId uniqueidentifier,
	etiqueta varchar(50),
	tiempo timestamp,
	fotografia varchar(max),
	constraint FK_Emocion_Sesion_SesionId foreign key (sesionId) references Sesion(id)
);

CREATE TABLE Niveles_Terminados(
	id bigint identity primary key,
	UsuarioId bigint,
	NivelId int,
	terminado bit default 0,
	puntaje int not null,
	constraint FK_NivelesTerminados_UsuarioId foreign key (UsuarioId) references Usuario(id),
	constraint FK_NivelesTerminados_NivelId foreign key (NivelId) references Nivel(id)
);