//  Created by Franco Leto on 28/09/2020.

import Foundation

protocol GuestFlow {
    func showGuestScreen<Cu: GuestCaseUse>(with caseUse: Cu)
}
