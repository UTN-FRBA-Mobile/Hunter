using Hunter.Database.Entities;
using Microsoft.EntityFrameworkCore;

namespace Hunter.Database
{
    public class HunterContext : DbContext
    {
        public static string ConnectionString = "";
        public HunterContext(DbContextOptions<HunterContext> options) : base(options) { }
        public DbSet<User> Users { get; set; }
        /*
        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.HasDefaultSchema("public");
            base.OnModelCreating(modelBuilder);
        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            optionsBuilder.UseNpgsql(ConnectionString);
        }
        */
    }
}