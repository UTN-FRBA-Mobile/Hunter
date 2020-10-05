//  Created by Hunter on 23/09/2020.

import UIKit

class HunterButton: UIButton {
    
    override var isEnabled: Bool {
        set {
            super.isEnabled = newValue
            newValue ? refreshEnabledStyle() : refreshDisabledStyle()
        }
        get { super.isEnabled }
    }
    
    required init() {
        super.init(frame: .zero)
        setupUI()
    }
    
    required init?(coder: NSCoder) { super.init(coder: coder) }
    
    private func setupUI() { heightAnchor.constraint(equalToConstant: 44).activate() }
    
    func applyBorders() { layer.borderWidth = 2.0 }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        layer.cornerRadius = 16.0
    }
    
    private func refreshEnabledStyle() {
        setTitleColor(Color.Hunter.white, for: .normal)
        backgroundColor = Color.Hunter.green
        layer.borderColor = Color.Hunter.yellow.cgColor
    }
    
    private func refreshDisabledStyle() {
        setTitleColor(Color.Hunter.grey, for: .normal)
        backgroundColor = Color.Hunter.lightGrey
        layer.borderColor = Color.Hunter.darkBlue.cgColor
    }
}
