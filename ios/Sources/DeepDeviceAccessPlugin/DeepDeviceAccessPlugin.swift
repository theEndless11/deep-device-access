import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(DeepDeviceAccessPlugin)
public class DeepDeviceAccessPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "DeepDeviceAccessPlugin"
    public let jsName = "DeepDeviceAccess"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "echo", returnType: CAPPluginReturnPromise)
    ]
    private let implementation = DeepDeviceAccess()

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.resolve([
            "value": implementation.echo(value)
        ])
    }
}
