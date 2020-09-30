//  Created by Franco Leto on 28/09/2020.

import Foundation

protocol GuestCaseUse {
    func userWantsToRegister(by method: RegisterMethod)
}

typealias SingleAction<A> = ((A) -> Void)
struct SignUpLogic: GuestCaseUse {
    var registers: [(RegisterMethod,SingleAction<Void>)]
    func userWantsToRegister(by method: RegisterMethod) {
        registers.forEach { actionMethod in
            guard actionMethod.0 == method else { return }
            actionMethod.1(())
        }
    }
}
