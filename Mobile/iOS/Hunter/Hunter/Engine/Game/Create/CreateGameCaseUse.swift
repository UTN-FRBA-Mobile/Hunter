import Foundation

protocol CreateGameCaseUse {
    var imageProvider: ImageCaseUse { get set }
    func saveImage(_ coded: String)
    func create(with clue: String?, onCompletion: ActionResult<NoReply, Error>)
}

class CreateGame: CreateGameCaseUse {
    private enum Status { case initialize, withGoal(image: String) }
    var imageProvider: ImageCaseUse
    private var status: CreateGame.Status = .initialize
    
    init(imageProvider: ImageCaseUse) { self.imageProvider = imageProvider }
    
    func saveImage(_ coded: String) { status = .withGoal(image: coded) }
    
    func create(with clue: String?, onCompletion: ActionResult<NoReply, Error>) {
        switch status {
        case .initialize:
            #warning("To be changed!")
            onCompletion(.failure(DummyError.value))
        case .withGoal(image: let codedImage):
            print("Hunter: image -> \(codedImage) and clue: \(clue ?? "")")
            #warning("Put service!")
            onCompletion(.success(NoReply()))
        }
    }
}

struct CreateGameServiceModel: Codable {
    let image: String
    let clue: String?
}

protocol ImageCaseUse {
    func userWantsToTakePhoto(_ callback: @escaping ((ImageResultFlow) -> Void))
    func userWantsToUploadPhoto(_ callback: @escaping ((ImageResultFlow) -> Void))
}

import UIKit
enum ImageResultFlow {
    case didSelectAn(UIImage)
    case cancel
}
