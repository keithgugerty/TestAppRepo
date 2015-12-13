package com.qualcomm.ftcrobotcontroller.opmodes;

        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.hardware.Servo;

public class TankMode1 extends OpMode {

    DcMotor leftMotorFront;
    DcMotor rightMotorFront;
//    DcMotor leftMotorBack;
//    DcMotor rightMotorBack;

    @Override
    public void init() {

        leftMotorFront = hardwareMap.dcMotor.get("left_drive_front");
        rightMotorFront = hardwareMap.dcMotor.get("right_drive_front");
 //       leftMotorBack = hardwareMap.dcMotor.get("left_drive_back");
 //       rightMotorBack = hardwareMap.dcMotor.get("right_drive_back");

        rightMotorFront.setDirection(DcMotor.Direction.REVERSE);
 //       leftMotorBack.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {

        double leftY = -gamepad1.left_stick_y;
        double rightY = -gamepad1.right_stick_y;

        double leftSquaredVal = leftY * leftY;
        double rightSquaredVal = rightY * rightY;

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
}

