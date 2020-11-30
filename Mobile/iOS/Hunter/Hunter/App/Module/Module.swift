import Foundation
import UIKit
import FirebaseAuth
import Firebase
import MapKit

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
    
    func showLoginFlow() {
        let viewResolver = LoginViewResolver()
        let router = LoginRouter(navigation: dependencies.navigation, factory: viewResolver)
        let login = Login(service: FirebaseLoginService())
        let coordinator = LoginCoordinator(flow: router, caseUse: login, onIsANewUser: startRegistryWithEmail)
        coordinator.start()
    }
    
    /// Step One: We check if the user was already authenticated (Have a local token)
    func checkAuthentication() {
        let searchForToken: ((String, @escaping ((Token) -> Void)) -> Void) = { (key, callback) in
            Auth.auth().addStateDidChangeListener { (_, possibleUser) in
                guard let user = possibleUser else {
                    return callback("")
                }
                
                user.getIDToken { (token, _) in callback(token ?? "") }
            }
        }
        let authentication = LocalAuthenticationRepository(key: "userToken",
                                                           save: { (_,_) in },
                                                           load: searchForToken)
        Launch(service: authentication,
               handleNonExistentUser: showLoginFlow)
            .checkStatus(onHaveLocalToken: startAuthenticate)
    }
    
    func startRegistryWithEmail() {
        let createUser = DoubleRegistration(first: RegisterUserService(),
                                            second: CreateUser(networking: dependencies.networking))
        let signUp = SignUpWithEmail(service: createUser)
        let viewResolver = LocalSignUpViewResolver()
        let router = LocalSignUpRouter(navigation: dependencies.navigation, factory: viewResolver)
        let coordinator = LocalSignUpCoordinator(flow: router, caseUse: signUp)
        coordinator.start()
    }
    
    func startAuthenticate(with token: Token) {
        dependencies.networking.setToken(token)
        sendHome()
    }
    
    func sendHome() {
        let viewResolver = HomeViewResolver()
        let router = HomeRouter(navigation: dependencies.navigation, factory: viewResolver)
        let home = Home(newGameFlow: createNewGame, joinGameFlow: sentToJoinAGame)
        let coordinator = HomeCoordinator(flow: router, caseUse: home)
        coordinator.start()
    }
    
    func createNewGame() {
        try? Auth.auth().signOut()
        return

        let viewResolver = CreateGameViewResolver()
        let router = CreateGameRouter(navigation: dependencies.navigation, factory: viewResolver)
        let createGame = CreateGame(imageProvider: LocaliOSImageModule())
        let coordinator =  CreateGameCoordinator(flow: router, caseUse: createGame, onCreated: sendToActiveGame)
        coordinator.start()
    }
    
    func sendToActiveGame(_ game: Game) {
    let startGame: ((LocationPermission) -> Void) = { self.showGame(game, with: $0) }
        let requestLocation = { self.requestUserLocation(to: startGame) }
        checkGpsPermissions(for: requestLocation)
    }
    
    var gpsPermissionsWasAsked: Bool { UserDefaults.standard.bool(forKey: "AskedForGpsPermissions") }
    
    func checkGpsPermissions(for action: @escaping (() -> Void)) {
        gpsPermissionsWasAsked ? action() : askGpsPermissionForFirstTime(action)
    }
    
    func askGpsPermissionForFirstTime(_ completion: @escaping (() -> Void)) {
        let request = {
            UserDefaults.standard.setValue(true, forKey: "AskedForGpsPermissions")
            completion()
        }
        askForGpsPermissions(onAccept: request)
    }
    
    func requestUserLocation(to performAction: @escaping ((LocationPermission) -> Void)) {
        let askForGps = { self.askForGpsPermissions(onAccept: self.openSettings) }
        let locationPermission = LocationPermission()
        let fireAction = { performAction(locationPermission) }
        locationPermission.requestUserLocation(ActionsForDecision(accept: fireAction, decline: askForGps))
    }
    
    func popToRoot() {
        let _ = dependencies.navigation.popToRootViewController(animated: true)
    }
    
    func askForGpsPermissions(onAccept agreeBlock: @escaping (() -> Void)) {
        let responseToAsk = ActionsForDecision(accept: agreeBlock, decline: popToRoot)
        let presenter = LocationPermissionPresenter()
        let viewResolver = PermissionViewResolver(presenter: presenter)
        let router = PermissionRouter(navigation: dependencies.navigation, factory: viewResolver)
        let coordinator = PermissionCoordinator(flow: router, actions: responseToAsk)
        coordinator.askForPermission()
    }
    
    func openSettings() {
        guard let appSettings = URL(string: UIApplication.openSettingsURLString) else { return }
        UIApplication.shared.open(appSettings, options: [:]) { _ in }
    }
    
    func showGame(_ game: Game, with locationPermission: LocationPermission) {
        let viewResolver = GamePlayViewResolver()
        let router = GamePlayRouter(navigation: dependencies.navigation, factory: viewResolver)
        let goal = LocationCoordinate2D(latitude: game.latitude, longitude: game.longitude)
        let gamePlay = GamePlay(goal, zoneRadius: 15) { $0.distance(from: $1) }
        
        let coordinator = GamePlayCoordinator(flow: router, caseUse: gamePlay)
        coordinator.start()
    }

    func sentToJoinAGame() {
        let viewResolver = JoinGameViewResolver()
        let router = JoinGameRouter(navigation: dependencies.navigation, factory: viewResolver)
        //let service = JoinGameRestService(networking: dependencies.networking)
        let service = JoinGameMockService(game: .mainGame)
        let joinGame = JoinGame(service: service)
        let coordinator = JoinGameCoordinator(flow: router, caseUse: joinGame, onComplete: sendToActiveGame)
        coordinator.start()
    }
}

fileprivate extension Module {
    private typealias Method = (RegisterMethod, SingleAction<Void>)

    @available(*, unavailable)
    func showGuestFlow() {
        #warning("We need to wire methods!")
        #warning("Maybe Case use/Coordinator was necesary to see what methods are available to authenticate")
        let googleAuth: Method = (.google, { _ in })
        let facebookAuth: Method = (.facebook, { _ in })
        let emailAuth: Method = (.email, { _ in self.startRegistryWithEmail() })
        let methods: [Method] = [googleAuth, facebookAuth, emailAuth]
        let presenter = RegisterGuestPresenter(methods: methods)
        let viewResolver = iOSGuestFactory(presenter: presenter)
        let router = GuestRouter(navigation: dependencies.navigation, factory: viewResolver)
        router.showGuestScreen()
    }
}
