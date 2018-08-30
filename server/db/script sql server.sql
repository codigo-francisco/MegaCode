USE [megacode]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Sesion]') AND type in (N'U'))
ALTER TABLE [dbo].[Sesion] DROP CONSTRAINT IF EXISTS [FK_Sesion_UsuarioId]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Sesion]') AND type in (N'U'))
ALTER TABLE [dbo].[Sesion] DROP CONSTRAINT IF EXISTS [FK_Sesion_NivelId]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Niveles_Terminados]') AND type in (N'U'))
ALTER TABLE [dbo].[Niveles_Terminados] DROP CONSTRAINT IF EXISTS [FK_NivelesTerminados_UsuarioId]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Niveles_Terminados]') AND type in (N'U'))
ALTER TABLE [dbo].[Niveles_Terminados] DROP CONSTRAINT IF EXISTS [FK_NivelesTerminados_NivelId]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Emociones_Sesion]') AND type in (N'U'))
ALTER TABLE [dbo].[Emociones_Sesion] DROP CONSTRAINT IF EXISTS [FK_Emocion_Sesion_SesionId]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ConexionSesion]') AND type in (N'U'))
ALTER TABLE [dbo].[ConexionSesion] DROP CONSTRAINT IF EXISTS [FK_ConexionSesion_SesionId]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ConexionSesion]') AND type in (N'U'))
ALTER TABLE [dbo].[ConexionSesion] DROP CONSTRAINT IF EXISTS [FK_ConexionSesion_ConexionId]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Conexion]') AND type in (N'U'))
ALTER TABLE [dbo].[Conexion] DROP CONSTRAINT IF EXISTS [FK_Conecion_UsuarioId]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Usuario]') AND type in (N'U'))
ALTER TABLE [dbo].[Usuario] DROP CONSTRAINT IF EXISTS [DF__Usuario__mientra__3B75D760]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Usuario]') AND type in (N'U'))
ALTER TABLE [dbo].[Usuario] DROP CONSTRAINT IF EXISTS [DF__Usuario__para__3A81B327]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Usuario]') AND type in (N'U'))
ALTER TABLE [dbo].[Usuario] DROP CONSTRAINT IF EXISTS [DF__Usuario__si__398D8EEE]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Usuario]') AND type in (N'U'))
ALTER TABLE [dbo].[Usuario] DROP CONSTRAINT IF EXISTS [DF__Usuario__variabl__38996AB5]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Sesion]') AND type in (N'U'))
ALTER TABLE [dbo].[Sesion] DROP CONSTRAINT IF EXISTS [DF__Sesion__id__440B1D61]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Niveles_Terminados]') AND type in (N'U'))
ALTER TABLE [dbo].[Niveles_Terminados] DROP CONSTRAINT IF EXISTS [DF__Niveles_T__termi__5165187F]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Emociones_Sesion]') AND type in (N'U'))
ALTER TABLE [dbo].[Emociones_Sesion] DROP CONSTRAINT IF EXISTS [DF__Emociones_Se__id__4D94879B]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ConexionSesion]') AND type in (N'U'))
ALTER TABLE [dbo].[ConexionSesion] DROP CONSTRAINT IF EXISTS [DF__ConexionSesi__id__48CFD27E]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Conexion]') AND type in (N'U'))
ALTER TABLE [dbo].[Conexion] DROP CONSTRAINT IF EXISTS [DF__Conexion__id__403A8C7D]
GO
/****** Object:  Index [UQ_Email]    Script Date: 29/08/2018 11:30:25 p. m. ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Usuario]') AND type in (N'U'))
ALTER TABLE [dbo].[Usuario] DROP CONSTRAINT IF EXISTS [UQ_Email]
GO
/****** Object:  Table [dbo].[Sesion]    Script Date: 29/08/2018 11:30:25 p. m. ******/
DROP TABLE IF EXISTS [dbo].[Sesion]
GO
/****** Object:  Table [dbo].[Niveles_Terminados]    Script Date: 29/08/2018 11:30:25 p. m. ******/
DROP TABLE IF EXISTS [dbo].[Niveles_Terminados]
GO
/****** Object:  Table [dbo].[Nivel]    Script Date: 29/08/2018 11:30:25 p. m. ******/
DROP TABLE IF EXISTS [dbo].[Nivel]
GO
/****** Object:  Table [dbo].[Emociones_Sesion]    Script Date: 29/08/2018 11:30:25 p. m. ******/
DROP TABLE IF EXISTS [dbo].[Emociones_Sesion]
GO
/****** Object:  Table [dbo].[ConexionSesion]    Script Date: 29/08/2018 11:30:25 p. m. ******/
DROP TABLE IF EXISTS [dbo].[ConexionSesion]
GO
/****** Object:  Table [dbo].[Conexion]    Script Date: 29/08/2018 11:30:25 p. m. ******/
DROP TABLE IF EXISTS [dbo].[Conexion]
GO
/****** Object:  UserDefinedFunction [dbo].[obtenerMarcadorUsuario]    Script Date: 29/08/2018 11:30:25 p. m. ******/
DROP FUNCTION IF EXISTS [dbo].[obtenerMarcadorUsuario]
GO
/****** Object:  Table [dbo].[Usuario]    Script Date: 29/08/2018 11:30:25 p. m. ******/
DROP TABLE IF EXISTS [dbo].[Usuario]
GO
USE [master]
GO
/****** Object:  Database [megacode]    Script Date: 29/08/2018 11:30:25 p. m. ******/
DROP DATABASE IF EXISTS [megacode]
GO
/****** Object:  Database [megacode]    Script Date: 29/08/2018 11:30:25 p. m. ******/
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'megacode')
BEGIN
CREATE DATABASE [megacode]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'megacode', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.MSSQLSERVER\MSSQL\DATA\megacode.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'megacode_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.MSSQLSERVER\MSSQL\DATA\megacode_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
END
GO
ALTER DATABASE [megacode] SET COMPATIBILITY_LEVEL = 140
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [megacode].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [megacode] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [megacode] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [megacode] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [megacode] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [megacode] SET ARITHABORT OFF 
GO
ALTER DATABASE [megacode] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [megacode] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [megacode] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [megacode] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [megacode] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [megacode] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [megacode] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [megacode] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [megacode] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [megacode] SET  DISABLE_BROKER 
GO
ALTER DATABASE [megacode] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [megacode] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [megacode] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [megacode] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [megacode] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [megacode] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [megacode] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [megacode] SET RECOVERY FULL 
GO
ALTER DATABASE [megacode] SET  MULTI_USER 
GO
ALTER DATABASE [megacode] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [megacode] SET DB_CHAINING OFF 
GO
ALTER DATABASE [megacode] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [megacode] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [megacode] SET DELAYED_DURABILITY = DISABLED 
GO
EXEC sys.sp_db_vardecimal_storage_format N'megacode', N'ON'
GO
ALTER DATABASE [megacode] SET QUERY_STORE = OFF
GO
USE [megacode]
GO
ALTER DATABASE SCOPED CONFIGURATION SET IDENTITY_CACHE = ON;
GO
ALTER DATABASE SCOPED CONFIGURATION SET LEGACY_CARDINALITY_ESTIMATION = OFF;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET LEGACY_CARDINALITY_ESTIMATION = PRIMARY;
GO
ALTER DATABASE SCOPED CONFIGURATION SET MAXDOP = 0;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET MAXDOP = PRIMARY;
GO
ALTER DATABASE SCOPED CONFIGURATION SET PARAMETER_SNIFFING = ON;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET PARAMETER_SNIFFING = PRIMARY;
GO
ALTER DATABASE SCOPED CONFIGURATION SET QUERY_OPTIMIZER_HOTFIXES = OFF;
GO
ALTER DATABASE SCOPED CONFIGURATION FOR SECONDARY SET QUERY_OPTIMIZER_HOTFIXES = PRIMARY;
GO
USE [megacode]
GO
/****** Object:  Table [dbo].[Usuario]    Script Date: 29/08/2018 11:30:26 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Usuario]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Usuario](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[nombre] [varchar](200) NOT NULL,
	[edad] [smallint] NOT NULL,
	[sexo] [varchar](20) NOT NULL,
	[variables] [smallint] NOT NULL,
	[si] [smallint] NOT NULL,
	[para] [smallint] NOT NULL,
	[mientras] [smallint] NOT NULL,
	[email] [varchar](200) NOT NULL,
	[contrasena] [varchar](max) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO
/****** Object:  UserDefinedFunction [dbo].[obtenerMarcadorUsuario]    Script Date: 29/08/2018 11:30:26 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[obtenerMarcadorUsuario]') AND type in (N'FN', N'IF', N'TF', N'FS', N'FT'))
BEGIN
execute dbo.sp_executesql @statement = N'CREATE FUNCTION [dbo].[obtenerMarcadorUsuario](@id bigint )
RETURNS TABLE
AS
RETURN 
	SELECT
	*
	FROM
		(SELECT
			nombre,
			variables,
			si,
			para,
			mientras,
			variables + si + para + mientras AS total
		FROM Usuario) as Consulta
	Where total > (select variables + si + para + mientras AS total from Usuario where Usuario.id = @id)


' 
END
GO
/****** Object:  Table [dbo].[Conexion]    Script Date: 29/08/2018 11:30:26 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Conexion]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Conexion](
	[id] [uniqueidentifier] NOT NULL,
	[usuarioId] [bigint] NULL,
	[entrada] [datetime] NOT NULL,
	[salida] [datetime] NULL,
	[duracion] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[ConexionSesion]    Script Date: 29/08/2018 11:30:26 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[ConexionSesion]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[ConexionSesion](
	[id] [uniqueidentifier] NOT NULL,
	[sesionId] [uniqueidentifier] NULL,
	[conexionId] [uniqueidentifier] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Emociones_Sesion]    Script Date: 29/08/2018 11:30:26 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Emociones_Sesion]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Emociones_Sesion](
	[id] [uniqueidentifier] NOT NULL,
	[sesionId] [uniqueidentifier] NULL,
	[etiqueta] [varchar](50) NULL,
	[tiempo] [timestamp] NOT NULL,
	[fotografia] [varchar](max) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Nivel]    Script Date: 29/08/2018 11:30:26 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Nivel]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Nivel](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[nombre] [varchar](200) NOT NULL,
	[ruta] [varchar](max) NOT NULL,
	[variables] [int] NOT NULL,
	[si] [int] NOT NULL,
	[para] [int] NOT NULL,
	[mientras] [int] NOT NULL,
	[dificultad] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Niveles_Terminados]    Script Date: 29/08/2018 11:30:26 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Niveles_Terminados]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Niveles_Terminados](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[UsuarioId] [bigint] NULL,
	[NivelId] [int] NULL,
	[terminado] [bit] NULL,
	[puntaje] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Sesion]    Script Date: 29/08/2018 11:30:26 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Sesion]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Sesion](
	[id] [uniqueidentifier] NOT NULL,
	[usuarioId] [bigint] NULL,
	[nivelId] [int] NULL,
	[tiempo] [timestamp] NOT NULL,
	[intentos] [int] NULL,
	[ayudas] [int] NULL,
	[inactividad] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET IDENTITY_INSERT [dbo].[Usuario] ON 

INSERT [dbo].[Usuario] ([id], [nombre], [edad], [sexo], [variables], [si], [para], [mientras], [email], [contrasena]) VALUES (1, N'francisco gonzalez', 29, N'Masculino', 5, 4, 3, 3, N'rockbass2560@gmail.com', N'contra1234')
INSERT [dbo].[Usuario] ([id], [nombre], [edad], [sexo], [variables], [si], [para], [mientras], [email], [contrasena]) VALUES (2, N'raul oramas', 41, N'Masculino', 4, 3, 2, 0, N'roramas@gmail.com', N'contra1234')
INSERT [dbo].[Usuario] ([id], [nombre], [edad], [sexo], [variables], [si], [para], [mientras], [email], [contrasena]) VALUES (3, N'pablo garcia', 25, N'Masculino', 5, 5, 4, 4, N'pablog@gmail.com', N'contra1234')
INSERT [dbo].[Usuario] ([id], [nombre], [edad], [sexo], [variables], [si], [para], [mientras], [email], [contrasena]) VALUES (4, N'Fernanda Martinez', 24, N'Femenino', 5, 5, 4, 3, N'fernandam@outlook.com', N'contra1234')
SET IDENTITY_INSERT [dbo].[Usuario] OFF
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ_Email]    Script Date: 29/08/2018 11:30:26 p. m. ******/
IF NOT EXISTS (SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[dbo].[Usuario]') AND name = N'UQ_Email')
ALTER TABLE [dbo].[Usuario] ADD  CONSTRAINT [UQ_Email] UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Conexion__id__403A8C7D]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Conexion] ADD  DEFAULT (newsequentialid()) FOR [id]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__ConexionSesi__id__48CFD27E]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[ConexionSesion] ADD  DEFAULT (newsequentialid()) FOR [id]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Emociones_Se__id__4D94879B]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Emociones_Sesion] ADD  DEFAULT (newsequentialid()) FOR [id]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Niveles_T__termi__5165187F]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Niveles_Terminados] ADD  DEFAULT ((0)) FOR [terminado]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Sesion__id__440B1D61]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Sesion] ADD  DEFAULT (newsequentialid()) FOR [id]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Usuario__variabl__38996AB5]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Usuario] ADD  DEFAULT ((0)) FOR [variables]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Usuario__si__398D8EEE]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Usuario] ADD  DEFAULT ((0)) FOR [si]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Usuario__para__3A81B327]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Usuario] ADD  DEFAULT ((0)) FOR [para]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Usuario__mientra__3B75D760]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Usuario] ADD  DEFAULT ((0)) FOR [mientras]
END
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Conecion_UsuarioId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Conexion]'))
ALTER TABLE [dbo].[Conexion]  WITH CHECK ADD  CONSTRAINT [FK_Conecion_UsuarioId] FOREIGN KEY([usuarioId])
REFERENCES [dbo].[Usuario] ([id])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Conecion_UsuarioId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Conexion]'))
ALTER TABLE [dbo].[Conexion] CHECK CONSTRAINT [FK_Conecion_UsuarioId]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_ConexionSesion_ConexionId]') AND parent_object_id = OBJECT_ID(N'[dbo].[ConexionSesion]'))
ALTER TABLE [dbo].[ConexionSesion]  WITH CHECK ADD  CONSTRAINT [FK_ConexionSesion_ConexionId] FOREIGN KEY([conexionId])
REFERENCES [dbo].[Conexion] ([id])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_ConexionSesion_ConexionId]') AND parent_object_id = OBJECT_ID(N'[dbo].[ConexionSesion]'))
ALTER TABLE [dbo].[ConexionSesion] CHECK CONSTRAINT [FK_ConexionSesion_ConexionId]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_ConexionSesion_SesionId]') AND parent_object_id = OBJECT_ID(N'[dbo].[ConexionSesion]'))
ALTER TABLE [dbo].[ConexionSesion]  WITH CHECK ADD  CONSTRAINT [FK_ConexionSesion_SesionId] FOREIGN KEY([conexionId])
REFERENCES [dbo].[Sesion] ([id])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_ConexionSesion_SesionId]') AND parent_object_id = OBJECT_ID(N'[dbo].[ConexionSesion]'))
ALTER TABLE [dbo].[ConexionSesion] CHECK CONSTRAINT [FK_ConexionSesion_SesionId]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Emocion_Sesion_SesionId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Emociones_Sesion]'))
ALTER TABLE [dbo].[Emociones_Sesion]  WITH CHECK ADD  CONSTRAINT [FK_Emocion_Sesion_SesionId] FOREIGN KEY([sesionId])
REFERENCES [dbo].[Sesion] ([id])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Emocion_Sesion_SesionId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Emociones_Sesion]'))
ALTER TABLE [dbo].[Emociones_Sesion] CHECK CONSTRAINT [FK_Emocion_Sesion_SesionId]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_NivelesTerminados_NivelId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Niveles_Terminados]'))
ALTER TABLE [dbo].[Niveles_Terminados]  WITH CHECK ADD  CONSTRAINT [FK_NivelesTerminados_NivelId] FOREIGN KEY([NivelId])
REFERENCES [dbo].[Nivel] ([id])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_NivelesTerminados_NivelId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Niveles_Terminados]'))
ALTER TABLE [dbo].[Niveles_Terminados] CHECK CONSTRAINT [FK_NivelesTerminados_NivelId]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_NivelesTerminados_UsuarioId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Niveles_Terminados]'))
ALTER TABLE [dbo].[Niveles_Terminados]  WITH CHECK ADD  CONSTRAINT [FK_NivelesTerminados_UsuarioId] FOREIGN KEY([UsuarioId])
REFERENCES [dbo].[Usuario] ([id])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_NivelesTerminados_UsuarioId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Niveles_Terminados]'))
ALTER TABLE [dbo].[Niveles_Terminados] CHECK CONSTRAINT [FK_NivelesTerminados_UsuarioId]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Sesion_NivelId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Sesion]'))
ALTER TABLE [dbo].[Sesion]  WITH CHECK ADD  CONSTRAINT [FK_Sesion_NivelId] FOREIGN KEY([nivelId])
REFERENCES [dbo].[Nivel] ([id])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Sesion_NivelId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Sesion]'))
ALTER TABLE [dbo].[Sesion] CHECK CONSTRAINT [FK_Sesion_NivelId]
GO
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Sesion_UsuarioId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Sesion]'))
ALTER TABLE [dbo].[Sesion]  WITH CHECK ADD  CONSTRAINT [FK_Sesion_UsuarioId] FOREIGN KEY([usuarioId])
REFERENCES [dbo].[Usuario] ([id])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Sesion_UsuarioId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Sesion]'))
ALTER TABLE [dbo].[Sesion] CHECK CONSTRAINT [FK_Sesion_UsuarioId]
GO
USE [master]
GO
ALTER DATABASE [megacode] SET  READ_WRITE 
GO
