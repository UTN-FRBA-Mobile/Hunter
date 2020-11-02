import Foundation
import UIKit

class LoginRouter<Nav: UINavigationController,
                  Factory: LoginFactory>: LoginFlow, Router {
    let navigation: Nav
    let factory: Factory
    
    init(navigation: Nav, factory: Factory) {
        self.navigation = navigation
        self.factory = factory
    }
    func showLogin<Cu>(with caseuse: Cu,
                       ifIsANewUser: @escaping (() -> Void))
    where Cu : LoginCaseUse {
        show(factory.loginScreen(with:caseuse, startGuestFlow: ifIsANewUser))
    }
}
