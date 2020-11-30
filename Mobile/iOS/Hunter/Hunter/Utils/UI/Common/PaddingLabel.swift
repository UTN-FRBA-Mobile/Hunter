import Foundation
import UIKit

class PaddingLabel: UILabel {
    
    var contentInsets: UIEdgeInsets = .zero

    override func drawText(in rect: CGRect) {
        super.drawText(in: rect.inset(by: contentInsets))
    }

    override var intrinsicContentSize: CGSize {
        super.intrinsicContentSize.adding(contentInsets.sizeSum)
    }

    override var bounds: CGRect {
        didSet {
            preferredMaxLayoutWidth = bounds.width - (contentInsets.widthSum)
        }
    }
}
