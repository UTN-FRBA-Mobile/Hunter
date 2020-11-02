import Foundation
import UIKit

class HomeRouter<Nav: UINavigationController,
                 Factory: HomeFactory>: HomeFlow, Router {
    let navigation: Nav
    let factory: Factory
    
    init(navigation: Nav, factory: Factory) {
        self.navigation = navigation
        self.factory = factory
    }
    
    func sendHome<Cu: HomeCaseUse>(with caseUse: Cu) {
        replaceAll(with: factory.homeScreen(with: caseUse))
    }
}
