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
        megacodeEntities entities = new megacodeEntities();

        [AllowAnonymous]
        [HttpGet]
        [Route("listarNiveles")]
        public IHttpActionResult listarNiveles()
        {
           return Json(from n in entities.Nivel
                   select new
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
                   });
        }

        [HttpGet]
        [Route("listarNiveles/{id}")]
        public IHttpActionResult listarNiveles(Int64? id)
        {

            var niveles = from n in entities.Nivel
                          select new
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
                          };

            var nivelesTerminados = from nt in entities.Niveles_Terminados
                            where nt.UsuarioId == id
                            select new
                            {
                                nt.id,
                                nivelId = nt.NivelId,
                                usuarioId = nt.UsuarioId,
                                nt.terminado,
                                nt.puntaje
                            };

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
