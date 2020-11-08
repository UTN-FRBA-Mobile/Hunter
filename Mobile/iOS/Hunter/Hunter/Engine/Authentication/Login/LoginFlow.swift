import Foundation

protocol LoginFlow {
    func showLogin<Cu: LoginCaseUse>(with caseuse: Cu, ifIsANewUser: @escaping (() -> Void))
}
