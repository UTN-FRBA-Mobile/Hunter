using System;
using System.Collections.Generic;

namespace Hunter.Database
{
    public partial class Clue
    {
        public int Id { get; set; }
        public int GameId { get; set; }
        public string Text { get; set; }
    }
}
