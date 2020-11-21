import Foundation
import UIKit

extension Double {
    
    // MARK: - Functions
    
    func divisor(_ places: Double) -> Double { pow(10.0, places) }

}

extension CGFloat {
    // MARK: - Computed Vars
  var toRadians: CGFloat { return self * .pi / 180 }
  var toDegrees: CGFloat { return self * 180 / .pi }
}

extension Double {
  var toRadians: Double { Double(CGFloat(self).toRadians) }
  var toDegress: Double { Double(CGFloat(self).toDegrees) }
}
