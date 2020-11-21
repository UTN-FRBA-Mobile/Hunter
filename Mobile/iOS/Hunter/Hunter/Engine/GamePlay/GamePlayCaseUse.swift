import Foundation

protocol GamePlayCaseUse {
    var goal: LocationCoordinate2D { get }
    var zoneRadius: Double { get }
    var state: GamePlay.State { get }
    func DidUserChangedZone(with location: LocationCoordinate2D) -> Bool
}

class GamePlay: GamePlayCaseUse {
    enum State {
        case unspecific
        case near(LocationCoordinate2D)
        case distant
    }
    
    private (set) var calculateDistance: ((LocationCoordinate2D, LocationCoordinate2D) -> Double)
    
    let goal: LocationCoordinate2D
    let zoneRadius: Double
    var state: GamePlay.State = .unspecific
    
    init(_ goal: LocationCoordinate2D, zoneRadius: Double, calculateDistance: @escaping ((LocationCoordinate2D, LocationCoordinate2D) -> Double)) {
        self.goal = goal
        self.zoneRadius = zoneRadius
        self.calculateDistance = calculateDistance
    }
    
    func DidUserChangedZone(with location: LocationCoordinate2D) -> Bool {
        switch state {
        case .unspecific, .distant:
            guard calculateDistance(goal, location) <  zoneRadius else { return false }
            self.state = .near(location)
        case .near(_):
            guard calculateDistance(goal, location) > zoneRadius else { return false }
            self.state = .distant
        }
        return true
    }
}
