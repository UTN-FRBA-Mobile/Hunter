import Foundation

protocol Networking {
    func call<M: Decodable, B: Encodable, E: Error>(
        _ resource: Http<B>,
        _ callback: ActionResult<M, E>)
}

struct Http<Body: Encodable> {
    let urlTyped: URL.Typed
    let method: Method<Body>
}

extension URL {
    enum Typed {
        case final(full: String)
        case business(endpoint: String)
    }
}

extension Http {
    enum Method<Body: Encodable> {
        case get
        case post(body: Body)
    }
}
