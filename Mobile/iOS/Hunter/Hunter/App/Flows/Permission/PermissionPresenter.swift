import Foundation

protocol PermissionPresenter {
    var title: String { get }
    var message: String { get }
    var acceptTitle: String { get }
    var rejectTitle: String { get }
}
