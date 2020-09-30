//  Created by Hunter on 23/09/2020.

import Foundation
import UIKit

extension UIView {
    @discardableResult func center(to view: UIView) -> [NSLayoutConstraint] {
        [centerXAnchor.constraint(equalTo: view.centerXAnchor),
         centerYAnchor.constraint(equalTo: view.centerYAnchor)]
            .activate()
    }
    
    @discardableResult func toEdges(of view: UIView) -> [NSLayoutConstraint] {
        [leftAnchor.constraint(equalTo: view.leftAnchor),
         rightAnchor.constraint(equalTo: view.rightAnchor),
         topAnchor.constraint(equalTo: view.topAnchor),
         bottomAnchor.constraint(equalTo: view.bottomAnchor)]
            .activate()
    }
    
    @discardableResult func asSubview(of view: UIView) -> UIView {
        view.addSubview(self)
        return self
    }
}
