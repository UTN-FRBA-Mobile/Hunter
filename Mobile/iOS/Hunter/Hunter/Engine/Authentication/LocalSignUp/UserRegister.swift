import Foundation

struct UserRegister: Encodable {
    let firstName: String
    let lastName: String
    let alias: String
    let email: String
    let password: String
}
