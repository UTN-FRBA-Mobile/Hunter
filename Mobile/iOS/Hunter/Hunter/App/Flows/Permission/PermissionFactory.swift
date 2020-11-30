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
        
        controller.titleLabel.text = presenter.title
        controller.titleLabel.textColor = .black
        
        controller.subtitleLabel.text = presenter.message
        controller.subtitleLabel.textColor = .black
        
        controller.setup(actions)
        controller.leftButton.setTitle(presenter.rejectTitle, for: .normal)
        controller.rightButton.setTitle(presenter.acceptTitle, for: .normal)
        
        controller.containerView.backgroundColor = .white
        controller.containerView.layer.borderWidth = 1.0
        controller.containerView.layer.borderColor = Color.Hunter.darkBlue.cgColor
        controller.containerView.layer.cornerRadius = 16.0
        
        return controller
    }
}
