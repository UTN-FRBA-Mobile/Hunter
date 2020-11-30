import Foundation
import Firebase

struct DoubleRegistration: SignUpService {
    
    let first: SignUpService
    let second: SignUpService
    
    func signUp(with registry: UserRegister, onCompletion: @escaping ActionResult<NoReply, Error>) {
        
        first.signUp(with: registry) { result in
            
            switch result {
            case .success(_):
                second.signUp(with: registry, onCompletion: onCompletion)
            case .failure(let error):
                onCompletion(.failure(error))
            }
        }
    }
    
}


class RegisterUserService: SignUpService {
    
    func signUp(with registry: UserRegister, onCompletion: @escaping ActionResult<NoReply, Error>) {
        Auth.auth().createUser(withEmail: registry.email, password: registry.password) { (result, error) in
            
            if let error = error {
                onCompletion(.failure(error))
                return
            }
            
            onCompletion(.success(NoReply()))
        }
    }
    
}
