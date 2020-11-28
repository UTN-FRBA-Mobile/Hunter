import Foundation

extension TextValidator {
    
    static func minimum(of number: Int = 1) -> Self {
        TextValidator(validate: .with([.minimum(of: number)]))
    }
    
    static var defaulted: TextValidator {
        TextValidator(validate: .with([
            .minimum(of: 2)
        ]))
    }
    
    static var secure: TextValidator {
        defaulted
    }
}
