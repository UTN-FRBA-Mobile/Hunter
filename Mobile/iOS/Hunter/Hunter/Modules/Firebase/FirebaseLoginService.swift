import Foundation
import Firebase

class FirebaseLoginService: LoginAuthentication {
    
    func authenticate(with serviceModel: LoginServiceModel, onCompletion: @escaping ActionResult<NoReply, Error>) -> Void {
        Auth.auth().signIn(withEmail: serviceModel.email,
                           password: serviceModel.password) { (auth, optionalError) in
            
            if let error = optionalError {
                onCompletion(.failure(error))
                
                return
            }
            
            onCompletion(.success(NoReply()))
        }
    }
}
