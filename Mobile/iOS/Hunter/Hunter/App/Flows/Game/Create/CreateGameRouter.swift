import Foundation
import UIKit

class CreateGameRouter<Nav: UINavigationController,
                       Factory: CreateGameFactory>: CreateGameFlow, Router {
    let navigation: Nav
    let factory: Factory
    
    init(navigation: Nav, factory: Factory) {
        self.navigation = navigation
        self.factory = factory
    }
    
    func sendToCreateGame<Cu: CreateGameCaseUse>(with caseUse: Cu,
                                                 onComplete: @escaping ((Game) -> Void)) {
        show(factory.createGameScreen(with: caseUse, onCreated: onComplete))
    }
}
