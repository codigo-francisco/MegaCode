CREATE FUNCTION obtenerMarcadorUsuario(@id bigint )
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


