import Foundation

protocol GamePlayCaseUse {
    func isNearToGoal(with location: LocationCoordinate2D) -> Bool
}

struct GamePlay: GamePlayCaseUse {
    func isNearToGoal(with location: LocationCoordinate2D) -> Bool {
        Bool.random()
    }
}
