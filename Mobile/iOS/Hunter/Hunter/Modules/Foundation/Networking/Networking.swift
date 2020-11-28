import Foundation

public protocol Networking {
    func call<M: Decodable, B: Encodable>(
        _ resource: Http<B>,
        _ callback: @escaping ActionResult<M, Error>)
    
    func setToken(_ token: String)
}
