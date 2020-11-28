import Foundation

protocol Adapter {
    associatedtype M
    associatedtype N
    func convert(_ model: M) -> N
    func convert(_ model: N) -> M
}
