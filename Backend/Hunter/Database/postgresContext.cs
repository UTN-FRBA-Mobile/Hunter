using System;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

namespace Hunter.Database
{
    public partial class postgresContext : DbContext
    {
        public static string ConnectionString = "Server=localhost;uid=postgres;pwd=admin;";
        public postgresContext()
        {
        }

        public postgresContext(DbContextOptions<postgresContext> options)
            : base(options)
        {
        }

        public virtual DbSet<Clue> Clue { get; set; }
        public virtual DbSet<Game> Game { get; set; }
        public virtual DbSet<GameUser> GameUser { get; set; }
        public virtual DbSet<User> User { get; set; }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
                optionsBuilder.UseNpgsql(ConnectionString);
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Clue>(entity =>
            {
                entity.ToTable("clue");

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .UseIdentityAlwaysColumn();

                entity.Property(e => e.GameId).HasColumnName("game_id");

                entity.Property(e => e.Text)
                    .IsRequired()
                    .HasColumnName("text");
            });

            modelBuilder.Entity<Game>(entity =>
            {
                entity.ToTable("game");

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .UseIdentityAlwaysColumn();

                entity.Property(e => e.CreatorId).HasColumnName("creator_id");

                entity.Property(e => e.EndDatetime).HasColumnName("end_datetime");
                entity.Property(e => e.StartDatetime).HasColumnName("start_datetime");

                entity.Property(e => e.Ended)
                    .HasColumnName("ended")
                    .HasColumnType("boolean");

                entity.Property(e => e.Latitude).HasColumnName("latitude");

                entity.Property(e => e.Longitude).HasColumnName("longitude");

                entity.Property(e => e.Photo)
                    .IsRequired()
                    .HasColumnName("photo")
                    .HasColumnType("character varying");

                entity.Property(e => e.WinCode)
                    .HasColumnName("win_code")
                    .HasColumnType("character varying");

                entity.Property(e => e.WinId).HasColumnName("win_id");

                entity.Property(e => e.WinTimestamp).HasColumnName("win_timestamp");

                entity.HasOne(d => d.Creator)
                    .WithMany(p => p.Game)
                    .HasForeignKey(d => d.CreatorId)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("game_creator_id_fkey");
            });

            modelBuilder.Entity<GameUser>(entity =>
            {
                entity.HasKey(e => new { e.UserId, e.GameId })
                    .HasName("game_user_user_id_game_id");

                entity.ToTable("game_user");

                entity.Property(e => e.UserId).HasColumnName("user_id");

                entity.Property(e => e.GameId).HasColumnName("game_id");
            });

            modelBuilder.Entity<User>(entity =>
            {
                entity.ToTable("user");

                entity.HasIndex(e => e.Sub)
                    .HasName("user_sub")
                    .IsUnique();

                entity.Property(e => e.Id)
                    .HasColumnName("id")
                    .UseIdentityAlwaysColumn();

                entity.Property(e => e.Alias)
                    .IsRequired()
                    .HasColumnName("alias")
                    .HasColumnType("character varying");

                entity.Property(e => e.FirstName)
                    .IsRequired()
                    .HasColumnName("first_name")
                    .HasColumnType("character varying");

                entity.Property(e => e.LastName)
                    .IsRequired()
                    .HasColumnName("last_name")
                    .HasColumnType("character varying");

                entity.Property(e => e.Mail)
                    .IsRequired()
                    .HasColumnName("mail")
                    .HasColumnType("character varying");

                entity.Property(e => e.Sub)
                    .IsRequired()
                    .HasColumnName("sub")
                    .HasColumnType("character varying");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
