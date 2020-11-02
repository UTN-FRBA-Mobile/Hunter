import UIKit

class HunterTextField: UITextField {
    
    var validationBlock: ((String?) throws -> Void) = { _ in }
        
    required init() {
        super.init(frame: .zero)
        setupUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupUI()
    }
    
    private func setupUI() {
        layer.borderColor = Color.Hunter.green.cgColor
    }
    
    func applyBorders() {
        layer.borderWidth = 2.0
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        layer.cornerRadius = 4
    }
    
    func validate() throws -> Void { try validationBlock(text) }
}
