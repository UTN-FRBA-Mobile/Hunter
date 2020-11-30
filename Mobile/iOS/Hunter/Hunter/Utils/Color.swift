import Foundation
import UIKit

struct Color {
    
    private init() {}
    
}
extension Color {
    struct Hunter {
        
        private init() {}
        
        // MARK: - Colors
        
        static var black: UIColor { UIColor(with: "#44444") }
        static var darkBlue: UIColor { UIColor(with: "#1E2C35") }
        static var green: UIColor { UIColor(with: "#538b2a") }
        static var yellow: UIColor { UIColor(with: "#E8DF97") }
        static var white: UIColor { UIColor(with: "#FEFEFE") }
        static var grey: UIColor { UIColor(with: "#69777D") }
        static var lightGrey: UIColor { UIColor(with: "#E4E4E4") }
    }
}
