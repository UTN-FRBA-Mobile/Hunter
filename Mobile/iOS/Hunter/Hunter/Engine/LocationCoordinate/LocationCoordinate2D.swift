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
        // -34.58361984851203
        // -58.47369960231983
        LocationCoordinate2D(latitude: -34.58361984851203, longitude: -58.47369960231983)
    }
    
    static var secondHome: LocationCoordinate2D {
        LocationCoordinate2D(latitude: -34.5870120, longitude: -58.5005040)
    }
    
}
