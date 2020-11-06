using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using System.Web.Http;

namespace Hunter.Database
{
    [Serializable]
    public class UserNotFoundException : HttpResponseException 
    {
        public UserNotFoundException() : base(HttpStatusCode.NotFound) { }
    }

    [Serializable]
    public class GameNotFoundException : HttpResponseException
    {
        public GameNotFoundException() : base(HttpStatusCode.NotFound) { }
    }
}
