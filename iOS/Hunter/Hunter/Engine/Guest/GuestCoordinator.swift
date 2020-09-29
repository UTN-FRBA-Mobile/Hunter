//  Created by Hunter on 28/09/2020.

import Foundation

class GuestCoordinator<CaseUse: GuestCaseUse, Flow: GuestFlow> {
    let flow: Flow
    let caseUse: CaseUse
    
    init(flow: Flow, caseUse: CaseUse) {
        self.flow = flow
        self.caseUse = caseUse
    }
    
    func start() { flow.showGuestScreen(with: caseUse) }
}
