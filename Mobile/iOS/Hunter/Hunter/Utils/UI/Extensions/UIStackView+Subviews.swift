//  Created by Hunter on 23/09/2020.

import Foundation
import UIKit

extension UIStackView {
    func addArrangedSubviews(_ subviews: [UIView]) { subviews.forEach { addArrangedSubview($0) } }
}
