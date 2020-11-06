//  Created by Franco Leto on 28/09/2020.

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
