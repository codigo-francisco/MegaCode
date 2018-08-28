﻿using ApiRest.Generatos;
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

        [HttpPost]
        public IHttpActionResult Login(Usuario usuario)
        {
            try
            {

                if (usuario == null) return BadRequest("Json incorrecto");

                var usuarioResult = 
                    entities.Usuario.FirstOrDefault(user => user.email == usuario.email && user.contrasena == usuario.contrasena);
                if (usuarioResult != null)
                {
                    //Se genera el token y se envía al usuario
                    RegistroResponse registroResponse = 
                        new RegistroResponse() { token = TokenGenerator.GenerateTokenJwt(usuario.email) };
                    return Json(registroResponse);
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
                    RegistroResponse response = new RegistroResponse { id=usuario.id, token=TokenGenerator.GenerateTokenJwt(usuario.email) };
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