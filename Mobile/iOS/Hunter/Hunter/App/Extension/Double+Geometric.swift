import Foundation

extension Double {
    
    // MARK: - Computed Vars
    
    var toRadians: Double { self * .pi / 180.0 }
    
    var toDegrees: Double { self * 180.0 / .pi }
    
    // MARK: - Functions
    
    func divisor(_ places: Double) -> Double { pow(10.0, places) }

}
