//  Created by Franco Leto on 28/09/2020.

import Foundation
import UIKit

class GuestRouter<Navigation: UINavigationController, Factory: GuestFactory>: GuestFlow, Router {
    let navigation: Navigation
    let factory: Factory
    init(navigation: Navigation, factory: Factory) {
        self.navigation = navigation
        self.factory = factory
    }
    
    func showGuestScreen<Cu: GuestCaseUse>(with caseUse: Cu) { show(factory.guestController(caseUse)) }
}
