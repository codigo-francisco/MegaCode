USE [megacode]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Sesion]') AND type in (N'U'))
ALTER TABLE [dbo].[Sesion] DROP CONSTRAINT IF EXISTS [FK_Sesion_UsuarioId]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Sesion]') AND type in (N'U'))
ALTER TABLE [dbo].[Sesion] DROP CONSTRAINT IF EXISTS [FK_Sesion_NivelId]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Sesion]') AND type in (N'U'))
ALTER TABLE [dbo].[Sesion] DROP CONSTRAINT IF EXISTS [FK_Sesion_ConexionId]
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
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Usuario]') AND type in (N'U'))
ALTER TABLE [dbo].[Usuario] DROP CONSTRAINT IF EXISTS [DF__Usuario__mientra__01142BA1]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Usuario]') AND type in (N'U'))
ALTER TABLE [dbo].[Usuario] DROP CONSTRAINT IF EXISTS [DF__Usuario__para__00200768]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Usuario]') AND type in (N'U'))
ALTER TABLE [dbo].[Usuario] DROP CONSTRAINT IF EXISTS [DF__Usuario__si__7F2BE32F]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Usuario]') AND type in (N'U'))
ALTER TABLE [dbo].[Usuario] DROP CONSTRAINT IF EXISTS [DF__Usuario__variabl__7E37BEF6]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Sesion]') AND type in (N'U'))
ALTER TABLE [dbo].[Sesion] DROP CONSTRAINT IF EXISTS [DF__Sesion__id__08B54D69]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Niveles_Terminados]') AND type in (N'U'))
ALTER TABLE [dbo].[Niveles_Terminados] DROP CONSTRAINT IF EXISTS [DF__Niveles_T__termi__123EB7A3]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Emociones_Sesion]') AND type in (N'U'))
ALTER TABLE [dbo].[Emociones_Sesion] DROP CONSTRAINT IF EXISTS [DF__Emociones_Se__id__0E6E26BF]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Conexion]') AND type in (N'U'))
ALTER TABLE [dbo].[Conexion] DROP CONSTRAINT IF EXISTS [DF__Conexion__id__05D8E0BE]
GO
/****** Object:  Table [dbo].[Usuario]    Script Date: 20/08/2018 08:48:27 p. m. ******/
DROP TABLE IF EXISTS [dbo].[Usuario]
GO
/****** Object:  Table [dbo].[Sesion]    Script Date: 20/08/2018 08:48:27 p. m. ******/
DROP TABLE IF EXISTS [dbo].[Sesion]
GO
/****** Object:  Table [dbo].[Niveles_Terminados]    Script Date: 20/08/2018 08:48:27 p. m. ******/
DROP TABLE IF EXISTS [dbo].[Niveles_Terminados]
GO
/****** Object:  Table [dbo].[Nivel]    Script Date: 20/08/2018 08:48:27 p. m. ******/
DROP TABLE IF EXISTS [dbo].[Nivel]
GO
/****** Object:  Table [dbo].[Emociones_Sesion]    Script Date: 20/08/2018 08:48:27 p. m. ******/
DROP TABLE IF EXISTS [dbo].[Emociones_Sesion]
GO
/****** Object:  Table [dbo].[Conexion]    Script Date: 20/08/2018 08:48:27 p. m. ******/
DROP TABLE IF EXISTS [dbo].[Conexion]
GO
USE [master]
GO
/****** Object:  Database [megacode]    Script Date: 20/08/2018 08:48:27 p. m. ******/
DROP DATABASE IF EXISTS [megacode]
GO
/****** Object:  Database [megacode]    Script Date: 20/08/2018 08:48:27 p. m. ******/
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'megacode')
BEGIN
CREATE DATABASE [megacode]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'megacode', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.MSSQLSERVER\MSSQL\DATA\megacode.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'megacode_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL14.MSSQLSERVER\MSSQL\DATA\megacode_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 COLLATE Modern_Spanish_CI_AS
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
/****** Object:  Table [dbo].[Conexion]    Script Date: 20/08/2018 08:48:27 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Conexion]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Conexion](
	[id] [uniqueidentifier] NOT NULL,
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
/****** Object:  Table [dbo].[Emociones_Sesion]    Script Date: 20/08/2018 08:48:28 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Emociones_Sesion]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Emociones_Sesion](
	[id] [uniqueidentifier] NOT NULL,
	[sesionId] [uniqueidentifier] NULL,
	[etiqueta] [varchar](50) COLLATE Modern_Spanish_CI_AS NULL,
	[tiempo] [timestamp] NOT NULL,
	[fotografia] [varchar](max) COLLATE Modern_Spanish_CI_AS NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
END
GO
/****** Object:  Table [dbo].[Nivel]    Script Date: 20/08/2018 08:48:28 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Nivel]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Nivel](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[nombre] [varchar](200) COLLATE Modern_Spanish_CI_AS NOT NULL,
	[ruta] [varchar](max) COLLATE Modern_Spanish_CI_AS NOT NULL,
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
/****** Object:  Table [dbo].[Niveles_Terminados]    Script Date: 20/08/2018 08:48:28 p. m. ******/
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
/****** Object:  Table [dbo].[Sesion]    Script Date: 20/08/2018 08:48:28 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Sesion]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Sesion](
	[id] [uniqueidentifier] NOT NULL,
	[usuarioId] [bigint] NULL,
	[conexionId] [uniqueidentifier] NULL,
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
/****** Object:  Table [dbo].[Usuario]    Script Date: 20/08/2018 08:48:28 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Usuario]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Usuario](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[nombre] [varchar](200) COLLATE Modern_Spanish_CI_AS NOT NULL,
	[edad] [smallint] NOT NULL,
	[sexo] [char](1) COLLATE Modern_Spanish_CI_AS NOT NULL,
	[variables] [smallint] NOT NULL,
	[si] [smallint] NOT NULL,
	[para] [smallint] NOT NULL,
	[mientras] [smallint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Conexion__id__05D8E0BE]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Conexion] ADD  DEFAULT (newsequentialid()) FOR [id]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Emociones_Se__id__0E6E26BF]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Emociones_Sesion] ADD  DEFAULT (newsequentialid()) FOR [id]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Niveles_T__termi__123EB7A3]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Niveles_Terminados] ADD  DEFAULT ((0)) FOR [terminado]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Sesion__id__08B54D69]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Sesion] ADD  DEFAULT (newsequentialid()) FOR [id]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Usuario__variabl__7E37BEF6]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Usuario] ADD  DEFAULT ((0)) FOR [variables]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Usuario__si__7F2BE32F]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Usuario] ADD  DEFAULT ((0)) FOR [si]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Usuario__para__00200768]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Usuario] ADD  DEFAULT ((0)) FOR [para]
END
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DF__Usuario__mientra__01142BA1]') AND type = 'D')
BEGIN
ALTER TABLE [dbo].[Usuario] ADD  DEFAULT ((0)) FOR [mientras]
END
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
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Sesion_ConexionId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Sesion]'))
ALTER TABLE [dbo].[Sesion]  WITH CHECK ADD  CONSTRAINT [FK_Sesion_ConexionId] FOREIGN KEY([conexionId])
REFERENCES [dbo].[Conexion] ([id])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Sesion_ConexionId]') AND parent_object_id = OBJECT_ID(N'[dbo].[Sesion]'))
ALTER TABLE [dbo].[Sesion] CHECK CONSTRAINT [FK_Sesion_ConexionId]
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
