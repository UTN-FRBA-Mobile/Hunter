import Foundation
import UIKit
import MapKit

protocol GamePlayFactory {
    func mapScreen<Cu: GamePlayCaseUse>(with caseUse: Cu,
                                        onUserLocationDidChanged: ((LocationCoordinate2D) -> Void)) -> UIViewController
}

class GamePlayViewResolver: GamePlayFactory {
    
    func mapScreen<Cu: GamePlayCaseUse>(with caseUse: Cu,
                                        onUserLocationDidChanged: ((LocationCoordinate2D) -> Void))
    -> UIViewController {
        let controller = GamePlayViewController()
        _ = controller.view
        let objective = CLLocationCoordinate2D(latitude: -34.5838433, longitude: -58.4757436)
        controller.display(objective)
        return controller
    }
}

protocol Adapter {
    associatedtype M
    associatedtype N
    func convert(_ model: M) -> N
    func reconvert(_ model: N) -> M
}

struct LocationAdapter: Adapter {
    typealias M = LocationCoordinate2D
    typealias N = CLLocationCoordinate2D
    func convert(_ model: M) -> N { N(latitude: model.latitude, longitude: model.longitude) }
    func reconvert(_ model: N) -> M { M(latitude: model.latitude, longitude: model.longitude) }
}

class UserLocationTracker: NSObject, CLLocationManagerDelegate {
    let locationManager: CLLocationManager
    var onUpdate: ((LocationCoordinate2D) -> Void) = { _ in }
    let adapter: LocationAdapter
    
    override init() {
        locationManager = CLLocationManager()
        adapter = LocationAdapter()
        super.init()
        locationManager.delegate = self
    }
    
    func requestAuthorization() {
        locationManager.requestWhenInUseAuthorization()
    }
    
    func startTracking(_ onUpdate: @escaping ((LocationCoordinate2D) -> Void)) {
        locationManager.distanceFilter = 100.0 // 100m
        locationManager.startUpdatingLocation()
    }
}

extension UserLocationTracker {
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
    }
}
