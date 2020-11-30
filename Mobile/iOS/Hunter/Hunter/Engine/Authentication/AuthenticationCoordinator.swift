import Foundation

protocol AuthenticationFlow {

}

protocol AuthenticationCaseUse {
    
}

class AuthenticationCoordinator<CaseUse: AuthenticationCaseUse, Flow: AuthenticationFlow> {
    let flow: Flow
    let caseUse: CaseUse
    
    init(flow: Flow, caseUse: CaseUse) {
        self.flow = flow
        self.caseUse = caseUse
    }
    
    func start() {
        
    }
}

import UIKit
class AuthenticationRouter<Nav: UINavigationController,
                           Factory: AuthenticationFactory>: AuthenticationFlow, Router {
    let navigation: Nav
    let factory: Factory
    
    init(navigation: Nav, factory: Factory) {
        self.navigation = navigation
        self.factory = factory
    }
    
    func showAuthentication() {
        show(factory.authenticationScreen())
    }
    
}

import UIKit
protocol AuthenticationFactory {
    func authenticationScreen() -> UIViewController
}

class AuthenticationViewResolver: AuthenticationFactory {
    func authenticationScreen() -> UIViewController {
        UIViewController()
    }
}
