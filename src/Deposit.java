import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by DotinSchool2 on 10/15/2015.
 */
public class Deposit implements Comparable<Deposit> {
//    double depositInterest;
    BigDecimal depositBalance;
    BigDecimal payedInterest;
    String customerNumber;
    long duration;
    DepositType depositType;

    /**
     * assigns the deposit's values  according to their deposit type
     *
     * @param depositBalance    maghbare mojudy
     * @param duration        moddat zaman
     * @param customerNumber     shomare moshtari
     * @param depositType noe hesab
     */
    public Deposit(BigDecimal depositBalance, String customerNumber, long duration, DepositType depositType) {
        this.depositBalance = depositBalance;
        this.customerNumber = customerNumber;
        this.duration = duration;
        this.depositType = depositType;
    }

    /**
     * this method takes the deposit information as an input and computes the deposit interest
     *
     * @return BigDecimal deposit interest
     */
    BigDecimal calculatePayedInterest() {
        payedInterest = depositBalance.multiply(new BigDecimal((duration * depositType.getInterestRate()))).divide(new BigDecimal(36500), 2, RoundingMode.HALF_UP);
        return payedInterest;
    }

    public int compareTo(Deposit deposit) {
        return -(payedInterest.compareTo(deposit.payedInterest));
    }

}
