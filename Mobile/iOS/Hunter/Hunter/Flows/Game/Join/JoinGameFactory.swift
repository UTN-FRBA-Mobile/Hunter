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
        return controller
    }
}
