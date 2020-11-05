using Hunter.Models;
using Microsoft.EntityFrameworkCore;
using System;
using System.Linq;

namespace Hunter.Services
{
    public class DatabaseService
    {
        public static User GetUserById(int id)
        {
            using (var context = new Database.postgresContext())
            {
                return ParseUser(context.User.Find(id));
            }
        }

        internal static void JoinGame(string sub, int game_id)
        {
            using (var context = new Database.postgresContext())
            {
                var user = context.User.FirstOrDefault(u => u.Sub == sub);

                if (user == null) throw new Database.UserNotFoundException();

                var game = context.Game.FirstOrDefault(g => g.Id == game_id);

                if (game == null) throw new Database.GameNotFoundException();

                Database.GameUser gu = new Database.GameUser();
                gu.GameId = game.Id;
                gu.UserId = user.Id;
                context.GameUser.Add(gu);

                context.SaveChanges();
            }
        }

        public static User GetUser(string sub)
        {
            using (var context = new Database.postgresContext())
            {
                return ParseUser(context.User.FirstOrDefault(u => u.Sub == sub));
            }
        }

        public static User GetUserByAlias(string alias)
        {
            using (var context = new Database.postgresContext())
            {
                return ParseUser(context.User.FirstOrDefault(u => u.Alias == alias));
            }
        }

        public static User FindUser(User user)
        {
            using (var context = new Database.postgresContext())
            {
                var u = context.User.FirstOrDefault(u => String.IsNullOrEmpty(user.Alias) ? u.Mail == user.Mail : u.Alias == user.Alias );
                return ParseUser(u);
            }
        }

        public static User SaveUser(User userNew)
        {
            using (var context = new Database.postgresContext())
            {
                var user = context.User.FirstOrDefault(u => u.Sub == userNew.Sub);
                
                if (user == null) user = new Database.User();

                user.Alias = userNew.Alias;
                user.FirstName = userNew.FirstName;
                user.LastName = userNew.LastName;
                user.Mail = userNew.Mail;
                user.Sub = userNew.Sub;

                if (user.Id == 0) context.User.Add(user);

                context.SaveChanges();

                return ParseUser(user);
            }
        }

        public static User SaveUser(string sub, string alias, string mail, string first_name, string last_name)
        {
            using (var context = new Database.postgresContext())
            {
                var user = context.User.FirstOrDefault(u => u.Sub == sub);

                if (user == null) user = new Database.User();

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
                var user = context.User.FirstOrDefault(u => u.Sub == sub);

                if (user == null) throw new Database.UserNotFoundException();

                var game_ids = context.GameUser.Where(gu => gu.UserId == user.Id).Select(gu => gu.GameId);

                return context.Game.Where(g => game_ids.Contains(g.Id) && g.Ended).Select(g => ParseGame(g)).ToArray();
            }
        }

        public static Game[] GetMyCreatedGames(string sub)
        {
            using (var context = new Database.postgresContext())
            {
                var user = context.User.FirstOrDefault(x => x.Sub == sub);

                if (user == null) throw new Database.UserNotFoundException();

                return context.Game.Where(g => g.CreatorId == user.Id).Select(g => ParseGame(g)).ToArray();
            }
        }

        public static Game GetGame(int game_id, string sub)
        {
            using (var context = new Database.postgresContext())
            {
                var user = context.User.FirstOrDefault(u => u.Sub == sub);

                if (user == null) throw new Database.UserNotFoundException();

                var game = context.Game.FirstOrDefault(g => g.Id == game_id);

                if (game == null) throw new Database.GameNotFoundException();

                game.Clues = context.Clue.Where(c => c.GameId == game.Id).Select(c => c.Text).ToArray();
                game.UserIds = context.GameUser.Where(c => c.GameId == game.Id).Select(c => c.UserId).ToArray();

                if (game.CreatorId != user.Id) game.WinCode = String.Empty;

                return ParseGame(game);
            }
        }

        public static Game WinGame(int game_id, string sub, string win_code)
        {
            using (var context = new Database.postgresContext())
            {
                var user = context.User.FirstOrDefault(u => u.Sub == sub);

                if (user == null) throw new Database.UserNotFoundException();

                using (var dbContextTransaction = context.Database.BeginTransaction())
                {
                    var game = context.Game.FirstOrDefault(g => g.Id == game_id);

                    if (game == null) throw new Database.GameNotFoundException();

                    if (!game.Ended)
                    {
                        if (game.WinCode == win_code)
                        {
                            game.WinId = user.Id;
                            game.WinTimestamp = DateTime.Now;
                            game.Ended = true;

                            context.SaveChanges();

                            CloudMessagingService.GameEnded(game_id, user);
                        }
                    }

                    dbContextTransaction.Commit();

                    return ParseGame(game);
                }
            }
        }

        internal static Game SaveGame(string sub, Game gameNew)
        {
            using (var context = new Database.postgresContext())
            {
                var user = context.User.First(u => u.Sub == sub);

                var game = new Database.Game()
                {
                    CreatorId = user.Id,
                    Ended = false,
                    Latitude = gameNew.Latitude,
                    Longitude = gameNew.Longitude,
                    Photo = gameNew.Photo,
                    StartDatetime = DateTime.Now,
                    EndDatetime = gameNew.EndDatetime,
                    WinCode = GenerateRandomCode()                    
                };

                context.Game.Add(game);

                context.SaveChanges();

                foreach (string clue in gameNew.Clues)
                {
                    Database.Clue c = new Database.Clue();
                    c.GameId = game.Id;
                    c.Text = clue;
                    context.Clue.Add(c);
                }

                foreach (int user_id in gameNew.UserIds)
                {
                    var u = context.User.Find(user_id);
                    Database.GameUser gu = new Database.GameUser();
                    gu.GameId = game.Id;
                    gu.UserId = user_id;
                    context.GameUser.Add(gu);
                    CloudMessagingService.GameInvitation(user.Sub, game.Id);
                }

                context.SaveChanges();



                return ParseGame(game);
            }
        }

        internal static Game SaveGame(string sub, DateTime endGame, double latitude, double longitude, string[] clues, int[] user_ids, string photo)
        {
            using (var context = new Database.postgresContext())
            {
                var user = context.User.FirstOrDefault(u => u.Sub == sub);

                if (user == null) throw new Database.UserNotFoundException();

                var game = new Database.Game()
                {
                    CreatorId = user.Id,
                    Ended = false,
                    Latitude = latitude,
                    Longitude = longitude,
                    Photo = photo,
                    StartDatetime = DateTime.Now,
                    EndDatetime = endGame,
                    WinCode = GenerateRandomCode()
                };
                
                context.Game.Add(game);

                context.SaveChanges();

                foreach (string clue in clues)
                {
                    Database.Clue c = new Database.Clue();
                    c.GameId = game.Id;
                    c.Text = clue;
                    context.Clue.Add(c);
                }

                foreach (int user_id in user_ids)
                {
                    var u = context.User.Find(user_id);
                    Database.GameUser gu = new Database.GameUser();
                    gu.GameId = game.Id;
                    gu.UserId = user_id;
                    context.GameUser.Add(gu);
                    CloudMessagingService.GameInvitation(user.Sub, game.Id);
                }

                context.SaveChanges();

                return ParseGame(game);
            }
        }

        internal static Game UpdatePhoto(int game_id, string sub, string photo)
        {
            using (var context = new Database.postgresContext())
            {
                var user = context.User.FirstOrDefault(u => u.Sub == sub);

                if (user == null) throw new Database.UserNotFoundException();

                if (user.Sub != sub) throw new Database.UserNotFoundException();

                var game = context.Game.Find(game_id);

                if (game == null) throw new Database.GameNotFoundException();

                game.Photo = photo;

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
            if (u == null || u.Id == 0) throw new Database.UserNotFoundException(); 

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
            if (g == null || g.Id == 0) throw new Database.GameNotFoundException();

            return new Game()
            {
                Id = g.Id,
                CreatorId = g.CreatorId,
                EndDatetime = g.EndDatetime,
                Ended = g.Ended,
                Latitude = g.Latitude,
                Longitude = g.Longitude,
                Photo = g.Photo,
                StartDatetime = g.StartDatetime,
                WinCode = g.WinCode,
                WinId = g.WinId,
                WinTimestamp = g.WinTimestamp,
                Clues = g.Clues,
                UserIds = g.UserIds
            };
        }
    }
}
