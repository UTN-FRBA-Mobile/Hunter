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
                                      onUserLocationDidChanged: @escaping ((LocationCoordinate2D) -> Void)) {
        replaceLeavingHomeAnd(factory.mapScreen(with: caseUse, onUserLocationDidChanged: onUserLocationDidChanged))
    }
    
    func showCloseToGoal<Cu: GamePlayCaseUse>(with caseUse: Cu,
                                              onUserLocationDidChanged: @escaping ((LocationCoordinate2D) -> Void)) {
        present(factory.nearToGoalScreen(with: caseUse, onUserLocationDidChanged: onUserLocationDidChanged))
    }
    
    func sendUserBackToMapView() {
        navigation.topViewController?.dismiss(animated: true)
    }
    
    func sendUserToWinnerScreen() {
        self.replaceLeavingHomeAnd(self.factory.winnerScreen(onComplete: self.popToRoot))
        navigation.topViewController?.dismiss(animated: true)
    }
}
