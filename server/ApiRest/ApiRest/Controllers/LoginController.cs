using ApiRest.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace ApiRest.Controllers
{
    [AllowAnonymous]
    [RoutePrefix("api/login")]
    public class LoginController : ApiController
    {
        private megacodeEntities entities = new megacodeEntities();

        [HttpGet]
        public IHttpActionResult Ping()
        {
            return Ok();
        }

        [HttpPost]
        [Route("registrar")]
        public IHttpActionResult Registrar(Usuario usuario)
        {
            try
            {
                if (usuario == null)
                {
                    return BadRequest("Modelo no valido");
                }
                //Validar si el usuario existe
                if (entities.Usuario.FirstOrDefault(us => us.email == usuario.email)!=null)
                {
                    return StatusCode(HttpStatusCode.Forbidden);
                }
                else
                {
                    entities.Usuario.Add(usuario);
                    entities.SaveChanges();
                    return Json(usuario);
                }
                
            }
            catch(Exception ex)
            {
                return InternalServerError(ex);
            }
        }
    }
}
