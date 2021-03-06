package frc.robot;

public class Constants {
    public static final double sparkmax_kP = 8e-4;
    public static final double sparkmax_kI = 0;
    public static final double sparkmax_kD = 0;
    public static final double sparkmax_kIz = 0;
    public static final double sparkmax_kFF = 0.000175;
    public static final double sparkmax_kMaxOut = 1;
    public static final double sparkmax_kMinOut = -1;

    public static final double hood_kP = 0.025;
    public static final double hood_kI = 0;
    public static final double hood_kD = 0.25;
    public static final double hood_kF = 0;
    public static final int hood_kIz = 0;
    public static final double hood_kMaxOut = 1;

    /**
	 * Which PID slot to pull gains from. Starting 2018, you can choose from
	 * 0,1,2 or 3. Only the first two (0,1) are visible in web-based
	 * configuration.
	 */
	public static final int kSlotIdx = 0;

	/**
	 * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For
	 * now we just want the primary one.
	 */
	public static final int kPIDLoopIdx = 0;

	/**
	 * Set to zero to skip waiting for confirmation, set to nonzero to wait and
	 * report to DS if action fails.
	 */
	public static final int kTimeoutMs = 30;
	
	/* Choose so that Talon does not report sensor out of phase */
	public static boolean kSensorPhase = true;

	/**
	 * Choose based on what direction you want to be positive,
	 * this does not affect motor invert. 
	 */
	public static boolean kMotorInvert = false;
}
