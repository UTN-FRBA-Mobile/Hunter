using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;

namespace Hunter.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class SessionController : ControllerBase
    { 
        [HttpGet]
        public string Get()
        {
            //return Database.HunterContext.ConnectionString;
            return "";
        }
    }
}
