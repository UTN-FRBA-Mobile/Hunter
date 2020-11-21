import Foundation

protocol LocalSignUpCaseUse {
    func signUp(with registry: UserRegister, onCompletion: @escaping ActionResult<NoReply, Error>)
}

protocol SignUpService {
    func signUp(with registry: UserRegister, onCompletion: @escaping ActionResult<NoReply, Error>)
}

class CreateUser: SignUpService {
    let networking: Networking
    let endpoint: String = "/api/User/Post"
    init(networking: Networking) { self.networking = networking }
    
    func signUp(with registry: UserRegister, onCompletion: @escaping ActionResult<NoReply, Error>) {
        let resource = Http(urlTyped: URL.Typed.business(endpoint: endpoint),
                            method: Http.Method.post(body: registry))
        networking.call(resource, onCompletion)
    }
}

class SignUpWithEmail: LocalSignUpCaseUse {
    let onWasRegistered: (() -> Void)
    let service: SignUpService
    
    init(service: SignUpService, onWasRegistered: @escaping (() -> Void)) {
        self.service = service
        self.onWasRegistered = onWasRegistered
    }
    
    func signUp(with registry: UserRegister, onCompletion: @escaping ActionResult<NoReply, Error>) {
        service.signUp(with: registry) { [weak self] result in
            guard let self = self else { return }
            onCompletion(result)
            switch result {
            case .success(_): self.onWasRegistered()
            default: break
            }
        }
    }
}

class LocalSignUpCoordinator<CaseUse: LocalSignUpCaseUse, Flow: LocalSignUpFlow> {
    let flow: Flow
    let caseUse: CaseUse
    
    init(flow: Flow, caseUse: CaseUse) {
        self.flow = flow
        self.caseUse = caseUse
    }
    
    func start() { flow.sendUserToRegister(with: caseUse) }
}
