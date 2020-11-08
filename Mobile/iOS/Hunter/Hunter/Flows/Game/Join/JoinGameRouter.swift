import Foundation
import UIKit

class JoinGameRouter<Nav: UINavigationController,
                     Factory: JoinGameFactory>: JoinGameFlow, Router {
    let navigation: Nav
    let factory: Factory
    
    init(navigation: Nav, factory: Factory) {
        self.navigation = navigation
        self.factory = factory
    }
    
    func sendToJoinGame<Cu>(with caseUse: Cu, onComplete: @escaping (() -> Void)) where Cu : JoinGameCaseUse {
        show(factory.JoinGameScreen(with: caseUse, onCreated: onComplete))
    }
}
