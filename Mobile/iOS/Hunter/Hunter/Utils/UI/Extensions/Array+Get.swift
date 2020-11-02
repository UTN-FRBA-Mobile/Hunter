import Foundation

extension Array {
    func get(at index: Int) -> Element? { (count >= 0 && count < index) ? self[index] : nil }
}
