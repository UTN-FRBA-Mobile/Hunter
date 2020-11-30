import Foundation

protocol LocalSignUpCaseUse {
    func signUp(with registry: UserRegister, onCompletion: @escaping ActionResult<NoReply, Error>)
}

class SignUpWithEmail: LocalSignUpCaseUse {
    let service: SignUpService
    
    init(service: SignUpService) {
        self.service = service
    }
    
    func signUp(with registry: UserRegister, onCompletion: @escaping ActionResult<NoReply, Error>) {
        service.signUp(with: registry, onCompletion: onCompletion)
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
