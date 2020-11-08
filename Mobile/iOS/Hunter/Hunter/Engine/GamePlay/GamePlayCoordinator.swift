import Foundation

class GamePlayCoordinator<CaseUse: GamePlayCaseUse, Flow: GamePlayFlow> {
    let flow: Flow
    let caseUse: CaseUse
    
    init(flow: Flow, caseUse: CaseUse) {
        self.flow = flow
        self.caseUse = caseUse
    }
    
    func start() { flow.showMap(with: caseUse, onUserLocationDidChanged: handleNewLocation) }
    
    func handleNewLocation(_ location: LocationCoordinate2D) {
        guard caseUse.isNearToGoal(with: location) else { return }
        flow.showCloseToGoal(with: caseUse)
    }
}
