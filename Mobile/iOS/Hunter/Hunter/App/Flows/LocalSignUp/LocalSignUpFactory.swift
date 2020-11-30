import Foundation
import UIKit

protocol LocalSignUpFactory {
    func LocalSignUpScreen<Cu: LocalSignUpCaseUse>(with caseUse: Cu) -> UIViewController
}

class LocalSignUpViewResolver: LocalSignUpFactory {
    func LocalSignUpScreen<Cu: LocalSignUpCaseUse>(with caseUse: Cu) -> UIViewController {
        let controller = LocalSignUpViewController()
        let _ = controller.view
        let presenter = RegistryPresenter()
        let fields = presenter.createInputs()
        controller.inputStack.addArrangedSubviews(fields)
        fields.forEach {
            $0.applyBorders()
            $0.delegate = controller
        }
        controller.validate = { try fields.forEach { try $0.validate() } }
        controller.nextFree = {
            fields.first { do { try $0.validate(); return false } catch { return true } }
        }
        controller.signUpButton.setup {
            do {
                let spinner = HunterLoading().fullScreen(of: controller.view).startLoading()
                caseUse.signUp(with: UserRegister(firstName: try fields.text(at: 0),
                                                  lastName: try fields.text(at: 1),
                                                  alias: try fields.text(at: 2),
                                                  email: try fields.text(at: 3),
                                                  password: try fields.text(at: 4)))
                { (result) in
                    spinner.stopLoading()
                    switch result {
                    case .success(_): return
                    case .failure(let error):
                        print("HDebug \(error)")
                    }
                }
            } catch {
                print("HDebug: No pudimos crear el usuario")
            }
        }

        return controller
    }
}

private extension Array where Element == HunterTextField {
    func text(at position: Int) throws -> String {
        guard let field = get(at: position), field.hasText else { throw DummyError.value }
        return field.text ?? ""
    }
}
