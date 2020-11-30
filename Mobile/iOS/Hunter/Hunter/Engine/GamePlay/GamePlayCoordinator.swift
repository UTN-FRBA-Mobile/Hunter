import Foundation

class GamePlayCoordinator<CaseUse: GamePlayCaseUse, Flow: GamePlayFlow> {
    let flow: Flow
    let caseUse: CaseUse
    
    init(flow: Flow, caseUse: CaseUse) {
        self.flow = flow
        self.caseUse = caseUse
    }
    
    func start() { flow.showMap(with: caseUse, onUserLocationDidChanged: checkIfItsNearToGoal) }
    
    func checkIfItsNearToGoal(_ location: LocationCoordinate2D) {
        guard caseUse.DidUserChangedZone(with: location) else { return }
        
        flow.showCloseToGoal(with: caseUse, onUserLocationDidChanged: checkIfItsFarFromGoal)
    }
    
    func checkIfItsFarFromGoal(_ location: LocationCoordinate2D) {
        if didWin(with: location) {
            return flow.sendUserToWinnerScreen()
        }
        
        guard caseUse.DidUserChangedZone(with: location) else { return }
        
        flow.sendUserBackToMapView()
    }
    
    func didWin(with location: LocationCoordinate2D) -> Bool {
        caseUse.goal.distance(from: location) <= 1
    }
}
