import UIKit

class LocalSignUpViewController: UIViewController {
    @IBOutlet weak var titleLabel: UILabel!
    @IBOutlet weak var inputStack: UIStackView!
    @IBOutlet weak var signUpButton: HunterButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        signUpButton.applyBorders()
    }
}
