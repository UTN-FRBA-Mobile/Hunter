import Foundation
import UIKit

struct HomePresenter {
    func createGameButton() -> HunterButton { button(with: "Crear juego!") }
    func joinGameButton() -> HunterButton { button(with: "Unirse a un juego!") }
    
    private func button(with title: String) -> HunterButton {
        UIView.loadFromCode { (btn: HunterButton) in
            btn.setTitle(title, for: .normal)
            btn.applyBorders()
            btn.isEnabled = true
        }
    }
}
