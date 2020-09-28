//  Created by Hunter on 23/09/2020.


import UIKit

class LoginViewController: UIViewController {
    
    weak var userTextField: UITextField!
    weak var passwordTextField: UITextField!
    weak var acceptButton: UIButton!

    var onAceppt: (() -> Void) = { }

    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        setupView()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        refreshStatus()
    }
    
    func refreshStatus() {
        let shouldBeEnabled = userTextField.hasText && passwordTextField.text?.count ?? 0 > 4
        acceptButton.isEnabled = shouldBeEnabled
    }
    
    @objc private func onAcceptButtonTap() { onAceppt() }
}

fileprivate extension LoginViewController {
    
    func setupView() {
        setupContainerView()
        setupButtonView()
    }
    
    func setupContainerView() {
        let containerView: UIStackView = UIView.loadFromCode { (stack: UIStackView) in
            stack.backgroundColor = .clear
            stack.axis = .vertical
            stack.spacing = 16.0
        }
        let userField: UITextField = UIView.loadFromCode()
        let passwordField: UITextField = UIView.loadFromCode() { (field: UITextField) in
            field.isSecureTextEntry = true
        }
        containerView.addArrangedSubviews([userField, passwordField])
        view.addSubview(containerView)
        userTextField = userField
        passwordTextField = passwordField
        containerView.center(to: view)
        containerView.leftAnchor.constraint(equalTo: view.leftAnchor, constant: 32.0).activate()
    }
    
    func setupButtonView() {
        acceptButton = UIView.loadFromCode() { (button: HunterButton) in
            button.applyBorders()
            button.addTarget(self, action: #selector(onAcceptButtonTap), for: .touchUpInside)
            view.addSubview(button)
            [button.leftAnchor.constraint(equalTo: view.leftAnchor, constant: 32),
             view.rightAnchor.constraint(equalTo: button.rightAnchor, constant: 32),
             view.bottomAnchor.constraint(equalTo: button.bottomAnchor, constant: 32)]
                .activate()
        }
    }
}
