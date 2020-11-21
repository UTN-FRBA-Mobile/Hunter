import Foundation

public typealias ActionResult<M, E: Error> = ((Result<M,E>) -> Void)

public struct NoReply: Codable {}
