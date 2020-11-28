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
    
    private func displayError() {
        showToast(with: "El código debe tener por lo menos 4 caracteres!", on: .top)
    }
    
    private func perfomValidation(_ onError: @escaping (() -> Void) = {}) {
        do {
            try validate()
            joinButton.isEnabled = true
        } catch {
            joinButton.isEnabled = false
            onError()
        }
    }
}

extension JoinGameViewController: UITextFieldDelegate {
    
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        perfomValidation()
        
        return true
    }
    
    func textFieldDidEndEditing(_ textField: UITextField) { perfomValidation(displayError) }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool { true }
}
