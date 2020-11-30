import Foundation

class CreateGameCoordinator<CaseUse: CreateGameCaseUse, Flow: CreateGameFlow> {
    let flow: Flow
    let caseUse: CaseUse
    let onGameCreated: ((Game) -> Void)
    
    init(flow: Flow, caseUse: CaseUse, onCreated: @escaping ((Game) -> Void)) {
        self.flow = flow
        self.caseUse = caseUse
        self.onGameCreated = onCreated
    }
    
    func start() { flow.sendToCreateGame(with: caseUse, onComplete: onGameCreated) }
}
