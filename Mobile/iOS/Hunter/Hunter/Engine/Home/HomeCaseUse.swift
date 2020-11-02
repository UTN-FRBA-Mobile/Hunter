import Foundation

protocol HomeCaseUse {
    var newGameFlow: (() -> Void) { get }
    var joinGameFlow: (() -> Void) { get }
}

struct Home: HomeCaseUse {
    let newGameFlow: (() -> Void)
    let joinGameFlow: (() -> Void)
}
