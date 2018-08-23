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
            if (usuario==null)
            {
                return BadRequest("Modelo no valido");
            }
            entities.Usuario.Add(usuario);
            entities.SaveChanges();
            return Json(usuario);
        }
    }
}
