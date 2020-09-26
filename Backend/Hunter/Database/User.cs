using System;
using System.Collections.Generic;

namespace Hunter.Database
{
    public partial class User
    {
        public User()
        {
            Game = new HashSet<Game>();
        }

        public int Id { get; set; }
        public string Sub { get; set; }
        public string Alias { get; set; }
        public string Mail { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }

        public virtual ICollection<Game> Game { get; set; }
    }
}
