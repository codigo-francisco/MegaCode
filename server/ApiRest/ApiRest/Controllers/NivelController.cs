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
                            n.variables,
                            n.si,
                            n.para,
                            n.mientras,
                            puntaje = (r==null ? 0 : r.puntaje)
                        };

            return Json(query);
        }
    }
}
