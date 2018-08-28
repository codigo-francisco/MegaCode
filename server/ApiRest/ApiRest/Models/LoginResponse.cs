using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ApiRest.Models.Responses
{
    public class LoginResponse
    {
        public Usuario usuario {get; set;}
        public String token { get; set; }
    }
}