import Foundation
import UIKit

struct GuestRouter<Navigation: UINavigationController,
                   Factory: GuestFactory>: GuestFlow, Router {
    let navigation: Navigation
    let factory: Factory
    func showGuestScreen() {
        show(factory.guestController())
    }
}
