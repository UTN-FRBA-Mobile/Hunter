import Foundation
import UIKit

class JoinGamePresenter {
    let gameCode = FieldViewModel(placeholder: "JOIN_GAME_PLACEHOLDER_CODE".localize(),
                                  validation: .defaulted)
}
