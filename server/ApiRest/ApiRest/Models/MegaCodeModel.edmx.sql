
-- --------------------------------------------------
-- Entity Designer DDL Script for SQL Server 2005, 2008, 2012 and Azure
-- --------------------------------------------------
-- Date Created: 02/04/2019 23:04:22
-- Generated from EDMX file: E:\MegaCode\server\ApiRest\ApiRest\Models\MegaCodeModel.edmx
-- --------------------------------------------------

SET QUOTED_IDENTIFIER OFF;
GO
USE [megacode];
GO
IF SCHEMA_ID(N'dbo') IS NULL EXECUTE(N'CREATE SCHEMA [dbo]');
GO

-- --------------------------------------------------
-- Dropping existing FOREIGN KEY constraints
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[FK_Conecion_UsuarioId]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[Conexion] DROP CONSTRAINT [FK_Conecion_UsuarioId];
GO
IF OBJECT_ID(N'[dbo].[FK_ConexionSesion_ConexionId]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[ConexionSesion] DROP CONSTRAINT [FK_ConexionSesion_ConexionId];
GO
IF OBJECT_ID(N'[dbo].[FK_ConexionSesion_SesionId]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[ConexionSesion] DROP CONSTRAINT [FK_ConexionSesion_SesionId];
GO
IF OBJECT_ID(N'[dbo].[FK_Emocion_Sesion_SesionId]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[Emociones_Sesion] DROP CONSTRAINT [FK_Emocion_Sesion_SesionId];
GO
IF OBJECT_ID(N'[dbo].[FK_Nivel_TipoNivel]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[Nivel] DROP CONSTRAINT [FK_Nivel_TipoNivel];
GO
IF OBJECT_ID(N'[dbo].[FK_NivelesTerminados_NivelId]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[Niveles_Terminados] DROP CONSTRAINT [FK_NivelesTerminados_NivelId];
GO
IF OBJECT_ID(N'[dbo].[FK_NivelesTerminados_UsuarioId]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[Niveles_Terminados] DROP CONSTRAINT [FK_NivelesTerminados_UsuarioId];
GO
IF OBJECT_ID(N'[dbo].[FK_Sesion_NivelId]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[Sesion] DROP CONSTRAINT [FK_Sesion_NivelId];
GO
IF OBJECT_ID(N'[dbo].[FK_Sesion_UsuarioId]', 'F') IS NOT NULL
    ALTER TABLE [dbo].[Sesion] DROP CONSTRAINT [FK_Sesion_UsuarioId];
GO

-- --------------------------------------------------
-- Dropping existing tables
-- --------------------------------------------------

IF OBJECT_ID(N'[dbo].[Conexion]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Conexion];
GO
IF OBJECT_ID(N'[dbo].[ConexionSesion]', 'U') IS NOT NULL
    DROP TABLE [dbo].[ConexionSesion];
GO
IF OBJECT_ID(N'[dbo].[Emociones_Sesion]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Emociones_Sesion];
GO
IF OBJECT_ID(N'[dbo].[Nivel]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Nivel];
GO
IF OBJECT_ID(N'[dbo].[Niveles_Terminados]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Niveles_Terminados];
GO
IF OBJECT_ID(N'[dbo].[Sesion]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Sesion];
GO
IF OBJECT_ID(N'[dbo].[TipoNivel]', 'U') IS NOT NULL
    DROP TABLE [dbo].[TipoNivel];
GO
IF OBJECT_ID(N'[dbo].[Usuario]', 'U') IS NOT NULL
    DROP TABLE [dbo].[Usuario];
GO

-- --------------------------------------------------
-- Creating all tables
-- --------------------------------------------------

-- Creating table 'Conexion'
CREATE TABLE [dbo].[Conexion] (
    [id] uniqueidentifier  NOT NULL,
    [entrada] datetime  NOT NULL,
    [salida] datetime  NULL,
    [duracion] int  NULL,
    [usuarioId] bigint  NULL
);
GO

-- Creating table 'Emociones_Sesion'
CREATE TABLE [dbo].[Emociones_Sesion] (
    [id] uniqueidentifier  NOT NULL,
    [sesionId] uniqueidentifier  NULL,
    [etiqueta] varchar(50)  NULL,
    [tiempo] binary(8)  NOT NULL,
    [fotografia] varchar(max)  NULL
);
GO

-- Creating table 'Nivel'
CREATE TABLE [dbo].[Nivel] (
    [id] int IDENTITY(1,1) NOT NULL,
    [nombre] varchar(200)  NOT NULL,
    [ruta] varchar(max)  NOT NULL,
    [comandos] int  NOT NULL,
    [si] int  NOT NULL,
    [para] int  NOT NULL,
    [mientras] int  NOT NULL,
    [dificultad] int  NOT NULL,
    [grupo] int  NOT NULL,
    [tipoNivel] smallint  NOT NULL,
    [cadenaOptima] nvarchar(30)  NOT NULL,
    [zoomInicial] real  NOT NULL,
    [coordenadaX] int  NOT NULL,
    [coordenadaY] int  NOT NULL
);
GO

-- Creating table 'Niveles_Terminados'
CREATE TABLE [dbo].[Niveles_Terminados] (
    [id] bigint IDENTITY(1,1) NOT NULL,
    [UsuarioId] bigint  NULL,
    [NivelId] int  NULL,
    [terminado] bit  NULL,
    [puntaje] int  NOT NULL
);
GO

-- Creating table 'Usuario'
CREATE TABLE [dbo].[Usuario] (
    [id] bigint IDENTITY(1,1) NOT NULL,
    [nombre] varchar(200)  NOT NULL,
    [edad] smallint  NOT NULL,
    [sexo] varchar(20)  NOT NULL,
    [comandos] smallint  NOT NULL,
    [si] smallint  NOT NULL,
    [para] smallint  NOT NULL,
    [mientras] smallint  NOT NULL,
    [email] varchar(200)  NOT NULL,
    [contrasena] varchar(max)  NOT NULL,
    [fotoPerfil] varchar(max)  NULL
);
GO

-- Creating table 'ConexionSesion'
CREATE TABLE [dbo].[ConexionSesion] (
    [id] uniqueidentifier  NOT NULL,
    [sesionId] uniqueidentifier  NULL,
    [conexionId] uniqueidentifier  NULL
);
GO

-- Creating table 'Sesion'
CREATE TABLE [dbo].[Sesion] (
    [id] uniqueidentifier  NOT NULL,
    [usuarioId] bigint  NULL,
    [nivelId] int  NULL,
    [tiempo] binary(8)  NOT NULL,
    [intentos] int  NULL,
    [ayudas] int  NULL,
    [inactividad] int  NULL
);
GO

-- Creating table 'TipoNivel'
CREATE TABLE [dbo].[TipoNivel] (
    [id] smallint  NOT NULL,
    [nombre] varchar(50)  NOT NULL
);
GO

-- --------------------------------------------------
-- Creating all PRIMARY KEY constraints
-- --------------------------------------------------

-- Creating primary key on [id] in table 'Conexion'
ALTER TABLE [dbo].[Conexion]
ADD CONSTRAINT [PK_Conexion]
    PRIMARY KEY CLUSTERED ([id] ASC);
GO

-- Creating primary key on [id] in table 'Emociones_Sesion'
ALTER TABLE [dbo].[Emociones_Sesion]
ADD CONSTRAINT [PK_Emociones_Sesion]
    PRIMARY KEY CLUSTERED ([id] ASC);
GO

-- Creating primary key on [id] in table 'Nivel'
ALTER TABLE [dbo].[Nivel]
ADD CONSTRAINT [PK_Nivel]
    PRIMARY KEY CLUSTERED ([id] ASC);
GO

-- Creating primary key on [id] in table 'Niveles_Terminados'
ALTER TABLE [dbo].[Niveles_Terminados]
ADD CONSTRAINT [PK_Niveles_Terminados]
    PRIMARY KEY CLUSTERED ([id] ASC);
GO

-- Creating primary key on [id] in table 'Usuario'
ALTER TABLE [dbo].[Usuario]
ADD CONSTRAINT [PK_Usuario]
    PRIMARY KEY CLUSTERED ([id] ASC);
GO

-- Creating primary key on [id] in table 'ConexionSesion'
ALTER TABLE [dbo].[ConexionSesion]
ADD CONSTRAINT [PK_ConexionSesion]
    PRIMARY KEY CLUSTERED ([id] ASC);
GO

-- Creating primary key on [id] in table 'Sesion'
ALTER TABLE [dbo].[Sesion]
ADD CONSTRAINT [PK_Sesion]
    PRIMARY KEY CLUSTERED ([id] ASC);
GO

-- Creating primary key on [id] in table 'TipoNivel'
ALTER TABLE [dbo].[TipoNivel]
ADD CONSTRAINT [PK_TipoNivel]
    PRIMARY KEY CLUSTERED ([id] ASC);
GO

-- --------------------------------------------------
-- Creating all FOREIGN KEY constraints
-- --------------------------------------------------

-- Creating foreign key on [NivelId] in table 'Niveles_Terminados'
ALTER TABLE [dbo].[Niveles_Terminados]
ADD CONSTRAINT [FK_NivelesTerminados_NivelId]
    FOREIGN KEY ([NivelId])
    REFERENCES [dbo].[Nivel]
        ([id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_NivelesTerminados_NivelId'
CREATE INDEX [IX_FK_NivelesTerminados_NivelId]
ON [dbo].[Niveles_Terminados]
    ([NivelId]);
GO

-- Creating foreign key on [UsuarioId] in table 'Niveles_Terminados'
ALTER TABLE [dbo].[Niveles_Terminados]
ADD CONSTRAINT [FK_NivelesTerminados_UsuarioId]
    FOREIGN KEY ([UsuarioId])
    REFERENCES [dbo].[Usuario]
        ([id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_NivelesTerminados_UsuarioId'
CREATE INDEX [IX_FK_NivelesTerminados_UsuarioId]
ON [dbo].[Niveles_Terminados]
    ([UsuarioId]);
GO

-- Creating foreign key on [usuarioId] in table 'Conexion'
ALTER TABLE [dbo].[Conexion]
ADD CONSTRAINT [FK_Conecion_UsuarioId]
    FOREIGN KEY ([usuarioId])
    REFERENCES [dbo].[Usuario]
        ([id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_Conecion_UsuarioId'
CREATE INDEX [IX_FK_Conecion_UsuarioId]
ON [dbo].[Conexion]
    ([usuarioId]);
GO

-- Creating foreign key on [conexionId] in table 'ConexionSesion'
ALTER TABLE [dbo].[ConexionSesion]
ADD CONSTRAINT [FK_ConexionSesion_ConexionId]
    FOREIGN KEY ([conexionId])
    REFERENCES [dbo].[Conexion]
        ([id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_ConexionSesion_ConexionId'
CREATE INDEX [IX_FK_ConexionSesion_ConexionId]
ON [dbo].[ConexionSesion]
    ([conexionId]);
GO

-- Creating foreign key on [conexionId] in table 'ConexionSesion'
ALTER TABLE [dbo].[ConexionSesion]
ADD CONSTRAINT [FK_ConexionSesion_SesionId]
    FOREIGN KEY ([conexionId])
    REFERENCES [dbo].[Sesion]
        ([id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_ConexionSesion_SesionId'
CREATE INDEX [IX_FK_ConexionSesion_SesionId]
ON [dbo].[ConexionSesion]
    ([conexionId]);
GO

-- Creating foreign key on [sesionId] in table 'Emociones_Sesion'
ALTER TABLE [dbo].[Emociones_Sesion]
ADD CONSTRAINT [FK_Emocion_Sesion_SesionId]
    FOREIGN KEY ([sesionId])
    REFERENCES [dbo].[Sesion]
        ([id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_Emocion_Sesion_SesionId'
CREATE INDEX [IX_FK_Emocion_Sesion_SesionId]
ON [dbo].[Emociones_Sesion]
    ([sesionId]);
GO

-- Creating foreign key on [nivelId] in table 'Sesion'
ALTER TABLE [dbo].[Sesion]
ADD CONSTRAINT [FK_Sesion_NivelId]
    FOREIGN KEY ([nivelId])
    REFERENCES [dbo].[Nivel]
        ([id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_Sesion_NivelId'
CREATE INDEX [IX_FK_Sesion_NivelId]
ON [dbo].[Sesion]
    ([nivelId]);
GO

-- Creating foreign key on [usuarioId] in table 'Sesion'
ALTER TABLE [dbo].[Sesion]
ADD CONSTRAINT [FK_Sesion_UsuarioId]
    FOREIGN KEY ([usuarioId])
    REFERENCES [dbo].[Usuario]
        ([id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_Sesion_UsuarioId'
CREATE INDEX [IX_FK_Sesion_UsuarioId]
ON [dbo].[Sesion]
    ([usuarioId]);
GO

-- Creating foreign key on [tipoNivel] in table 'Nivel'
ALTER TABLE [dbo].[Nivel]
ADD CONSTRAINT [FK_Nivel_TipoNivel]
    FOREIGN KEY ([tipoNivel])
    REFERENCES [dbo].[TipoNivel]
        ([id])
    ON DELETE NO ACTION ON UPDATE NO ACTION;
GO

-- Creating non-clustered index for FOREIGN KEY 'FK_Nivel_TipoNivel'
CREATE INDEX [IX_FK_Nivel_TipoNivel]
ON [dbo].[Nivel]
    ([tipoNivel]);
GO

-- --------------------------------------------------
-- Script has ended
-- --------------------------------------------------