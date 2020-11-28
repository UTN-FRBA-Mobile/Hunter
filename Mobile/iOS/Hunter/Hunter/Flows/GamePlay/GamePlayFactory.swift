import Foundation
import UIKit
import MapKit

protocol GamePlayFactory {
    
    func mapScreen<Cu: GamePlayCaseUse>(with caseUse: Cu, onUserLocationDidChanged: @escaping ((LocationCoordinate2D) -> Void)) -> UIViewController
    
    func nearToGoalScreen<Cu: GamePlayCaseUse>(with caseUse: Cu, onUserLocationDidChanged: @escaping ((LocationCoordinate2D) -> Void)) -> UIViewController
}

class GamePlayViewResolver: GamePlayFactory {
    
    func mapScreen<Cu: GamePlayCaseUse>(with caseUse: Cu, onUserLocationDidChanged: @escaping ((LocationCoordinate2D) -> Void)) -> UIViewController {
        
        let adapter = LocationAdapter()
        let locationTracker = UserLocationTracker()
        
        let controller = GamePlayViewController()
        _ = controller.view
        controller.display(adapter.convert(caseUse.goal))
        
        locationTracker.startTracking { onUserLocationDidChanged(adapter.convert($0.coordinate)) }
        controller.beforeDie = locationTracker.stopTracking
        
        return controller
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
        locationTracker.startTracking { onUserLocationDidChanged(adapter.convert($0.coordinate)) }
        locationTracker.startHeading { controller.heading = $0 }
        
        return controller
    }
}
