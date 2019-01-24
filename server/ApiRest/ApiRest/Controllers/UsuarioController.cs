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
    [RoutePrefix("api/usuario")]
    public class UsuarioController : ApiController
    {
        private MegacodeEntities entities = new MegacodeEntities();

        [HttpPost]
        [Route("registrarFoto")]
        public IHttpActionResult registrarFoto(Usuario usuario)
        {
            if (usuario == null) return BadRequest("Usuario mal formado");

            var user = entities.Usuario.FirstOrDefault(u => u.id == usuario.id);

            if (user != null)
            {
                user.fotoPerfil = usuario.fotoPerfil;
                entities.SaveChanges();

                return Ok();
            }
            else
            {
                return BadRequest("Usuario no encontrado");
            }
        }
    }
}
