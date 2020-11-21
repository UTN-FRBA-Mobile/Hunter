import Foundation

protocol LaunchCaseUse {
    func checkStatus(onHaveLocalToken: @escaping ((Token) -> Void))
}

struct Launch: LaunchCaseUse {
    let service: LocalAuthenticationChecker
    let handleNonExistentUser: (() -> Void)
    
    func checkStatus(onHaveLocalToken: @escaping ((Token) -> Void)) {
        service.wasAuthenticated { token in
            token.count > 0 ? onHaveLocalToken(token) : handleNonExistentUser()
        }
    }
}
