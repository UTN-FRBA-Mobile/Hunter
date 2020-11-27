import UIKit

class HunterTextField: UITextField {
    
    var validationBlock: ((String?) throws -> Void) = { _ in }
    let padding = UIEdgeInsets(top: 2, left: 4, bottom: 2, right: 4)
        
    required init() {
        super.init(frame: .zero)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupUI()
    }
    
    func setupUI() {
        layer.borderColor = Color.Hunter.green.cgColor
    }
    
    func applyBorders() {
        layer.borderWidth = 2.0
    }
    
    override open func textRect(forBounds bounds: CGRect) -> CGRect { bounds.inset(by: padding) }

    override open func placeholderRect(forBounds bounds: CGRect) -> CGRect { bounds.inset(by: padding) }

    override open func editingRect(forBounds bounds: CGRect) -> CGRect { bounds.inset(by: padding) }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        layer.cornerRadius = 4
    }
    
    func validate() throws -> Void { try validationBlock(text) }
}
