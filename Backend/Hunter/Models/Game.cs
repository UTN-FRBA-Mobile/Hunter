using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Hunter.Models
{
    public class Game
    {
        public int Id { get; set; }
        public int CreatorId { get; set; }
        public DateTime StartDatetime { get; set; }
        public DateTime? EndDatetime { get; set; }
        public double Latitude { get; set; }
        public double Longitude { get; set; }
        public string Photo { get; set; }
        public bool Ended { get; set; }
        public int? WinId { get; set; }
        public DateTime? WinTimestamp { get; set; }
        public string WinCode { get; set; }
    }
}
