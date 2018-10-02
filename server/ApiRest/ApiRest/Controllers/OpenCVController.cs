using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Mime;
using System.Web.Http;

namespace ApiRest.Controllers
{
    [AllowAnonymous]
    [RoutePrefix("api/archivos")]
    public class OpenCVController : ApiController
    {
        [Route("{architectureCpu}")]
        [HttpGet]
        public IHttpActionResult ObtenerOpenCVManager(string architectureCpu)
        {
            string fileName = $"OpenCV_Manager_{architectureCpu}.apk";

            HttpResponseMessage httpResponseMessage = new HttpResponseMessage(HttpStatusCode.OK);

            HttpContent httpContent = new ByteArrayContent(File.ReadAllBytes($"{AppDomain.CurrentDomain.BaseDirectory}/Archivos/{fileName}"));

            httpContent.Headers.Add("Content-Type", "application/vnd.android.package-archive");

            httpResponseMessage.Content = httpContent;

            return ResponseMessage(httpResponseMessage);
        }
    }
}
