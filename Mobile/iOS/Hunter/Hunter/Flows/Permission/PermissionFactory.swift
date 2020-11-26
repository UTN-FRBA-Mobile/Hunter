import Foundation
import UIKit

protocol PermissionFactory {
    func askForPermissionScreen(with actions: ActionsForDecision) -> UIViewController
}

struct PermissionViewResolver: PermissionFactory {
    
    let presenter: PermissionPresenter
    
    func askForPermissionScreen(with actions: ActionsForDecision) -> UIViewController {
        let controller = PermissionViewController()
        _ = controller.view
        controller.setup(actions)
        return controller
    }
}
