import Foundation
import UIKit

protocol LoginFactory {
    func loginScreen<Cu: LoginCaseUse>(with caseuse: Cu,
                                       startGuestFlow: @escaping (() -> Void)) -> UIViewController
}

class LoginViewResolver: LoginFactory {
    func loginScreen<Cu: LoginCaseUse>(with caseuse: Cu,
                                       startGuestFlow: @escaping (() -> Void))
    -> UIViewController {
        let controller = LoginViewController()
        _ = controller.view
        let presenter = LoginPresenter()
        let fields = presenter.createInputs()
        controller.inputStack.addArrangedSubviews(fields)
        fields.forEach {
            $0.applyBorders()
            $0.delegate = controller
        }
        let loginButton: UIButton = UIView.loadFromCode { (btn: HunterButton) in
            btn.setTitle("Login", for: .normal)
            btn.isEnabled = false
            btn.setup {
                controller.view.backgroundColor = Color.Hunter.darkBlue
                let email = controller.inputs.first?.text ?? ""
                let password = controller.inputs.last?.text ?? ""
                caseuse.authenticate(with: email, and: password) { result in
                    controller.view.backgroundColor = Color.Hunter.white
                    switch result {
                    case .success(_):
                        break
                    case .failure(let error):
                        print("Error: \(error)")
                    }
                }
            }
        }
        controller.validate = { try fields.forEach { try $0.validate() }; loginButton.isEnabled = true }
        controller.nextFree = {
            fields.first {
                do { try $0.validate(); return false }
                catch { loginButton.isEnabled = false; return true}
            }
        }
        let signUpButton: UIButton = UIView.loadFromCode { (btn: HunterButton) in
            btn.setTitle("Sign Up!", for: .normal)
            btn.isEnabled = true
            btn.setup(forTap: startGuestFlow)
        }
        controller.actionsStack.addArrangedSubview(loginButton)
        controller.loginButton = loginButton
        controller.actionsStack.addArrangedSubview(signUpButton)
        return controller
    }
}
