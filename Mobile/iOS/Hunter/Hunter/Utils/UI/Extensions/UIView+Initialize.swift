import Foundation
import UIKit

extension UIView {
    static func loadFromCode<View: UIView>(_ customizing: ((View) -> Void) = { _ in }) -> View {
        let view = View()
        view.translatesAutoresizingMaskIntoConstraints = false
        customizing(view)
        return view
    }
    
    static func Clear<View: UIView>() -> View { loadFromCode { $0.backgroundColor = .clear } }
}
