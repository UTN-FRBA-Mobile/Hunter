import Foundation

protocol LoginAuthentication {
    func authenticate<SM: Encodable, M: Decodable>(with serviceModel: SM,
                                                   onCompletion: ActionResult<M, Error>)
}

struct LoginServiceModel { let email, password: String }

class RestLoginService: LoginAuthentication {
    func authenticate<SM, M>(with serviceModel: SM, onCompletion: (Result<M, Error>) -> Void)
    where SM : Encodable, M : Decodable
    {
        
    }
}

protocol LoginCaseUse {
    func authenticate(with email: String,
                      and password: String,
                      onCompletion: @escaping ActionResult<NoReply, Error>)
}

class Login: LoginCaseUse {
    let onWasAuthenticated: (() -> Void)
    
    init(onWasAuthenticated: @escaping (() -> Void)) {
        self.onWasAuthenticated = onWasAuthenticated
    }
    
    func authenticate(with email: String,
                      and password: String,
                      onCompletion: @escaping ActionResult<NoReply, Error>) {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            onCompletion(.success(NoReply()))
            self.onWasAuthenticated()
        }
    }
}

class LoginCoordinator<CaseUse: LoginCaseUse, Flow: LoginFlow> {
    let flow: Flow
    let caseUse: CaseUse
    let startNewUserFlow: (() -> Void)
    
    init(flow: Flow, caseUse: CaseUse, onIsANewUser: @escaping (() -> Void)) {
        self.flow = flow
        self.caseUse = caseUse
        self.startNewUserFlow = onIsANewUser
    }
    
    func start() { flow.showLogin(with: caseUse, ifIsANewUser: startNewUserFlow) }
}
