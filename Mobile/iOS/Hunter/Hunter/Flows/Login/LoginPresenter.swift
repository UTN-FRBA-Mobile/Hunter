import Foundation
import UIKit

struct LoginPresenter {
    let loginPlaceholder: String = ""
    
    func createInputs() -> [HunterTextField] {
        let email = FieldViewModel(placeholder: "LOGIN_FIELD_PLACEHOLDER_EMAIL".localize(),
                                   validation: .defaulted)
        let password = FieldViewModel(placeholder: "LOGIN_FIELD_PLACEHOLDER_PASSWORD",
                                      validation: .secure)
        let passwordInput = input(with: password)
        var fields = [email].map { input(with: $0) }
        fields.append(passwordInput)
        return fields
    }
    
    #warning("Duplicated code")
    private func input(with viewModel: FieldViewModel,
               additional validation: @escaping ((String?) throws -> Void) = {_ in }) -> HunterTextField
    {
        UIView.loadFromCode { (field: HunterTextField) in
            field.isSecureTextEntry = viewModel.isSecure
            field.placeholder = viewModel.placeholder
            field.validationBlock = {
                try viewModel.validation.validate($0 ?? "")
                try validation($0)
            }
        }
    }
}
