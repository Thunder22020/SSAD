package ITP_Ass3;

import java.util.Scanner;

public class Main {
    // Variables
    static final int MIN_TEMPERATURE = 15;
    static final int BASIC_TEMPERATURE = 20;
    static final int MAX_TEMPERATURE = 30;
    static final int MIN_ANGLE = -60;
    static final int BASIC_ANGLE = 45;
    static final int MAX_ANGLE = 60;
    static final int DEVICE_AMOUNT = 10;
    static final int MIN_LIGHT_ID = 0;
    static final int MAX_LIGHT_ID = 3;
    static final int MIN_CAMERA_ID = 4;
    static final int MAX_CAMERA_ID = 5;
    static final int MIN_HEATER_ID = 6;
    static final int MAX_HEATER_ID = 9;
    static final int DEVICE_ATTR_INDEX = 3;
    static final int DEVICE_FIELDS_AMOUNT_ONE = 3;
    static final int DEVICE_FIELDS_AMOUNT_TWO = 4;
    /**
     * Function which returns true if string array contains string, else returns false
     * @param arr iterable string array
     * @param line string to check
     * @return true if contains, false if it does not
     */
    public static boolean contains(String[] arr, String line) {
        for (String el : arr) {
            if (el.equals(line)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether all chars in string are digits
     * @param a input string
     * @return true if all chars are digits, false if not
     */
    public static boolean validDigitString(String a) {
        for (char c : a.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether the command satisfies all conditions
     * @param arr string array of split by space command
     * @param numberOfFields number of fields of the command from the task
     * @return true if the command does not satisfy all conditions, false if it does
     */
    public static boolean incorrectCommandStructure(String[] arr, int numberOfFields) {
        return !((contains(arr, "Heater") || contains(arr, "Camera")
                || contains(arr, "Light")) && (arr.length == numberOfFields)
                && validDigitString(arr[2]));
    }

    /**
     * Checks whether the device id correlates with device name
     * @param deviceName device name
     * @param deviceId device id
     * @return true if device id correlates with device name, false if it does not
     */
    public static boolean deviceInSystem(String deviceName, int deviceId) {
        switch (deviceName) {
            case "Heater":
                if (MIN_HEATER_ID <= deviceId && deviceId <= MAX_HEATER_ID) {
                    return true;
                }
                break;
            case "Camera":
                if (MIN_CAMERA_ID <= deviceId && deviceId <= MAX_CAMERA_ID) {
                    return true;
                }
                break;
            case "Light":
                if (MIN_LIGHT_ID <= deviceId && deviceId <= MAX_LIGHT_ID) {
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * Checks whether the attribute of the command is in correct range
     * @param deviceName device name
     * @param value value of attribute
     * @return true if value is in range, false if it does not
     */
    public static boolean valueInRange(String deviceName, int value) {
        switch (deviceName) {
            case "Camera":
                if (MIN_ANGLE <= value && value <= MAX_ANGLE) {
                    return true;
                }
                break;
            case "Heater":
                if (MIN_TEMPERATURE <= value && value <= MAX_TEMPERATURE) {
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * Checks whether the attribute of the command is in correct range
     * @param deviceName device name
     * @param value value of attribute
     * @return true if value is in range, false if it does not
     */
    public static boolean valueInRange(String deviceName, String value) {
        switch (deviceName) {
            case "Brightness":
                if (contains(new String[]{"LOW", "MEDIUM", "HIGH"}, value)) {
                    return true;
                }
                break;
            case "Light":
                if (contains(new String[]{"WHITE", "YELLOW"}, value)) {
                    return true;
                }
                break;
            default:
                break;
        }
        return false;
    }

    /**
     * Implements "DisplayAllStatus" command
     * @param lineWords string array of split command
     * @param devices devices array
     * @return false if any mistake occurred, true if it can list all devices
     */
    public static boolean displayAllStatus(String[] lineWords, SmartDevice[] devices) {
        // Case of incorrect command
        if (lineWords.length > 1) {
            System.out.println("Invalid command");
            return false;
        }
        // Outputs statuses of all devices
        for (int i = 0; i < DEVICE_AMOUNT; i++) {
            System.out.println(devices[i].displayStatus());
        }
        return true;
    }
    /**
     * Implements "TurnOn" command
     * @param lineWords string array of split command
     * @param devices devices array
     * @return false if any mistake occurred, true if it can turn on the device
     */
    public static boolean turnOn(String[] lineWords, SmartDevice[] devices) {
        // Case of incorrect command
        if (incorrectCommandStructure(lineWords, DEVICE_FIELDS_AMOUNT_ONE)) {
            System.out.println("Invalid command");
            return false;
        }
        // Case of incorrect device number
        if (!deviceInSystem(lineWords[1], Integer.parseInt(lineWords[2]))) {
            System.out.println("The smart device was not found");
            return false;
        }
        if (devices[Integer.parseInt(lineWords[2])].getStatus() == SmartDevice.Status.ON) {
            System.out.printf("%s %d is already on\n", lineWords[1], Integer.parseInt(lineWords[2]));
            return false;
        }
        // Turning on the device
        devices[Integer.parseInt(lineWords[2])].turnOn();
        System.out.printf("%s %d is on\n", lineWords[1], Integer.parseInt(lineWords[2]));
        return true;
    }
    /**
     * Implements "TurnOff" command
     * @param lineWords string array of split command
     * @param devices devices array
     * @return false if any mistake occurred, true if it can turn off the device
     */
    public static boolean turnOff(String[] lineWords, SmartDevice[] devices) {
        // Case of incorrect command
        if (incorrectCommandStructure(lineWords, DEVICE_FIELDS_AMOUNT_ONE)) {
            System.out.println("Invalid command");
            return false;
        }
        // Case of incorrect device number
        if (!deviceInSystem(lineWords[1], Integer.parseInt(lineWords[2]))) {
            System.out.println("The smart device was not found");
            return false;
        }
        if (devices[Integer.parseInt(lineWords[2])].getStatus() == SmartDevice.Status.OFF) {
            System.out.printf("%s %d is already off\n", lineWords[1], Integer.parseInt(lineWords[2]));
            return false;
        }
        // Turning off the device
        devices[Integer.parseInt(lineWords[2])].turnOff();
        System.out.printf("%s %d is off\n", lineWords[1], Integer.parseInt(lineWords[2]));
        return true;
    }
    /**
     * Implements "StartCharging" command
     * @param lineWords string array of split command
     * @param devices devices array
     * @return false if any mistake occurred, true if it can start charging the device
     */
    public static boolean startCharging(String[] lineWords, SmartDevice[] devices) {
        // Case of incorrect command
        if (incorrectCommandStructure(lineWords, DEVICE_FIELDS_AMOUNT_ONE)) {
            System.out.println("Invalid command");
            return false;
        }
        // Case of incorrect device number
        if (!deviceInSystem(lineWords[1], Integer.parseInt(lineWords[2]))) {
            System.out.println("The smart device was not found");
            return false;
        }

        if (devices[Integer.parseInt(lineWords[2])] instanceof Chargeable) {
            if (((Chargeable) devices[Integer.parseInt(lineWords[2])]).isCharging()) {
                System.out.printf("%s %d is already charging\n",
                        lineWords[1], Integer.parseInt(lineWords[2]));
                return false;
            }
            // Starting charging the device
            ((Chargeable) devices[Integer.parseInt(lineWords[2])]).startCharging();
            System.out.printf("%s %d is charging\n", lineWords[1], Integer.parseInt(lineWords[2]));
        } else {
            System.out.printf("%s %d is not chargeable\n", lineWords[1], Integer.parseInt(lineWords[2]));
            return false;
        }
        return true;
    }
    /**
     * Implements "StopCharging" command
     * @param lineWords string array of split command
     * @param devices devices array
     * @return false if any mistake occurred, true if it can stop charging the device
     */
    public static boolean stopCharging(String[] lineWords, SmartDevice[] devices) {
        // Case of incorrect command
        if (incorrectCommandStructure(lineWords, DEVICE_FIELDS_AMOUNT_ONE)) {
            System.out.println("Invalid command");
            return false;
        }
        // Case of incorrect device number
        if (!deviceInSystem(lineWords[1], Integer.parseInt(lineWords[2]))) {
            System.out.println("The smart device was not found");
            return false;
        }

        if (devices[Integer.parseInt(lineWords[2])] instanceof Chargeable) {
            if (!((Chargeable) devices[Integer.parseInt(lineWords[2])]).isCharging()) {
                System.out.printf("%s %d is not charging\n", lineWords[1], Integer.parseInt(lineWords[2]));
                return false;
            }
            // Stopping charging the device
            ((Chargeable) devices[Integer.parseInt(lineWords[2])]).stopCharging();
            System.out.printf("%s %d stopped charging\n", lineWords[1], Integer.parseInt(lineWords[2]));
        } else {
            System.out.printf("%s %d is not chargeable\n", lineWords[1], Integer.parseInt(lineWords[2]));
            return false;
        }
        return true;
    }
    /**
     * Implements "SetTemperature" command
     * @param lineWords string array of split command
     * @param devices devices array
     * @return false if any mistake occurred, true if it can set the temperature of the device
     */
    public static boolean setTemperature(String[] lineWords, SmartDevice[] devices) {
        // Case of incorrect command
        if (incorrectCommandStructure(lineWords, DEVICE_FIELDS_AMOUNT_TWO)) {
            System.out.println("Invalid command");
            return false;
        }
        // Case of incorrect device number
        if (!deviceInSystem(lineWords[1], Integer.parseInt(lineWords[2]))) {
            System.out.println("The smart device was not found");
            return false;
        }

        if (!devices[Integer.parseInt(lineWords[2])].isOn()) {
            System.out.printf("You can't change the status of the %s %d while it is off\n",
                    lineWords[1], Integer.parseInt(lineWords[2]));
            return false;
        } else {
            switch (lineWords[1]) {
                case "Heater":
                    if (valueInRange("Heater", Integer.parseInt(lineWords[DEVICE_ATTR_INDEX]))) {
                        // Setting the temperature for Heater
                        ((Heater) devices[Integer.parseInt(lineWords[2])])
                                .setTemperature(Integer.parseInt(lineWords[DEVICE_ATTR_INDEX]));
                        System.out.printf("Heater %d temperature is set to %d\n",
                                Integer.parseInt(lineWords[2]),
                                Integer.parseInt(lineWords[DEVICE_ATTR_INDEX]));
                    } else {
                        System.out.printf("Heater %d temperature should be in the range [15, 30]\n",
                                Integer.parseInt(lineWords[2]));
                        break;
                    }
                    break;
                case "Camera", "Light":
                    System.out.printf("%s %d is not a heater\n",
                            lineWords[1], Integer.parseInt(lineWords[2]));
                    break;
                default:
                    break;
            }
        }
        return true;
    }
    /**
     * Implements "SetBrightness" command
     * @param lineWords string array of split command
     * @param devices devices array
     * @return false if any mistake occurred, true if it can set the brightness of the device
     */
    public static boolean setBrightness(String[] lineWords, SmartDevice[] devices) {
        // Case of incorrect command
        if (incorrectCommandStructure(lineWords, DEVICE_FIELDS_AMOUNT_TWO)) {
            System.out.println("Invalid command");
            return false;
        }
        // Case of incorrect device number
        if (!deviceInSystem(lineWords[1], Integer.parseInt(lineWords[2]))) {
            System.out.println("The smart device was not found");
            return false;
        }

        if (!devices[Integer.parseInt(lineWords[2])].isOn()) {
            System.out.printf("You can't change the status of the %s %d while it is off\n",
                    lineWords[1], Integer.parseInt(lineWords[2]));
            return false;
        } else {
            switch (lineWords[1]) {
                case "Light":
                    if (valueInRange("Brightness", lineWords[DEVICE_ATTR_INDEX])) {
                        // Setting brightness for Light
                        ((Light) devices[Integer.parseInt(lineWords[2])])
                                .setBrightnessLevel(Light.BrightnessLevel
                                        .valueOf(lineWords[DEVICE_ATTR_INDEX]));
                        System.out.printf("Light %d brightness level is set to %s\n",
                                Integer.parseInt(lineWords[2]), lineWords[DEVICE_ATTR_INDEX]);

                    } else {
                        System.out.println("The brightness can only be one of "
                                + "\"LOW\", \"MEDIUM\", or \"HIGH\"");
                        break;
                    }
                    break;
                case "Camera", "Heater":
                    System.out.printf("%s %d is not a light\n",
                            lineWords[1], Integer.parseInt(lineWords[2]));
                    break;
                default:
                    break;
            }
        }
        return true;
    }
    /**
     * Implements "SetColor" command
     * @param lineWords string array of split command
     * @param devices devices array
     * @return false if any mistake occurred, true if it can set the color of the device
     */
    public static boolean setColor(String[] lineWords, SmartDevice[] devices) {
        // Case of incorrect command
        if (incorrectCommandStructure(lineWords, DEVICE_FIELDS_AMOUNT_TWO)) {
            System.out.println("Invalid command");
            return false;
        }
        // Case of incorrect device number
        if (!deviceInSystem(lineWords[1], Integer.parseInt(lineWords[2]))) {
            System.out.println("The smart device was not found");
            return false;
        }

        if (!devices[Integer.parseInt(lineWords[2])].isOn()) {
            System.out.printf("You can't change the status of the %s %d while it is off\n",
                    lineWords[1], Integer.parseInt(lineWords[2]));
            return false;
        } else {
            switch (lineWords[1]) {
                case "Light":
                    if (valueInRange("Light", lineWords[DEVICE_ATTR_INDEX])) {
                        // Setting color for Light
                        ((Light) devices[Integer.parseInt(lineWords[2])])
                                .setLightColor(Light.LightColor.valueOf(lineWords[DEVICE_ATTR_INDEX]));
                        System.out.printf("Light %d color is set to %s\n",
                                Integer.parseInt(lineWords[2]), lineWords[DEVICE_ATTR_INDEX]);
                    } else {
                        System.out.println("The light color can only be \"YELLOW\" or \"WHITE\"");
                        break;
                    }
                    break;
                case "Camera", "Heater":
                    System.out.printf("%s %d is not a light\n",
                            lineWords[1], Integer.parseInt(lineWords[2]));
                    break;
                default:
                    break;
            }
        }
        return true;
    }
    /**
     * Implements "SetAngle" command
     * @param lineWords string array of split command
     * @param devices devices array
     * @return false if any mistake occurred, true if it can set the angle of the device
     */
    public static boolean setAngle(String[] lineWords, SmartDevice[] devices) {
        // Case of incorrect command
        if (incorrectCommandStructure(lineWords, DEVICE_FIELDS_AMOUNT_TWO)) {
            System.out.println("Invalid command");
            return false;
        }
        // Case of incorrect device number
        if (!deviceInSystem(lineWords[1], Integer.parseInt(lineWords[2]))) {
            System.out.println("The smart device was not found");
            return false;
        }

        if (!devices[Integer.parseInt(lineWords[2])].isOn()) {
            System.out.printf("You can't change the status of the %s %d while it is off\n",
                    lineWords[1], Integer.parseInt(lineWords[2]));
            return false;
        } else {
            switch (lineWords[1]) {
                case "Camera":
                    if (valueInRange("Camera", Integer.parseInt(lineWords[DEVICE_ATTR_INDEX]))) {
                        // Setting angle for Camera
                        ((Camera) devices[Integer.parseInt(lineWords[2])])
                                .setCameraAngle(Integer.parseInt(lineWords[DEVICE_ATTR_INDEX]));
                        System.out.printf("Camera %d angle is set to %d\n",
                                Integer.parseInt(lineWords[2]),
                                Integer.parseInt(lineWords[DEVICE_ATTR_INDEX]));
                    } else {
                        System.out.printf("Camera %d angle should be in the range [-60, 60]\n",
                                Integer.parseInt(lineWords[2]));
                        break;
                    }
                    break;
                case "Light", "Heater":
                    System.out.printf("%s %d is not a camera\n",
                            lineWords[1], Integer.parseInt(lineWords[2]));
                    break;
                default:
                    break;
            }
        }
        return true;
    }
    /**
     * Implements "StartRecording" command
     * @param lineWords string array of split command
     * @param devices devices array
     * @return false if any mistake occurred, true if it can start the recording of the device
     */
    public static boolean startRecording(String[] lineWords, SmartDevice[] devices) {
        // Case of incorrect command
        if (incorrectCommandStructure(lineWords, DEVICE_FIELDS_AMOUNT_ONE)) {
            System.out.println("Invalid command");
            return false;
        }
        // Case of incorrect device number
        if (!deviceInSystem(lineWords[1], Integer.parseInt(lineWords[2]))) {
            System.out.println("The smart device was not found");
            return false;
        }

        if (!devices[Integer.parseInt(lineWords[2])].isOn()) {
            System.out.printf("You can't change the status of the %s %d while it is off\n",
                    lineWords[1], Integer.parseInt(lineWords[2]));
            return false;
        } else {
            switch (lineWords[1]) {
                case "Camera":
                    if (((Camera) devices[Integer.parseInt(lineWords[2])]).isRecording()) {
                        System.out.printf("%s %d is already recording\n",
                                lineWords[1], Integer.parseInt(lineWords[2]));
                    } else {
                        // Starting recording for Camera
                        ((Camera) devices[Integer.parseInt(lineWords[2])]).startRecording();
                        System.out.printf("%s %d started recording\n",
                                lineWords[1], Integer.parseInt(lineWords[2]));
                    }
                    break;
                case "Light", "Heater":
                    System.out.printf("%s %d is not a camera\n",
                            lineWords[1], Integer.parseInt(lineWords[2]));
                    break;
                default:
                    break;
            }
        }
        return true;
    }
    /**
     * Implements "StopRecording" command
     * @param lineWords string array of split command
     * @param devices devices array
     * @return false if any mistake occurred, true if it can stop the recording of the device
     */
    public static boolean stopRecording(String[] lineWords, SmartDevice[] devices) {
        // Case of incorrect command
        if (incorrectCommandStructure(lineWords, DEVICE_FIELDS_AMOUNT_ONE)) {
            System.out.println("Invalid command");
            return false;
        }
        // Case of incorrect device number
        if (!deviceInSystem(lineWords[1], Integer.parseInt(lineWords[2]))) {
            System.out.println("The smart device was not found");
            return false;
        }

        if (!devices[Integer.parseInt(lineWords[2])].isOn()) {
            System.out.printf("You can't change the status of the %s %d while it is off\n",
                    lineWords[1], Integer.parseInt(lineWords[2]));
            return false;
        } else {
            switch (lineWords[1]) {
                case "Camera":
                    if (!((Camera) devices[Integer.parseInt(lineWords[2])]).isRecording()) {
                        System.out.printf("%s %d is not recording\n",
                                lineWords[1], Integer.parseInt(lineWords[2]));
                    } else {
                        // Stopping recording for Camera
                        ((Camera) devices[Integer.parseInt(lineWords[2])]).stopRecording();
                        System.out.printf("%s %d stopped recording\n",
                                lineWords[1], Integer.parseInt(lineWords[2]));
                    }
                    break;
                case "Light", "Heater":
                    System.out.printf("%s %d is not a camera\n",
                            lineWords[1], Integer.parseInt(lineWords[2]));
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    /**
     * Implements the main logic of the program
     * @param devices devices array
     * @param scan scanner to get data from console
     */
    public static void mainLogic(SmartDevice[] devices, Scanner scan) {
        // Instructions
        while (true) {
            // Scanning the command
            String line = scan.nextLine();
            String[] lineWords = line.split(" ");
            // Case of "end" command
            if (line.equals("end")) {
                break;
            }
            // Main logic body
            switch (lineWords[0]) {
                default:
                    System.out.println("Invalid command");
                    break;
                case "DisplayAllStatus":
                    // Case of incorrect command
                    displayAllStatus(lineWords, devices);
                    break;
                case "TurnOn":
                    turnOn(lineWords, devices);
                    break;
                case "TurnOff":
                    turnOff(lineWords, devices);
                    break;
                case "StartCharging":
                    startCharging(lineWords, devices);
                    break;
                case "StopCharging":
                    stopCharging(lineWords, devices);
                    break;
                case "SetTemperature":
                    setTemperature(lineWords, devices);
                    break;
                case "SetBrightness":
                    setBrightness(lineWords, devices);
                    break;
                case "SetColor":
                    setColor(lineWords, devices);
                    break;
                case "SetAngle":
                    setAngle(lineWords, devices);
                    break;
                case "StartRecording":
                    startRecording(lineWords, devices);
                    break;
                case "StopRecording":
                    stopRecording(lineWords, devices);
                    break;
            }
        }
    }
    public static void main(String[] args) {

        // Variables
        Scanner scan = new Scanner(System.in);
        // Devices array
        SmartDevice[] devices = new SmartDevice[]{
                new Light(Light.Status.ON, false, Light.BrightnessLevel.LOW, Light.LightColor.YELLOW),
                new Light(Light.Status.ON, false, Light.BrightnessLevel.LOW, Light.LightColor.YELLOW),
                new Light(Light.Status.ON, false, Light.BrightnessLevel.LOW, Light.LightColor.YELLOW),
                new Light(Light.Status.ON, false, Light.BrightnessLevel.LOW, Light.LightColor.YELLOW),
                new Camera(Camera.Status.ON, false, false, BASIC_ANGLE),
                new Camera(Camera.Status.ON, false, false, BASIC_ANGLE),
                new Heater(Heater.Status.ON, BASIC_TEMPERATURE),
                new Heater(Heater.Status.ON, BASIC_TEMPERATURE),
                new Heater(Heater.Status.ON, BASIC_TEMPERATURE),
                new Heater(Heater.Status.ON, BASIC_TEMPERATURE)
        };
        mainLogic(devices, scan);

    }
}

interface Controllable {
    boolean turnOff();
    boolean turnOn();
    boolean isOn();
}


interface Chargeable {
    boolean isCharging();
    boolean startCharging();
    boolean stopCharging();
}

/**
 * Abstract class from which all devices inherit
 * Implements interface Controllable
 */
abstract class SmartDevice implements Controllable {

    public enum Status {
        OFF,
        ON
    }
    private Status status;
    private int deviceId;
    private static int numberOfDevices;

    public SmartDevice(Status status) {
        this.status = status;
        this.deviceId = numberOfDevices++;
    }

    public String displayStatus() {
        return "";
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean isOn() {
        return status == Status.ON;
    }

    @Override
    public boolean turnOff() {
        this.status = Status.OFF;
        return true;
    }

    @Override
    public boolean turnOn() {
        this.status = Status.ON;
        return true;
    }

    public boolean checkStatusAccess() {
        return false;
    }
}

/**
 * Class of Heater device
 */
final class Heater extends SmartDevice {
    private int temperature;
    public static final int MAX_HEATER_TEMP = 30;
    public static final int MIN_HEATER_TEMP = 15;

    public Heater(Status status, int temperature) {
        super(status);
        this.temperature = temperature;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    @Override
    public String displayStatus() {
        return String.format("Heater %d is %s and the temperature is %d.",
                getDeviceId(), getStatus(), getTemperature());
    }
}

/**
 * Class of Camera device
 * Implements interface Chargeable
 */
final class Camera extends SmartDevice implements Chargeable {
    public static final int MAX_CAMERA_ANGLE = 60;
    public static final int MIN_CAMERA_ANGLE = -60;
    private boolean charging;
    private boolean recording;
    private int angle;

    public Camera(Status status, boolean charging, boolean recording, int angle) {
        super(status);
        this.charging = charging;
        this.recording = recording;
        this.angle = angle;
    }

    public int getAngle() {
        return this.angle;
    }

    public boolean setCameraAngle(int angle) {
        this.angle = angle;
        return true;
    }

    public boolean startRecording() {
        this.recording = true;
        return true;
    }

    public boolean stopRecording() {
        this.recording = false;
        return true;
    }

    public boolean isRecording() {
        return this.recording;
    }

    @Override
    public boolean isCharging() {
        return this.charging;
    }

    @Override
    public boolean startCharging() {
        this.charging = true;
        return true;
    }

    @Override
    public boolean stopCharging() {
        this.charging = false;
        return true;
    }

    @Override
    public String displayStatus() {
        return String.format("Camera %d is %s, the angle is %d,"
                        + " the charging status is %b, and the recording status is %b.",
                getDeviceId(), getStatus(), getAngle(), isCharging(), isRecording());
    }
}

/**
 * Class of Light device
 * Implements interface Chargeable
 */
final class Light extends SmartDevice implements Chargeable {

    public enum LightColor {
        WHITE,
        YELLOW
    }
    public enum BrightnessLevel {
        HIGH,
        MEDIUM,
        LOW
    }

    private boolean charging;
    private BrightnessLevel brightnessLevel;
    private LightColor lightColor;

    public Light(Status status, boolean charging, BrightnessLevel brightnessLevel, LightColor lightColor) {
        super(status);
        this.charging = charging;
        this.brightnessLevel = brightnessLevel;
        this.lightColor = lightColor;
    }

    public LightColor getLightColor() {
        return lightColor;
    }

    public void setLightColor(LightColor lightColor) {
        this.lightColor = lightColor;
    }

    public BrightnessLevel getBrightnessLevel() {
        return brightnessLevel;
    }

    public void setBrightnessLevel(BrightnessLevel brightnessLevel) {
        this.brightnessLevel = brightnessLevel;
    }

    @Override
    public boolean isCharging() {
        return charging;
    }

    @Override
    public boolean startCharging() {
        this.charging = true;
        return true;
    }

    @Override
    public boolean stopCharging() {
        this.charging = false;
        return true;
    }

    @Override
    public String displayStatus() {
        return String.format("Light %d is %s, the color is %s, "
                        + "the charging status is %b, and the brightness level is %s.",
                getDeviceId(), getStatus(), getLightColor(), isCharging(), getBrightnessLevel());
    }
}
