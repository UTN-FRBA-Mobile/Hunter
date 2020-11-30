import CoreLocation

class UserLocationTracker: NSObject, CLLocationManagerDelegate {
    
    // MARK: - Attributes
    
    let locationManager: CLLocationManager = CLLocationManager()
    
    // MARK: - Private Callbacks
    
    private var onNewLocation: ((CLLocation) -> Void) = { _ in }
    private var onNewHeading: ((CLHeading) -> Void) = { _ in }

    // MARK: - Functions - Tracking
    
    func startTracking(_ block: @escaping ((CLLocation) -> Void)) {
        
        onNewLocation = block
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.distanceFilter = 10
        locationManager.startUpdatingLocation()
    }
    
    func startHeading(_ block: @escaping ((CLHeading) -> Void)) {
        
        onNewHeading = block
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
        locationManager.headingFilter = 5
        locationManager.startUpdatingHeading()
    }
    
    func stopTracking() {
        
        locationManager.stopUpdatingHeading()
        onNewHeading = { _ in }
        
        locationManager.stopUpdatingLocation()
        onNewLocation = { _ in }
        
        locationManager.delegate = nil
    }
}

extension UserLocationTracker {
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let location = locations.first else { return }
        
        onNewLocation(location)
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateHeading newHeading: CLHeading) {
        onNewHeading(newHeading)
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("We should handle Error: \(error)")
    }
    
}
