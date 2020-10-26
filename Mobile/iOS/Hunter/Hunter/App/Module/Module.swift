import Foundation
import UIKit

public class Module<Nav: UINavigationController, Net: Networking> {
    
    public struct Dependencies<Nav: UINavigationController, Net: Networking> {
        let navigation: Nav
        let networking: Net
    }
    
    private let dependencies: Dependencies<Nav, Net>
    public init(_ dependencies: Dependencies<Nav, Net>) { self.dependencies = dependencies }

    func launch() { checkAuthentication() }
}

fileprivate extension Module {
    /// Step One: We check if the user was already authenticated (Have a local token)
    #warning("We need to use a secure storage such as Keychain")
    func checkAuthentication() {
        let authentication = LocalAuthenticationRepository(key: "userToken",
                                                           save: UserDefaults.standard.set,
                                                           load: UserDefaults.standard.string)
        Launch(service: authentication,
               handleNonExistentUser: showGuestFlow)
            .checkStatus(onHaveLocalToken: startAuthenticate)
    }
    private typealias Method = (RegisterMethod, SingleAction<Void>)
    func showGuestFlow() {
        let googleAuth: Method = (.google, { _ in print("Google!") })
        let facebookAuth: Method = (.facebook, { _ in print("Facebook!") })
        let emailAuth: Method = (.email, { _ in self.signUpWithEmail() })
        let methods: [Method] = [googleAuth, facebookAuth, emailAuth]
        let register = SignUpLogic(registers: methods)
        let presenter = RegisterGuestPresenter(methods: methods)
        let viewResolver = iOSGuestFactory(presenter: presenter)
        let router = GuestRouter(navigation: dependencies.navigation,
                                 factory: viewResolver)
        let coordinator = GuestCoordinator(flow: router, caseUse: register)
        coordinator.start()
    }
    
    func signUpWithEmail() {
        #warning("To Be Coded!")
    }
    
    func startAuthenticate(with token: Token) {
        #warning("To Be Coded!")
    }
}
