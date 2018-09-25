using ApiRest.Generatos;
using ApiRest.Models;
using ApiRest.Models.Responses;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace ApiRest.Controllers
{
    [AllowAnonymous]
    [RoutePrefix("api/autenticacion")]
    public class LoginController : ApiController
    {
        private megacodeEntities entities = new megacodeEntities();

        [HttpPost]
        [Route("login")]
        public IHttpActionResult Login(Usuario usuario)
        {
            try
            {

                if (usuario == null) return BadRequest("Json incorrecto");

                Usuario usuarioResult = 
                    entities.Usuario.FirstOrDefault(user => user.email == usuario.email && user.contrasena == usuario.contrasena);
                if (usuarioResult != null)
                {
                    //Se genera el token y se envía al usuario
                    String token = TokenGenerator.GenerateTokenJwt(usuario.email);
                    LoginResponse loginResponse = new LoginResponse()
                    {
                        usuario = new Usuario()
                        {
                            id = usuarioResult.id,
                            sexo = usuarioResult.sexo,
                            email = usuarioResult.email,
                            contrasena = usuarioResult.contrasena,
                            nombre = usuarioResult.nombre,
                            edad = usuarioResult.edad,
                            variables = usuarioResult.variables,
                            si = usuarioResult.si,
                            para = usuarioResult.para,
                            mientras = usuarioResult.mientras,
                            fotoPerfil = usuarioResult.fotoPerfil
                        },
                        token = token
                    };
                    return Json(loginResponse);
                }
                else
                {
                    return StatusCode(HttpStatusCode.Forbidden);
                }
            }
            catch (Exception ex)
            {
                return InternalServerError(ex);
            }
        }

        [HttpPost]
        [Route("registrar")]
        public IHttpActionResult Registrar(Usuario usuario)
        {
            try
            {
                if (usuario == null) return BadRequest("Json Incorrecto");
                //Validar si el usuario existe
                if (entities.Usuario.FirstOrDefault(us => us.email == usuario.email)!=null)
                {
                    return StatusCode(HttpStatusCode.Forbidden);
                }
                else
                {
                    entities.Usuario.Add(usuario);
                    entities.SaveChanges();
                    //Creamos un token para el usuario que se acaba de registrar
                    RegistroResponse response = new RegistroResponse {
                        id =usuario.id,
                        token =TokenGenerator.GenerateTokenJwt(usuario.email)
                    };
                    return Json(response);
                }
                
            }
            catch(Exception ex)
            {
                return InternalServerError(ex);
            }
        }
    }
}
