import Foundation

public protocol Networking {
    func call<M: Decodable, B: Encodable, E: Error>(
        _ resource: Http<B>,
        _ callback: ActionResult<M, E>)
}
