import UIKit

class LocalSignUpViewController: UIViewController {
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var inputStack: UIStackView!
    @IBOutlet weak var signUpButton: HunterButton!
    
    private var inputs: [UITextField] { inputStack.subviews.compactMap { $0 as? UITextField } }
    
    var validate: (() throws -> Void) = { }
    var nextFree: (() -> UITextField?) = { return nil }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        signUpButton.applyBorders()
    }
}

extension LocalSignUpViewController: UITextFieldDelegate {
    func textFieldDidEndEditing(_ textField: UITextField) {
        do {
            try validate()
            signUpButton.isEnabled = true
        } catch {
            signUpButton.isEnabled = false
            print("We need to show validation!")
        }
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        nextFree()?.becomeFirstResponder()
        return false
    }
}
