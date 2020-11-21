import Foundation
import MapKit

extension CLLocation {
    
    func bearing(with location: CLLocation) -> Double {
        
        let lat1 = coordinate.latitude.toRadians
        let lon1 = coordinate.longitude.toRadians
        
        let lat2 = location.coordinate.latitude.toRadians
        let lon2 = location.coordinate.longitude.toRadians
        
        let dLon = lon2 - lon1
        
        let y = sin(dLon) * cos(lat2)
        let x = cos(lat1) * sin(lat2) - sin(lat1) * cos(lat2) * cos(dLon)
        
        return atan2(y, x).toDegrees
    }
}

extension CLLocationCoordinate2D {
    
    var toLocation: CLLocation { CLLocation(latitude: latitude, longitude: longitude) }
    
    func trunc(_ digit: Double) -> CLLocationCoordinate2D {
        CLLocationCoordinate2D(latitude: latitude.trunc(for: 6), longitude: longitude.trunc(for: 6))
    }
    
}

extension CLLocationDegrees {
    
    func trunc(for places: Double) -> CLLocationDegrees {
        truncl(self * divisor(places)) / divisor(places)
    }
    
}

extension LocationCoordinate2D {
    
    func distance(from other: LocationCoordinate2D) -> Double {
        let adapter = LocationAdapter()
        let myLocation = adapter.convert(self).toLocation
        let location = adapter.convert(other).toLocation
        
        return location.distance(from: myLocation)
    }

}
