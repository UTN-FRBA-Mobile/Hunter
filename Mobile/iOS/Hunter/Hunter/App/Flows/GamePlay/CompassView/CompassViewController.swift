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

    private var adjAngle: CGFloat {
        guard let window = UIApplication.shared.windows.first?.windowScene else { return 0 }
        
        switch window.interfaceOrientation {
        case .landscapeLeft:
            return 90
        case .landscapeRight:
            return -90
        case .portrait, .unknown:
            return 0
        case .portraitUpsideDown:
            return -180
        @unknown default:
            return 0
        }
    }

    private func recalculateSize() {

        lastDistance = target.toLocation.distance(from: location)
    }
    
    private func recalculateDirections() {
        
        let dirRadiant = location.bearing(with: target.toLocation)
        let originalHeading = (dirRadiant - CGFloat(heading.trueHeading).toRadians)
        lastAngle = CGFloat(adjAngle.toRadians + originalHeading)
        
        UIView.animate(withDuration: 0.3) {
            self.arrowImageView.transform = CGAffineTransform(rotationAngle: self.lastAngle)
        }
    }
    
    // MARK: - Deinit
    
    deinit { beforeDie() }
    
}
