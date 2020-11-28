import Foundation
import Alamofire

class AlamofireClient: Networking {
    private let rootPathUrl: String = "https://utn-mobile-hunter.herokuapp.com"
    private let sessionManager: Session = Session()
    private var baseHeaders = HTTPHeaders()
    
    func call<M, B>(_ resource: Http<B>,
                    _ callback: @escaping (Result<M, Error>) -> Void)
    where M : Decodable, B : Encodable {
        do {
            let url = try createUrl(with: resource.urlTyped)
            let request = sessionManager.request(url, method: resource.method.toHttpMethod,
                                                 parameters: resource.body,
                                                 encoding: JSONEncoding.default,
                                                 headers: baseHeaders)
            let decoder = JSONDecoder()
            decoder.keyDecodingStrategy = .useDefaultKeys
            request.responseDecodable(of: M.self, decoder: decoder) { response in
                DispatchQueue.main.async {
                    guard let model = response.value else {
                        callback(.failure(response.error ?? DummyError.value))
                        return
                    }
                    callback(.success(model))
                }
            }
        } catch let error {
            print("HDebug - \(error)")
            #warning("We should throw an error!")
        }
    }
    
    func setToken(_ token: String) {
        baseHeaders.add(HTTPHeader(name: "Authorization", value: "Bearer \(token)"))
    }
}

fileprivate extension Http.Method {
    var toHttpMethod: HTTPMethod {
        switch self {
        case .get: return HTTPMethod.get
        case .post(_): return HTTPMethod.post
        }
    }
}

fileprivate extension AlamofireClient {
    
    func createUrl(with typedUrl: URL.Typed) throws -> URL {
        switch typedUrl {
        case .business(let endpoint): return try "\(rootPathUrl)\(endpoint)".toUrl()
        case .final(let absoluteUrl): return try absoluteUrl.toUrl()
        }
    }
}

fileprivate enum URLBuildingError: Error { case cantParseToUrl }

fileprivate extension String {
    func toUrl() throws -> URL {
        guard let url = URL(string: self) else { throw URLBuildingError.cantParseToUrl }
        return url
    }
}

extension Http {
    var body: [String: Any]? {
        switch method {
        case .post(let serviceModel): return serviceModel.toDictionary()
        default: return nil
        }
    }
}

extension Encodable {
    func toDictionary() -> [String: Any]? {
        do {
            let encoder = JSONEncoder()
            encoder.dateEncodingStrategy = .millisecondsSince1970
            let json = try encoder.encode(self)
            return try JSONSerialization.jsonObject(with: json, options: []) as? [String: Any]
        } catch {
            return nil
        }
    }
}
