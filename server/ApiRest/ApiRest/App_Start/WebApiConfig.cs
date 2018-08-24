using ApiRest.Generatos;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http.Formatting;
using System.Net.Http.Headers;
using System.Web.Http;

namespace ApiRest
{
    public static class WebApiConfig
    {
        public static void Register(HttpConfiguration config)
        {
            // Configuración y servicios de API web

            // Rutas de API web
            config.MapHttpAttributeRoutes();

            config.MessageHandlers.Add(new TokenHandler());

            JsonMediaTypeFormatter jsonFormatter = config.Formatters.JsonFormatter;
            jsonFormatter.SupportedMediaTypes.Add(new MediaTypeHeaderValue("application/json"));
            jsonFormatter.SupportedMediaTypes.Add(new MediaTypeHeaderValue("text/html"));
            jsonFormatter.SupportedMediaTypes.Add(new MediaTypeHeaderValue("multipart/form-data"));

            //GlobalConfiguration.Configuration.Formatters.XmlFormatter.SupportedMediaTypes.Clear();
            //config.Formatters.XmlFormatter.SupportedMediaTypes.Clear();

            config.Routes.MapHttpRoute(
                name: "DefaultApi",
                routeTemplate: "api/{controller}/{id}",
                defaults: new { id = RouteParameter.Optional }
            );
        }
    }
}
