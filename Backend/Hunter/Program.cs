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
            //Database.postgresContext.ConnectionString = Environment.GetEnvironmentVariable("DATABASE_URL");
            Database.postgresContext.ConnectionString = "postgres://ouaqzwgabbxhxt:0687c2478f0521a595632a35a7638252b71b6db73f35badddf40bcc5fdc2153d@ec2-3-216-92-193.compute-1.amazonaws.com:5432/d5qpniim1huq2a";
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
    }
}
