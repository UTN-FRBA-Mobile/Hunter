import UIKit
import MapKit
import CoreLocation

class GamePlayViewController: UIViewController {

    @IBOutlet weak var mapView: MKMapView!
    var beforeDie: (() -> Void) = {}
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mapView.delegate = self
    }
    
    func display(_ objective: CLLocationCoordinate2D, animated: Bool = true) {
        cleanOverlays()
        let circle = MKCircle(center: objective, radius: 2000)
        mapView.addOverlay(circle)
        let span = MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05)
        let region = MKCoordinateRegion(center: objective, span: span)
        mapView.setRegion(region, animated: animated)
    }

    private func cleanOverlays() { mapView.removeOverlays(mapView.overlays) }
    
    deinit { beforeDie() }
}

extension GamePlayViewController: MKMapViewDelegate {
    
    func mapView(_ mapView: MKMapView,
                 rendererFor overlay: MKOverlay)
    -> MKOverlayRenderer {
        guard let circle = overlay as? MKCircle else { return MKOverlayRenderer(overlay: overlay) }
        
        let renderer = MKCircleRenderer(overlay: circle)
        renderer.fillColor = Color.Hunter.green
        renderer.alpha = 0.2
        
        return renderer
    }
}
