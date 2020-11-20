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
    
    func showEnablePermission(onAccept: @escaping (() -> Void), onCancel: @escaping (() -> Void)) {
        present(factory.askForPermissionScreen(with: ActionsForDecision(accept: onAccept, decline: onCancel)))
    }
}

