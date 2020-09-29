//  Created by Hunter on 23/09/2020.

import Foundation

protocol AuthenticationFlow {
}

protocol AuthenticationCaseUse {
    
}

class AuthenticationCoordinator<CaseUse: AuthenticationCaseUse, Flow: AuthenticationFlow> {
    let flow: Flow
    let caseUse: CaseUse
    
    init(flow: Flow, caseUse: CaseUse) {
        self.flow = flow
        self.caseUse = caseUse
    }
    
    func start() {
        
    }
}
