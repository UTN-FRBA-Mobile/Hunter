import Foundation
import UIKit

class TextFieldHandler {
    private let callback: ((String) -> Void)
    init(callback: @escaping ((String) -> Void)) { self.callback = callback }
    @objc func onDidChange(_ textField: UITextField) { callback(textField.text ?? "") }
}

extension UITextField {
    @discardableResult
    func addHandler(_ handler: TextFieldHandler) -> TextFieldHandler {
        addTarget(handler, action: #selector(handler.onDidChange(_:)), for: .editingChanged)
        return handler
    }
}
