import Foundation
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
