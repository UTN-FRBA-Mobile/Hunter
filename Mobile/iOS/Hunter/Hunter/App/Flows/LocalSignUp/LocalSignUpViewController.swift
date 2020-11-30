import UIKit

class LocalSignUpViewController: UIViewController {
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var inputStack: UIStackView!
    @IBOutlet weak var signUpButton: HunterButton!
    
    private var inputs: [UITextField] { inputStack.subviews.compactMap { $0 as? UITextField } }
    
    var validate: (() throws -> Void) = { }
    var nextFree: (() -> UITextField?) = { return nil }
    private var allCompleted: Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        signUpButton.applyBorders()
        titleLabel.text = "WELCOME_TO_HUNTER".localize()
        signUpButton.setTitle("REGISTER_BUTTON_SIGNUP".localize(), for: .normal)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(false, animated: animated)
    }
    
    private func performAValidation() {
        do {
            try validate()
            signUpButton.isEnabled = true
        } catch {
            signUpButton.isEnabled = false
            #warning("Missing Show Validation")
            if allCompleted {
                return
            }
        }
    }
}

extension LocalSignUpViewController: UITextFieldDelegate {
    
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
