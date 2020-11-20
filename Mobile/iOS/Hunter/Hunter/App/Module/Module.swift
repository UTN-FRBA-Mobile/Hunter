import Foundation
import UIKit
import FirebaseAuth
import Firebase

public class Module<Nav: UINavigationController, Net: Networking> {
    
    public struct Dependencies<Nav: UINavigationController, Net: Networking> {
        let navigation: Nav
        let networking: Net
    }
    
    private let dependencies: Dependencies<Nav, Net>
    public init(_ dependencies: Dependencies<Nav, Net>) { self.dependencies = dependencies }

    func launch() {
        checkAuthentication()
    }
}

fileprivate extension Module {
    
    func showLoginFlow() {
        let viewResolver = LoginViewResolver()
        let router = LoginRouter(navigation: dependencies.navigation, factory: viewResolver)
        let login = Login(onWasAuthenticated: sendHome)
        let coordinator = LoginCoordinator(flow: router, caseUse: login, onIsANewUser: showGuestFlow)
        coordinator.start()
    }
    
    /// Step One: We check if the user was already authenticated (Have a local token)
    func checkAuthentication() {
        let searchForToken: ((String, @escaping ((Token) -> Void)) -> Void) = { (key, callback) in
            Auth.auth().addStateDidChangeListener { (_, possibleUser) in
                guard let user = possibleUser else { return callback("") }
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
        let createUser = CreateUser(networking: dependencies.networking)
        let signUp = SignUpWithEmail(service: createUser, onWasRegistered: sendHome)
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
        let viewResolver = CreateGameViewResolver()
        let router = CreateGameRouter(navigation: dependencies.navigation, factory: viewResolver)
        let createGame = CreateGame(imageProvider: LocaliOSImageModule())
        let coordinator =  CreateGameCoordinator(flow: router, caseUse: createGame, onCreated: sendToActiveGame)
        coordinator.start()
    }
    
    func sendToActiveGame() {
        let popToRoot = { let _ = self.dependencies.navigation.popToRootViewController(animated: true) }
        let locationPermission = LocationPermission()
        let requestUserLocation = {
            let startGame = { self.showGame(with: locationPermission) }
            locationPermission.requestUserLocation(ActionsForDecision(accept: startGame, decline: popToRoot))
        }
        let responseToAsk = ActionsForDecision(accept: requestUserLocation, decline: popToRoot)
        let presenter = LocationPermissionPresenter()
        let viewResolver = PermissionViewResolver(presenter: presenter)
        let router = PermissionRouter(navigation: dependencies.navigation, factory: viewResolver)
        let coordinator = PermissionCoordinator(flow: router, actions: responseToAsk)
        coordinator.askForPermission()
    }
    
    func showGame(with locationPermission: LocationPermission) {
        let viewResolver = GamePlayViewResolver()
        let router = GamePlayRouter(navigation: dependencies.navigation, factory: viewResolver)
        let gamePlay = GamePlay()
        let coordinator = GamePlayCoordinator(flow: router, caseUse: gamePlay)
        coordinator.start()
    }

    func sentToJoinAGame() {
        let viewResolver = JoinGameViewResolver()
        let router = JoinGameRouter(navigation: dependencies.navigation, factory: viewResolver)
        let joinGame = JoinGame()
        let coordinator = JoinGameCoordinator(flow: router, caseUse: joinGame, onComplete: sendToActiveGame)
        coordinator.start()
    }
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

fileprivate extension Module {
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
}

import MapKit
import CoreLocation
class LocationPermission: NSObject {
    enum State {
        case shouldAsk
        case granted
        case denied
    }
    
    let manager = CLLocationManager()
    
    var state: LocationPermission.State { parseTo(manager.authorizationStatus) }
    
    private func parseTo(_ authorizationStatus: CLAuthorizationStatus) -> LocationPermission.State {
        switch authorizationStatus {
        case .authorizedAlways, .authorizedWhenInUse: return .granted
        case .denied, .restricted: return .denied
        case .notDetermined: return .shouldAsk
        default: return .denied
        }
    }
    
    enum Status: Equatable {
        case defined
        case requesting(ActionsForDecision)
        static func ==(_ lhs: Status,_ rhs: Status) -> Bool {
            switch (lhs,rhs) {
            case (.defined, .defined): return true
            case (.requesting(_), .requesting(_)): return true
            default: return false
            }
        }
    }
    
    private var status: Status = .defined
    func requestUserLocation(_ actions: ActionsForDecision) {
        guard status == .defined else { return }
        status = .requesting(actions)
        manager.delegate = self
        manager.requestWhenInUseAuthorization()
    }
}
extension LocationPermission: CLLocationManagerDelegate {
    func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
        switch status {
        case .requesting(let actions):
            switch parseTo(manager.authorizationStatus) {
            case .granted: actions.accept()
            case .denied: actions.decline()
            default: break
            }
        default:
            #warning("Maybe we need to post a notification in order to see if we are going to ask the user to turn on the gps!")
            break
        }
    }
}


struct LocationPermissionPresenter: PermissionPresenter {
    
}
