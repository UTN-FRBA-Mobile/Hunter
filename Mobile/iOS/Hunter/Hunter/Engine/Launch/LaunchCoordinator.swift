import Foundation

enum DummyError: Error {
    case value
}

protocol AuthenticationStatusService {
    func callService<Model: Decodable>(_ onCompletion: ActionResult<Model, Error>)
}

class UserStatusRestService: AuthenticationStatusService {
    func callService<Model: Decodable>(_ onCompletion: ActionResult<Model, Error>)
    {
        if Bool.random() {
            onCompletion(.success(Authentication(token: "asd") as! Model))
        } else {
            onCompletion(.failure(DummyError.value))
        }
    }
}

struct Authentication: Decodable {
    var token: String
}

typealias Token = String

protocol LocalAuthenticationChecker {
    func wasAuthenticated() -> String
    func userPassAuthentication(with value: String)
}

struct LocalAuthenticationRepository: LocalAuthenticationChecker {
    let key: String
    let save: ((Token, String) -> Void)
    let load: ((String) -> Token?)
    func wasAuthenticated() -> String { load(key) ?? "" }
    func userPassAuthentication(with value: String) { save(value, key) }
}
