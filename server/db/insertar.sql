USE [megacode]
GO
SET IDENTITY_INSERT [dbo].[Usuario] ON 
GO
INSERT [dbo].[Usuario] ([id], [nombre], [edad], [sexo], [variables], [si], [para], [mientras], [email], [contrasena]) VALUES (1, N'francisco gonzalez', 29, N'Masculino', 5, 4, 3, 3, N'rockbass2560@gmail.com', N'contra1234')
GO
INSERT [dbo].[Usuario] ([id], [nombre], [edad], [sexo], [variables], [si], [para], [mientras], [email], [contrasena]) VALUES (2, N'raul oramas', 41, N'Masculino', 4, 3, 2, 0, N'roramas@gmail.com', N'contra1234')
GO
INSERT [dbo].[Usuario] ([id], [nombre], [edad], [sexo], [variables], [si], [para], [mientras], [email], [contrasena]) VALUES (3, N'pablo garcia', 25, N'Masculino', 5, 5, 4, 4, N'pablog@gmail.com', N'contra1234')
GO
INSERT [dbo].[Usuario] ([id], [nombre], [edad], [sexo], [variables], [si], [para], [mientras], [email], [contrasena]) VALUES (4, N'Fernanda Martinez', 24, N'Femenino', 5, 5, 4, 3, N'fernandam@outlook.com', N'contra1234')
GO
SET IDENTITY_INSERT [dbo].[Usuario] OFF
--GO
--INSERT [dbo].[Conexion] ([id], [usuarioId], [entrada], [salida], [duracion]) VALUES (N'8151429e-abab-e811-9bcb-88b111a9fd3e', 1, CAST(N'2018-08-26T00:00:00.000' AS DateTime), NULL, NULL)
--GO
--INSERT [dbo].[Conexion] ([id], [usuarioId], [entrada], [salida], [duracion]) VALUES (N'eee4c8b9-abab-e811-9bcb-88b111a9fd3e', 3, CAST(N'2018-08-27T00:00:00.000' AS DateTime), NULL, NULL)
--GO
