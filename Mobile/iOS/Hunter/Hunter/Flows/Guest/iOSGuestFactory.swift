//  Created by Franco Leto on 28/09/2020.

import UIKit

struct iOSGuestFactory: GuestFactory {
    let presenter: GuestPresenter
    func guestController() -> UIViewController {
        let controller = GuestViewController()
        _ = controller.view
        controller.buttonStack.addArrangedSubviews(presenter.actionsButton())
        return controller
    }
}
