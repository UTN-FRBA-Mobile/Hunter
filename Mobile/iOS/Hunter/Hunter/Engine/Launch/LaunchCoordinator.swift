import Foundation
enum DummyError: Error {
    case value
}
typealias ActionResult<M, E: Error> = ((Result<M,E>) -> Void)

class LaunchCoordinator<CaseUse: LaunchCaseUse, Flow: LaunchFlow> {
    let flow: Flow
    let caseUse: CaseUse
    
    init(flow: Flow, caseUse: CaseUse) {
        self.flow = flow
        self.caseUse = caseUse
    }
    
    func start() { flow.showLaunch(with: caseUse) }
}

protocol LaunchFlow {
    func showLaunch<Cu: LaunchCaseUse>(with caseUse: Cu)
}

protocol LaunchCaseUse {
    
}

protocol AuthenticationStatusService {
    func callService<Model: Decodable>(_ onCompletion: ActionResult<Model, Error>)
}

class UserStatusRestService: AuthenticationStatusService {
    func callService<Model: Decodable>(_ onCompletion: ActionResult<Model, Error>)
    {
        if Bool.random() {
            onCompletion(.success(Authentication(token: "asd") as! Model))
        } else {
            onCompletion(.failure(DummyError.value))
        }
    }
}

struct Authentication: Decodable {
    var token: String
}

class Launch {
    let service: AuthenticationStatusService
    
    init(service: AuthenticationStatusService) {
        self.service = service
    }
    
    func checkUserStatus<Model: Decodable>(
        onDidAuthenticated: @escaping ((Model) -> Void),
        handleFailure: @escaping ((Error) ->Void))
    {
        service.callService { (result: Result<Model,Error>) in
            switch result {
            case .success(let model): onDidAuthenticated(model)
            case .failure(let error): handleFailure(error)
            }
        }
    }
}

import UIKit
protocol LaunchFactory {
    func launchScreen()
    -> UIViewController
}

class LaunchViewResolver: LaunchFactory {
    func launchScreen() -> UIViewController {
        UIViewController()
    }
}

class LaunchRouter<Nav: UINavigationController, Factory: LaunchFactory>: LaunchFlow, Router {
    let navigation: Nav
    let factory: Factory
    
    init(navigation: Nav, factory: Factory) {
        self.navigation = navigation
        self.factory = factory
    }
    
    func showLaunch<Cu>(with caseUse: Cu) where Cu : LaunchCaseUse
    {
        show(factory.launchScreen())
    }
    
}
