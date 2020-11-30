import Foundation
import UIKit

protocol Loading {
    func startLoading() -> Self
    func stopLoading(shouldBeRemove value: Bool, _ onComplete: @escaping (() -> Void))
    func removeLoading()
}

class HunterLoading: UIView {
    
    private (set) weak var spinner: UIActivityIndicatorView!
    private (set) weak var descriptionLabel: PaddingLabel!
    private let defaultText: String = "Cargando..."
    
    init() {
        
        let spinner = UIActivityIndicatorView(style: .large)
        spinner.translatesAutoresizingMaskIntoConstraints = false
        spinner.tintColor = Color.Hunter.green
        spinner.color = Color.Hunter.green
        
        let label = PaddingLabel()
        label.contentInsets = .init(top: 4, left: 8, bottom: 4, right: 8)
        label.isHidden = true
        label.translatesAutoresizingMaskIntoConstraints = false
        
        let container: UIView = UIView.loadFromCode { $0.backgroundColor = .clear }

        self.spinner = spinner
        self.descriptionLabel = label
        
        container.addSubview(spinner)
        container.addSubview(label)
        
        super.init(frame: .zero)
        self.translatesAutoresizingMaskIntoConstraints = false
        
        label.text = defaultText
        label.font = .systemFont(ofSize: 24, weight: .semibold)
        label.clipsToBounds = true

        addInside(centerFlexible: container, axis: [.horizontal, .vertical])
        container.flexible(for: .vertical, with: spinner)
        container.centerX(to: spinner).activate()
        
        [
            container.topAnchor.constraint(equalTo: spinner.topAnchor),
            label.topAnchor.constraint(equalTo: spinner.bottomAnchor, constant: 40),
            container.leadingAnchor.constraint(equalTo: label.leadingAnchor),
            container.trailingAnchor.constraint(equalTo: label.trailingAnchor),
            container.bottomAnchor.constraint(equalTo: label.bottomAnchor)
        ].activate()
    }
    
    required init?(coder: NSCoder) { super.init(coder: coder) }

    // MARK: - Public Functions
    
    @discardableResult
    func fullScreen(of superview: UIView, colored: UIColor = .black, withText text: String? = nil) -> Self {
        asSubview(of: superview).toEdges(of: superview)
        descriptionLabel.text = text ?? defaultText
        startLoading()
        
        return self
    }
    
    @discardableResult
    func centered(in superview: UIView, withText text: String? = nil) -> Self {
        addInside(centerFlexible: superview, axis: [.vertical, .horizontal])
        descriptionLabel.text = text ?? defaultText
        startLoading()
        
        return self
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        
        descriptionLabel.layer.cornerRadius = descriptionLabel.frame.height / 4
    }
}

extension HunterLoading: Loading {
    
    @discardableResult
    func startLoading() -> Self {
        descriptionLabel.isHidden = false
        UIView.animate(withDuration: 0.3) {
            self.spinner.transform = .init(scaleX: 2, y: 2)
            self.descriptionLabel.backgroundColor = .white
            self.descriptionLabel.textColor = .black
        }
        spinner.startAnimating()
        
        return self
    }
    
    func stopLoading(shouldBeRemove value: Bool = true, _ onComplete: @escaping (() -> Void) = {}) {
        descriptionLabel.isHidden = true
        
        UIView.animate(withDuration: 0.4, delay: 0, usingSpringWithDamping: 0.3,
                       initialSpringVelocity: 0.3,
                       options: .curveEaseIn) { [weak self] in
            self?.spinner.transform = .identity
        } completion: { [weak self] finished in
            guard finished else { return }
            onComplete()
            self?.spinner.stopAnimating()
            value ? self?.removeLoading() : nil
        }
    }
    
    func removeLoading() {
        removeFromSuperview()
    }
    
}
