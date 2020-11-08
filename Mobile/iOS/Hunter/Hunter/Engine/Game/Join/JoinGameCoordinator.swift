import Foundation

class JoinGameCoordinator<CaseUse: JoinGameCaseUse, Flow: JoinGameFlow> {
    let flow: Flow
    let caseUse: CaseUse
    let onGameJoined: (() -> Void)
    
    init(flow: Flow, caseUse: CaseUse, onComplete: @escaping (() -> Void)) {
        self.flow = flow
        self.caseUse = caseUse
        self.onGameJoined = onComplete
    }
    
    func start() { flow.sendToJoinGame(with: caseUse, onComplete: onGameJoined) }
}
