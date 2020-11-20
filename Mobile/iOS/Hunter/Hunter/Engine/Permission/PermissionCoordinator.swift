import Foundation

struct ActionsForDecision {
    let accept: (() -> Void)
    let decline: (() -> Void)
}

class PermissionCoordinator<Flow: PermissionFlow> {
    let flow: Flow
    let actions: ActionsForDecision
    
    init(flow: Flow, actions: ActionsForDecision) {
        self.flow = flow
        self.actions = actions
    }
    
    func askForPermission() {
        flow.showEnablePermission(onAccept: actions.accept, onCancel: actions.decline)
    }
}
