import Foundation

protocol Validation {
    associatedtype Value
    func validate(_ value: Value) throws -> Void
}
