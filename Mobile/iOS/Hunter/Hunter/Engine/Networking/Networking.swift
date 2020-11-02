import Foundation

public typealias ActionResult<M, E: Error> = ((Result<M,E>) -> Void)

class RestClient: Networking {
    func call<M: Decodable, B: Encodable, E: Error>(
        _ resource: Http<B>,
        _ callback: ActionResult<M, E>) {
        print("Calling \(resource.method)-\(resource.urlTyped)")
    }
}

public protocol Networking {
    func call<M: Decodable, B: Encodable, E: Error>(
        _ resource: Http<B>,
        _ callback: ActionResult<M, E>)
}

public struct Http<Body: Encodable> {
    let urlTyped: URL.Typed
    let method: Method<Body>
}

public extension URL {
    enum Typed {
        case final(full: String)
        case business(endpoint: String)
    }
}

public extension Http {
    enum Method<Body: Encodable> {
        case get
        case post(body: Body)
    }
}

struct NoReply: Codable {}
