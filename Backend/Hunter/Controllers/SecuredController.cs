using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace Hunter.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    [Authorize]
    public class SecuredController : ControllerBase
    {
        //aca hay que sacar el sub del token
        protected string Sub
        {
            get
            {
                var sub = User.Claims.FirstOrDefault(x => x.Type == "sub")?.Value;
                if (string.IsNullOrEmpty(sub))
                {
                    throw new UnauthorizedAccessException();
                }

                return sub;
            }
        }
    }
}
