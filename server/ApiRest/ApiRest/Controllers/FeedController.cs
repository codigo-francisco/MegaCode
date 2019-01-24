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
        MegacodeEntities entities = new MegacodeEntities();

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
                var siguienteNivelId = entities.Niveles_Terminados
                    .Join(entities.Usuario, nt => nt.UsuarioId, u => u.id, (nt, u) => nt)
                    .OrderBy(nt => nt.NivelId)
                    .Select(nt => nt.NivelId)
                    .LastOrDefault() + 1;

                var dataResult = entities.Nivel
                    .Where(n => n.id == siguienteNivelId)
                    .Select(
                        n => new
                        {
                            n.id,
                            n.nombre,
                            n.ruta
                        }
                    ).FirstOrDefault();

                return Json(dataResult);
            }

            return result;
        }
    }
}
