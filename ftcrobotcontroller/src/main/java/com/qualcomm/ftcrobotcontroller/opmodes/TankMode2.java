package com.qualcomm.ftcrobotcontroller.opmodes;

        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.hardware.Servo;
        import com.qualcomm.robotcore.hardware.CompassSensor;
        import com.qualcomm.robotcore.hardware.GyroSensor;
        import com.qualcomm.robotcore.hardware.ColorSensor;
        import com.qualcomm.robotcore.util.Range;
        import com.qualcomm.ftccommon.*;
        import java.lang.Exception;


public class TankMode2 extends OpMode{
// Declare Drive motor objects
    private DcMotor leftMotorFront;
    private DcMotor rightMotorFront;
// Declare Servo Motors
    private Servo arm;
    private Servo claw;
    private Servo lift;
// Declare Sensors
    private CompassSensor compass;
    private GyroSensor RobotGyro;
    private ColorSensor rgbSensor;


    @Override
    public void init() {
// Define motors and motor directions
        try{
            leftMotorFront = hardwareMap.dcMotor.get("left_drive_front");
            rightMotorFront = hardwareMap.dcMotor.get("right_drive_front");
            rightMotorFront.setDirection(DcMotor.Direction.REVERSE);
        } catch (Exception e){
            DbgLog.msg("RobotCoreException Error initializing DCMotors");
            DbgLog.error(e.getMessage());
            DbgLog.logStacktrace(e);
        }


// Define Servos
        try{
            arm = hardwareMap.servo.get("Arm");
            claw = hardwareMap.servo.get("Claw");
            lift = hardwareMap.servo.get("Lift");
        } catch (Exception e){
            DbgLog.msg("RobotCoreException Error initializing Servos");
            DbgLog.error(e.getMessage());
            DbgLog.logStacktrace(e);
        }


// Define Sensors
        try{
            compass= hardwareMap.compassSensor.get("Compass");
            RobotGyro= hardwareMap.gyroSensor.get("Gyro");
            rgbSensor= hardwareMap.colorSensor.get("Color");
        } catch (Exception e){
            DbgLog.msg("RobotCoreException Error initializing Servos");
            DbgLog.error(e.getMessage());
            DbgLog.logStacktrace(e);
        }

// Define Controls


    }

    @Override
    public void loop() {
        dual_stick_drive();
        //connectivity test
        if (gamepad1 == null) {
            power_zero();
        }

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
        right = scaleInput(right);
        left = scaleInput(left);

        // write the values to the motors
        rightMotorFront.setPower(right);
        leftMotorFront.setPower(left);
    }

    /**
     * Scale the joystick input using a nonlinear algorithm.
     */
    float scaleInput (float p_power)
    {
        //
        // Assume no scaling.
        //
        float l_scale = 0.0f;

        //
        // Ensure the values are legal.
        //
        float l_power = Range.clip (p_power, -1, 1);

        float[] l_array =
                { 0.00f, 0.05f, 0.09f, 0.10f, 0.12f
                        , 0.15f, 0.18f, 0.24f, 0.30f, 0.36f
                        , 0.43f, 0.50f, 0.60f, 0.72f, 0.85f
                        , 1.00f, 1.00f
                };

        //
        // Get the corresponding index for the specified argument/parameter.
        //
        int l_index = (int)(l_power * 16.0);
        if (l_index < 0)
        {
            l_index = -l_index;
        }
        else if (l_index > 16)
        {
            l_index = 16;
        }
        if (l_power < 0)
        {
            l_scale = -l_array[l_index];
        }
        else
        {
            l_scale = l_array[l_index];
        }
        return l_scale;

    } // scale_motor_power

    /**
     *
     */
    public String check_color() {
        //assign color value
        return check_color();
    }
}

