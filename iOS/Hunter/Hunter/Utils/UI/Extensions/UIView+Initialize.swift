//  Created by Hunter on 23/09/2020.


import Foundation
import UIKit

extension UIView {
    static func loadFromCode<View: UIView>(_ customizing: ((View) -> Void) = { _ in }) -> View {
        let view = View()
        view.translatesAutoresizingMaskIntoConstraints = false
        customizing(view)
        return view
    }
}
