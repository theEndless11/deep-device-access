import { WebPlugin } from '@capacitor/core';

import type {
  DeepDeviceAccessPlugin,
  DeviceIdentifiers,
  InstalledAppsResult,
  CPUInfo,
  MemoryInfo,
  BatteryInfo,
  SensorsResult,
  NetworkDetails,
  BluetoothInfo,
  LocationInfo,
  RecentFilesResult,
  ContactsResult,
} from './definitions';

export class DeepDeviceAccessWeb extends WebPlugin implements DeepDeviceAccessPlugin {
  async requestAllPermissions(): Promise<void> {
    throw this.unimplemented('Not implemented on web.');
  }

  async enableLocationServices(): Promise<{ opened: boolean }> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getDeviceIdentifiers(): Promise<DeviceIdentifiers> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getInstalledApps(): Promise<InstalledAppsResult> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getCPUInfo(): Promise<CPUInfo> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getMemoryInfo(): Promise<MemoryInfo> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getDetailedBatteryInfo(): Promise<BatteryInfo> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getAllSensors(): Promise<SensorsResult> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getNetworkDetails(): Promise<NetworkDetails> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getBluetoothDevices(): Promise<BluetoothInfo> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getLocationInfo(): Promise<LocationInfo> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getRecentFiles(): Promise<RecentFilesResult> {
    throw this.unimplemented('Not implemented on web.');
  }

  async getContacts(): Promise<ContactsResult> {
    throw this.unimplemented('Not implemented on web.');
  }
}