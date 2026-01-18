// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "DeepDeviceAccess",
    platforms: [.iOS(.v15)],
    products: [
        .library(
            name: "DeepDeviceAccess",
            targets: ["DeepDeviceAccessPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "8.0.0")
    ],
    targets: [
        .target(
            name: "DeepDeviceAccessPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/DeepDeviceAccessPlugin"),
        .testTarget(
            name: "DeepDeviceAccessPluginTests",
            dependencies: ["DeepDeviceAccessPlugin"],
            path: "ios/Tests/DeepDeviceAccessPluginTests")
    ]
)