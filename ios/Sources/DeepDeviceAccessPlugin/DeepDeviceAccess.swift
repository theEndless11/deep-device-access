import Foundation

@objc public class DeepDeviceAccess: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
