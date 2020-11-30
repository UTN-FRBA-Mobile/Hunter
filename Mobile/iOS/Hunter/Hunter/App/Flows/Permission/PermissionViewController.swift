import UIKit

class PermissionViewController: UIViewController {

    // MARK: - Attributes
    
    @IBOutlet private (set) weak var containerView: UIView!
    @IBOutlet private (set) weak var titleLabel: UILabel!
    @IBOutlet private (set) weak var subtitleLabel: UILabel!
    @IBOutlet private (set) weak var leftButton: HunterButton!
    @IBOutlet private (set) weak var rightButton: HunterButton!
    
    // MARK: - ViewController Function

    override func viewDidLoad() {
        super.viewDidLoad()
        
        titleLabel.numberOfLines = 0
        titleLabel.textAlignment = .center
        subtitleLabel.numberOfLines = 0
        leftButton.applyBorders()
        rightButton.applyBorders()
    }
    
    // MARK: - Public Functions

    func setup(_ actions: ActionsForDecision) {
        leftButton.isEnabled = true
        leftButton.setup(forTap: dismiss(andPerform: actions.decline))
        leftButton.setTitleColor(Color.Hunter.black, for: .normal)
        leftButton.backgroundColor = Color.Hunter.white
        leftButton.layer.borderColor = Color.Hunter.lightGrey.cgColor
        rightButton.isEnabled = true
        rightButton.setup(forTap: dismiss(andPerform: actions.accept))
    }
    
    // MARK: - Private Functions
    
    private func dismiss(andPerform block: @escaping (()->Void)) -> (() -> Void) {
        { [weak self] in self?.dismiss(animated: true, completion: block) }
    }
    
}
