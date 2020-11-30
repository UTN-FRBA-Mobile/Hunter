import Foundation

protocol SignUpService {
    func signUp(with registry: UserRegister, onCompletion: @escaping ActionResult<NoReply, Error>)
}

class CreateUser: SignUpService {
    
    private struct SignUpServiceModel: Codable {
        let alias: String
        let mail: String
        let firstName: String
        let lastName: String
    }

    let networking: Networking
    let endpoint: String = "/api/User/Post"
    
    init(networking: Networking) { self.networking = networking }
    
    func signUp(with registry: UserRegister, onCompletion: @escaping ActionResult<NoReply, Error>) {
        
        let serviceModel = SignUpServiceModel(alias: registry.alias, mail: registry.email, firstName: registry.firstName, lastName: registry.lastName)
        
        let resource = Http(urlTyped: URL.Typed.business(endpoint: endpoint),
                            method: Http.Method.post(body: serviceModel))
        
        networking.call(resource, onCompletion)
        
    }
    
}
