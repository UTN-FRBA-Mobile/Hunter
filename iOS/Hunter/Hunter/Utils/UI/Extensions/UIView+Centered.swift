//  Created by Hunter on 23/09/2020.

import Foundation
import UIKit

extension UIView {
    @discardableResult
    func center(to view: UIView) -> [NSLayoutConstraint] {
        [centerXAnchor.constraint(equalTo: view.centerXAnchor),
         centerYAnchor.constraint(equalTo: view.centerYAnchor)]
            .activate()
    }
}
