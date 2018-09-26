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
        megacodeEntities entities = new megacodeEntities();

        [HttpGet]
        [AllowAnonymous]
        public IHttpActionResult ObtenerScores()
        {
            var query = (from u in entities.Usuario
                         join c in entities.Conexion on u.id equals c.usuarioId
                         select new
                         {
                             u.nombre,
                             score = u.variables + u.si + u.para + u.mientras,
                             dia = c.entrada.Day,
                             mes = c.entrada.Month,
                             anio = c.entrada.Year,
                             u.fotoPerfil
                         }).Take(10).OrderByDescending(o => o.score);

            return Json(query);
                
        }
    }
}
