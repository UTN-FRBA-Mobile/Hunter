import Foundation

protocol LaunchFlow {
    func showLaunch<Cu: LaunchCaseUse>(with caseUse: Cu)
}
