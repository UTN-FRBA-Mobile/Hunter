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

extension UIEdgeInsets {
    
    var widthSum: CGFloat { left + right }
    
    var heightSum: CGFloat { top + bottom }
    
    var sizeSum: CGSize { CGSize(width: widthSum, height: heightSum) }
    
}

extension CGSize {
    func adding(_ size: CGSize) -> CGSize {
        CGSize(width: width + size.width, height: height + size.height)
    }
}
