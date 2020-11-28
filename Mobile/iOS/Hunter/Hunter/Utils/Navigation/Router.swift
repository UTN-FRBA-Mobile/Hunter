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
    
    func replaceLeavingHomeAnd(_ controller: UIViewController, animated: Bool = true) {
        
        guard navigation.viewControllers.count >= 1 else { return }
        
        navigation.pushViewController(controller, animated: true)
        
        guard let root = navigation.viewControllers.first else { return }
        
        DispatchQueue.main.asyncAfter(deadline: .now()) {
            navigation.setViewControllers([root, controller], animated: false)
        }
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
