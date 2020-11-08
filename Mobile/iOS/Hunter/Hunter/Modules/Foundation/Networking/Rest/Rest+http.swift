import Foundation

public struct Http<Body: Encodable> {
    let urlTyped: URL.Typed
    let method: Method<Body>
}

public extension Http {
    enum Method<Body: Encodable> {
        case get
        case post(body: Body)
    }
}
