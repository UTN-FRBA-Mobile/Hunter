import UIKit

class JoinGameViewController: UIViewController {
    
    @IBOutlet weak var codeTextField: HunterTextField!
    @IBOutlet weak var joinButton: HunterButton!
    
    var validate: (() throws -> Void) = { }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        codeTextField.delegate = self
        joinButton.applyBorders()
        joinButton.isEnabled = false
    }
    
    private func performAValidation() {
        do {
            try validate()
            joinButton.isEnabled = true
        } catch {
            joinButton.isEnabled = false
            print("We need to show validation!")
        }
    }
}

extension JoinGameViewController: UITextFieldDelegate {
    func textFieldDidEndEditing(_ textField: UITextField) { performAValidation() }
}
