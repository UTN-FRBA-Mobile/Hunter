import Foundation

protocol LoginAuthentication {
    func authenticate(with serviceModel: LoginServiceModel,
                      onCompletion: @escaping ActionResult<NoReply, Error>)
}

struct LoginServiceModel: Codable { let email, password: String }

protocol LoginCaseUse {
    func authenticate(with email: String,
                      and password: String,
                      onCompletion: @escaping ActionResult<NoReply, Error>)
}

class Login: LoginCaseUse {
    let service: LoginAuthentication
    let onWasAuthenticated: (() -> Void)
    
    init(service: LoginAuthentication, onWasAuthenticated: @escaping (() -> Void)) {
        self.service = service
        self.onWasAuthenticated = onWasAuthenticated
    }
    
    func authenticate(with email: String,
                      and password: String,
                      onCompletion: @escaping ActionResult<NoReply, Error>) {
        
        let serviceModel = LoginServiceModel(email: email, password: password)
        service.authenticate(with: serviceModel) { [weak self] result in
            switch result {
            case .success(_):
                self?.onWasAuthenticated()
            default:
                break
            }
            onCompletion(result)
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
