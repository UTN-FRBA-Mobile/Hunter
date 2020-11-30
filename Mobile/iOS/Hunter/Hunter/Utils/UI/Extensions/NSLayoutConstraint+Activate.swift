import Foundation
import UIKit

extension NSLayoutConstraint {
    @discardableResult
    func activate() -> NSLayoutConstraint {
        isActive = true
        return self
    }
}

extension Array where Element == NSLayoutConstraint {
    @discardableResult
    func activate() -> [Element] { map { $0.activate() } }
}
