import Foundation

protocol LocalSignUpFlow {
    func sendUserToRegister<Cu: LocalSignUpCaseUse>(with caseUse: Cu)
}
