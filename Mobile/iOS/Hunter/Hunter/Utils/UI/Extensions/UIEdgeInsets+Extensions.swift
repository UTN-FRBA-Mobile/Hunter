import Foundation
import UIKit

extension UIEdgeInsets {
    
    var widthSum: CGFloat { left + right }
    
    var heightSum: CGFloat { top + bottom }
    
    var sizeSum: CGSize { CGSize(width: widthSum, height: heightSum) }
    
}
