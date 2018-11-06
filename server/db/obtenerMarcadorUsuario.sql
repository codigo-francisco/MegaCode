CREATE FUNCTION obtenerMarcadorUsuario(@id bigint )
RETURNS TABLE
AS
RETURN 
	SELECT
	*
	FROM
		(SELECT
			nombre,
			comandos,
			si,
			para,
			mientras,
			comandos + si + para + mientras AS total
		FROM Usuario) as Consulta
	Where total > (select comandos + si + para + mientras AS total from Usuario where Usuario.id = @id)


