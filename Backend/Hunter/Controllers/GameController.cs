using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Hunter.Models;
using Hunter.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace Hunter.Controllers
{
    [Route("api/[controller]/[action]")]
    [ApiController]
    public class GameController : SecuredController
    {
        [HttpGet]
        public Game Get(int id)
        {
            return DatabaseService.GetGame(id, Sub);
        }

        [HttpGet]
        public Game[] GetMyGames()
        {
            return DatabaseService.GetMyCreatedGames(Sub);
        }

        [HttpPost]
        public Game Post([FromForm] int durationMins, [FromForm] float latitude, [FromForm] float longitude, [FromForm] string[] clues, [FromForm] int[] userIds, [FromForm] string photo)
        {
            var endDatetime = DateTime.Now.Add(new TimeSpan(0, durationMins, 0));
            return DatabaseService.SaveGame(Sub, endDatetime, latitude, longitude, clues, userIds, photo);
        }

        [HttpPost]
        public Game Win([FromForm] int game_id, [FromForm] string win_code)
        {
            return DatabaseService.WinGame(game_id, Sub, win_code);
        }
    }
}
