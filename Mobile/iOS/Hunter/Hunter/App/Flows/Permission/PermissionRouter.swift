import Foundation
import UIKit

class PermissionRouter<Nav: UINavigationController,
                       Factory: PermissionFactory>: Router, PermissionFlow {
    let navigation: Nav
    let factory: Factory
    
    init(navigation: Nav, factory: Factory) {
        self.navigation = navigation
        self.factory = factory
    }
    
    func showEnablePermission(with actions: ActionsForDecision) {
        present(factory.askForPermissionScreen(with: actions))
    }
}

