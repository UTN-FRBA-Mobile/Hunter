import Foundation

public extension URL {
    enum Typed {
        case final(full: String)
        case business(endpoint: String)
    }
}
