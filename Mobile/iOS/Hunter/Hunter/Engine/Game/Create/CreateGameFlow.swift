import Foundation

protocol CreateGameFlow {
    func sendToCreateGame<Cu: CreateGameCaseUse>(with caseUse: Cu,
                                                 onComplete: @escaping (() -> Void))
}
