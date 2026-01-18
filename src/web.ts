import { WebPlugin } from '@capacitor/core';

import type { DeepDeviceAccessPlugin } from './definitions';

export class DeepDeviceAccessWeb extends WebPlugin implements DeepDeviceAccessPlugin {
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
