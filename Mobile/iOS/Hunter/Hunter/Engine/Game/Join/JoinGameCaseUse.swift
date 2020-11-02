import Foundation

protocol JoinGameCaseUse {
    func join(with code: String, onCompletion: @escaping ActionResult<Game, Error>)
}

class JoinGame: JoinGameCaseUse {
    func join(with code: String, onCompletion: @escaping ActionResult<Game, Error>) {
        DispatchQueue.main.asyncAfter(deadline: .now()+2) {
            onCompletion(.success(Game()))
        }
    }
}


struct Game: Codable {
    
}
