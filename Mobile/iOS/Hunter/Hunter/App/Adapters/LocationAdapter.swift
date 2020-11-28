import Foundation
import MapKit

struct LocationAdapter: Adapter {
    typealias M = LocationCoordinate2D
    typealias N = CLLocationCoordinate2D
    func convert(_ model: M) -> N { N(latitude: model.latitude, longitude: model.longitude) }
    func convert(_ model: N) -> M { M(latitude: model.latitude, longitude: model.longitude) }
}
