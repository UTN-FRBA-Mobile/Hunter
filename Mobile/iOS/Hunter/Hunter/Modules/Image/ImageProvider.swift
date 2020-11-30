import Foundation
import UIKit

#warning("We need to put final implementation!")
class LocaliOSImageModule: ImageCaseUse {

    func userWantsToTakePhoto(_ callback: @escaping ((ImageResultFlow) -> Void)) {
        callback(.didSelectAn(UIImage()))
    }

    func userWantsToUploadPhoto(_ callback: @escaping ((ImageResultFlow) -> Void)) {
        callback(.didSelectAn(UIImage()))
    }
}
