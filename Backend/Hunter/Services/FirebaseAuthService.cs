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
    public class FirebaseAuthService
    {
        static string url = "https://fcm.googleapis.com/fcm/send";
        public static string API_KEY = "";

        public static void GameEnded(int game_id, Database.User user)
        {
            try
            {
                var push = new PushMessage()
                {
                    to = String.Format("/topics/{0}", game_id),

                    notification = new PushMessageData()
                    {
                        text = String.Format("El juego ha finalizado. El ganador es: {0} {1}", user.FirstName, user.LastName),
                        title = String.Format("El juego ha finalizado. El ganador es: {0} {1}", user.FirstName, user.LastName),
                        click_action = String.Format("El juego ha finalizado. El ganador es: {0} {1}", user.FirstName, user.LastName)
                    },
                    data = game_id,
                    time_to_live = 1800
                };

                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
                request.Method = "POST";
                request.Headers.Add("Authorization", "key=" + API_KEY);
                request.ContentType = "application/json";
                string json = JsonConvert.SerializeObject(push);
                byte[] byteArray = Encoding.UTF8.GetBytes(json);
                request.ContentLength = byteArray.Length;
                Stream dataStream = request.GetRequestStream();
                dataStream.Write(byteArray, 0, byteArray.Length);
                dataStream.Close();
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                if (response.StatusCode == HttpStatusCode.Accepted || response.StatusCode == HttpStatusCode.OK || response.StatusCode == HttpStatusCode.Created)
                {
                    StreamReader read = new StreamReader(response.GetResponseStream());
                    String result = read.ReadToEnd();
                    read.Close();
                    response.Close();
                    dynamic stuff = JsonConvert.DeserializeObject(result);
                }
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
                var push = new PushMessage()
                {
                    to = String.Format("/topics/{0}", sub),

                    notification = new PushMessageData()
                    {
                        text = "Ha sido invitado a una nueva busqueda del tesoro!",
                        title = "Ha sido invitado a una nueva busqueda del tesoro!",
                        click_action = "Ha sido invitado a una nueva busqueda del tesoro!"
                    },
                    data = game_id,
                    time_to_live = 1800
                };

                HttpWebRequest request = (HttpWebRequest)WebRequest.Create(url);
                request.Method = "POST";
                request.Headers.Add("Authorization", "key=" + API_KEY);
                request.ContentType = "application/json";
                string json = JsonConvert.SerializeObject(push);
                byte[] byteArray = Encoding.UTF8.GetBytes(json);
                request.ContentLength = byteArray.Length;
                Stream dataStream = request.GetRequestStream();
                dataStream.Write(byteArray, 0, byteArray.Length);
                dataStream.Close();
                HttpWebResponse response = (HttpWebResponse)request.GetResponse();
                if (response.StatusCode == HttpStatusCode.Accepted || response.StatusCode == HttpStatusCode.OK || response.StatusCode == HttpStatusCode.Created)
                {
                    StreamReader read = new StreamReader(response.GetResponseStream());
                    String result = read.ReadToEnd();
                    read.Close();
                    response.Close();
                    dynamic stuff = JsonConvert.DeserializeObject(result);
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error Firebase: {0}", ex.Message);
            }
        }

        public class PushMessage
        {
            private string _to;
            private PushMessageData _notification;

            private dynamic _data;
            private dynamic _click_action;

            private int _time_to_live;

            public dynamic data
            {
                get { return _data; }
                set { _data = value; }
            }

            public string to
            {
                get { return _to; }
                set { _to = value; }
            }
            public int time_to_live
            {
                get { return _time_to_live; }
                set { _time_to_live = value; }
            }

            public PushMessageData notification
            {
                get { return _notification; }
                set { _notification = value; }
            }

            public dynamic click_action
            {
                get
                {
                    return _click_action;
                }

                set
                {
                    _click_action = value;
                }
            }
        }

        public class PushMessageData
        {
            private string _title;
            private string _text;
            private string _sound = "default";
            private string _click_action;
            public string sound
            {
                get { return _sound; }
                set { _sound = value; }
            }

            public string title
            {
                get { return _title; }
                set { _title = value; }
            }
            public string text
            {
                get { return _text; }
                set { _text = value; }
            }

            public string click_action
            {
                get
                {
                    return _click_action;
                }

                set
                {
                    _click_action = value;
                }
            }
        }
    }
}