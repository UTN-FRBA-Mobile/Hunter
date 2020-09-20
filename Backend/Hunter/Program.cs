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
            Database.HunterContext.ConnectionString = Environment.GetEnvironmentVariable("DATABASE_URL");
            CreateHostBuilder(args).Build().Run();
        }

        public static IHostBuilder CreateHostBuilder(string[] args) =>
            Host.CreateDefaultBuilder(args)
                .ConfigureWebHostDefaults(webBuilder =>
                {
                    webBuilder
                        .UseKestrel()
                        .UseUrls("http://0.0.0.0:" + Environment.GetEnvironmentVariable("PORT"))
                        .UseStartup<Startup>();
                });
    }
}
