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
        #warning("We need to wire methods!")
        #warning("Maybe Case use/Coordinator was necesary to see what methods are available to authenticate")
        let googleAuth: Method = (.google, { _ in print("Google!") })
        let facebookAuth: Method = (.facebook, { _ in print("Facebook!") })
        let emailAuth: Method = (.email, { _ in self.startRegistryWithEmail() })
        let methods: [Method] = [googleAuth, facebookAuth, emailAuth]
        let presenter = RegisterGuestPresenter(methods: methods)
        let viewResolver = iOSGuestFactory(presenter: presenter)
        let router = GuestRouter(navigation: dependencies.navigation, factory: viewResolver)
        router.showGuestScreen()
    }
    
    func startRegistryWithEmail() {
        let signUp = SignUpWithEmail()
        let viewResolver = LocalSignUpViewResolver()
        let router = LocalSignUpRouter(navigation: dependencies.navigation, factory: viewResolver)
        let coordinator = LocalSignUpCoordinator(flow: router, caseUse: signUp)
        coordinator.start()
    }
    
    func startAuthenticate(with token: Token) {
        #warning("For now we send to Home")
        onUserDidAuthenticated()
    }
    
    func onUserDidAuthenticated() {
        let viewResolver = HomeViewResolver()
        let router = HomeRouter(navigation: dependencies.navigation, factory: viewResolver)
        let home = Home(newGameFlow: createNewGame, joinGameFlow: joinGame)
        let coordinator = HomeCoordinator(flow: router, caseUse: home)
        coordinator.start()
    }
    
    func createNewGame() {
        let viewResolver = CreateGameViewResolver()
        let router = CreateGameRouter(navigation: dependencies.navigation, factory: viewResolver)
        let createGame = CreateGame(imageProvider: LocaliOSImageModule())
        let coordinator =  CreateGameCoordinator(flow: router, caseUse: createGame, onCreated: sendToActiveGame)
        coordinator.start()
    }
    
    #warning("Missing implementation of Active Game")
    func sendToActiveGame() {
        print("Hunter: Send to your Active Game")
    }
    
    #warning("Missing implementation of Join Game!")
    func joinGame() { print("Hunter: Join Game") }
}

#warning("We need to put final implementation!")
class LocaliOSImageModule: ImageCaseUse {

    func userWantsToTakePhoto(_ callback: @escaping ((ImageResultFlow) -> Void)) {
        callback(.didSelectAn(UIImage()))
    }

    func userWantsToUploadPhoto(_ callback: @escaping ((ImageResultFlow) -> Void)) {
        callback(.didSelectAn(UIImage()))
    }
}
