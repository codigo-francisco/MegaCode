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
        [Route("ping/{id}")]
        public IHttpActionResult Ping(String id)
        {

            return Ok(id);
        }

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

        /*[Route("siguienteEjercicio")]
        public IHttpActionResult SiguienteEjercicio(Usuario usuario)
        {
            if (usuario == null)
            {
                return BadRequest();
            }

            Usuario usuarioConsulta = entities.Usuario.Where(user => user.email == usuario.email).FirstOrDefault();
            if (usuarioConsulta.Niveles_Terminados.Count() == 0)
            {
                return Ok("El usuario no ha realizado ningún ejercicio");
            }
            else
            {
                //Niveles sin terminar
            }
        }*/
    }
}
