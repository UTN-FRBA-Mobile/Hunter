import CoreLocation
import Foundation
import MapKit

struct GPSActions {
    let onShouldRequest: (() -> Void)
    let wasAnsweredBefore: ActionsForDecision
}

class LocationPermission: NSObject {
    enum State {
        case shouldAsk
        case granted
        case denied
    }
    
    let manager = CLLocationManager()
    
    var state: LocationPermission.State {
        if #available(iOS 14.0, *) {
            return parseTo(manager.authorizationStatus)
        } else {
            return parseTo(CLLocationManager.authorizationStatus())
        }
    }
    
    private func parseTo(_ authorizationStatus: CLAuthorizationStatus) -> LocationPermission.State {
        switch authorizationStatus {
        case .authorizedAlways, .authorizedWhenInUse:
            return .granted
        case .denied:
            return .denied
        case .restricted:
            return .denied
        case .notDetermined:
            return .shouldAsk
        default:
            return .denied
        }
    }
    
    enum Status: Equatable {
        case defined
        case requesting(ActionsForDecision)
        static func ==(_ lhs: Status,_ rhs: Status) -> Bool {
            switch (lhs,rhs) {
            case (.defined, .defined): return true
            case (.requesting(_), .requesting(_)): return true
            default: return false
            }
        }
    }
    
    private var status: Status = .defined
    
    func requestUserLocation(_ actions: ActionsForDecision) {
        guard status == .defined else { return }
        status = .requesting(actions)
        manager.delegate = self
        manager.requestWhenInUseAuthorization()
    }
}

extension LocationPermission: CLLocationManagerDelegate {
    func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
        switch status {
        case .requesting(let actions):
            switch state {
            case .granted: actions.accept()
            case .denied: actions.decline()
            default: break
            }
        default:
            #warning("Maybe we need to post a notification in order to see if we are going to ask the user to turn on the gps!")
            break
        }
    }
}

struct LocationPermissionPresenter: PermissionPresenter {
    let title: String = "PERMISSION_LOCATION_TITLE".localize()
    let message: String = "PERMISSION_LOCATION_MESSAGE".localize()
    let acceptTitle: String = "PERMISSION_LOCATION_ACCEPT_TITLE".localize()
    let rejectTitle: String = "PERMISSION_LOCATION_REJECT_TITLE".localize()
}
