import Foundation
import UIKit

protocol Router {
    associatedtype Navigation: UINavigationController
    var navigation: Navigation { get }
}

extension Router {
    
    func replaceAll(with controller: UIViewController, animated: Bool = true) {
        navigation.setViewControllers([controller], animated: animated)
    }
    
    func replaceLast(with controller: UIViewController, animated: Bool = true) {
        guard navigation.viewControllers.count >= 1 else { return }
        var controllers = navigation.viewControllers.dropLast()
        controllers.append(controller)
        navigation.setViewControllers(Array(controllers), animated: animated)
    }
    
    func show(_ controller: UIViewController, animated: Bool = true) {
        navigation.pushViewController(controller, animated: animated)
    }
    
    func pop(animated: Bool = true) { navigation.popViewController(animated: animated) }
    
    func popToRoot(animated: Bool = true) { navigation.popToRootViewController(animated: animated) }
    
    func present(_ controller: UIViewController, style: UIModalPresentationStyle = .fullScreen, animated: Bool = true) {
        controller.modalPresentationStyle = style
        navigation.present(controller, animated: animated)
    }
}
