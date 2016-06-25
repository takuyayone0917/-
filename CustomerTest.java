import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;



@RunWith(Theories.class)
public class CustomerTest {

    private static final String CUSTOMER_NAME = "テスト";

    private Customer sut = new Customer(CUSTOMER_NAME);

    @DataPoints
    public static Fixture[] testDatas = {
            new Fixture("movie1", Movie.NEW_RELEASE, 1),
            new Fixture("movie1", Movie.NEW_RELEASE, 2),
            new Fixture("movie1", Movie.NEW_RELEASE, 3),
            new Fixture("movie1", Movie.NEW_RELEASE, 4),
            new Fixture("movie2", Movie.CHILDRENS,   1),
            new Fixture("movie2", Movie.CHILDRENS,   2),
            new Fixture("movie2", Movie.CHILDRENS,   3),
            new Fixture("movie2", Movie.CHILDRENS,   4),
            new Fixture("movie3", Movie.REGULAR,     1),
            new Fixture("movie3", Movie.REGULAR,     2),
            new Fixture("movie3", Movie.REGULAR,     3),
            new Fixture("movie3", Movie.REGULAR,     4),
            null
    };

    @Theory
    public void test(Fixture fixture1, Fixture fixture2) {
        // 【テスト条件のセットアップ】
        if( fixture1 != null ) {
            sut.addRental( fixture1.rental );
        }
        if( fixture2 != null ) {
            sut.addRental( fixture2.rental );
        }

        // 【テスト実施】
        String result = sut.statement();

        // 【テスト結果の検証】
        // 期待値作成
        String expected = "Rental Record for " + CUSTOMER_NAME + "\n";
        expected += buildAmountPartString(fixture1);
        expected += buildAmountPartString(fixture2);
        double totalAmount = calcTotalAmount(fixture1,fixture2);
        expected += "Amount owed is " + String.valueOf(totalAmount) + "\n";
        int frequentREnterPoints = calcPoint(fixture1,fixture2);
        expected += "You earned " + String.valueOf(frequentREnterPoints) + " frequent renter points";

        assertThat( result, is(expected) );

    }

    private double calcTotalAmount(Fixture... fixtures) {
        double totalAmount = 0;

        for(Fixture fixture : fixtures) {
            if( fixture == null ) continue;
            totalAmount += fixture.amount;
        }

        return totalAmount;
    }

    /**
     * ボーナスポイントを計算する。
     * @param fixtures
     * @return
     */
    private int calcPoint(Fixture... fixtures) {
        int frequentREnterPoints = 0;
        for(Fixture fixture : fixtures) {
            if( fixture == null ) continue;
            frequentREnterPoints++;
            // 新作を二日以上借りた場合はボーナスポイント
            if( (fixture.movie.getPriceCode() == Movie.NEW_RELEASE ) &&
                    fixture.rental.getDaysRented() > 1) {
                frequentREnterPoints++;
            }
        }

        return frequentREnterPoints;
    }

    /**
     * Fixtureを元に、個別のmovieの料金を示す文字列を作成する。
     * @param fixture
     * @return
     */
    private String buildAmountPartString(Fixture fixture) {
        if( fixture == null ) {
            return "";
        }
        final String amountPartFormat = "\t%s\t%s\n";
        String movieTitle = fixture.movie.getTitle();
        String amount     = String.valueOf(fixture.amount);
        return String.format(amountPartFormat, movieTitle, amount);
    }

    /**
     * テスト用データをひとまとめに扱うためのクラス
     */
    static class Fixture {
        private Movie  movie;
        private Rental rental;
        private double amount;
        public Fixture(String movieTitle, int priceCode, int daysRented) {
            this.movie  = new Movie(movieTitle, priceCode);
            this.rental = new Rental(movie, daysRented);

            // 一つのmovieの料金を計算
            amount = 0;
            switch(priceCode) {
            case Movie.REGULAR:
                amount += 2;
                if( daysRented > 2)
                    amount += (daysRented - 2 ) * 1.5;
                break;
            case Movie.NEW_RELEASE:
                amount += daysRented * 3;
                break;
            case Movie.CHILDRENS:
                amount += 1.5;
                if ( daysRented > 3 )
                    amount += (daysRented -3 ) * 1.5;
                break
                ;
            }

        }

    }

}
