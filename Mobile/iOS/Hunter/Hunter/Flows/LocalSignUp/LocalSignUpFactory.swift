import Foundation
import UIKit

protocol LocalSignUpFactory {
    func LocalSignUpScreen() -> UIViewController
}

class LocalSignUpViewResolver: LocalSignUpFactory {
    func LocalSignUpScreen() -> UIViewController {
        let controller = LocalSignUpViewController()
        let _ = controller.view
        let presenter = RegistryPresenter()
        let fields = presenter.createInputs()
        controller.inputStack.addArrangedSubviews(fields)
        fields.forEach {
            $0.applyBorders()
            $0.delegate = controller
        }
        controller.validate = { try fields.forEach { try $0.validate() } }
        controller.nextFree = {
            return fields.first { do { try $0.validate(); return false } catch { return true } }
        }
        return controller
    }
}
