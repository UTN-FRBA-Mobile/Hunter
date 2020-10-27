import Foundation

protocol LocalSignUpFlow {
    func sendUserToRegister<Cu: LocalSignUpCaseUse>(with caseUse: Cu)
}

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

import UIKit
class LocalSignUpRouter<Nav: UINavigationController,
                           Factory: LocalSignUpFactory>: LocalSignUpFlow, Router {
    let navigation: Nav
    let factory: Factory
    
    init(navigation: Nav, factory: Factory) {
        self.navigation = navigation
        self.factory = factory
    }
    
    func sendUserToRegister<Cu: LocalSignUpCaseUse>(with caseUse: Cu) {
        show(factory.LocalSignUpScreen())
    }
    
}

import UIKit
protocol LocalSignUpFactory {
    func LocalSignUpScreen() -> UIViewController
}

class LocalSignUpViewResolver: LocalSignUpFactory {
    func LocalSignUpScreen() -> UIViewController {
        let controller = LocalSignUpViewController()
        return controller
    }
}
