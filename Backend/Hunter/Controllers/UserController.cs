using Hunter.Models;
using Hunter.Services;
using Microsoft.AspNetCore.Mvc;

namespace Hunter.Controllers
{
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class UserController : SecuredController
    {
        [HttpGet]
        public User Get()
        {
            return DatabaseService.GetUser(Sub);
        }

        [HttpPost]
        public User Post([FromForm] string alias, [FromForm] string mail, [FromForm] string first_name, [FromForm] string last_name)
        {
            return DatabaseService.SaveUser(Sub, alias, mail, first_name, last_name);
        }

        [HttpGet]
        public Game[] History()
        {
            return DatabaseService.GetGames(Sub);
        }
    }
}
