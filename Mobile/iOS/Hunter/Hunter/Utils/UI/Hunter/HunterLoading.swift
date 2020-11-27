import Foundation
import UIKit

protocol Loading {
    func startLoading()
    func stopLoading()
}

class HunterLoading: UIView {
    
    private weak var spinner: UIActivityIndicatorView!
    private weak var descriptionLabel: UILabel!
    
    init() {
        
        let spinner = UIActivityIndicatorView(style: .large)
        spinner.translatesAutoresizingMaskIntoConstraints = false
        let label = UILabel()
        label.text = "Cargando..."
        label.isHidden = true
        label.translatesAutoresizingMaskIntoConstraints = false
        let container: UIView = UIView.loadFromCode { $0.backgroundColor = .clear }
        
        self.spinner = spinner
        self.descriptionLabel = label
        
        container.addSubview(spinner)
        container.addSubview(label)
        
        super.init(frame: .zero)
        self.translatesAutoresizingMaskIntoConstraints = false
        
        addInside(centerFlexible: container, axis: [.horizontal, .vertical])
        container.flexible(for: .vertical, with: spinner)
        container.centerX(to: spinner).activate()
        
        [
            container.topAnchor.constraint(equalTo: spinner.topAnchor),
            label.topAnchor.constraint(equalTo: spinner.bottomAnchor),
            container.leadingAnchor.constraint(equalTo: label.leadingAnchor),
            container.trailingAnchor.constraint(equalTo: label.trailingAnchor),
            container.bottomAnchor.constraint(equalTo: label.bottomAnchor)
        ].activate()
    }
    
    required init?(coder: NSCoder) { super.init(coder: coder) }
}

extension HunterLoading: Loading {
    
    func startLoading() {
        descriptionLabel.isHidden = false
        spinner.startAnimating()
    }
    
    func stopLoading() {
        descriptionLabel.isHidden = true
        spinner.stopAnimating()
    }
    
}
