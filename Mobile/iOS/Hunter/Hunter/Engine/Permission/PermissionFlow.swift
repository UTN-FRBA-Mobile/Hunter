import Foundation

protocol PermissionFlow {
    func showEnablePermission(onAccept: @escaping (() -> Void), onCancel: @escaping (() -> Void))
}
