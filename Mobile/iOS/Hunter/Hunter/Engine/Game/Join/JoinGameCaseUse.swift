import Foundation

protocol JoinGameCaseUse {
    func join(with code: String, onCompletion: @escaping ActionResult<Game, Error>)
}

class JoinGame: JoinGameCaseUse {
    let service: JoinGameService
    
    init(service: JoinGameService) {
        self.service = service
    }
    
    func join(with code: String, onCompletion: @escaping ActionResult<Game, Error>) {
        service.join(with: code, onCompletion: onCompletion)
    }
}

protocol JoinGameService {
    func join(with gameID: String, onCompletion: @escaping ActionResult<Game, Error>)
}

class JoinGameRestService: JoinGameService {
    
    let networking: Networking
    
    init(networking: Networking) { self.networking = networking }
    
    func join(with gameID: String, onCompletion: @escaping ActionResult<Game, Error>) {
        
        let resource = Http<NoReply>(urlTyped: URL.Typed.business(endpoint: endpoint(with: gameID)),
                                     method: Http.Method.get)
        
        networking.call(resource, onCompletion)
    }
    
    private func endpoint(with id: String) -> String { "/api/Game/\(id)" }
}

class JoinGameMockService: JoinGameService {
    let game: Game
    
    init(game: Game = .mainGame) {
        self.game = game
    }
    
    func join(with gameID: String, onCompletion: @escaping ActionResult<Game, Error>) {
        DispatchQueue.main.asyncAfter(deadline: .now()+3) { [weak self] in
            guard let self = self else { return }
            onCompletion(.success(self.game))
        }
    }
}

struct Game: Codable {
    var id: Int = 0
    var creatorId: Int = 0
    var startDatetime: String?
    var endDatetime: String?
    var latitude: Double
    var longitude: Double
    var photo: String?
    var ended: Bool?
    var winId: Int? = 0
    var winTimestamp: String?
    var winCode: String?
    var clues: [String]?
    var userIds: [Int]?
}


extension Game {
    static var mainGame: Game {
        Game(id: 0,
             creatorId: 0,
             startDatetime: "",
             endDatetime: "",
             latitude: LocationCoordinate2D.mainHome.latitude,
             longitude: LocationCoordinate2D.mainHome.longitude,
             photo: "",
             ended: false,
             winId: nil,
             winTimestamp: nil,
             winCode: nil,
             clues: [],
             userIds: [])
    }
}
