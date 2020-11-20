import UIKit

class PermissionViewController: UIViewController {

    @IBOutlet private (set) weak var containerView: UIView!
    @IBOutlet private (set) weak var titleLabel: UILabel!
    @IBOutlet private (set) weak var subtitleLabel: UILabel!
    @IBOutlet private weak var leftButton: HunterButton!
    @IBOutlet private weak var rightButton: HunterButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        containerView.backgroundColor = Color.Hunter.white
        containerView.layer.cornerRadius = containerView.bounds.size.height / 0.1
        containerView.layer.borderWidth = 1.0
        containerView.layer.borderColor = Color.Hunter.darkBlue.cgColor
        leftButton.applyBorders()
        rightButton.applyBorders()
    }
    
    func setup(_ actions: ActionsForDecision) {
        leftButton.isEnabled = true
        leftButton.setup(forTap: dismiss(andPerform: actions.decline))
        leftButton.setTitleColor(Color.Hunter.black, for: .normal)
        leftButton.backgroundColor = Color.Hunter.white
        leftButton.layer.borderColor = Color.Hunter.lightGrey.cgColor
        rightButton.isEnabled = true
        rightButton.setup(forTap: dismiss(andPerform: actions.accept))
    }
    
    private func dismiss(andPerform block: @escaping (()->Void)) -> (() -> Void) {
        { [weak self] in self?.dismiss(animated: true, completion: block) }
    }
}
