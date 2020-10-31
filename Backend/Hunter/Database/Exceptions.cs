using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Hunter.Database
{
    [Serializable]
    public class UserNotFoundException : Exception { }

    [Serializable]
    public class GameNotFoundException : Exception { }
}
