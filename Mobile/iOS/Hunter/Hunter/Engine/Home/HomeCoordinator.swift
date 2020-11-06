import Foundation

class HomeCoordinator<CaseUse: HomeCaseUse, Flow: HomeFlow> {
    let flow: Flow
    let caseUse: CaseUse
    
    init(flow: Flow, caseUse: CaseUse) {
        self.flow = flow
        self.caseUse = caseUse
    }
    
    func start() { flow.sendHome(with: caseUse) }
}
