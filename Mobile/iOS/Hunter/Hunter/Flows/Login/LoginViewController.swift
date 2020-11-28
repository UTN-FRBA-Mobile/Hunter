import UIKit

class LoginViewController: UIViewController {
    
    @IBOutlet weak var inputStack: UIStackView!
    @IBOutlet weak var actionsStack: UIStackView!
    weak var userTextField: UITextField!
    weak var passwordTextField: UITextField!
    weak var loginButton: UIButton!
    
    var inputs: [UITextField] { inputStack.subviews.compactMap { $0 as? UITextField } }
    
    var validate: (() throws -> Void) = { }
    var nextFree: (() -> UITextField?) = { return nil }
    
    private func performAValidation() {
        do {
            try validate()
            loginButton.isEnabled = true
        } catch {
            loginButton.isEnabled = false
            print("We need to show validation!")
        }
    }
}

extension LoginViewController: UITextFieldDelegate {
    func textFieldDidEndEditing(_ textField: UITextField) { performAValidation() }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        nextFree()?.becomeFirstResponder()
        return false
    }
}
