//  Created by Franco Leto on 28/09/2020.

import Foundation
import UIKit

protocol GuestFactory {
    func guestController<Cu: GuestCaseUse>(_ caseUse: Cu) -> UIViewController
}
