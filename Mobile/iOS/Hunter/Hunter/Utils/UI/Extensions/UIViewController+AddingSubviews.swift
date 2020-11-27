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


extension UIView {
    
    @discardableResult
    func addInside<View: UIView>(centerFlexible aView: View,
                                 axis: [NSLayoutConstraint.Axis]) -> View {
        let view = aView.asSubview(of: self)
        axis.forEach { view.flexible(for: $0, with: self) }
        view.center(to: self)
        return aView
    }
    
}


extension UIViewController {
    
    enum ToastPosition {
        case top
        case bottom
    }
    
    func showToast(with text: String, on position: ToastPosition = .bottom, for duration: Double = 2) {
        
        let label = PaddingLabel()
        label.clipsToBounds = true
        label.translatesAutoresizingMaskIntoConstraints = false
        label.contentInsets = .init(top: 8, left: 16, bottom: 8, right: 16)
        label.textColor = .white
        label.numberOfLines = 0
        label.backgroundColor = Color.Hunter.green
        label.layer.cornerRadius = 16
        label.font = .systemFont(ofSize: 18.0, weight: .medium)
        label.text = text
        
        label.asSubview(of: view)
        [label.leftAnchor.constraint(equalTo: view.leftAnchor, constant: 16),
         view.rightAnchor.constraint(equalTo: label.rightAnchor, constant: 16)
        ].activate()
        
        var initialTransformation: CGAffineTransform
        switch position {
        case .top:
            label.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 32).activate()
            initialTransformation = .init(translationX: 0, y: -500)
        case .bottom:
            view.bottomAnchor.constraint(equalTo: label.bottomAnchor, constant: 32).activate()
            initialTransformation = .init(translationX: 0, y: 500)
        }
        
        let inOutTransformation = { label.transform = initialTransformation }
        inOutTransformation()
        
        toastAnimation({ label.transform = .identity }) { [weak self] in
            DispatchQueue.main.asyncAfter(deadline: .now()+duration) { [weak self] in
                self?.toastAnimation( inOutTransformation,
                                     onComplete: label.removeFromSuperview)
            }
        }

    }
    
    private func toastAnimation(_ block: @escaping (() -> Void), onComplete: @escaping (() -> Void)) {
        UIView.animate(withDuration: 0.3, delay: 0,
                       usingSpringWithDamping: 0, initialSpringVelocity: 0,
                       options: .curveLinear, animations: block)  { finished in
            guard finished else { return }
            onComplete()
        }
    }
}
