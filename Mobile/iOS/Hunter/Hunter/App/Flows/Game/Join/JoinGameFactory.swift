import Foundation
import UIKit

protocol JoinGameFactory {
    func JoinGameScreen<Cu: JoinGameCaseUse>(with caseUse: Cu,
                                             onJoined: @escaping ((Game) -> Void)) -> UIViewController
}

class JoinGameViewResolver: JoinGameFactory {
    
    func JoinGameScreen<Cu: JoinGameCaseUse>(with caseUse: Cu,
                                             onJoined: @escaping ((Game) -> Void)) -> UIViewController {
        let controller = JoinGameViewController()
        _ = controller.view
        let presenter = JoinGamePresenter()
        let gameCodeField = presenter.gameCode
        controller.codeTextField.isSecureTextEntry = gameCodeField.isSecure
        controller.codeTextField.validationBlock = { try gameCodeField.validation.validate($0 ?? "") }
        controller.validate = { try controller.codeTextField.validate() }
        controller.joinButton.setTitle("Join Game", for: .normal)
        controller.joinButton.setup {
            controller.view.endEditing(true)
            let spinner = HunterLoading().fullScreen(of: controller.view).startLoading()
            caseUse.join(with: controller.codeTextField.text ?? "") { result in
                spinner.stopLoading()
                switch result {
                case .success(let game):
                    onJoined(game)
                case .failure(let error):
                    controller.showToast(with: "El código es invalido, prueba con otro!", on: .top)
                }
            }
        }

        return controller
    }
}
