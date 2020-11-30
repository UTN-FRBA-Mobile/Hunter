import Foundation

protocol GamePlayFlow {
    func showMap<Cu: GamePlayCaseUse>(with caseUse: Cu, onUserLocationDidChanged: @escaping ((LocationCoordinate2D) -> Void))
    func showCloseToGoal<Cu: GamePlayCaseUse>(with caseUse: Cu, onUserLocationDidChanged: @escaping ((LocationCoordinate2D) -> Void))
    func sendUserBackToMapView()
    func sendUserToWinnerScreen()
}
