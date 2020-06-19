package pri.vvoyek.wsb.game;

import java.time.LocalDateTime;

public class Settings {
    public final static LocalDateTime START_OF_THE_GAME = LocalDateTime.of(2020,1, 1, 0, 0);
    public final static long GAME_SEED = 123456;
    public final static double COMPANY_INITIAL_CASH = 1000.0;
    public final static int WORK_ITEM_MAX_DAYS = 10; //28;
    public final static int COMPLEX_WORK_ITEM_DAYS = 5;
    public final static int INITIAL_PROJECT_COUNT = 3;
    public final static int INITIAL_FREE_EMPLOYEES = 3;
    public final static int TRIES_TO_NEW_PROJECT = 2; //5;
    public final static int PROGRAMMER_MAX_BUG_RATE = 10;
    public final static int PROGRAMMER_MAX_DELAY_RATE = 20;
    public final static double EMPLOYEE_FIXED_COST = 50.0;
    public final static double EMPLOYEE_SOCIAL_TAX_RATE = 0.32;
    public final static double EMPLOYMENT_COST = 25.0;
    public final static double LAY_OFF_COST = 25.0;
    public final static double SALES_TAX = 0.1;
    public final static int SICK_DAYS_PER_YEAR = 14;
    public final static double HEADHUNTER_COST = 50.0;

    public final static LocalDateTime[] HOLIDAYS = new LocalDateTime[] {
            LocalDateTime.of(2020,5,1,0,0),
            LocalDateTime.of(2020,12,25,0,0),
    };
}
