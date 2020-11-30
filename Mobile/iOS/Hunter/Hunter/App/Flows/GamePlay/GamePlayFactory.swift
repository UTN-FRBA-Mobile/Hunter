import Foundation
import UIKit
import MapKit

protocol GamePlayFactory {
    
    func mapScreen<Cu: GamePlayCaseUse>(with caseUse: Cu, onUserLocationDidChanged: @escaping ((LocationCoordinate2D) -> Void)) -> UIViewController
    
    func nearToGoalScreen<Cu: GamePlayCaseUse>(with caseUse: Cu, onUserLocationDidChanged: @escaping ((LocationCoordinate2D) -> Void)) -> UIViewController
    
    func winnerScreen(onComplete: @escaping (() -> Void)) -> UIViewController
}

class GamePlayViewResolver: GamePlayFactory {
    
    func mapScreen<Cu: GamePlayCaseUse>(with caseUse: Cu, onUserLocationDidChanged: @escaping ((LocationCoordinate2D) -> Void)) -> UIViewController {
        
        let adapter = LocationAdapter()
        let locationTracker = UserLocationTracker()
        
        let controller = GamePlayViewController()
        _ = controller.view
        controller.display(adapter.convert(caseUse.goal))
    
        locationTracker.startTracking { _ in
            // onUserLocationDidChanged(adapter.convert($0.coordinate))
        }
        controller.beforeDie = locationTracker.stopTracking
        
        #warning("To be remove!")
        var index = 0
        let locations: [LocationCoordinate2D] = [
            LocationCoordinate2D(latitude: -34.60361984851203, longitude: -58.50369960231983),
            LocationCoordinate2D(latitude: -34.59361984851203, longitude: -58.43369960231983),
            LocationCoordinate2D(latitude: -34.58361984851290, longitude: -58.47369960231920)
        ]
        let block: (() -> Bool) = {
            guard let location = locations.get(at: index) else { return false }
            onUserLocationDidChanged(location)
            index += 1
            return true
        }
        perform(block, after: 1)
        
        return controller
    }
    
    func perform(_ block: @escaping (() -> Bool), after duration: Double) {
        DispatchQueue.main.asyncAfter(deadline: .now()+duration) {
            guard block() else { return }
            self.perform(block, after: duration)
        }
    }
    
    func nearToGoalScreen<Cu: GamePlayCaseUse>(with caseUse: Cu, onUserLocationDidChanged: @escaping ((LocationCoordinate2D) -> Void)) -> UIViewController {
        
        let adapter = LocationAdapter()
        let locationTracker = UserLocationTracker()
        
        let controller = CompassViewController()
        _ = controller.view
        controller.view.backgroundColor = UIColor.white
        controller.target = adapter.convert(caseUse.goal)
        controller.beforeDie = locationTracker.stopTracking
        
        switch caseUse.state {
        case .near(let userLocation): controller.location = adapter.convert(userLocation).toLocation
        default: break
        }
        locationTracker.startTracking { _ in
//            onUserLocationDidChanged(adapter.convert($0.coordinate))
        }
        locationTracker.startHeading { controller.heading = $0 }
        
        #warning("To be remove!")
        var index = 0
        let locations: [LocationCoordinate2D] = [
            LocationCoordinate2D(latitude: -34.58360054951390, longitude: -58.47369920292220),
            LocationCoordinate2D(latitude: -34.58360294951390, longitude: -58.47369560292220),
            LocationCoordinate2D(latitude: -34.58360134951390, longitude: -58.47369260292220),
            LocationCoordinate2D(latitude: -34.58360084951390, longitude: -58.47369960292220),
            LocationCoordinate2D(latitude: -34.58360280651600, longitude: -58.47369960201080),
            LocationCoordinate2D.mainHome
        ]
        let block: (() -> Bool) = {
            guard let location = locations.get(at: index) else { return false }
            let distance = caseUse.goal.distance(from: location).rounded(.up)
            controller.showToast(with: "Estas a \(distance) metros!", on: .top, for: 2)
            onUserLocationDidChanged(location)
            index += 1
            return true
        }
        perform(block, after: 3.2)
        
        return controller
    }
    
    func winnerScreen(onComplete: @escaping (() -> Void)) -> UIViewController {
        let controller = UIViewController()
        _ = controller.view
        controller.view.backgroundColor = .white
        
        let label = UILabel()
        label.translatesAutoresizingMaskIntoConstraints = false
        
        controller.add(centerFlexible: label,
                       axis: .horizontal,
                       to: controller.view).text = "Ha ganado el juego!"
        
        let button = HunterButton()
        button.translatesAutoresizingMaskIntoConstraints = false
        button.setTitle("Volver al Lobby!", for: .normal)
        button.setup(forTap: onComplete)
        button.asSubview(of: controller.view)
        button.isEnabled = true
        
        [button.leadingAnchor.constraint(equalTo: controller.view.leadingAnchor, constant: 32),
         controller.view.trailingAnchor.constraint(equalTo: button.trailingAnchor, constant: 32),
         controller.view.bottomAnchor.constraint(equalTo: button.bottomAnchor, constant: 32),
         button.heightAnchor.constraint(equalToConstant: 44)
        ].activate()
        
        return controller
    }
}
