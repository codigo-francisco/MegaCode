using ApiRest.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace ApiRest.Controllers
{
    [Authorize]
    [RoutePrefix("api/nivel")]
    public class NivelController : ApiController
    {
        MegacodeEntities entities = new MegacodeEntities();

        [AllowAnonymous]
        [HttpGet]
        [Route("listarNiveles")]
        public IHttpActionResult ListarNiveles()
        {
           return Json(entities.Nivel.
                   Select( n => new
                   {
                       n.id,
                       n.nombre,
                       n.dificultad,
                       n.grupo,
                       n.mientras,
                       n.comandos,
                       n.si,
                       n.para,
                       n.ruta,
                       n.tipoNivel,
                       n.cadenaOptima
                   }));
        }

        [HttpGet]
        [Route("listarNiveles/{id}")]
        public IHttpActionResult ListarNiveles(Int64? id)
        {

            var niveles = entities.Nivel.Select(
                    n => new
                    {
                        n.id,
                        n.nombre,
                        n.dificultad,
                        n.grupo,
                        n.mientras,
                        n.comandos,
                        n.si,
                        n.para,
                        n.ruta,
                        n.tipoNivel,
                        n.cadenaOptima,
                        n.zoomInicial
                    }
               );

            var nivelesTerminados = entities.Niveles_Terminados.Where(nt => nt.UsuarioId == id)
                .Select(nt => new
                {
                    nt.id,
                    nivelId = nt.NivelId,
                    usuarioId = nt.UsuarioId,
                    nt.terminado,
                    nt.puntaje
                });

            return Json(
                new
                {
                    niveles,
                    nivelesTerminados
                }
           );
        }
    }
}
