package android.bluetooth.le;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/*  JADX ERROR: NullPointerException in pass: ReSugarCode
    java.lang.NullPointerException
    	at jadx.core.dex.visitors.ReSugarCode.initClsEnumMap(ReSugarCode.java:159)
    	at jadx.core.dex.visitors.ReSugarCode.visit(ReSugarCode.java:44)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:12)
    	at jadx.core.ProcessClass.process(ProcessClass.java:32)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
    	at java.lang.Iterable.forEach(Iterable.java:75)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
    	at jadx.core.ProcessClass.process(ProcessClass.java:37)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
    */
/*  JADX ERROR: NullPointerException in pass: ExtractFieldInit
    java.lang.NullPointerException
    	at jadx.core.dex.visitors.ExtractFieldInit.checkStaticFieldsInit(ExtractFieldInit.java:58)
    	at jadx.core.dex.visitors.ExtractFieldInit.visit(ExtractFieldInit.java:44)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:12)
    	at jadx.core.ProcessClass.process(ProcessClass.java:32)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
    	at java.lang.Iterable.forEach(Iterable.java:75)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
    	at jadx.core.ProcessClass.process(ProcessClass.java:37)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
    */
public final class ScanFilter implements Parcelable {
    public static final Creator<ScanFilter> CREATOR = null;
    private static final ScanFilter EMPTY = null;
    private final String mDeviceAddress;
    private final String mDeviceName;
    private final byte[] mManufacturerData;
    private final byte[] mManufacturerDataMask;
    private final int mManufacturerId;
    private final byte[] mServiceData;
    private final byte[] mServiceDataMask;
    private final ParcelUuid mServiceDataUuid;
    private final ParcelUuid mServiceUuid;
    private final ParcelUuid mServiceUuidMask;

    public static final class Builder {
        private String mDeviceAddress;
        private String mDeviceName;
        private byte[] mManufacturerData;
        private byte[] mManufacturerDataMask;
        private int mManufacturerId = -1;
        private byte[] mServiceData;
        private byte[] mServiceDataMask;
        private ParcelUuid mServiceDataUuid;
        private ParcelUuid mServiceUuid;
        private ParcelUuid mUuidMask;

        public Builder setDeviceName(String deviceName) {
            this.mDeviceName = deviceName;
            return this;
        }

        public Builder setDeviceAddress(String deviceAddress) {
            if (deviceAddress == null || BluetoothAdapter.checkBluetoothAddress(deviceAddress)) {
                this.mDeviceAddress = deviceAddress;
                return this;
            }
            throw new IllegalArgumentException("invalid device address " + deviceAddress);
        }

        public Builder setServiceUuid(ParcelUuid serviceUuid) {
            this.mServiceUuid = serviceUuid;
            this.mUuidMask = null;
            return this;
        }

        public Builder setServiceUuid(ParcelUuid serviceUuid, ParcelUuid uuidMask) {
            if (this.mUuidMask == null || this.mServiceUuid != null) {
                this.mServiceUuid = serviceUuid;
                this.mUuidMask = uuidMask;
                return this;
            }
            throw new IllegalArgumentException("uuid is null while uuidMask is not null!");
        }

        public Builder setServiceData(ParcelUuid serviceDataUuid, byte[] serviceData) {
            if (serviceDataUuid == null) {
                throw new IllegalArgumentException("serviceDataUuid is null");
            }
            this.mServiceDataUuid = serviceDataUuid;
            this.mServiceData = serviceData;
            this.mServiceDataMask = null;
            return this;
        }

        public Builder setServiceData(ParcelUuid serviceDataUuid, byte[] serviceData, byte[] serviceDataMask) {
            if (serviceDataUuid == null) {
                throw new IllegalArgumentException("serviceDataUuid is null");
            }
            if (this.mServiceDataMask != null) {
                if (this.mServiceData == null) {
                    throw new IllegalArgumentException("serviceData is null while serviceDataMask is not null");
                } else if (this.mServiceData.length != this.mServiceDataMask.length) {
                    throw new IllegalArgumentException("size mismatch for service data and service data mask");
                }
            }
            this.mServiceDataUuid = serviceDataUuid;
            this.mServiceData = serviceData;
            this.mServiceDataMask = serviceDataMask;
            return this;
        }

        public Builder setManufacturerData(int manufacturerId, byte[] manufacturerData) {
            if (manufacturerData == null || manufacturerId >= 0) {
                this.mManufacturerId = manufacturerId;
                this.mManufacturerData = manufacturerData;
                this.mManufacturerDataMask = null;
                return this;
            }
            throw new IllegalArgumentException("invalid manufacture id");
        }

        public Builder setManufacturerData(int manufacturerId, byte[] manufacturerData, byte[] manufacturerDataMask) {
            if (manufacturerData == null || manufacturerId >= 0) {
                if (this.mManufacturerDataMask != null) {
                    if (this.mManufacturerData == null) {
                        throw new IllegalArgumentException("manufacturerData is null while manufacturerDataMask is not null");
                    } else if (this.mManufacturerData.length != this.mManufacturerDataMask.length) {
                        throw new IllegalArgumentException("size mismatch for manufacturerData and manufacturerDataMask");
                    }
                }
                this.mManufacturerId = manufacturerId;
                this.mManufacturerData = manufacturerData;
                this.mManufacturerDataMask = manufacturerDataMask;
                return this;
            }
            throw new IllegalArgumentException("invalid manufacture id");
        }

        public ScanFilter build() {
            return new ScanFilter(this.mDeviceName, this.mDeviceAddress, this.mServiceUuid, this.mUuidMask, this.mServiceDataUuid, this.mServiceData, this.mServiceDataMask, this.mManufacturerId, this.mManufacturerData, this.mManufacturerDataMask, null);
        }
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: bogus opcode: 00e9 in method: android.bluetooth.le.ScanFilter.<clinit>():void, dex: 
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:118)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:248)
        	at jadx.core.ProcessClass.process(ProcessClass.java:29)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        Caused by: java.lang.IllegalArgumentException: bogus opcode: 00e9
        	at com.android.dx.io.OpcodeInfo.get(OpcodeInfo.java:1227)
        	at com.android.dx.io.OpcodeInfo.getName(OpcodeInfo.java:1234)
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:581)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:74)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:104)
        	... 9 more
        */
    static {
        /*
        // Can't load method instructions: Load method exception: bogus opcode: 00e9 in method: android.bluetooth.le.ScanFilter.<clinit>():void, dex: 
        */
        throw new UnsupportedOperationException("Method not decompiled: android.bluetooth.le.ScanFilter.<clinit>():void");
    }

    /* synthetic */ ScanFilter(String name, String deviceAddress, ParcelUuid uuid, ParcelUuid uuidMask, ParcelUuid serviceDataUuid, byte[] serviceData, byte[] serviceDataMask, int manufacturerId, byte[] manufacturerData, byte[] manufacturerDataMask, ScanFilter scanFilter) {
        this(name, deviceAddress, uuid, uuidMask, serviceDataUuid, serviceData, serviceDataMask, manufacturerId, manufacturerData, manufacturerDataMask);
    }

    private ScanFilter(String name, String deviceAddress, ParcelUuid uuid, ParcelUuid uuidMask, ParcelUuid serviceDataUuid, byte[] serviceData, byte[] serviceDataMask, int manufacturerId, byte[] manufacturerData, byte[] manufacturerDataMask) {
        this.mDeviceName = name;
        this.mServiceUuid = uuid;
        this.mServiceUuidMask = uuidMask;
        this.mDeviceAddress = deviceAddress;
        this.mServiceDataUuid = serviceDataUuid;
        this.mServiceData = serviceData;
        this.mServiceDataMask = serviceDataMask;
        this.mManufacturerId = manufacturerId;
        this.mManufacturerData = manufacturerData;
        this.mManufacturerDataMask = manufacturerDataMask;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 0;
        if (this.mDeviceName == null) {
            i = 0;
        } else {
            i = 1;
        }
        dest.writeInt(i);
        if (this.mDeviceName != null) {
            dest.writeString(this.mDeviceName);
        }
        if (this.mDeviceAddress == null) {
            i = 0;
        } else {
            i = 1;
        }
        dest.writeInt(i);
        if (this.mDeviceAddress != null) {
            dest.writeString(this.mDeviceAddress);
        }
        if (this.mServiceUuid == null) {
            i = 0;
        } else {
            i = 1;
        }
        dest.writeInt(i);
        if (this.mServiceUuid != null) {
            dest.writeParcelable(this.mServiceUuid, flags);
            if (this.mServiceUuidMask == null) {
                i = 0;
            } else {
                i = 1;
            }
            dest.writeInt(i);
            if (this.mServiceUuidMask != null) {
                dest.writeParcelable(this.mServiceUuidMask, flags);
            }
        }
        if (this.mServiceDataUuid == null) {
            i = 0;
        } else {
            i = 1;
        }
        dest.writeInt(i);
        if (this.mServiceDataUuid != null) {
            dest.writeParcelable(this.mServiceDataUuid, flags);
            if (this.mServiceData == null) {
                i = 0;
            } else {
                i = 1;
            }
            dest.writeInt(i);
            if (this.mServiceData != null) {
                dest.writeInt(this.mServiceData.length);
                dest.writeByteArray(this.mServiceData);
                if (this.mServiceDataMask == null) {
                    i = 0;
                } else {
                    i = 1;
                }
                dest.writeInt(i);
                if (this.mServiceDataMask != null) {
                    dest.writeInt(this.mServiceDataMask.length);
                    dest.writeByteArray(this.mServiceDataMask);
                }
            }
        }
        dest.writeInt(this.mManufacturerId);
        if (this.mManufacturerData == null) {
            i = 0;
        } else {
            i = 1;
        }
        dest.writeInt(i);
        if (this.mManufacturerData != null) {
            dest.writeInt(this.mManufacturerData.length);
            dest.writeByteArray(this.mManufacturerData);
            if (this.mManufacturerDataMask != null) {
                i2 = 1;
            }
            dest.writeInt(i2);
            if (this.mManufacturerDataMask != null) {
                dest.writeInt(this.mManufacturerDataMask.length);
                dest.writeByteArray(this.mManufacturerDataMask);
            }
        }
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public ParcelUuid getServiceUuid() {
        return this.mServiceUuid;
    }

    public ParcelUuid getServiceUuidMask() {
        return this.mServiceUuidMask;
    }

    public String getDeviceAddress() {
        return this.mDeviceAddress;
    }

    public byte[] getServiceData() {
        return this.mServiceData;
    }

    public byte[] getServiceDataMask() {
        return this.mServiceDataMask;
    }

    public ParcelUuid getServiceDataUuid() {
        return this.mServiceDataUuid;
    }

    public int getManufacturerId() {
        return this.mManufacturerId;
    }

    public byte[] getManufacturerData() {
        return this.mManufacturerData;
    }

    public byte[] getManufacturerDataMask() {
        return this.mManufacturerDataMask;
    }

    public boolean matches(ScanResult scanResult) {
        if (scanResult == null) {
            return false;
        }
        BluetoothDevice device = scanResult.getDevice();
        if (this.mDeviceAddress != null && (device == null || !this.mDeviceAddress.equals(device.getAddress()))) {
            return false;
        }
        ScanRecord scanRecord = scanResult.getScanRecord();
        if (scanRecord == null && (this.mDeviceName != null || this.mServiceUuid != null || this.mManufacturerData != null || this.mServiceData != null)) {
            return false;
        }
        if (this.mDeviceName != null && !this.mDeviceName.equals(scanRecord.getDeviceName())) {
            return false;
        }
        if (this.mServiceUuid != null && !matchesServiceUuids(this.mServiceUuid, this.mServiceUuidMask, scanRecord.getServiceUuids())) {
            return false;
        }
        if (this.mServiceDataUuid != null && !matchesPartialData(this.mServiceData, this.mServiceDataMask, scanRecord.getServiceData(this.mServiceDataUuid))) {
            return false;
        }
        if (this.mManufacturerId < 0 || matchesPartialData(this.mManufacturerData, this.mManufacturerDataMask, scanRecord.getManufacturerSpecificData(this.mManufacturerId))) {
            return true;
        }
        return false;
    }

    private boolean matchesServiceUuids(ParcelUuid uuid, ParcelUuid parcelUuidMask, List<ParcelUuid> uuids) {
        if (uuid == null) {
            return true;
        }
        if (uuids == null) {
            return false;
        }
        for (ParcelUuid parcelUuid : uuids) {
            if (matchesServiceUuid(uuid.getUuid(), parcelUuidMask == null ? null : parcelUuidMask.getUuid(), parcelUuid.getUuid())) {
                return true;
            }
        }
        return false;
    }

    private boolean matchesServiceUuid(UUID uuid, UUID mask, UUID data) {
        boolean z = false;
        if (mask == null) {
            return uuid.equals(data);
        }
        if ((uuid.getLeastSignificantBits() & mask.getLeastSignificantBits()) != (data.getLeastSignificantBits() & mask.getLeastSignificantBits())) {
            return false;
        }
        if ((uuid.getMostSignificantBits() & mask.getMostSignificantBits()) == (data.getMostSignificantBits() & mask.getMostSignificantBits())) {
            z = true;
        }
        return z;
    }

    private boolean matchesPartialData(byte[] data, byte[] dataMask, byte[] parsedData) {
        if (parsedData == null || parsedData.length < data.length) {
            return false;
        }
        int i;
        if (dataMask == null) {
            for (i = 0; i < data.length; i++) {
                if (parsedData[i] != data[i]) {
                    return false;
                }
            }
            return true;
        }
        for (i = 0; i < data.length; i++) {
            if ((dataMask[i] & parsedData[i]) != (dataMask[i] & data[i])) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return "BluetoothLeScanFilter [mDeviceName=" + this.mDeviceName + ", mDeviceAddress=" + this.mDeviceAddress + ", mUuid=" + this.mServiceUuid + ", mUuidMask=" + this.mServiceUuidMask + ", mServiceDataUuid=" + Objects.toString(this.mServiceDataUuid) + ", mServiceData=" + Arrays.toString(this.mServiceData) + ", mServiceDataMask=" + Arrays.toString(this.mServiceDataMask) + ", mManufacturerId=" + this.mManufacturerId + ", mManufacturerData=" + Arrays.toString(this.mManufacturerData) + ", mManufacturerDataMask=" + Arrays.toString(this.mManufacturerDataMask) + "]";
    }

    public int hashCode() {
        Object[] objArr = new Object[10];
        objArr[0] = this.mDeviceName;
        objArr[1] = this.mDeviceAddress;
        objArr[2] = Integer.valueOf(this.mManufacturerId);
        objArr[3] = Integer.valueOf(Arrays.hashCode(this.mManufacturerData));
        objArr[4] = Integer.valueOf(Arrays.hashCode(this.mManufacturerDataMask));
        objArr[5] = this.mServiceDataUuid;
        objArr[6] = Integer.valueOf(Arrays.hashCode(this.mServiceData));
        objArr[7] = Integer.valueOf(Arrays.hashCode(this.mServiceDataMask));
        objArr[8] = this.mServiceUuid;
        objArr[9] = this.mServiceUuidMask;
        return Objects.hash(objArr);
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ScanFilter other = (ScanFilter) obj;
        if (Objects.equals(this.mDeviceName, other.mDeviceName) && Objects.equals(this.mDeviceAddress, other.mDeviceAddress) && this.mManufacturerId == other.mManufacturerId && Objects.deepEquals(this.mManufacturerData, other.mManufacturerData) && Objects.deepEquals(this.mManufacturerDataMask, other.mManufacturerDataMask) && Objects.equals(this.mServiceDataUuid, other.mServiceDataUuid) && Objects.deepEquals(this.mServiceData, other.mServiceData) && Objects.deepEquals(this.mServiceDataMask, other.mServiceDataMask) && Objects.equals(this.mServiceUuid, other.mServiceUuid)) {
            z = Objects.equals(this.mServiceUuidMask, other.mServiceUuidMask);
        }
        return z;
    }

    public boolean isAllFieldsEmpty() {
        return EMPTY.equals(this);
    }
}
