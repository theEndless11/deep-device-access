export interface DeepDeviceAccessPlugin {
  requestAllPermissions(): Promise<void>;
  enableLocationServices(): Promise<{ opened: boolean }>;
  getDeviceIdentifiers(): Promise<DeviceIdentifiers>;
  getInstalledApps(): Promise<InstalledAppsResult>;
  getCPUInfo(): Promise<CPUInfo>;
  getMemoryInfo(): Promise<MemoryInfo>;
  getDetailedBatteryInfo(): Promise<BatteryInfo>;
  getAllSensors(): Promise<SensorsResult>;
  getNetworkDetails(): Promise<NetworkDetails>;
  getBluetoothDevices(): Promise<BluetoothInfo>;
  getLocationInfo(): Promise<LocationInfo>;
  getRecentFiles(): Promise<RecentFilesResult>;
  getContacts(): Promise<ContactsResult>;
}

export interface DeviceIdentifiers {
  androidId: string;
  serial: string;
  brand: string;
  manufacturer: string;
  model: string;
  device: string;
  product: string;
  hardware: string;
  board: string;
  display: string;
  fingerprint: string;
  bootloader: string;
  tags: string;
}

export interface InstalledAppsResult {
  userApps: InstalledApp[];
  systemApps: InstalledApp[];
  totalApps: number;
  userAppsCount: number;
  systemAppsCount: number;
}

export interface InstalledApp {
  packageName: string;
  appName: string;
  isSystemApp: boolean;
  versionName: string;
  versionCode: number;
}

export interface CPUInfo {
  cores: number;
  currentFrequency: number;
  minFrequency: number;
  maxFrequency: number;
  cpuUsage: number;
  architecture: string;
}

export interface MemoryInfo {
  totalRAM: number;
  availableRAM: number;
  usedRAM: number;
  ramPercentage: number;
  isLowMemory: boolean;
  totalStorage: number;
  availableStorage: number;
  usedStorage: number;
  storagePercentage: number;
}

export interface BatteryInfo {
  level: number;
  health: string;
  temperature: number;
  voltage: number;
  technology: string;
  plugType: string;
  capacity: number;
  isCharging: boolean;
}

export interface LocationInfo {
  gpsEnabled: boolean;
  networkEnabled: boolean;
  latitude?: number;
  longitude?: number;
  accuracy?: number;
  altitude?: number;
  timestamp?: number;
  provider?: string;
  ageSeconds?: number;
  noLocation?: boolean;
  message?: string;
  permissionDenied?: boolean;
  error?: string;
}

export interface RecentFilesResult {
  recentFiles: any[];
  recentPhotos: PhotoInfo[];
  photosPermissionDenied?: boolean;
}

export interface PhotoInfo {
  id: number;
  name: string;
  size: number;
  dateAdded: number;
  dateAddedFormatted: string;
  path: string;
}

export interface ContactsResult {
  contacts: Contact[];
  totalContacts: number;
  permissionDenied?: boolean;
}

export interface Contact {
  id: string;
  name: string;
  phones: PhoneNumber[];
}

export interface PhoneNumber {
  number: string;
  type: string;
}

export interface SensorsResult {
  sensors: SensorInfo[];
  totalSensors: number;
}

export interface SensorInfo {
  name: string;
  type: string;
  vendor: string;
  power: number;
}

export interface NetworkDetails {
  wifiSSID: string;
  wifiLinkSpeed: number;
  wifiRSSI: number;
  wifiIPAddress: string;
  networkOperator: string;
  simOperator?: string;
  networkType: string;
  phoneType?: string;
  isNetworkRoaming?: boolean;
  simState?: string;
}

export interface BluetoothInfo {
  supported: boolean;
  enabled: boolean;
  name: string;
  pairedDevices: BluetoothDevice[];
  pairedCount: number;
}

export interface BluetoothDevice {
  name: string;
  address: string;
}