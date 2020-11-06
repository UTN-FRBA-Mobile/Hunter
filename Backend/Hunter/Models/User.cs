using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Hunter.Models
{
    public class User
    {
        public int Id { get; set; }
        public string Sub { get; set; }
        public string Alias { get; set; }
        public string Mail { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }

    }
}
