using Hunter.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Hunter.Services
{
    public class DatabaseService
    {
        public static User GetUser(string sub)
        {
            using (var context = new Database.postgresContext())
            {
                return ParseUser(context.User.First(u => u.Sub == sub));
            }
        }

        public static User GetUserByAlias(string alias)
        {
            using (var context = new Database.postgresContext())
            {
                return ParseUser(context.User.First(u => u.Alias == alias));
            }
        }

        public static User SaveUser(string sub, string alias, string mail, string first_name, string last_name)
        {
            using (var context = new Database.postgresContext())
            {
                var user = context.User.First(u => u.Sub == sub); 
                user.Alias = alias;
                user.FirstName = first_name;
                user.LastName = last_name;
                user.Mail = mail;
                user.Sub = sub;

                if (user.Id == 0) context.User.Add(user);

                context.SaveChanges();

                return ParseUser(user);
            }
        }

        public static Game[] GetGames(string sub)
        {
            using (var context = new Database.postgresContext())
            {
                var user = context.User.First(u => u.Sub == sub);
                var game_ids = context.GameUser.Where(gu => gu.UserId == user.Id).Select(gu => gu.GameId);

                return context.Game.Where(g => game_ids.Contains(g.Id) && g.Ended.Get(0)).Select(g => ParseGame(g)).ToArray();
            }
        }

        public static Game[] GetMyCreatedGames(string sub)
        {
            using (var context = new Database.postgresContext())
            {
                var user = context.User.First(u => u.Sub == sub);
                return context.Game.Where(g => g.CreatorId == user.Id).Select(g => ParseGame(g)).ToArray();
            }
        }

        public static Game GetGame(int game_id, string sub)
        {
            using (var context = new Database.postgresContext())
            {
                var user = context.User.First(u => u.Sub == sub);
                var game = context.Game.Include("Clue").First(g => g.Id == game_id);

                if (game.CreatorId != user.Id) game.WinCode = String.Empty;

                return ParseGame(game);
            }
        }

        public static Game WinGame(int game_id, string sub, string win_code)
        {
            using (var context = new Database.postgresContext())
            {
                var user = context.User.First(u => u.Sub == sub);

                using (var dbContextTransaction = context.Database.BeginTransaction())
                {
                    var game = context.Game.First(g => g.Id == game_id);

                    if (game.Ended.Get(0) == false)
                    {
                        if (game.WinCode == win_code)
                        {
                            game.WinId = user.Id;
                            game.WinTimestamp = DateTime.Now;
                            game.Ended.SetAll(true);

                            context.SaveChanges();
                        }
                    }

                    dbContextTransaction.Commit();

                    return ParseGame(game);
                }
            }
        }

        internal static Game SaveGame(string sub, DateTime endGame, double latitude, double longitude, string[] clues, int[] user_ids, string photo)
        {
            using (var context = new Database.postgresContext())
            {
                var user = context.User.First(u => u.Sub == sub);

                var game = new Database.Game()
                {
                    CreatorId = user.Id,
                    Ended = new BitArray(8, false),
                    Latitude = latitude,
                    Longitude = longitude,
                    Photo = photo,
                    StartDatetime = DateTime.Now,
                    EndDatetime = endGame,
                    WinCode = GenerateRandomCode()
                };
                
                context.Game.Add(game);

                context.SaveChanges();

                return ParseGame(game);
            }
        }

        private static Random random = new Random();
        private static string GenerateRandomCode()
        {
            const string chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            return new string(Enumerable.Repeat(chars, 50)
              .Select(s => s[random.Next(s.Length)]).ToArray());
        }

        private static User ParseUser(Database.User u)
        {
            return new User()
            {
                Id = u.Id,
                Alias = u.Alias,
                FirstName = u.FirstName,
                LastName = u.LastName,
                Mail = u.Mail,
                Sub = u.Sub
            };
        }

        private static Game ParseGame(Database.Game g)
        {
            return new Game()
            {
                Id = g.Id,
                CreatorId = g.CreatorId,
                EndDatetime = g.EndDatetime,
                Ended = g.Ended.Get(0),
                Latitude = g.Latitude,
                Longitude = g.Longitude,
                Photo = g.Photo,
                StartDatetime = g.StartDatetime,
                WinCode = g.WinCode,
                WinId = g.WinId,
                WinTimestamp = g.WinTimestamp
            };
        }
    }
}
