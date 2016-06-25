import java.util.Enumeration;
import java.util.Vector;


public class Customer {
    private String name;
    private Vector<Rental> rentals = new Vector<>();

    public Customer (String name) {
        this.name = name;
    }

    public void addRental(Rental arg) {
        rentals.addElement(arg);
    }

    public String getName() {
        return name;
    }

    public String statement() {
        double totalAmount = 0;
        int frequentREnterPoints = 0;
        Enumeration<Rental> rentals = this.rentals.elements();

        String result = "Rental Record for " + getName() + "\n";
        while( rentals.hasMoreElements() ) {
            double thisAmount = 0;
            Rental each = (Rental) rentals.nextElement();

            switch( each.getMovie().getPriceCode() ) {
            case Movie.REGULAR:
                thisAmount += 2;
                if( each.getDaysRented() > 2)
                    thisAmount += ( each.getDaysRented() -2) * 1.5;
                break;
            case Movie.NEW_RELEASE:
                thisAmount += each.getDaysRented() * 3;
                break;
            case Movie.CHILDRENS:
                thisAmount += 1.5;
                if ( each.getDaysRented() > 3 )
                    thisAmount += (each.getDaysRented() -3 ) * 1.5;
                break;
            }

            // レンタルポイントを加算
            frequentREnterPoints ++;
            // 新作を二日以上借りた場合はボーナスポイント
            if ((each.getMovie().getPriceCode() == Movie.NEW_RELEASE) &&
                    each.getDaysRented() > 1) frequentREnterPoints ++;

            // この貸し出しに関する数値の表示
            result += "\t" + each.getMovie().getTitle() + "\t" +
                    String.valueOf(thisAmount) + "\n";
            totalAmount += thisAmount;
        }

        result += "Amount owed is " + String.valueOf(totalAmount) + "\n";
        result += "You earned " + String.valueOf(frequentREnterPoints) +
                " frequent renter points";
        return result;
    }

}
