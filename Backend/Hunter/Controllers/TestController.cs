using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Hunter.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace Hunter.Controllers
{
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class TestController : ControllerBase
    {

        [HttpPost]
        public void SendInvitationTest([FromForm] string sub, [FromForm] int game_id)
        {
            CloudMessagingService.GameInvitation(sub, game_id);
        }

        [HttpPost]
        public void WinTest([FromForm] int game_id, [FromForm] string first_name, [FromForm] string last_name)
        {
            CloudMessagingService.GameEnded(game_id, first_name, last_name);
        }
    }
}
