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
    [RoutePrefix("api/score")]
    public class ScoreController : ApiController
    {
        MegacodeEntities entities = new MegacodeEntities();

        [HttpGet]
        [AllowAnonymous]
        public IHttpActionResult ObtenerScores()
        {
            var query = entities.Usuario
                .Join(entities.Conexion, u => u.id, c => c.usuarioId, (u,c) =>
                new
                {
                    u.nombre,
                    score = u.comandos + u.si + u.para + u.mientras,
                    dia = c.entrada.Day,
                    mes = c.entrada.Month,
                    anio = c.entrada.Year,
                    u.fotoPerfil
                }).Take(10).OrderByDescending(o => o.score);

            return Json(query);
                
        }
    }
}
