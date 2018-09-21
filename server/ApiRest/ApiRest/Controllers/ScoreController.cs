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
    [RoutePrefix("api/score/")]
    public class ScoreController : ApiController
    {
        megacodeEntities entities = new megacodeEntities();

        [HttpGet]
        public IHttpActionResult ObtenerScores()
        {
            var query = (from u in entities.Usuario
                         join c in entities.Conexion on u.id equals c.usuarioId
                         select new
                         {
                             Nombre = u.nombre,
                             Score = u.variables + u.si + u.para + u.mientras,
                             Dia = c.entrada.Day,
                             Mes = c.entrada.Month,
                             Anio = c.entrada.Year
                         }).OrderByDescending(o => o.Score);

            return Json(query);
                
        }
    }
}
