using Hunter.Models;
using Hunter.Services;
using Microsoft.AspNetCore.Mvc;

namespace Hunter.Controllers
{
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class UserController : SecuredController
    {
        [HttpPost]
        public void JoinGame([FromBody] int game_id)
        {
            DatabaseService.JoinGame(Sub, game_id);
        }

        [HttpGet]
        public User Find([FromBody] User user)
        {
            return DatabaseService.FindUser(user);
        }

        [HttpGet]
        public User Get()
        {
            return DatabaseService.GetUser(Sub);
        }

        [HttpPost]
        public User Post([FromBody] User user)
        {
            user.Sub = Sub;
            return DatabaseService.SaveUser(user);
        }

        [HttpGet]
        public Game[] History(int page, int countPerPage)
        {
            return DatabaseService.GetGames(Sub, page, countPerPage);
        }
    }
}
