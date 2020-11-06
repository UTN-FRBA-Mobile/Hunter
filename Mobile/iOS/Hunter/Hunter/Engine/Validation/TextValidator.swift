import Foundation

struct TextValidator: Validation {
    typealias Value = String
    var validate: Option
    
    func validate(_ value: Value) throws {
        switch validate {
        case .none: return
        case .with(let validations): try validations.forEach { try $0.validate(value) }
        }
    }
}

extension TextValidator {
    enum Option {
        case none
        case with([Validations])
    }
}

extension TextValidator {
    enum Validations: Validation {
        typealias Value = String
        enum VError: Error {
            case needMoreCharacters
            case excessCharacters
            case cantContain(pattern: String)
            case missing(pattern: String)
        }
        case minimum(of: Int)
        case maximum(of: Int)
        case avoidPattern(String)
        case contains(String)
        case block(((String) throws -> Void))
        
        func validate(_ value: String) throws {
            switch self {
            case .minimum(of: let minimum):
                guard value.count > minimum else { throw VError.needMoreCharacters }
            case .maximum(of: let maximum):
                guard value.count < maximum else { throw VError.excessCharacters }
            case .avoidPattern(let pattern):
                guard !value.contains(pattern) else { throw VError.cantContain(pattern: pattern)}
            case .contains(let pattern):
                guard value.contains(pattern) else { throw VError.missing(pattern: pattern)}
            case .block(let action):
                try action(value)
            }
        }
    }
}

