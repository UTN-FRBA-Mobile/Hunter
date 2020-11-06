import Foundation
import UIKit

extension UIViewController {
    
    func add<View: UIView>(centerFlexible aView: View,
                           axis: NSLayoutConstraint.Axis,
                           to aSuperView: UIView) -> View {
        aView.asSubview(of: aSuperView)
            .flexible(for: axis, with: aSuperView)
            .center(to: aSuperView)
        return aView
    }
    
    func stackView(axis: NSLayoutConstraint.Axis,
                   distribution: UIStackView.Distribution,
                   spacing: CGFloat = 16.0) -> UIStackView {
        UIView.loadFromCode { (stack: UIStackView) in
            stack.backgroundColor = .clear
            stack.axis = axis
            stack.spacing = spacing
            stack.distribution = distribution
        }
    }
}
