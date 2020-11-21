import Foundation

struct LocationCoordinate2D {
    let latitude: Double
    let longitude: Double
    
    init(latitude: Double, longitude: Double) {
        self.latitude = latitude
        self.longitude = longitude
    }
}

extension LocationCoordinate2D {
    
    static var mainHome: LocationCoordinate2D {
        LocationCoordinate2D(latitude: -34.5838088, longitude: -58.4757028)
    }
    
    static var secondHome: LocationCoordinate2D {
        LocationCoordinate2D(latitude: -34.5870120, longitude: -58.5005040)
    }
    
}
