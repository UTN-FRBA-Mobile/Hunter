using Microsoft.AspNetCore;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Hosting;
using System;

namespace Hunter
{
    public class Program
    {
        public static void Main(string[] args)
        {
            Database.postgresContext.ConnectionString = ComposeConnectionString(Environment.GetEnvironmentVariable("DATABASE_URL"));
            Services.FirebaseAuthService.API_KEY = Environment.GetEnvironmentVariable("FIREBASE_APIKEY");
            //Database.postgresContext.ConnectionString = "Server=ec2-3-216-92-193.compute-1.amazonaws.com;uid=ouaqzwgabbxhxt;pwd=0687c2478f0521a595632a35a7638252b71b6db73f35badddf40bcc5fdc2153d;Database=d5qpniim1huq2a;";
            CreateHostBuilder(args).Build().Run();
        }

        public static IHostBuilder CreateHostBuilder(string[] args) =>
            Host.CreateDefaultBuilder(args)
                .ConfigureWebHostDefaults(webBuilder =>
                {
                    webBuilder
                        .UseKestrel()
                        .UseUrls("http://0.0.0.0:" + Environment.GetEnvironmentVariable("PORT"))
                        //.UseUrls("http://0.0.0.0:80")
                        .UseStartup<Startup>();
                });

        private static string ComposeConnectionString(string database_url)
        {
            database_url = database_url.Replace("postgres://", "");
            string uid = database_url.Substring(0, database_url.IndexOf(":"));
            database_url = database_url.Replace(uid + ":", "");
            string pwd = database_url.Substring(0, database_url.IndexOf("@"));
            database_url = database_url.Replace(pwd + "@", "");
            string server = database_url.Substring(0, database_url.IndexOf(":"));
            string database = database_url.Substring(database_url.LastIndexOf("/") + 1);
            return String.Format("Server={0};uid={1};pwd={2};Database={3};", server, uid, pwd, database);
        }
    }
}
