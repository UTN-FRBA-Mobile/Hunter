import Foundation

public class RestClient: Networking {
    #warning("We are showing a print, but in a near future we are going to perform the request")
    public func call<M: Decodable, B: Encodable, E: Error>(
        _ resource: Http<B>,
        _ callback: ActionResult<M, E>) {
        print("Calling \(resource.method)-\(resource.urlTyped)")
    }
}
