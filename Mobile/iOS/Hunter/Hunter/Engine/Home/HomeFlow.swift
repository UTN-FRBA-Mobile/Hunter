import Foundation

protocol HomeFlow {
    func sendHome<Cu: HomeCaseUse>(with caseUse: Cu)
}
