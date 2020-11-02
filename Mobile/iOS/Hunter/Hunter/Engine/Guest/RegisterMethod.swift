//  Created by Franco Leto on 28/09/2020.

import Foundation

enum RegisterMethod: String {
    case email
    case google
    case facebook
    
    static var all: [RegisterMethod] { [.email, .google, .facebook] }
}
