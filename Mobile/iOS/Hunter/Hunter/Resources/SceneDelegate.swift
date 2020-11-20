import Firebase
import FirebaseAuth
import IQKeyboardManagerSwift
import UIKit

class SceneDelegate: UIResponder, UIWindowSceneDelegate {

    var window: UIWindow?
    var listener: Any?

    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        // Use this method to optionally configure and attach the UIWindow `window` to the provided UIWindowScene `scene`.
        // If using a storyboard, the `window` property will automatically be initialized and attached to the scene.
        // This delegate does not imply the connecting scene or session are new (see `application:configurationForConnectingSceneSession` instead).
        do {
            let window = try createWindow(from: scene)
            let navigation = UINavigationController()
            window.rootViewController = navigation
            window.makeKeyAndVisible()
            setupIQKeyboardManager()
            setupFirebase()
            start(navigation)
            self.window = window
        } catch {
            #warning("We need to handle this better!")
            print("Error on creating a window!")
        }
    }
    
    private func start<Navigation: UINavigationController>(_ nav: Navigation) {
        let client = AlamofireClient()
        let module = Module(Module.Dependencies(navigation: nav,
                                                networking: client))
        #warning("We need to move this piece of code to Authentication Flow")
        listener = Auth.auth().addStateDidChangeListener { (_, possibleUser) in
            possibleUser?.getIDToken(completion: { (token, _) in
                guard let token = token else { return }
                client.setToken(token)
            })
        }
        module.launch()
    }

    func sceneDidDisconnect(_ scene: UIScene) {
        // Called as the scene is being released by the system.
        // This occurs shortly after the scene enters the background, or when its session is discarded.
        // Release any resources associated with this scene that can be re-created the next time the scene connects.
        // The scene may re-connect later, as its session was not necessarily discarded (see `application:didDiscardSceneSessions` instead).
    }

    func sceneDidBecomeActive(_ scene: UIScene) {
        // Called when the scene has moved from an inactive state to an active state.
        // Use this method to restart any tasks that were paused (or not yet started) when the scene was inactive.
    }

    func sceneWillResignActive(_ scene: UIScene) {
        // Called when the scene will move from an active state to an inactive state.
        // This may occur due to temporary interruptions (ex. an incoming phone call).
    }

    func sceneWillEnterForeground(_ scene: UIScene) {
        // Called as the scene transitions from the background to the foreground.
        // Use this method to undo the changes made on entering the background.
    }

    func sceneDidEnterBackground(_ scene: UIScene) {
        // Called as the scene transitions from the foreground to the background.
        // Use this method to save data, release shared resources, and store enough scene-specific state information
        // to restore the scene back to its current state.
    }
}

// MARK: - Create Window
fileprivate extension SceneDelegate {
    enum WindowError: Error { case cantParseToWindowScene }
    func createWindow(from scene: UIScene) throws -> UIWindow {
        guard let windowScene = (scene as? UIWindowScene) else { throw WindowError.cantParseToWindowScene }
        let window = UIWindow(frame: windowScene.coordinateSpace.bounds)
        window.windowScene = windowScene
        return window
    }
}

// MARK: - Use IQKeyboardManagerSwift
fileprivate extension SceneDelegate {
    func setupIQKeyboardManager() {
        IQKeyboardManager.shared.enable = true
        IQKeyboardManager.shared.shouldResignOnTouchOutside = true
        IQKeyboardManager.shared.enableAutoToolbar = true
        IQKeyboardManager.shared.toolbarManageBehaviour = .byPosition
        IQKeyboardManager.shared.previousNextDisplayMode = .alwaysHide
    }
}

// MARK: - Use Firebase
fileprivate extension SceneDelegate {
    func setupFirebase() {
        FirebaseApp.configure()
    }
}
