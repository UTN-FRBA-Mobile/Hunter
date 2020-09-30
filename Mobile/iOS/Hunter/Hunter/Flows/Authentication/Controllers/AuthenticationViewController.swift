//  Created by Hunter on 23/09/2020.

import UIKit

class AuthenticationViewController: UIViewController {
    private (set) weak var contentStack: UIStackView!
    private (set) weak var actionStack: UIStackView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        setupView()
    }
    
    private func setupView() {
        let mainStackView = stackView(axis: .vertical, distribution: .fillEqually)
        mainStackView.asSubview(of: view).toEdges(of: view)
        let textsContainerView = containerView
        let buttonsContainerView = containerView
        mainStackView.addArrangedSubviews([textsContainerView, buttonsContainerView])
        contentStack = stackCenterWithFlexible(with: textsContainerView)
        actionStack = stackCenterWithFlexible(with: buttonsContainerView)
    }
    
    private var containerView: UIView { UIView.loadFromCode { $0.backgroundColor = UIColor.clear } }
    
    private func stackCenterWithFlexible(with container: UIView) -> UIStackView {
        let stack = stackView(axis: .vertical, distribution: .fillEqually)
        stack.asSubview(of: container).center(to: container)
        stack.leftAnchor.constraint(greaterThanOrEqualTo: container.leftAnchor).activate()
        return stack
    }
        
    private func stackView(axis: NSLayoutConstraint.Axis, distribution: UIStackView.Distribution) -> UIStackView {
        UIView.loadFromCode { (stack: UIStackView) in
            stack.backgroundColor = .clear
            stack.axis = axis
            stack.spacing = 16.0
            stack.distribution = distribution
        }
    }
}
