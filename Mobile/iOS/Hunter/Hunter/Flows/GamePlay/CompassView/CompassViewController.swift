import UIKit
import MapKit

class CompassViewController: UIViewController {

    @IBOutlet private weak var arrowImageView: UIImageView!

    // MARK: - Public Attributes
    
    var beforeDie: (() -> Void) = { }
    var target: CLLocationCoordinate2D = .init()
    
    var location = CLLocation() { didSet { recalculateSize() } }
    var heading = CLHeading() { didSet { recalculateDirections() } }
    
    // MARK: - Private Functions
    
    private var lastAngle: CGFloat = 0
    private var lastDistance: CLLocationDistance = 0
    
    private func recalculateSize() {
        lastDistance = target.toLocation.distance(from: location)
    }
    
    private func recalculateDirections() {
        let headingR = Measurement(value: heading.trueHeading, unit: UnitAngle.degrees).converted(to: UnitAngle.radians).value
        let dirRadiant = location.bearing(with: target.toLocation)
        lastAngle = CGFloat(dirRadiant - headingR)
        UIView.animate(withDuration: 0.3) {
            self.arrowImageView.transform = CGAffineTransform(rotationAngle: self.lastAngle)
        }
    }
    
    // MARK: - Deinit
    
    deinit { beforeDie() }
    
}
