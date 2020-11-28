using FirebaseAdmin;
using FirebaseAdmin.Messaging;
using Google.Apis.Auth.OAuth2;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;

namespace Hunter.Services
{
    public class CloudMessagingService
    {
        static FirebaseApp firebaseApp = null;

        private static void SendNotification(Message message)
        {
            try
            {
                if (firebaseApp == null)
                {
                    firebaseApp = FirebaseApp.Create(new AppOptions()
                    {
                        Credential = GoogleCredential.FromFile(Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "key.json")),
                    });
                }

                Console.WriteLine(firebaseApp.Name); // "[DEFAULT]"

                var messaging = FirebaseMessaging.DefaultInstance;
                var result = messaging.SendAsync(message);
                result.Wait();
                Console.WriteLine(result.Result); //projects/myapp/messages/2492588335721724324
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error Firebase Sending: {0}", ex.Message);
            }
        }

        public static void GameEnded(int game_id, Database.User user)
        {
            GameEnded(game_id, user.FirstName, user.LastName);
        }

        public static void GameEnded(int game_id, string first_name, string last_name)
        {
            try
            {
                var message = new Message()
                {
                    Topic = game_id.ToString(),
                    Notification = new Notification()
                    {
                        Body = String.Format("El ganador es: {0} {1}", first_name, last_name),
                        Title = String.Format("El juego ha finalizado!"),
                    },
                    Data = new Dictionary<string, string>() {  ["game_id"] = game_id.ToString() }
                };

                SendNotification(message);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error Firebase: {0}", ex.Message);
            }
        }

        public static void GameInvitation(string sub, int game_id)
        {
            try
            {
                var message = new Message()
                {
                    Topic = sub,
                    Notification = new Notification()
                    {
                        Body = "Ha sido invitado a una nueva búsqueda del tesoro!",
                        Title = "Importante!",
                    },
                    Data = new Dictionary<string, string>() { ["game_id"] = game_id.ToString() }
                };

                SendNotification(message);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error Firebase: {0}", ex.Message);
            }
        }
    }
}
