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
    
    private var allCompleted: Bool = false
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }
    
    private func performAValidation() {
        do {
            try validate()
            loginButton.isEnabled = true
        } catch {
            loginButton.isEnabled = false
            #warning("We need to show validation!")
            if allCompleted {
                return
            }
        }
    }
}

extension LoginViewController: UITextFieldDelegate {

    func textFieldDidEndEditing(_ textField: UITextField) { performAValidation() }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        if let nextTextField = nextFree() {
            nextTextField.becomeFirstResponder()
            allCompleted = false
        } else {
            allCompleted = true
        }

        return false
    }
}
