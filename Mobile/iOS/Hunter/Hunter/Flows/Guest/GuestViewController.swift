//  Created by Franco Leto on 28/09/2020.

import Foundation
import UIKit

class GuestViewController: UIViewController {
    @IBOutlet weak var titleLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = Color.Hunter.darkBlue
        let button: HunterButton = UIView.loadFromCode()
        button.setTitle("Prueba de concepto", for: .normal)
        button.asSubview(of: view).center(to: view).activate()
        button.setup { print("Hello!") }
    }
}
