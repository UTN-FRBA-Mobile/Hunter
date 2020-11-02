import Foundation

protocol LocalSignUpCaseUse {
    
}

class SignUpWithEmail: LocalSignUpCaseUse {
    
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
