import UIKit

class AuthenticationViewController: UIViewController {
    private weak var contentStack: UIStackView!
    private weak var actionStack: UIStackView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        setupViews()
    }
    
    func show(content: UIView) { contentStack.addArrangedSubview(content) }
    func show(action: UIView) { actionStack.addArrangedSubview(action) }
}

// MARK: - Create Subviews
fileprivate extension AuthenticationViewController {
    
    private func setupViews() {
        let mainStackView = stackView(axis: .vertical, distribution: .fillEqually, spacing: 32.0)
        mainStackView.asSubview(of: view).toEdges(of: view)
        contentStack = stackView(in: mainStackView)
        actionStack = stackView(in: mainStackView)
    }

    private func stackView(in main: UIStackView) -> UIStackView {
        let containerView = UIView.Clear()
        main.addArrangedSubview(containerView)
        return add(centerFlexible: stackView(axis: .vertical, distribution: .fillEqually),
                   axis: .horizontal,
                   to: containerView)
    }
}
