import Foundation

protocol JoinGameFlow {
    func sendToJoinGame<Cu: JoinGameCaseUse>(with caseUse: Cu,
                                             onComplete: @escaping ((Game) -> Void))
}
