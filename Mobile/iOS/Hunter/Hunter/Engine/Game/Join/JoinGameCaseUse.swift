import Foundation

protocol JoinGameCaseUse {
    func join(with code: String, onCompletion: ActionResult<Game, Error>)
}

class JoinGame: JoinGameCaseUse {
    func join(with code: String, onCompletion: (Result<Game, Error>) -> Void) {
        #warning("Missing implementation!")
    }
}


struct Game: Codable {
    
}
