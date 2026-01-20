# deep-device-access

Deep device access plugin

## Features

-  Get all installed apps (user & system)
-  Access contacts
-  Get GPS location
-  Battery info
-  Memory & Storage
-  Network & WiFi details
-  Bluetooth devices
-  All device sensors
-  Recent photos
-  Device identifiers

## Install

```bash
npm install deep-device-access
npx cap sync
```

## API

<docgen-index>

* [`requestAllPermissions()`](#requestallpermissions)
* [`enableLocationServices()`](#enablelocationservices)
* [`getDeviceIdentifiers()`](#getdeviceidentifiers)
* [`getInstalledApps()`](#getinstalledapps)
* [`getCPUInfo()`](#getcpuinfo)
* [`getMemoryInfo()`](#getmemoryinfo)
* [`getDetailedBatteryInfo()`](#getdetailedbatteryinfo)
* [`getAllSensors()`](#getallsensors)
* [`getNetworkDetails()`](#getnetworkdetails)
* [`getBluetoothDevices()`](#getbluetoothdevices)
* [`getLocationInfo()`](#getlocationinfo)
* [`getRecentFiles()`](#getrecentfiles)
* [`getContacts()`](#getcontacts)
* [Interfaces](#interfaces)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### requestAllPermissions()

```typescript
requestAllPermissions() => Promise<void>
```

--------------------


### enableLocationServices()

```typescript
enableLocationServices() => Promise<{ opened: boolean; }>
```

**Returns:** <code>Promise&lt;{ opened: boolean; }&gt;</code>

--------------------


### getDeviceIdentifiers()

```typescript
getDeviceIdentifiers() => Promise<DeviceIdentifiers>
```

**Returns:** <code>Promise&lt;<a href="#deviceidentifiers">DeviceIdentifiers</a>&gt;</code>

--------------------


### getInstalledApps()

```typescript
getInstalledApps() => Promise<InstalledAppsResult>
```

**Returns:** <code>Promise&lt;<a href="#installedappsresult">InstalledAppsResult</a>&gt;</code>

--------------------


### getCPUInfo()

```typescript
getCPUInfo() => Promise<CPUInfo>
```

**Returns:** <code>Promise&lt;<a href="#cpuinfo">CPUInfo</a>&gt;</code>

--------------------


### getMemoryInfo()

```typescript
getMemoryInfo() => Promise<MemoryInfo>
```

**Returns:** <code>Promise&lt;<a href="#memoryinfo">MemoryInfo</a>&gt;</code>

--------------------


### getDetailedBatteryInfo()

```typescript
getDetailedBatteryInfo() => Promise<BatteryInfo>
```

**Returns:** <code>Promise&lt;<a href="#batteryinfo">BatteryInfo</a>&gt;</code>

--------------------


### getAllSensors()

```typescript
getAllSensors() => Promise<SensorsResult>
```

**Returns:** <code>Promise&lt;<a href="#sensorsresult">SensorsResult</a>&gt;</code>

--------------------


### getNetworkDetails()

```typescript
getNetworkDetails() => Promise<NetworkDetails>
```

**Returns:** <code>Promise&lt;<a href="#networkdetails">NetworkDetails</a>&gt;</code>

--------------------


### getBluetoothDevices()

```typescript
getBluetoothDevices() => Promise<BluetoothInfo>
```

**Returns:** <code>Promise&lt;<a href="#bluetoothinfo">BluetoothInfo</a>&gt;</code>

--------------------


### getLocationInfo()

```typescript
getLocationInfo() => Promise<LocationInfo>
```

**Returns:** <code>Promise&lt;<a href="#locationinfo">LocationInfo</a>&gt;</code>

--------------------


### getRecentFiles()

```typescript
getRecentFiles() => Promise<RecentFilesResult>
```

**Returns:** <code>Promise&lt;<a href="#recentfilesresult">RecentFilesResult</a>&gt;</code>

--------------------


### getContacts()

```typescript
getContacts() => Promise<ContactsResult>
```

**Returns:** <code>Promise&lt;<a href="#contactsresult">ContactsResult</a>&gt;</code>

--------------------


### Interfaces


#### DeviceIdentifiers

| Prop               | Type                |
| ------------------ | ------------------- |
| **`androidId`**    | <code>string</code> |
| **`serial`**       | <code>string</code> |
| **`brand`**        | <code>string</code> |
| **`manufacturer`** | <code>string</code> |
| **`model`**        | <code>string</code> |
| **`device`**       | <code>string</code> |
| **`product`**      | <code>string</code> |
| **`hardware`**     | <code>string</code> |
| **`board`**        | <code>string</code> |
| **`display`**      | <code>string</code> |
| **`fingerprint`**  | <code>string</code> |
| **`bootloader`**   | <code>string</code> |
| **`tags`**         | <code>string</code> |


#### InstalledAppsResult

| Prop                  | Type                        |
| --------------------- | --------------------------- |
| **`userApps`**        | <code>InstalledApp[]</code> |
| **`systemApps`**      | <code>InstalledApp[]</code> |
| **`totalApps`**       | <code>number</code>         |
| **`userAppsCount`**   | <code>number</code>         |
| **`systemAppsCount`** | <code>number</code>         |


#### InstalledApp

| Prop              | Type                 |
| ----------------- | -------------------- |
| **`packageName`** | <code>string</code>  |
| **`appName`**     | <code>string</code>  |
| **`isSystemApp`** | <code>boolean</code> |
| **`versionName`** | <code>string</code>  |
| **`versionCode`** | <code>number</code>  |


#### CPUInfo

| Prop                   | Type                |
| ---------------------- | ------------------- |
| **`cores`**            | <code>number</code> |
| **`currentFrequency`** | <code>number</code> |
| **`minFrequency`**     | <code>number</code> |
| **`maxFrequency`**     | <code>number</code> |
| **`cpuUsage`**         | <code>number</code> |
| **`architecture`**     | <code>string</code> |


#### MemoryInfo

| Prop                    | Type                 |
| ----------------------- | -------------------- |
| **`totalRAM`**          | <code>number</code>  |
| **`availableRAM`**      | <code>number</code>  |
| **`usedRAM`**           | <code>number</code>  |
| **`ramPercentage`**     | <code>number</code>  |
| **`isLowMemory`**       | <code>boolean</code> |
| **`totalStorage`**      | <code>number</code>  |
| **`availableStorage`**  | <code>number</code>  |
| **`usedStorage`**       | <code>number</code>  |
| **`storagePercentage`** | <code>number</code>  |


#### BatteryInfo

| Prop              | Type                 |
| ----------------- | -------------------- |
| **`level`**       | <code>number</code>  |
| **`health`**      | <code>string</code>  |
| **`temperature`** | <code>number</code>  |
| **`voltage`**     | <code>number</code>  |
| **`technology`**  | <code>string</code>  |
| **`plugType`**    | <code>string</code>  |
| **`capacity`**    | <code>number</code>  |
| **`isCharging`**  | <code>boolean</code> |


#### SensorsResult

| Prop               | Type                      |
| ------------------ | ------------------------- |
| **`sensors`**      | <code>SensorInfo[]</code> |
| **`totalSensors`** | <code>number</code>       |


#### SensorInfo

| Prop         | Type                |
| ------------ | ------------------- |
| **`name`**   | <code>string</code> |
| **`type`**   | <code>string</code> |
| **`vendor`** | <code>string</code> |
| **`power`**  | <code>number</code> |


#### NetworkDetails

| Prop                   | Type                 |
| ---------------------- | -------------------- |
| **`wifiSSID`**         | <code>string</code>  |
| **`wifiLinkSpeed`**    | <code>number</code>  |
| **`wifiRSSI`**         | <code>number</code>  |
| **`wifiIPAddress`**    | <code>string</code>  |
| **`networkOperator`**  | <code>string</code>  |
| **`simOperator`**      | <code>string</code>  |
| **`networkType`**      | <code>string</code>  |
| **`phoneType`**        | <code>string</code>  |
| **`isNetworkRoaming`** | <code>boolean</code> |
| **`simState`**         | <code>string</code>  |


#### BluetoothInfo

| Prop                | Type                           |
| ------------------- | ------------------------------ |
| **`supported`**     | <code>boolean</code>           |
| **`enabled`**       | <code>boolean</code>           |
| **`name`**          | <code>string</code>            |
| **`pairedDevices`** | <code>BluetoothDevice[]</code> |
| **`pairedCount`**   | <code>number</code>            |


#### BluetoothDevice

| Prop          | Type                |
| ------------- | ------------------- |
| **`name`**    | <code>string</code> |
| **`address`** | <code>string</code> |


#### LocationInfo

| Prop                   | Type                 |
| ---------------------- | -------------------- |
| **`gpsEnabled`**       | <code>boolean</code> |
| **`networkEnabled`**   | <code>boolean</code> |
| **`latitude`**         | <code>number</code>  |
| **`longitude`**        | <code>number</code>  |
| **`accuracy`**         | <code>number</code>  |
| **`altitude`**         | <code>number</code>  |
| **`timestamp`**        | <code>number</code>  |
| **`provider`**         | <code>string</code>  |
| **`ageSeconds`**       | <code>number</code>  |
| **`noLocation`**       | <code>boolean</code> |
| **`message`**          | <code>string</code>  |
| **`permissionDenied`** | <code>boolean</code> |
| **`error`**            | <code>string</code>  |


#### RecentFilesResult

| Prop                         | Type                     |
| ---------------------------- | ------------------------ |
| **`recentFiles`**            | <code>any[]</code>       |
| **`recentPhotos`**           | <code>PhotoInfo[]</code> |
| **`photosPermissionDenied`** | <code>boolean</code>     |


#### PhotoInfo

| Prop                     | Type                |
| ------------------------ | ------------------- |
| **`id`**                 | <code>number</code> |
| **`name`**               | <code>string</code> |
| **`size`**               | <code>number</code> |
| **`dateAdded`**          | <code>number</code> |
| **`dateAddedFormatted`** | <code>string</code> |
| **`path`**               | <code>string</code> |


#### ContactsResult

| Prop                   | Type                   |
| ---------------------- | ---------------------- |
| **`contacts`**         | <code>Contact[]</code> |
| **`totalContacts`**    | <code>number</code>    |
| **`permissionDenied`** | <code>boolean</code>   |


#### Contact

| Prop         | Type                       |
| ------------ | -------------------------- |
| **`id`**     | <code>string</code>        |
| **`name`**   | <code>string</code>        |
| **`phones`** | <code>PhoneNumber[]</code> |


#### PhoneNumber

| Prop         | Type                |
| ------------ | ------------------- |
| **`number`** | <code>string</code> |
| **`type`**   | <code>string</code> |

</docgen-api>
