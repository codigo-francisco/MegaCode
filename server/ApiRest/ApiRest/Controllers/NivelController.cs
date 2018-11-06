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
                       n.tipoNivel
                   });
        }

        [HttpGet]
        [Route("listarNiveles/{id}")]
        public IHttpActionResult listarNiveles(Int64? id)
        {

            var query = from n in entities.Nivel
                        join nt in entities.Niveles_Terminados.Where(nt=>nt.UsuarioId==id) on n.id equals nt.NivelId
                        into ntt
                        from r in ntt.DefaultIfEmpty()
                        select new
                        {
                            n.nombre,
                            n.ruta,
                            n.comandos,
                            n.si,
                            n.para,
                            n.mientras,
                            puntaje = (r==null ? 0 : r.puntaje)
                        };

            return Json(query);
        }
    }
}
