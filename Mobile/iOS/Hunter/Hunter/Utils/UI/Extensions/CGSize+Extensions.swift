import Foundation
import UIKit

extension CGSize {
    func adding(_ size: CGSize) -> CGSize {
        CGSize(width: width + size.width, height: height + size.height)
    }
}
