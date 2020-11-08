import Foundation
import UIKit

class GamePlayRouter<Nav: UINavigationController,
                     Factory: GamePlayFactory>: GamePlayFlow, Router {
    let navigation: Nav
    let factory: Factory
    
    init(navigation: Nav, factory: Factory) {
        self.navigation = navigation
        self.factory = factory
    }
    
    func showMap<Cu: GamePlayCaseUse>(with caseUse: Cu,
                                      onUserLocationDidChanged: ((LocationCoordinate2D) -> Void)) {
        show(factory.mapScreen(with: caseUse, onUserLocationDidChanged: onUserLocationDidChanged))
    }
    
    #warning("Not implemented!")
    func showCloseToGoal<Cu: GamePlayCaseUse>(with caseUse: Cu) {
        print("Not implemented yet!")
    }
}
