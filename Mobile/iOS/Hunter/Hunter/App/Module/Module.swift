import Foundation
import UIKit

public class Module<Nav: UINavigationController, Net: Networking> {
    
    public struct Dependencies<Nav: UINavigationController, Net: Networking> {
        let navigation: Nav
        let networking: Net
    }
    
    private let dependencies: Dependencies<Nav, Net>
    init(_ dependencies: Dependencies<Nav, Net>) { self.dependencies = dependencies }

    func launch() {
        checkAuthentication()
        #warning("We need to change the Waiting Controller")
        let waitingController = UIViewController()
        waitingController.view.backgroundColor = UIColor.green
        dependencies.navigation.pushViewController(waitingController, animated: true)
    }
    
    func checkAuthentication() {
        let authentication = LocalAuthenticationRepository(key: "userToken",
                                                           save: UserDefaults.standard.set,
                                                           load: UserDefaults.standard.string)
        Launch(service: authentication,
               handleNonExistentUser: showGuestFlow)
            .checkStatus(onHaveLocalToken: startAuthenticate)
    }
    
    func showGuestFlow() {
        
    }
    
    func startAuthenticate(with token: Token) {
    }
}
