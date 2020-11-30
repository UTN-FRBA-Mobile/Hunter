import Foundation
import UIKit

protocol CreateGameFactory {
    func createGameScreen<Cu: CreateGameCaseUse>(with caseUse: Cu,
                                                 onCreated: @escaping ((Game) -> Void)) -> UIViewController
}

class CreateGameViewResolver: CreateGameFactory {
    
    func createGameScreen<Cu: CreateGameCaseUse>(with caseUse: Cu,
                                                 onCreated: @escaping ((Game) -> Void)) -> UIViewController {
        let controller = CreateGameViewController()
        _ = controller.view
        let presenter = CreateGamePresenter()
        controller.setClue(placeholder: presenter.cluePlaceholder)
        controller.createButton.setTitle(presenter.create, for: .normal)
        controller.createButton.setup {
            print("Hunter: Create Game. With Clue: \(controller.clue ?? "")")
            caseUse.create(with: controller.clue) { result in
                switch result {
                case .success(let game): onCreated(game)
                case .failure(let error): print("Hunter Error \(error)")
                }
            }
        }
        let callbackLoadImage: ((ImageResultFlow) -> Void) = { result in
            switch result {
            case .didSelectAn(let image):
                controller.setImage(image)
                #warning("Missing from image to String")
                caseUse.saveImage("")
                controller.createButton.isEnabled = true
            case .cancel: break
            }
        }
        controller.uploadButton.setTitle(presenter.uploadPhoto, for: .normal)
        controller.uploadButton.setup {
            caseUse.imageProvider.userWantsToUploadPhoto(callbackLoadImage)
        }
        controller.takePhotoButton.setTitle(presenter.takePhoto, for: .normal)
        controller.takePhotoButton.setup {
            caseUse.imageProvider.userWantsToTakePhoto(callbackLoadImage)
        }
        return controller
    }

}
