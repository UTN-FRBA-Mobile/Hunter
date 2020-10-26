import Foundation
import UIKit

protocol GuestPresenter {
    func actionsButton() -> [UIButton]
}

struct RegisterGuestPresenter: GuestPresenter {
    let methods: [(RegisterMethod, SingleAction<Void>)]
    
    func actionsButton() -> [UIButton] {
        methods.map { button(with: $0.0.rawValue.capitalized, action: $0.1) }
    }
    
    private func button(with text: String, action: @escaping SingleAction<Void>) -> UIButton {
        UIView.loadFromCode() { (button: HunterButton) in
            button.setTitle(text, for: .normal)
            button.setup { action(Void()) }
            button.isEnabled = true
            button.applyBorders()
        }
    }
}
