import Foundation
import UIKit

class RegistryPresenter {
    
    enum FieldValidation: Error {
        case isEmpty(field: String)
        case didntMatch(field: String)
    }
    
    func createInputs() -> [HunterTextField] {
        let name = FieldViewModel(placeholder: "REGISTER_FIELD_PLACEHOLDER_NAME".localize(),
                                  validation: .defaulted)
        let lastname = FieldViewModel(placeholder: "REGISTER_FIELD_PLACEHOLDER_LASTNAME".localize(),
                                      validation: .defaulted)
        let alias = FieldViewModel(placeholder: "REGISTER_FIELD_PLACEHOLDER_ALIAS".localize(),
                                   validation: .defaulted)
        let email = FieldViewModel(placeholder: "REGISTER_FIELD_PLACEHOLDER_EMAIL".localize(),
                                   validation: .defaulted)
        let password = FieldViewModel(placeholder: "REGISTER_FIELD_PLACEHOLDER_PASSWORD",
                                      validation: .secure)
        let passwordInput = input(with: password)
        let confirmPassword = FieldViewModel(placeholder: "REGISTER_FIELD_PLACEHOLDER_CONFIRM_PASSWORD",
                                             validation: .secure)
        let confirmPasswordInput = input(with: confirmPassword) { [weak passwordInput] in
            guard let password = passwordInput?.text else { throw FieldValidation.isEmpty(field: "Confirm Password") }
            guard let value = $0 else { throw FieldValidation.isEmpty(field: "Password") }
            guard value == password else { throw FieldValidation.didntMatch(field: "Password") }
        }
        var fields = [name, lastname, alias, email].map { input(with: $0) }
        fields.append(passwordInput)
        fields.append(confirmPasswordInput)
        return fields
    }

    func input(with viewModel: FieldViewModel,
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
