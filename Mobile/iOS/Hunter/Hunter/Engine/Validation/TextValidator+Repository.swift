import Foundation

extension TextValidator {
    static var defaulted: TextValidator {
        TextValidator(validate: .with([
            .minimum(of: 2)
        ]))
    }
    
    static var secure: TextValidator {
        defaulted
    }
}
