import Foundation
import UIKit

protocol JoinGameFactory {
    func JoinGameScreen<Cu: JoinGameCaseUse>(with caseUse: Cu,
                                                 onCreated: @escaping (() -> Void)) -> UIViewController
}

class JoinGameViewResolver: JoinGameFactory {
    
    func JoinGameScreen<Cu: JoinGameCaseUse>(with caseUse: Cu,
                                                 onCreated: @escaping (() -> Void)) -> UIViewController {
        let controller = JoinGameViewController()
        _ = controller.view
        let presenter = JoinGamePresenter()
        let gameCodeField = presenter.gameCode
        controller.codeTextField.isSecureTextEntry = gameCodeField.isSecure
        controller.codeTextField.validationBlock = { try gameCodeField.validation.validate($0 ?? "") }
        controller.validate = { try controller.codeTextField.validate() }
        controller.joinButton.setTitle("Join Game", for: .normal)
        controller.joinButton.setup {
            caseUse.join(with: controller.codeTextField.text ?? "") { result in
                switch result {
                case .success(let game):
                    print("Game \(game)")
                    onCreated()
                case .failure(let error):
                    print("Hunter Error: \(error)")
                }
            }
        }
        return controller
    }
}
