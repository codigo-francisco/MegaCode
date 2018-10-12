using ApiRest.Generatos;
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
    [RoutePrefix("api/feed")]
    public class FeedController : ApiController
    {
        megacodeEntities entities = new megacodeEntities();

        [HttpGet]
        [Route("posicionContraOtros/{id}")]
        public IHttpActionResult PosicionContraOtros(Int64? id)
        {
            IHttpActionResult result=null;
            if (id==null)
            {
                result = BadRequest();
            }
            else
            {
                result = Json(entities.obtenerMarcadorUsuario(id));
            }

            return result;
        }

        [HttpGet]
        [Route("siguienteEjercicio/{id}")]
        public IHttpActionResult SiguienteEjercicio(Int64? id)
        {
            IHttpActionResult result = null;

            if (id == null)
            {
                result = BadRequest();
            }
            else
            {
                var dataResult = (from n in entities.Nivel
                                  where n.id == ((from nt in entities.Niveles_Terminados
                                                  join u in entities.Usuario on nt.UsuarioId equals u.id
                                                  orderby nt.NivelId descending
                                                  select nt.NivelId).FirstOrDefault() + 1)
                                  select new
                                  {
                                      n.id,
                                      n.nombre,
                                      n.ruta
                                  }).FirstOrDefault();

                return Json(dataResult);
            }

            return result;
        }
    }
}
