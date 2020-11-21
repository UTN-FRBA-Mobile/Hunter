import Foundation

extension Array {
    func get(at index: Int) -> Element? { (index >= 0 && count > index) ? self[index] : nil }
}
