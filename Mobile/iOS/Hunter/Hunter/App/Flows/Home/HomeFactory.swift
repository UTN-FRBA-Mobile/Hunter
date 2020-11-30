import Foundation
import UIKit

protocol HomeFactory {
    func homeScreen<Cu: HomeCaseUse>(with caseUse: Cu) -> UIViewController
}

class HomeViewResolver: HomeFactory {

    func homeScreen<Cu: HomeCaseUse>(with caseUse: Cu) -> UIViewController {
        let controller = HomeViewController()
        let _ = controller.view
        let presenter = HomePresenter()
        let createGameBtn = presenter.createGameButton()
        createGameBtn.setup(forTap: caseUse.newGameFlow)
        let joinGameBtn = presenter.joinGameButton()
        joinGameBtn.setup(forTap: caseUse.joinGameFlow)
        controller.mainStack.addArrangedSubview(createGameBtn)
        controller.mainStack.addArrangedSubview(joinGameBtn)
        return controller
    }
}
