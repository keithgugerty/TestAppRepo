package com.qualcomm.ftcrobotcontroller.opmodes;

        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.hardware.Servo;
        import com.qualcomm.robotcore.hardware.CompassSensor;
        import com.qualcomm.robotcore.*;
        import com.qualcomm.ftccommon.*;
        import java.lang.Exception;


public class TankMode2 extends OpMode {
// Declare Drive motor objects
    private DcMotor leftMotorFront;
    private DcMotor rightMotorFront;
// Declare Servo Motors
    private Servo arm;
    private Servo claw;
    private Servo lift;
// Declare Sensors
    private compassSensor compass;
    private gyroSensor RobotGyro;
    private colorSensor rgbSensor;


    @Override
    public void init() {
// Define motors and motor directions
        try{
            leftMotorFront = hardwareMap.dcMotor.get("left_drive_front");
            rightMotorFront = hardwareMap.dcMotor.get("right_drive_front");
            rightMotorFront.setDirection(DcMotor.Direction.REVERSE);
        } catch (RobotCoreException rce) {
            DbgLog.msg("RobotCoreException Error initializing DCMotors");
            DbgLog.error(rce.getMessage);
            DbgLog.LogStacktrace(rce);
        } catch (Exception e){
            DbgLog.msg("RobotCoreException Error initializing DCMotors");
            DbgLog.error(e.getMessage);
            DbgLog.LogStacktrace(e);
        }


// Define Servos
        try{
            arm = hardwareMap.servo.get("Arm");
            claw = hardwareMap.servo.get("Claw");
            lift = hardwareMap.servo.get("Lift");
        } catch (RobotCoreException rce) {
            DbgLog.msg("RobotCoreException Error initializing Servos");
            DbgLog.error(rce.getMessage);
            DbgLog.LogStacktrace(rce);
        } catch (Exception e){
            DbgLog.msg("RobotCoreException Error initializing Servos");
            DbgLog.error(e.getMessage);
            DbgLog.LogStacktrace(e);
        }


// Define Sensors
        try{
            compass= hardwareMap.compassSensor.get("Compass");
            RobotGyro= hardwareMap.gyroSensor.get("Gyro");
            rgbSensor= hardwareMap.rgbSensor.get("Color");
        } catch (RobotCoreException rce) {
            DbgLog.msg("RobotCoreException Error initializing Servos");
            DbgLog.error(rce.getMessage);
            DbgLog.LogStacktrace(rce);
        } catch (Exception e){
            DbgLog.msg("RobotCoreException Error initializing Servos");
            DbgLog.error(e.getMessage);
            DbgLog.LogStacktrace(e);
        }

// Define Controls


    }

    @Override
    public void loop() {
        dual_stick_drive();

        //add connectivity test
        power_zero();


    }

    /***
     * Run the Robot with both Joy sticks.  Left Joystick controls the direction and power of the left track.
     * Right Joy Stick controls the right side.
     */
    public void dual_stick_drive() {

        double leftY = -gamepad1.left_stick_y/2;
        double rightY = -gamepad1.right_stick_y/2;

        double leftSquaredVal = (leftY * leftY)/6;
        double rightSquaredVal = (rightY * rightY)/6;

        double leftPower;
        double rightPower;

        if(leftY < 0) {
            leftPower = -leftSquaredVal;
        } else {
            leftPower = leftSquaredVal;
        }

        if(rightY < 0) {
            rightPower = -rightSquaredVal;
        } else {
            rightPower = rightSquaredVal;
        }
        leftMotorFront.setPower(leftPower);
        // leftMotorBack.setPower(leftPower);

        rightMotorFront.setPower(rightPower);
        // rightMotorBack.setPower(rightPower);

    }

    /***
     * Safety control.  This will set the motor powers to zero stopping the robot from moving.
     * in conjuction with a connectivity test this function should prevent a "run away" robot.
     */
    public void power_zero(){
        leftMotorFront.setPower(0);
        // leftMotorBack.setPower(leftPower);
        rightMotorFront.setPower(0);
        // rightMotorBack.setPower(rightPower);
    }

    /***
     * Left Joy stick controls direction and speed of both motors.  
     */
    public void single_stick_drive() {
        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float throttle = -gamepad1.left_stick_y;
        float direction = gamepad1.left_stick_x;
        float right = throttle - direction;
        float left = throttle + direction;

        // clip the right/left values so that the values never exceed +/- 1
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        // write the values to the motors
        motorRight.setPower(right);
        motorLeft.setPower(left);
    }
}

