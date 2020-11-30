import UIKit

class CreateGameViewController: UIViewController {
    
    @IBOutlet weak private var goalImageView: UIImageView!
    @IBOutlet weak private var clueTextView: UITextView!
    @IBOutlet weak var uploadButton: HunterButton!
    @IBOutlet weak var takePhotoButton: HunterButton!
    @IBOutlet weak var createButton: HunterButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setInitialUI(for: uploadButton)
        setInitialUI(for: takePhotoButton)
        setInitialUI(for: createButton, isActive: false)
        clueTextView.delegate = self
    }
    
    var clue: String? {
        guard clueTextView.text ?? "" != placeholderClue else { return nil }
        return clueTextView.text
    }
    private var placeholderClue: String = ""
    func setClue(placeholder: String) { placeholderClue = placeholder; updateClueView() }
    
    private func updateClueView() {
        let shouldShowPlaceholder = clueTextView.text.isEmpty
        clueTextView.textColor = shouldShowPlaceholder ? UIColor.lightGray : Color.Hunter.black
        if shouldShowPlaceholder { clueTextView.text = placeholderClue }
    }

    func clearImage() { goalImageView.image = nil }
    func setImage(_ image: UIImage) { goalImageView.image = image }
    
    #warning("Missing Handling!")
    func handle(_ error: Error) {
        print("We have this issue: \(error)")
    }
    
    private func setInitialUI(for button: HunterButton, isActive: Bool = true) {
        button.isEnabled = isActive
        button.applyBorders()
    }
}

extension CreateGameViewController: UITextViewDelegate {
    func textViewDidBeginEditing(_ textView: UITextView) {
        guard textView.text ?? "" == placeholderClue else { return }
        textView.text = nil
        textView.textColor = Color.Hunter.black
    }
    func textViewDidEndEditing(_ textView: UITextView) { updateClueView() }
}
